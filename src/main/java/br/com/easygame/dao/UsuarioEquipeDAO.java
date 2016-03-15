/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;

import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.entity.UsuarioEquipe;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.entity.UsuarioEquipe;

/**
 * @author mobilesys.alexandre
 * 
 */
public class UsuarioEquipeDAO {

	EntityManager entityManager;

	@Inject
	public UsuarioEquipeDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UsuarioEquipeDAO() {

	}

	public void salvar(UsuarioEquipe usuarioEquipe) {
		entityManager.persist(usuarioEquipe);
	}

	public void salvar(List<UsuarioEquipe> listaUsuarioEquipe) {
		if (CollectionUtils.isNotEmpty(listaUsuarioEquipe)) {
			for (UsuarioEquipe usuarioEquipe : listaUsuarioEquipe) {
				salvar(usuarioEquipe);
			}
		}
	}

	public void editar(UsuarioEquipe usuarioEquipe) {
		entityManager.merge(usuarioEquipe);
	}

	/**
	 * Listar {@link Usuario} jogador da {@link Equipe}
	 */
	public List<UsuarioEquipe> listar(Equipe equipe) {
		try {
			// cria um entityManager
			StringBuilder jpql = new StringBuilder("SELECT u FROM UsuarioEquipe u ")
					.append(" WHERE u.equipe = :equipe ");
			TypedQuery<UsuarioEquipe> tq = entityManager.createQuery(jpql.toString(), UsuarioEquipe.class);
			tq.setParameter("equipe", equipe);
			List<UsuarioEquipe> resultList = tq.getResultList();

			return resultList;
		} catch (Exception e) {
			return new ArrayList<UsuarioEquipe>();
		}
	}
	
	public List<UsuarioEquipe> listarUsuariosConvitesPendentes() {
		try {
			// cria um entityManager
			StringBuilder jpql = new StringBuilder("SELECT u FROM UsuarioEquipe u ")
					.append(" WHERE u.pendente = :sim ");
			TypedQuery<UsuarioEquipe> tq = entityManager.createQuery(jpql.toString(), UsuarioEquipe.class);
			tq.setParameter("sim", SimNao.SIM);
			List<UsuarioEquipe> resultList = tq.getResultList();

			return resultList;
		} catch (Exception e) {
			return new ArrayList<UsuarioEquipe>();
		}
	}
	public List<UsuarioEquipe> listarUsuariosConvitesPendentes(Equipe equipe) {
		try {
			// cria um entityManager
			StringBuilder jpql = new StringBuilder("SELECT u FROM UsuarioEquipe u ")
					.append(" WHERE u.pendente = :sim ")
					.append(" AND u.equipe = :equipe");
			TypedQuery<UsuarioEquipe> tq = entityManager.createQuery(jpql.toString(), UsuarioEquipe.class);
			tq.setParameter("sim", SimNao.SIM)
			.setParameter("equipe", equipe);
			List<UsuarioEquipe> resultList = tq.getResultList();

			return resultList;
		} catch (Exception e) {
			return new ArrayList<UsuarioEquipe>();
		}
	}
	
	public List<UsuarioEquipe> listarUsuariosConvitesPendentes(Usuario usuario) {
		try {
			// cria um entityManager
			StringBuilder jpql = new StringBuilder("SELECT u FROM UsuarioEquipe u ")
					.append(" WHERE u.pendente = :sim ")
					.append(" AND u.usuario = :usuario");
			TypedQuery<UsuarioEquipe> tq = entityManager.createQuery(jpql.toString(), UsuarioEquipe.class);
			tq.setParameter("sim", SimNao.SIM)
			.setParameter("usuario", usuario);
			List<UsuarioEquipe> resultList = tq.getResultList();

			return resultList;
		} catch (Exception e) {
			return new ArrayList<UsuarioEquipe>();
		}
	}

	// aqui um exemplo de como listar todos os usuarios
	public List<UsuarioEquipe> listar() {
		try {
			// cria um entityManager
			StringBuilder builder = new StringBuilder("SELECT u FROM UsuarioEquipe u ");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), UsuarioEquipe.class).getResultList();
		} catch (Exception e) {
			return new ArrayList<UsuarioEquipe>();
		}
	}

	public UsuarioEquipe pesquisarPorId(Long id) {
		return entityManager.find(UsuarioEquipe.class, id);
	}

	public void flush() {
		entityManager.flush();
	}
}
