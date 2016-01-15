/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.easygame.entity.UsuarioEquipe;
import br.com.easygame.entity.UsuarioEquipe;

/**
 * @author mobilesys.alexandre
 * 
 */
public class UsuarioEquipeDAO {

	EntityManager entityManager;

	public UsuarioEquipeDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UsuarioEquipeDAO() {

	}

	// um detalhe que esqueci, sempre que usar um EntityManager, depois de
	// utilizar ele SEMPRE tem que
	// fechar ele, se não fechar ele fica mantendo uma conexão com o banco
	// preza, então o correto é fazer
	// do jeito que vou fazer aqui
	public void salvar(UsuarioEquipe usuarioEquipe) {
		entityManager.persist(usuarioEquipe);// usa o entityManager pra fazer a
										// operação no banco
	}

	public void editar(UsuarioEquipe usuarioEquipe) {
		entityManager.merge(usuarioEquipe);// usa o entityManager pra fazer a operação
									// no banco
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
