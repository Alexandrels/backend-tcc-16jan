package br.com.easygame.teste.service.entity;

import java.util.Arrays;

import org.junit.Test;

import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.SimNao;
import br.com.easygame.enuns.TipoPosicao;
import br.com.easygame.enuns.TipoUsuario;

public class UsuarioTeste {

	@Test
	public void usuarioParaJson() {
		Usuario usuario = new Usuario();
		usuario.setNome("Murici");
		usuario.setApelido("Murici");
		usuario.setFacebook(SimNao.NAO);
		usuario.setLogin("pepe");
		usuario.setTipoPosicao(TipoPosicao.EXTRA_CAMPO);
		usuario.setSenha("1");
		usuario.salvarTipoUsuario(Arrays.asList(TipoUsuario.TECNICO));
		System.out.println(usuario.toJSON());

	}

}
