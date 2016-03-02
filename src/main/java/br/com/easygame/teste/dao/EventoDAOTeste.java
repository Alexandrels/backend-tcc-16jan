package br.com.easygame.teste.dao;

import java.util.List;

import javax.json.JsonObject;
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
		// entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void salvarEvento() {
		Local local = new Local();
		local.setEndereco("Rua Da Cagurer");
		local.setNomeLocal("Quadra Sitio Cerejeira");
		local.setProprietario("Ganzales");

		Equipe equipe = equipeDAO.pesquisarPorId(1l);
		Usuario usuario = usuarioDAO.pesquisarPorId(2l);

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

	/**
	 * {"descricao":"Churrasco bom chimarrão!","dataHora":"28/01/2016 21:42:54"
	 * ,"tipo":0,"usuario":3, "local":{"nomeLocal":"Sitio Cerejeira","endereco":
	 * "Rua Padre Domingos Marine","proprietario":"Tiago"}, "equipes":[1],
	 * "usuarios":[1,2,6]}
	 */
	@Test
	public void salvarEventoRecebendoJson() {
		JsonObject eventoJSON = criarJSONEvento();
		eventoDAO.salvar(Evento.toEvento(eventoJSON));
		eventoDAO.flush();

	}

	public JsonObject criarJSONEvento() {
		Local local = new Local();
		local.setEndereco("Rua Rui Puppi, 524");
		local.setNomeLocal("Sitio Cerejeira");
		local.setProprietario("Tiago");

		Equipe equipe = equipeDAO.pesquisarPorId(1l);
		Usuario usuario = usuarioDAO.pesquisarPorId(3l);

		List<Usuario> todos = usuarioDAO.listarTodos();
		todos.remove(usuario);

		Evento evento = new Evento();
		evento.setUsuario(usuario);
		evento.setLocal(local);
		evento.setDataHora(LocalDateTime.now().toDate());
		evento.setDescricao("Churrasco bom chimarrão!");
		evento.adcionarEquipe(equipe);
		evento.adicionarUsuarios(todos);
		evento.setTipoEvento(TipoEvento.JOGO);
		return evento.toJSON();

	}

	@Test
	public void listarEventoPorId() {
		Evento evento = eventoDAO.pesquisarPorId(2l);
		System.out.println(evento.toString());
	}

	@Test
	public void listarEventos() {
		List<Evento> listar = eventoDAO.listar();
		System.out.println(listar.size());
	}
}
