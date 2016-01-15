package br.com.easygame.enuns;

public enum SimNao {
	/**
	 * Não.
	 */
	NAO("Não"),
	/**
	 * Sim
	 */
	SIM("Sim");

	private final String descricao;

	private SimNao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtem o valor de descricao.
	 * 
	 * @return O valor de descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

}
