/**
 * 
 */
package br.com.easygame.enuns;

/**
 * @author alexandre
 *
 */
public enum TipoUsuario {
	
	TECNICO("Administrador Time"),
	JOGADOR("Usuario Comum");
	
	private TipoUsuario(String descricao) {
		this.descricao = descricao;
	}
	private final String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
}
