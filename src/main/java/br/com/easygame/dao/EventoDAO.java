/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.easygame.entity.Evento;
import br.com.easygame.entity.UsuarioEquipe;
import br.com.easygame.entity.UsuarioEquipe;

/**
 * @author mobilesys.alexandre
 * 
 */
public class EventoDAO {

	EntityManager entityManager;

	public EventoDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoDAO() {

	}

	// um detalhe que esqueci, sempre que usar um EntityManager, depois de
	// utilizar ele SEMPRE tem que
	// fechar ele, se não fechar ele fica mantendo uma conexão com o banco
	// preza, então o correto é fazer
	// do jeito que vou fazer aqui
	public void salvar(Evento evento) {
		entityManager.persist(evento);// usa o entityManager pra fazer a
										// operação no banco
	}

	public void editar(Evento evento) {
		entityManager.merge(evento);// usa o entityManager pra fazer a operação
									// no banco
	}

	// aqui um exemplo de como listar todos os usuarios
	public List<Evento> listar() {
		try {
			// cria um entityManager
			StringBuilder builder = new StringBuilder("SELECT u FROM UsuarioEquipe u ");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), Evento.class).getResultList();
		} catch (Exception e) {
			return new ArrayList<Evento>();
		}
	}

	public Evento pesquisarPorId(Long id) {
		return entityManager.find(Evento.class, id);
	}

	public void flush() {
		entityManager.flush();
	}
}
