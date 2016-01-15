package br.com.easygame.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.json.JsonException;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.LocalDate;

import br.com.easygame.entity.Evento;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;

@Table(name = "equipe")
@Entity
public class Equipe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "nome")
	private String nome;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fundacao")
	private Date dataFundacao;
	
	
	
	
	public Equipe() {
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public Date getDataFundacao() {
		return dataFundacao;
	}


	public void setDataFundacao(Date dataFundacao) {
		this.dataFundacao = dataFundacao;
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
		Equipe other = (Equipe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Equipe toEquipe(JsonObject jsonObject) {
		Equipe equipe = new Equipe();
		try {
			if (jsonObject.containsKey("id")) {
				equipe.setId(Long.valueOf(jsonObject.get("id").toString()));
			}
			equipe.setNome(jsonObject.getString("nome"));
			String dataFundacao = jsonObject.getString("dataFundacao");
			equipe.setDataFundacao(new LocalDate(dataFundacao).toDate());
			return equipe;
		} catch (JsonException e) {
			throw new RuntimeException("Erro ao ler JSON de Usuario", e);
		}

	}

}
