/**
 * 
 */
package br.com.easygame.teste.service;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Jogador;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.enuns.TipoUsuario;
import br.com.easygame.servico.UsuarioService;

/**
 * @author alexandre
 *
 */
public class UsuarioServiceTeste {

	private EntityManager entityManager;
	private EquipeDAO equipeDAO;
	private UsuarioDAO usuarioDAO;
	private int cont = 0;

	@Before
	public void antes() {
		entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();

		equipeDAO = new EquipeDAO(entityManager);
		usuarioDAO = new UsuarioDAO(entityManager);
	}

	@After
	public void depois() {
//		entityManager.getTransaction().commit();
		entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void editarJogador() {
		Jogador jogador = new Jogador(1l);
		System.out.println("Jogador " + jogador.toString());

	}

	@Test
	public void cadastrarUsuarioTecnico() throws Exception {
		UsuarioService service = new UsuarioService(usuarioDAO);

		Usuario usuario = new Usuario();
		usuario.setNome("Joao");
		usuario.setApelido("jo");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("jo");
		usuario.setTipoPosicao(TipoPosicao.EXTRA_CAMPO);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.TECNICO));
		Response response = service.cadastrarUsuario(usuario.toJSON());

		System.out.println("Response: " + response.getEntity());
		System.out.println("Response: " + response.getLocation());

	}

	@Test
	public void cadastrarUsuarioJogador() throws Exception {
		try {
			UsuarioService service = new UsuarioService(usuarioDAO);

			Usuario usuario = new Usuario();
			usuario.setNome("Geraldo");
			usuario.setApelido("geraldo");
			usuario.setFacebook(SimNao.NAO);
			usuario.setLogin("geraldo");
			usuario.setTipoPosicao(TipoPosicao.MEIO_CAMPO);
			usuario.setSenha("1");
			usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
			Response response = service.cadastrarUsuario(usuario.toJSON());

			System.out.println("Response: " + response.getStatus());
			System.out.println("Response: " + response.getEntity());
			System.out.println("Response: " + response.getLocation());

		} catch (WebApplicationException e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	@Test
	public void editarUsuarioJogador() throws Exception {
		try {
			UsuarioService service = new UsuarioService(usuarioDAO);
			Usuario usuario = usuarioDAO.pesquisarPorId(11l);
			entityManager.detach(usuario);
			usuario.setNome("Geraldo");
			usuario.setApelido("geraldo");
			usuario.setFacebook(SimNao.NAO);
			usuario.setLogin("geraldo");
			usuario.setTipoPosicao(TipoPosicao.MEIO_CAMPO);
			usuario.setSenha("1");
			usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
			service.atualizarUsuario(usuario.getId().toString(), usuario.toJSON());

		} catch (WebApplicationException e) {
			System.out.println(e.getResponse().getStatus());
		}

	}

	@Test
	public void listarUsuarioPeloId() {
		UsuarioService service = new UsuarioService(usuarioDAO);
//		Usuario usuario = service.retornaUsuario(String.valueOf(1));
		Usuario usuario = service.retornaUsuario(String.valueOf(133));
		if (usuario != null) {
			System.out.println(usuario.toString());
		}

	}

	@Test
	public void listarUsuarioJogador() throws Exception {
		UsuarioService service = new UsuarioService(usuarioDAO);

		Usuario usuario = new Usuario();
		usuario.setNome("Alexandre");
		usuario.setApelido("ale");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("ale");
		usuario.setTipoPosicao(TipoPosicao.ZAGUEIRO);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
		Response response = service.cadastrarUsuario(usuario.toJSON());

		System.out.println("Response: " + response.getEntity());
		System.out.println("Response: " + response.getLocation());

	}

}
