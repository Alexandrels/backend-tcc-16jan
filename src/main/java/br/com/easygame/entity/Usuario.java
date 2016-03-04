/**
 * 
 */
package br.com.easygame.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mysql.jdbc.StringUtils;

import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.enuns.TipoUsuario;

/**
 * @author mobilesys.alexandre
 * 
 */
@Table(name = "usuario")
@Entity
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	public Usuario(Long id) {
		this.id = id;
	}

	public Usuario() {
	}

	@Column(name = "login")
	private String login;
	@Column(name = "senha")
	private String senha;
	@Column(name = "nome")
	private String nome;
	@Column(name = "latitude")
	private Double latitude;
	@Column(name = "longitude")
	private Double longitude;
	@Column(name = "tipo")
	private String tipoUsuario;
	@Column(name = "facebook")
	private SimNao facebook = SimNao.NAO;
	@Column(name = "apelido")
	private String apelido;
	@Column(name = "posicao")
	private TipoPosicao tipoPosicao;

	@OneToMany(mappedBy = "usuario")
	private List<Evento> eventos;

	@OneToMany(mappedBy = "usuario")
	private List<Equipe> equipes;

	private String Telefone;

	/*
	 * um ou mais tipos
	 */
	public void salvarTipoUsuario(List<TipoUsuario> tiposUsuario) {
		StringBuilder builder = new StringBuilder();
		if (!StringUtils.isNullOrEmpty(tipoUsuario)) {
			List<TipoUsuario> novosExistentesAjustados = ajustaNovosTiposUsuarioComJaExistentes(tiposUsuario);
			for (TipoUsuario tipoUsuario : novosExistentesAjustados) {
				builder.append(tipoUsuario.ordinal()).append(";");
			}
			this.tipoUsuario = builder.toString();
		} else {
			for (TipoUsuario tipoUsuario : tiposUsuario) {
				builder.append(tipoUsuario.ordinal()).append(";");
			}
			this.tipoUsuario = builder.toString();
		}
	}

	public List<TipoUsuario> ajustaNovosTiposUsuarioComJaExistentes(List<TipoUsuario> novosTiposUsuario) {
		List<TipoUsuario> tiposDoUsuario = recuperarTipoUsuario();
		for (TipoUsuario novoTipoUsuario : novosTiposUsuario) {
			if (!tiposDoUsuario.contains(novoTipoUsuario)) {
				tiposDoUsuario.add(novoTipoUsuario);
			}
		}
		return tiposDoUsuario;

	}

	/**
	 * Recuperar um ou mais tipos desse usuario
	 * 
	 * @return
	 */
	public List<TipoUsuario> recuperarTipoUsuario() {
		List<TipoUsuario> tiposUsuarios = new ArrayList<>();
		String[] tipo = getTipoUsuario().split(";");
		for (String string : tipo) {
			tiposUsuarios.add(TipoUsuario.values()[Integer.valueOf(string)]);
		}
		return tiposUsuarios;

	}

	public List<TipoUsuario> tiposDoUsuario() {

		return null;

	}

	public SimNao getFacebook() {
		return facebook;
	}

	public void setFacebook(SimNao facebook) {
		this.facebook = facebook;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public TipoPosicao getTipoPosicao() {
		return tipoPosicao;
	}

	public void setTipoPosicao(TipoPosicao tipoPosicao) {
		this.tipoPosicao = tipoPosicao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}

	public String getTelefone() {
		return Telefone;
	}

	public void setTelefone(String telefone) {
		Telefone = telefone;
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
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", senha=" + senha + ", nome=" + nome + ", latitude="
				+ latitude + ", longitude=" + longitude + ", tipoUsuario=" + tipoUsuario + ", facebook=" + facebook
				+ ", apelido=" + apelido + ", tipoPosicao=" + tipoPosicao + "]";
	}

	public JsonObject toJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (getId() != null) {
			builder.add("id", getId());
		}
		builder.add("nome", getNome()).add("apelido", getApelido()).add("login", getLogin()).add("senha", getSenha())
				.add("latitude", getLatitude() != null ? getLatitude() : 0)
				.add("longitude", getLongitude() != null ? getLongitude() : 0).add("facebook", getFacebook().ordinal())
				.add("posicao", getTipoPosicao().ordinal()).add("tipoUsuario", getTipoUsuario())
				.add("telefone", getTelefone());

		return builder.build();
	}

	public JsonObject toUsuarioCoordenadasJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (getId() != null) {
			builder.add("id", getId());
		}
		builder.add("nome", getNome())
				.add("apelido", getApelido())
				.add("latitude", getLatitude() != null ? getLatitude() : 0)
				.add("longitude", getLongitude() != null ? getLongitude() : 0)
				.add("posicao", getTipoPosicao().ordinal())
				.add("telefone", getTelefone());

		return builder.build();
	}

	public static Usuario toUsuario(JsonObject jsonObject) {
		Usuario usuario = new Usuario();
		try {
			if (jsonObject.containsKey("id")) {
				usuario.setId(Long.valueOf(jsonObject.get("id").toString()));
			}
			usuario.setNome(jsonObject.getString("nome"));
			usuario.setApelido(jsonObject.getString("apelido"));
			usuario.setFacebook(jsonObject.getInt("facebook") == 1 ? SimNao.SIM : SimNao.NAO);
			usuario.setLatitude(Double.valueOf(jsonObject.get("latitude").toString()));
			usuario.setLongitude(Double.valueOf(jsonObject.get("longitude").toString()));
			usuario.setLogin(jsonObject.getString("login"));
			usuario.setSenha(jsonObject.getString("senha"));
			usuario.setTipoPosicao(TipoPosicao.values()[jsonObject.getInt("posicao")]);
			usuario.setTipoUsuario(jsonObject.getString("tipoUsuario"));
			usuario.setTelefone(jsonObject.getString("telefone"));
			;
		} catch (JsonException e) {
			throw new RuntimeException("Erro ao ler JSON de Usuario", e);
		}
		return usuario;

	}

}
