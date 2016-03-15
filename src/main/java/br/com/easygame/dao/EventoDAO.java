/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Evento;
import br.com.easygame.enuns.StatusEvento;

/**
 * @author mobilesys.alexandre
 * 
 */
public class EventoDAO {

	EntityManager entityManager;

	@Inject
	public EventoDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoDAO() {

	}

	public void apagar(Evento evento) {
		String sql = "delete from evento_has_equipe where id_equipe = ?";
		entityManager.createNativeQuery(sql).setParameter(1, evento.getId());
		entityManager.remove(evento);
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
			StringBuilder builder = new StringBuilder("SELECT u FROM Evento u ")
					.append(" WHERE u.statusEvento = :statusEvento");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), Evento.class)
					.setParameter("statusEvento", StatusEvento.ATIVO)
					.getResultList();
		} catch (Exception e) {
			return new ArrayList<Evento>();
		}
	}
	
	public Evento pesquisarPorId(Long id) {
		try {
			StringBuilder builder = new StringBuilder("SELECT u FROM Evento u ")
					.append(" WHERE u.id = :id ");
			return entityManager.createQuery(builder.toString(), Evento.class).setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	public List<Evento> listar(Equipe equipe) {
		try {
			// cria um entityManager
			StringBuilder builder = new StringBuilder("SELECT u FROM Evento u ")
					.append(" WHERE u.statusEvento = :statusEvento")
					.append(" AND u.equipes = :equipe ");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), Evento.class)
					.setParameter("statusEvento", StatusEvento.ATIVO)
					.setParameter("equipe",equipe)
					.getResultList();
		} catch (Exception e) {
			return new ArrayList<Evento>();
		}
	}

	public void flush() {
		entityManager.flush();
	}
}
