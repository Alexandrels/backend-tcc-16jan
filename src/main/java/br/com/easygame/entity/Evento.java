package br.com.easygame.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;

import br.com.easygame.enuns.StatusEvento;
import br.com.easygame.enuns.TipoEvento;
import br.com.easygame.util.DataUtils;

@Entity
@Table(name = "evento")
public class Evento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "descricao")
	private String descricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora")
	private Date dataHora;

	@Enumerated
	@Column(name = "tipo")
	private TipoEvento tipoEvento;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "evento_has_local", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_local") })
	private Local local;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_equipe", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_equipe") })
	private List<Equipe> equipes;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "evento_has_notificacao", joinColumns = {
			@JoinColumn(name = "id_evento") }, inverseJoinColumns = { @JoinColumn(name = "id_notificacao") })
	private Notificacao notificacao;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_usuario", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_usuario") })
	private List<Usuario> usuarios;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_recorrencia", joinColumns = {
			@JoinColumn(name = "id_evento") }, inverseJoinColumns = { @JoinColumn(name = "id_recorrencia") })
	private List<Recorrencia> recorrencias = new ArrayList<Recorrencia>();

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "status")
	private StatusEvento statusEvento = StatusEvento.ATIVO;

	public Evento() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Recorrencia> getRecorrencias() {
		return recorrencias;
	}

	public void setRecorrencias(List<Recorrencia> recorrencias) {
		this.recorrencias = recorrencias;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public StatusEvento getStatusEvento() {
		return statusEvento;
	}

	public void setStatusEvento(StatusEvento statusEvento) {
		this.statusEvento = statusEvento;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void adicionarEquipes(List<Equipe> equipes) {
		if (getEquipes() == null) {
			setEquipes(new ArrayList<Equipe>());
		}
		getEquipes().addAll(equipes);
	}

	public void adcionarEquipe(Equipe equipe) {
		adicionarEquipes(Arrays.asList(equipe));
	}

	public void adicionarUsuarios(List<Usuario> usuarios) {
		if (getUsuarios() == null) {
			setUsuarios(new ArrayList<Usuario>());
		}
		getUsuarios().addAll(usuarios);
	}

	public void adicionarUsuario(Usuario usuario) {
		adicionarUsuarios(Arrays.asList(usuario));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public JsonObject toJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (getId() != null) {
			builder.add("id", getId());
		}
		builder.add("descricao", getDescricao())
				.add("dataHora", DataUtils.formatarDate(getDataHora(), "dd/MM/yyyy HH:mm:ss"))
				.add("tipo", getTipoEvento().ordinal()).add("usuario", getUsuario().getId())
				.add("statusEvento", getStatusEvento().ordinal());
		if (getLocal() != null) {
			// avaliar os demais ralcionamentos parecidos, que podem ou não ter
			// o relacionamento
			builder.add("local", getLocal().toJSON());
		}
		if (CollectionUtils.isNotEmpty(getEquipes())) {
			JsonArrayBuilder arrayEquipes = Json.createArrayBuilder();
			for (Equipe equipe : getEquipes()) {
				arrayEquipes.add(equipe.getId());
			}
			builder.add("equipes", arrayEquipes);
		}
		if (CollectionUtils.isNotEmpty(getUsuarios())) {
			JsonArrayBuilder arrayUsuarios = Json.createArrayBuilder();
			for (Usuario usuario : getUsuarios()) {
				arrayUsuarios.add(usuario.getId());
			}
			builder.add("usuarios", arrayUsuarios);
		}

		return builder.build();
	}

	public static Evento toEvento(JsonObject jsonObject) {
		Evento evento = new Evento();

		if (jsonObject.containsKey("id")) {
			evento.setId(Long.valueOf(jsonObject.get("id").toString()));
		}
		evento.setDescricao(jsonObject.getString("descricao"));
		String dataHora = jsonObject.getString("dataHora");
		evento.setDataHora(DataUtils.parseDate(dataHora, "dd/MM/yyyy HH:mm:ss"));
		evento.setUsuario(new Usuario(Long.valueOf(jsonObject.getInt("usuario"))));
		evento.setTipoEvento(TipoEvento.values()[jsonObject.getInt("tipo")]);
		evento.setLocal(new Local().toLocal(jsonObject.getJsonObject("local")));
		evento.setStatusEvento(StatusEvento.values()[jsonObject.getInt("local")]);
		JsonArray arrayEquipes = jsonObject.getJsonArray("equipes");
		for (int i = 0; i < arrayEquipes.size(); i++) {
			Equipe equipe = new Equipe(Long.valueOf(arrayEquipes.getInt((i))));
			evento.adcionarEquipe(equipe);
		}
		JsonArray arrayUsuarios = jsonObject.getJsonArray("usuarios");
		for (int i = 0; i < arrayUsuarios.size(); i++) {
			evento.adicionarUsuario(new Usuario(Long.valueOf(arrayUsuarios.getInt(i))));
		}

		return evento;

	}
}
