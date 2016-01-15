package br.com.easygame.enuns;

public enum TipoEvento {

	JOGO("Jogo"), OUTROS("Outros ");

	private TipoEvento(String descricao) {
		this.descricao = descricao;
	}

	private final String descricao;

	public String getDescricao() {
		return descricao;
	}

}
