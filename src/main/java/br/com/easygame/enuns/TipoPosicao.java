/**
 * 
 */
package br.com.easygame.enuns;

/**
 * @author alexandre
 *
 */
public enum TipoPosicao {

	ATACANTE("Atacante"), GOLEIRO("Goleiro"), 
	MEIO_CAMPO("Meio Campo"), 
	LATERAL("Lateral"), 
	VOLANTE("Volante"), 
	ZAGUEIRO("Zagueiro"), 
	EXTRA_CAMPO("Extra Campo");
	private final String descricao;

	private TipoPosicao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
