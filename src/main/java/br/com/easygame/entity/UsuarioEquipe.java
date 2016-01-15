/**
 * 
 */
package br.com.easygame.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.easygame.enuns.TipoPosicao;

/**
 * @author Alexandre
 *
 */
@Table(name = "usuario_has_equipe")
@Entity
public class UsuarioEquipe {

	@ManyToOne
	private Usuario usuario;
	@ManyToOne
	private Equipe equipe;
	private TipoPosicao posicao;
	private Date dataContratacao;
}
