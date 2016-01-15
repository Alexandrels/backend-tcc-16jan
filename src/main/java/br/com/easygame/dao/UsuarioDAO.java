/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.TipoUsuario;

/**
 * @author mobilesys.alexandre
 * 
 */
public class UsuarioDAO {

	EntityManager entityManager;

	@Inject
	public UsuarioDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UsuarioDAO() {

	}

	public void flush() {
		entityManager.flush();
	}

	// um detalhe que esqueci, sempre que usar um EntityManager, depois de
	// utilizar ele SEMPRE tem que
	// fechar ele, se não fechar ele fica mantendo uma conexão com o banco
	// preza, então o correto é fazer
	// do jeito que vou fazer aqui
	public void salvar(Usuario usuario) {
		entityManager.persist(usuario);
	}

	public void editar(Usuario usuario) {
		entityManager.merge(usuario);
	}

	public void excluir(Usuario usuario) {
		entityManager.remove(usuario);
	}

	public boolean autenticar(String login, String senha) {
		try {
			StringBuilder builder = new StringBuilder("SELECT u FROM Usuario u ").append(" WHERE u.login = :login ")
					.append(" AND u.senha = :senha ");
			// usa o entityManager
			entityManager.createQuery(builder.toString(), Usuario.class).setParameter("login", login)
					.setParameter("senha", senha).getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// aqui um exemplo de como listar todos os usuarios
	public List<Usuario> listarTodos() {
		try {
			StringBuilder builder = new StringBuilder("SELECT u FROM Usuario u ");
			return entityManager.createQuery(builder.toString(), Usuario.class).getResultList();
		} catch (Exception e) {
			return new ArrayList<Usuario>();
		}
	}

	public Usuario pesquisarPorId(Long id) {
		try {
			StringBuilder builder = new StringBuilder("SELECT u FROM Usuario u ")
					.append(" WHERE u.id = :id ");
			return entityManager.createQuery(builder.toString(), Usuario.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

	public List<Usuario> listar(TipoUsuario tipoUsuario) {
		try {
			StringBuilder builder = new StringBuilder(" SELECT u FROM Usuario u ")
					.append(" WHERE u.tipoUsuario LIKE ")
					.append(" '%" + tipoUsuario.ordinal() + "%' ");
			return entityManager.createQuery(builder.toString(), Usuario.class)
					.getResultList();
		} catch (Exception e) {
			return new ArrayList<Usuario>();
		}
	}

	public boolean existeLogin(String login) {
		try {
			StringBuilder builder = new StringBuilder(" SELECT u.id FROM Usuario u ")
					.append(" WHERE u.login =  :login");
			entityManager.createQuery(builder.toString(), Long.class)
					.setParameter("login", login)
					.setMaxResults(1)
					.getSingleResult();
			return true;

		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
