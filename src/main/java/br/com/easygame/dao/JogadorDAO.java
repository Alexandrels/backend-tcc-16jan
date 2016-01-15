/**
 * 
 */
package br.com.easygame.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.easygame.entity.Jogador;

/**
 * @author Alexandre
 *
 */
public class JogadorDAO {
	
	EntityManager entityManager;

	public JogadorDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void salvar(Jogador jogador) {
		// cria um entityManager
		// é por isso que aqui não funciona pq ele tenta pegar um outro
		// entityManager que só funciona no servidor
		entityManager.persist(jogador);// usa o entityManager pra fazer a
										// operação no banco
	}

	public void alterar(Jogador jogador) {
		entityManager.merge(jogador);
	}

	public Jogador pesquisarPorId(Long id) {
		return entityManager.find(Jogador.class, id);
	}

	/**
	 * LIsta todos os itens
	 * 
	 * @param cliente
	 * @return
	 */
	public List<Jogador> listar() {
		try {
			// cria um entityManager
			StringBuilder builder = new StringBuilder("SELECT i FROM Jogador i ");
			// usa o entityManager
			return entityManager.createQuery(builder.toString(), Jogador.class).getResultList();
		} catch (Exception e) {
			return new ArrayList<Jogador>();
		}
	}

}