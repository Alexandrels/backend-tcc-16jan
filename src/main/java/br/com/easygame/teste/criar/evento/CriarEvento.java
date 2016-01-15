/**
 * 
 */
package br.com.easygame.teste.criar.evento;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.EventoDAO;
import br.com.easygame.dao.JogadorDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Evento;
import br.com.easygame.entity.Jogador;
import br.com.easygame.entity.Local;
import br.com.easygame.entity.Notificacao;
import br.com.easygame.entity.Recorrencia;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.TipoEvento;

/**
 * @author Alexandre
 *
 */
public class CriarEvento {
	private EntityManager entityManager;
	private EventoDAO eventoDAO;
	private JogadorDAO jogadorDAO;
	private UsuarioDAO usuarioDAO;

	@Before
	public void antes() {
		entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();
		usuarioDAO = new UsuarioDAO(entityManager);
		eventoDAO = new EventoDAO(entityManager);
	}

	@After
	public void depois() {
		// entityManager.getTransaction().commit();
		entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void criarEvento() {
		Usuario usuario = usuarioDAO.pesquisarPorId(5l);
		Evento evento = new Evento();
		evento.setDescricao("Evento Teste");
		evento.setTipoEvento(TipoEvento.JOGO);
		evento.setDataHora(new LocalDateTime().toDate());
		evento.setUsuarios(new ArrayList<>(Arrays.asList(usuarioDAO.pesquisarPorId(6l))));
		//evento.setUsuario(usuario);

		Local local = new Local();
		local.setNomeLocal("Cancha do Kulik");
		local.setEndereco("Rua Rui Puppi");
		local.setNomeProprietario(usuario.getNome());

		Recorrencia recorrencia = new Recorrencia();
		
		Notificacao notificacao = new Notificacao();
		notificacao.setDataHora(new LocalDateTime().toDate());

		evento.setLocais(new ArrayList<>(Arrays.asList(local)));

		evento.setRecorrencias(new ArrayList<>(Arrays.asList(recorrencia)));

		eventoDAO.salvar(evento);
		eventoDAO.flush();
		System.out.println(evento.toString());

	}
	
	@Test
	public void recebimentoJsonEvento(){
		Evento evento = new Evento();
		evento.setDescricao("Evento Teste");
		evento.setTipoEvento(TipoEvento.JOGO);
		evento.setDataHora(new LocalDateTime().toDate());
		
		eventoDAO.salvar(evento);
		eventoDAO.flush();
		
		//System.out.println(evento.toJSON());
	}
}
