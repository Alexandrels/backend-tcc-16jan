package br.com.easygame.teste.dao;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.enuns.TipoUsuario;

public class UsuarioDAOTeste {
	private EntityManager entityManager;
	private EquipeDAO equipeDAO;
	private int cont = 0;
	private UsuarioDAO usuarioDAO;

	@Before
	public void antes() {
		entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();

		equipeDAO = new EquipeDAO(entityManager);
		usuarioDAO = new UsuarioDAO(entityManager);
	}

	@After
	public void depois() {
		entityManager.getTransaction().commit();
		// entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void salvarUsuarioTipoJogador() {
		Usuario usuario = new Usuario();
		usuario.setNome("Joao");
		usuario.setApelido("joao");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("joao");
		usuario.setTipoPosicao(TipoPosicao.ATACANTE);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
		usuarioDAO.salvar(usuario);
		usuarioDAO.flush();
		System.out.println(usuario.toJSON());
	}
	@Test
	public void alterarUsuarioComMaisTipos() {
		Usuario usuario = usuarioDAO.pesquisarPorId(1l);
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
		usuarioDAO.editar(usuario);
		usuarioDAO.flush();
		System.out.println(usuario.toJSON());
	}

	public JsonObject criarJSONUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("Joao");
		usuario.setApelido("Joao");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("joao");
		usuario.setTipoPosicao(TipoPosicao.GOLEIRO);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
		return usuario.toJSON();

	}

	/**
<<<<<<< HEAD
	 * teste 
=======
	 * {"nome":"Cristiano","apelido":"Fuba","login":"fuba","senha":"1",
	 * "latitude":0.0,"longitude":0.0,"facebook":0,"posicao":0,"tipoUsuario":"1;"}
>>>>>>> branch 'master' of https://github.com/yeahsistemas/backend-tcc-16jan.git
	 */
	@Test
	public void salvarUsuarioTipoJogadorComJson() { 
		JsonObject jsonUsuario = criarJSONUsuario();
		Usuario usuario = Usuario.toUsuario(jsonUsuario);
		
		usuarioDAO.salvar(usuario);
		usuarioDAO.flush();
	}

	@Test
	public void listarUsuarioJogador() {
		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.JOGADOR);
		for (Usuario jogador : jogadores) {
			System.out.println(String.format("Nome: %s - Posição: %s ", jogador.getApelido(),
					jogador.getTipoPosicao().getDescricao()));

		}

	}

	@Test
	public void listarUsuarioTecnico() {
		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.TECNICO);
		for (Usuario jogador : jogadores) {
			System.out.println(String.format("Nome: %s - Posição: %s ", jogador.getApelido(),
					jogador.getTipoPosicao().getDescricao()));

		}

	}

	@Test
	public void lerObjetoJsonComArrayDentro() {
		try {
			JsonObjectBuilder timeJson = Json.createObjectBuilder();
			timeJson.add("nome", "Aranhas Pretas");
			// aqui um exemplo de como retornar todos os usuarios com JSON
			UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);
			List<Usuario> usuarios = usuarioDAO.listarTodos();
			JsonArrayBuilder jogadoresJson = Json.createArrayBuilder();
			cont = 0;
			for (Usuario usuario : usuarios) {
				if (cont < 4) {
					jogadoresJson.add(Json.createObjectBuilder().add("id", usuario.getId().toString()));
					cont++;
				}
			}
			timeJson.add("jogadores", jogadoresJson.build());

			JsonReader jsonReader = Json.createReader(new StringReader(timeJson.build().toString()));
			JsonObject jsonObject = jsonReader.readObject();
			String nome = jsonObject.getString("nome");
			if (nome != null) {
				Equipe equipe = new Equipe();
				equipe.setNome(nome);
				// equipe.setJogadores(new ArrayList<Jogador>());
				// if (!jsonObject.getJsonArray("jogadores").isEmpty()) {
				// for (JsonValue jogadorJson :
				// jsonObject.getJsonArray("jogadores")) {
				// JsonReader reader = Json.createReader(new
				// StringReader(jogadorJson.toString()));
				// JsonObject objeto = reader.readObject();
				// Jogador jogador =
				// jogadorDAO.pesquisarPorId(Long.valueOf(objeto.getString("id")));
				// equipe.getJogadores().add(jogador);
				// }
				// }
				equipeDAO.salvar(equipe);
				System.out.println("Equipe salva " + equipe.toString());
			}

		} catch (JsonException e) {
			System.out.println(e.getStackTrace());
		}
	}

	@Test
	public void listarUsuarioPorId() {
		Usuario usuario = usuarioDAO.pesquisarPorId(1l);
		System.out.println(usuario.toString());
	}

}
