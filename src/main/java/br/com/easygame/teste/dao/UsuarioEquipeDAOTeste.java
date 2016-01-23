package br.com.easygame.teste.dao;

import java.io.StringReader;
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

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.dao.UsuarioEquipeDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.entity.UsuarioEquipe;
import br.com.easygame.enuns.TipoPosicao;

public class UsuarioEquipeDAOTeste {
	
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
		// entityManager.getTransaction().rollback();
		entityManager.close();
	}
	
	@Test
	public void salvarUsuarioEquipe() {
		UsuarioEquipe usuarioEquipe = new UsuarioEquipe();
		Equipe equipe = equipeDAO.pesquisarPorId(2l);
		Usuario usuario = usuarioDAO.pesquisarPorId(5l);
		
		usuarioEquipe.setEquipe(equipe);
		usuarioEquipe.setUsuario(usuario);
		usuarioEquipe.setDataContratacao(LocalDate.now().toDate());
		usuarioEquipe.setPosicao(TipoPosicao.ZAGUEIRO);
		
		usuarioEquipeDAO.salvar(usuarioEquipe);
		usuarioEquipeDAO.flush();
	}
	
	@Test
	public void listarJogadoresDaEquipe() {
		Equipe equipe = equipeDAO.pesquisarPorId(4l);
		System.out.println("Equipe " + equipe.getNome());
		List<UsuarioEquipe> jogadores = usuarioEquipeDAO.listar(equipe);
		if (CollectionUtils.isNotEmpty(jogadores)) {
			for (UsuarioEquipe jogador : jogadores) {
				System.out.println(
						"Nome: " + jogador.getUsuario().getNome() + " Posição: " + jogador.getPosicao().getDescricao());
						
			}
			
		}
	}
	
	@Test
	public void listarJogadoresConvitesPendetes() {
		List<UsuarioEquipe> pendentes = usuarioEquipeDAO.listarUsuariosConvitesPendentes();
		System.out.println("Convites pendentes");
		for (UsuarioEquipe usuarioEquipe : pendentes) {
			System.out.println(
					String.format("Jogador: %s Time: %s", usuarioEquipe.getUsuario().getNome(), usuarioEquipe.getEquipe().getNome()));
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
