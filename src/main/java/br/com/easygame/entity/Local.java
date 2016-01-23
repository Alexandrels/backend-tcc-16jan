package br.com.easygame.entity;

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;;

@Table(name = "local")
@Entity
public class Local implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "nome_local")
	private String nomeLocal;
	@Column(name = "endereco")
	private String endereco;
	@Column(name = "proprietario")
	private String proprietario;

	public Local() {
	}

	public Local(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeLocal() {
		return nomeLocal;
	}

	public void setNomeLocal(String nomeLocal) {
		this.nomeLocal = nomeLocal;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
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
		Local other = (Local) obj;
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
		builder.add("nomeLocal", getNomeLocal()).add("endereco", getEndereco()).add("proprietario", getProprietario());

		return builder.build();
	}

	public Local toLocal(JsonObject jsonObject) {
		Local local = new Local();
		if (jsonObject.containsKey("id")) {
			int idInt = jsonObject.getInt("id");
			local.setId(Long.valueOf(idInt));
		}
		local.setNomeLocal(jsonObject.getString("nomeLocal"));
		local.setEndereco(jsonObject.getString("endereco"));
		local.setProprietario(jsonObject.getString("proprietario"));
		return local;
	}

}
