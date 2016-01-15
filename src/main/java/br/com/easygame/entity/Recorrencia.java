package br.com.easygame.entity;

import java.io.Serializable;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.easygame.enuns.TipoRecorrencia;

@Entity
@Table(name = "recorrencia")
public class Recorrencia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Enumerated
	@Column(name = "tipo")
	private TipoRecorrencia tipoRecorrencia = TipoRecorrencia.SEMANAL;

	public Recorrencia() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		Recorrencia other = (Recorrencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static Recorrencia toRecorrencia(String json) {
		Recorrencia recorrencia = new Recorrencia();

		JsonReader jsonReader = Json.createReader(new StringReader(json));
		JsonObject jsonObject = jsonReader.readObject();
		if (!jsonObject.containsKey("tipoRecorrencia")) {
			throw new IllegalArgumentException("Atributo 'nome' é obrigatório");
		}
		
		return recorrencia;
	}

}
