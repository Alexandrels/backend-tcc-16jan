/**
 *
 */
package br.com.easygame.conexao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * 
 * @author mobilesys.alexandre
 *
 */
@ApplicationScoped
public class ProducerEntityManager {
	
	public final static String UNIT_NAME = "easy-game";
	@PersistenceUnit(unitName = UNIT_NAME, name = UNIT_NAME)
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@Default
	@RequestScoped
	public EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public void dispose(@Disposes @Default EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}

}
