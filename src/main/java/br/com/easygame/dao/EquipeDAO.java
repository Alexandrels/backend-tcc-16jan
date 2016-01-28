/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;

/**
 * @author mobilesys.alexandre
 * 
 */
public class EquipeDAO {

	EntityManager entityManager;

	@Inject
	public EquipeDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EquipeDAO() {

	}

	// um detalhe que esqueci, sempre que usar um EntityManager, depois de
	// utilizar ele SEMPRE tem que
	// fechar ele, se não fechar ele fica mantendo uma conexão com o banco
	// preza, então o correto é fazer
	// do jeito que vou fazer aqui
	public void salvar(Equipe equipe) {
		entityManager.persist(equipe);// usa o entityManager pra fazer a
										// operação no banco
	}

	public void editar(Equipe equipe) {
		entityManager.merge(equipe);// usa o entityManager pra fazer a operação
									// no banco
	}

	// aqui um exemplo de como listar todos os usuarios
	public List<Equipe> listar() {
		try {
			// cria um entityManager
			StringBuilder builder = new StringBuilder("SELECT u FROM Equipe u ");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), Equipe.class).getResultList();
		} catch (Exception e) {
			return new ArrayList<Equipe>();
		}
	}

	public Equipe pesquisarPorId(Long id) {
		try {
			StringBuilder builder = new StringBuilder("SELECT u FROM Equipe u ")
					.append(" WHERE u.id = :id ");
			return entityManager.createQuery(builder.toString(), Equipe.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

	public boolean existeEquipe(Equipe equipe) {
		try {
			StringBuilder builder = new StringBuilder(" SELECT u.id FROM Equipe u ").append(" WHERE u.nome =  :nome")
					.append(" AND u.usuario = :usuario ");
			entityManager.createQuery(builder.toString(), Long.class).setParameter("nome", equipe.getNome())
					.setParameter("usuario", equipe.getUsuario()).setMaxResults(1).getSingleResult();
			return true;

		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void flush() {
		entityManager.flush();
	}
}
