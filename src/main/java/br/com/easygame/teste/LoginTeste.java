package br.com.easygame.teste;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.com.easygame.dao.UsuarioDAO;

public class LoginTeste {

	/**
	 * @param args
	 * @throws PortalException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		EntityManager entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();

		UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);

		String login = "ale@ale.com.br";
		String senha = "joao";
		boolean autenticar = usuarioDAO.autenticar(login, senha);

		if (autenticar) {
			System.out.println("Logou");

		} else {
			System.out.println("Não logou");
		}

		entityManager.close();
	}

}
