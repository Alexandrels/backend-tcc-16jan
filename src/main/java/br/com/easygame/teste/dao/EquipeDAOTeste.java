package br.com.easygame.teste.dao;

import java.io.StringReader;
import java.util.ArrayList;
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
import org.junit.Before;
import org.junit.Test;

import com.sun.faces.util.CollectionsUtils;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.dao.UsuarioEquipeDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.entity.UsuarioEquipe;
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
		entityManager.getTransaction().commit();
		//entityManager.getTransaction().rollback();
		entityManager.close();
	}

	@Test
	public void salvarEquipe() {
		Equipe equipe = new Equipe();
		equipe.setNome("Barcelona");
		equipe.setDataFundacao(LocalDate.now().toDate());

		equipeDAO.salvar(equipe);

	}
//teste commit do linux
	@Test
	public void salvarEquipeComJogadores() {
		Equipe equipe = new Equipe();
		equipe.setNome("Barcelona");
		equipe.setDataFundacao(LocalDate.now().toDate());
		equipeDAO.salvar(equipe);
		equipeDAO.flush();

		List<Usuario> jogadores = usuarioDAO.listar(TipoUsuario.JOGADOR);
		List<UsuarioEquipe> listaUsuarioEquipe = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(jogadores)) {
			for (Usuario usuario : jogadores) {
				UsuarioEquipe usuarioEquipe = new UsuarioEquipe();
				usuarioEquipe.setEquipe(equipe);
				usuarioEquipe.setUsuario(usuario);
				usuarioEquipe.setPosicao(usuario.getTipoPosicao());
				usuarioEquipe.setDataContratacao(LocalDate.now().toDate());

				listaUsuarioEquipe.add(usuarioEquipe);
			}
			usuarioEquipeDAO.salvar(listaUsuarioEquipe);
		}

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
