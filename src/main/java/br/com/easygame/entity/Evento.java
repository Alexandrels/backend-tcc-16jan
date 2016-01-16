package br.com.easygame.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.easygame.enuns.TipoEvento;

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

	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name = "evento_has_local", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_local") })
	private List<Local> locais = new ArrayList<Local>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_equipe", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_equipe") })
	private List<Equipe> equipes = new ArrayList<Equipe>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_notificacao", joinColumns = {
			@JoinColumn(name = "id_evento") }, inverseJoinColumns = { @JoinColumn(name = "id_notificacao") })
	private List<Notificacao> notificacoes = new ArrayList<Notificacao>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_usuario", joinColumns = { @JoinColumn(name = "id_evento") }, inverseJoinColumns = {
			@JoinColumn(name = "id_usuario") })
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "evento_has_recorrencia", joinColumns = {
			@JoinColumn(name = "id_evento") }, inverseJoinColumns = { @JoinColumn(name = "id_recorrencia") })
	private List<Recorrencia> recorrencias = new ArrayList<Recorrencia>();

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

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

	public List<Local> getLocais() {
		return locais;
	}

	public void setLocais(List<Local> locais) {
		this.locais = locais;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}

	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
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
	public void adicionarLocais(List<Local> locais) {
		if (getLocais() == null) {
			setLocais(new ArrayList<Local>());
		}
		getLocais().addAll(locais);
	}

	public void adicionarLocal(Local local) {
		adicionarLocais(Arrays.asList(local));
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

	// public static Equipe toEquipe(String json) {
	// Equipe equipe = new Equipe();
	//
	// JsonReader jsonReader = Json.createReader(new StringReader(json));
	// JsonObject jsonObject = jsonReader.readObject();
	// if (!jsonObject.containsKey("nome")) {
	// throw new IllegalArgumentException("Atributo 'nome' é obrigatório");
	// }
	// String nome = jsonObject.getString("nome");
	//
	// equipe.setNome(nome);
	// equipe.setJogadores(new ArrayList<Jogador>());
	// if (!jsonObject.getJsonArray("jogadores").isEmpty()) {
	// for (JsonValue jogadorJson : jsonObject.getJsonArray("jogadores")) {
	// JsonReader reader = Json.createReader(new
	// StringReader(jogadorJson.toString()));
	// JsonObject objeto = reader.readObject();
	// Jogador jogador = new Jogador(Long.valueOf(objeto.getString("id")));
	// equipe.adicionarJogador(jogador);
	// }
	// }
	// return equipe;
	// }

}
