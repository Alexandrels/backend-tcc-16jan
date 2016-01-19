package br.com.easygame.teste.service.entity;

import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.dao.UsuarioEquipeDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.enuns.TipoUsuario;

public class EquipeTeste {
	
	private EntityManager entityManager;
	private EquipeDAO equipeDAO;
	private UsuarioDAO usuarioDAO;
	private UsuarioEquipeDAO usuarioEquipeDAO;

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
	public void equipeParaJson() {
		Equipe equipe = equipeDAO.pesquisarPorId(1l);
		
		System.out.println(equipe.toJSON());
		
	}
	
	@Test
	public void usuarioJSONParaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("Rivaldo");
		usuario.setApelido("rivaldo");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("rivaldo");
		usuario.setTipoPosicao(TipoPosicao.ATACANTE);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.JOGADOR));
		JsonObject usuarioJSon = usuario.toJSON();
		
		Usuario usuarioNovo = new Usuario();
		usuarioNovo = usuarioNovo.toUsuario(usuarioJSon);
		
		System.out.println(usuarioNovo.toString());
		
	}
	
}
