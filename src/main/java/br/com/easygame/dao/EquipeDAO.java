/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.easygame.conexao.ProducerEntityManager;
import br.com.easygame.entity.Equipe;

/**
 * @author mobilesys.alexandre
 * 
 */
public class EquipeDAO {

	EntityManager entityManager;

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
		return entityManager.find(Equipe.class, id);
	}
}
