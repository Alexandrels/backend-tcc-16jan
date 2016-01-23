package br.com.easygame.teste.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.EventoDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Evento;
import br.com.easygame.entity.Local;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.TipoEvento;

public class EventoDAOTeste {
	private EntityManager entityManager;
	private EquipeDAO equipeDAO;
	private UsuarioDAO usuarioDAO;
	private EventoDAO eventoDAO;

	@Before
	public void antes() {
		entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();

		equipeDAO = new EquipeDAO(entityManager);
		usuarioDAO = new UsuarioDAO(entityManager);
		eventoDAO = new EventoDAO(entityManager);
	}

	@After
	public void depois() {
		 entityManager.getTransaction().commit();
		//entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void salvarEvento() {
		Local local = new Local();
		local.setEndereco("Rua Da Bambulha");
		local.setNomeLocal("Quandra Sport All");
		local.setProprietario("Juarez");

		Equipe equipe = equipeDAO.pesquisarPorId(5l);
		Usuario usuario = usuarioDAO.pesquisarPorId(5l);

		List<Usuario> todos = usuarioDAO.listarTodos();
		todos.remove(usuario);

		Evento evento = new Evento();
		evento.setUsuario(usuario);
		evento.setLocal(local);
		evento.setDataHora(LocalDateTime.now().toDate());
		evento.setDescricao("Vaiter churrasco depois do jogo");
		evento.adcionarEquipe(equipe);
		evento.adicionarUsuarios(todos);
		evento.setTipoEvento(TipoEvento.JOGO);
		
		eventoDAO.salvar(evento);
		eventoDAO.flush();

	}
}
