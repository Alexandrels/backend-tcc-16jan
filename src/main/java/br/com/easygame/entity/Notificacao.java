
package br.com.easygame.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.easygame.enuns.SimNao;

@Entity
@Table(name = "notificacao")
public class Notificacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora")
	private Date dataHora;

	@Column(name = "notificado")
	private SimNao notiifcado;
	@ManyToMany(mappedBy = "notificacoes")
	private List<Evento> eventos;

	public Notificacao() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public SimNao getNotiifcado() {
		return notiifcado;
	}

	public void setNotiifcado(SimNao notiifcado) {
		this.notiifcado = notiifcado;
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
		Notificacao other = (Notificacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static Notificacao toEquipe(String json) {
		Notificacao notificacao = new Notificacao();

		JsonReader jsonReader = Json.createReader(new StringReader(json));
		JsonObject jsonObject = jsonReader.readObject();
		if (!jsonObject.containsKey("dataHora")) {
			throw new IllegalArgumentException("Atributo 'nome' é obrigatório");
		}
		return notificacao;
	}

}
