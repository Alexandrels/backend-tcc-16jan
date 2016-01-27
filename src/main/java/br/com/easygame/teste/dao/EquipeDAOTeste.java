package br.com.easygame.teste.dao;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.dao.UsuarioEquipeDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.TipoUsuario;

public class EquipeDAOTeste {
	private EntityManager entityManager;
	private EquipeDAO equipeDAO;
	private UsuarioDAO usuarioDAO;
	private UsuarioEquipeDAO usuarioEquipeDAO;
	private int cont = 0;

	@Before
	public void antes() {
		entityManager = Persistence.createEntityManagerFactory("easy-game-local").createEntityManager();
		entityManager.getTransaction().begin();
		usuarioDAO = new UsuarioDAO(entityManager);
		equipeDAO = new EquipeDAO(entityManager);
		usuarioEquipeDAO = new UsuarioEquipeDAO(entityManager);

	}

	@After
	public void depois() {
		//entityManager.getTransaction().commit();
		 entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void salvarEquipe() {
		Equipe equipe = new Equipe();
		equipe.setNome("Barcelona");
		equipe.setDataFundacao(LocalDate.now().toDate());

		equipeDAO.salvar(equipe);

	}

	// teste commit do linux
	@Test
	public void salvarEquipeComJogadores() {
		Equipe equipe = new Equipe();
		equipe.setNome("Clube do Remo");
		equipe.setUsuario(new Usuario(1l));
		equipe.setDataFundacao(LocalDate.now().toDate());

		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.JOGADOR);
		if (CollectionUtils.isNotEmpty(jogadores)) {
			for (Usuario usuario : jogadores) {
				equipe.adicionarUsuario(usuario, usuario.getTipoPosicao());
			}
		}
		equipeDAO.salvar(equipe);
		equipeDAO.flush();

	}

	/**
	 * {"nome":"Clube do Remo","data_fundacao":"23/01/2016","usuario":2}
	 */
	@Test
	public void salvarEquipeSemJogadoresRecebeJSON() {
		JsonObject jsonEquipe = criarEquipeSEMJogadoresJSON();
		Equipe equipe = new Equipe();
		equipe.toEquipe(jsonEquipe);
		equipeDAO.salvar(equipe);
		equipeDAO.flush();

	}

	public JsonObject criarEquipeSEMJogadoresJSON() {
		Equipe equipe = new Equipe();
		equipe.setNome("Clube do Remo");
		equipe.setDataFundacao(LocalDate.now().toDate());
		equipe.setUsuario(new Usuario(2l));

		return equipe.toJSON();

	}

	/**
	 * {"id":1,"nome":"Clube do Remo","dataFundacao":"23/01/2016","usuario":2,
	 * "listaUsuarioEquipe":[
	 * {"usuario":1,"equipe":1,"posicao":0,"dataContratacao":"23/01/2016"},
	 * {"usuario":2,"equipe":1,"posicao":5,"dataContratacao":"23/01/2016"},
	 * {"usuario":3,"equipe":1,"posicao":1,"dataContratacao":"23/01/2016"}]}
	 */
	@Test
	public void salvarEquipeCOMJogadoresRecebeJSON() {
		JsonObject jsonEquipe = criarEquipeCOMJogadoresJSON();
		Equipe equipe = new Equipe();
		equipe.toEquipe(jsonEquipe);
		equipeDAO.editar(equipe);
		equipeDAO.flush();

	}

	public JsonObject criarEquipeCOMJogadoresJSON() {
		Equipe equipe = equipeDAO.pesquisarPorId(1l);
		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.JOGADOR);
		if (CollectionUtils.isNotEmpty(jogadores)) {
			for (Usuario usuario : jogadores) {
				equipe.adicionarUsuario(usuario, usuario.getTipoPosicao());
			}
		}
		return equipe.toJSON();

	}
	
	@Test
	public void salvarEquipeNovaCOMJogadoresRecebeJSON() {
		JsonObject jsonEquipe = criarEquipeNovaCOMJogadoresJSON();
		Equipe equipe = Equipe.toEquipe(jsonEquipe);
		equipeDAO.salvar(equipe);
		equipeDAO.flush();

	}
	public JsonObject criarEquipeNovaCOMJogadoresJSON() {
		Equipe equipe = new Equipe();
		equipe.setNome("Equipe Nova");
		equipe.setDataFundacao(new Date());
		equipe.setUsuario(new Usuario(1l));
		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.JOGADOR);
		if (CollectionUtils.isNotEmpty(jogadores)) {
			for (Usuario usuario : jogadores) {
				equipe.adicionarUsuario(usuario, usuario.getTipoPosicao());
			}
		}
		return equipe.toJSON();

	}

	@Test
	public void listarEquipe() {
		Equipe equipe = equipeDAO.pesquisarPorId(1l);
		System.out.println("Equipe " + equipe.getNome());
		System.out.println("Jogadores");
		// for (Jogador jogador : equipe.getJogadores()) {
		// System.out.println("Nome: " + jogador.getNome() + " Posição: " +
		// jogador.getPosicao());
		//
		// }
	}

	@Test
	public void existeEquipeCOmNOmeEUsarioIgual() {
		Equipe equipe = new Equipe();
		equipe.setNome("Barcelona");
		equipe.setUsuario(new Usuario(1l));

		Assert.assertTrue(equipeDAO.existeEquipe(equipe));
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
				if (!jsonObject.getJsonArray("jogadores").isEmpty()) {
					for (JsonValue jogadorJson : jsonObject.getJsonArray("jogadores")) {
						JsonReader reader = Json.createReader(new StringReader(jogadorJson.toString()));
						JsonObject objeto = reader.readObject();
						// Jogador jogador =
						// jogadorDAO.pesquisarPorId(Long.valueOf(objeto.getString("id")));
						// equipe.getJogadores().add(jogador);
					}
				}
				equipeDAO.salvar(equipe);
				System.out.println("Equipe salva " + equipe.toString());
			}

		} catch (JsonException e) {
			System.out.println(e.getStackTrace());
		}
	}

}
