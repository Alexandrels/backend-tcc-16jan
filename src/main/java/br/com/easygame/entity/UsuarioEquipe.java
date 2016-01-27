/**
 * 
 */
package br.com.easygame.entity;

import java.io.Serializable;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.util.DataUtils;

/**
 * @author Alexandre
 * 
 */
@Table(name = "usuario_has_equipe")
@Entity
public class UsuarioEquipe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name = "id_equipe")
	private Equipe equipe;
	@Enumerated
	@Column(name = "posicao")
	private TipoPosicao posicao;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_contratacao")
	private Date dataContratacao;
	@Column(name = "pendente")
	private SimNao pendente = SimNao.SIM;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_convite")
	private Date dataConvite;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public TipoPosicao getPosicao() {
		return posicao;
	}

	public void setPosicao(TipoPosicao posicao) {
		this.posicao = posicao;
	}

	public Date getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(Date dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SimNao getPendente() {
		return pendente;
	}

	public void setPendente(SimNao pendente) {
		this.pendente = pendente;
	}

	public Date getDataConvite() {
		return dataConvite;
	}

	public void setDataConvite(Date dataConvite) {
		this.dataConvite = dataConvite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equipe == null) ? 0 : equipe.hashCode());
		result = prime * result + ((posicao == null) ? 0 : posicao.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	public JsonObject toJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (getId() != null) {
			builder.add("id", getId());
		}
		builder.add("usuario", getUsuario().getId());
		if (getEquipe().getId() != null) {
			builder.add("equipe", getEquipe().getId());
		}
		builder.add("posicao", getPosicao().ordinal()).add("dataContratacao",
				DataUtils.formatarDate(getDataContratacao(), "dd/MM/yyy"));

		return builder.build();
	}

	public static UsuarioEquipe toUsuarioEquipe(JsonObject jsonObject) {
		UsuarioEquipe usuarioEquipe = new UsuarioEquipe();
		if (jsonObject.containsKey("id")) {
			usuarioEquipe.setId(Long.valueOf(jsonObject.getInt("id")));
		}
		usuarioEquipe.setUsuario(new Usuario(Long.valueOf(jsonObject.getInt("usuario"))));
		if (jsonObject.containsKey("equipe")) {
			usuarioEquipe.setEquipe(new Equipe(Long.valueOf(jsonObject.getInt("equipe"))));
		}
		String dataContratacao = jsonObject.getString("dataContratacao");
		usuarioEquipe.setPosicao(TipoPosicao.values()[jsonObject.getInt("posicao")]);
		usuarioEquipe.setDataContratacao(DataUtils.parseDate(dataContratacao, "dd/MM/yyyy"));
		return usuarioEquipe;

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UsuarioEquipe))
			return false;
		UsuarioEquipe other = (UsuarioEquipe) obj;
		if (equipe == null) {
			if (other.equipe != null)
				return false;
		} else if (!equipe.equals(other.equipe))
			return false;
		if (posicao != other.posicao)
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
