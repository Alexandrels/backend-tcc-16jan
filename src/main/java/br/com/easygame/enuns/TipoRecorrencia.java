package br.com.easygame.enuns;

public enum TipoRecorrencia {
	DIARIA("Diária"),
	SEMANAL("Semanal"),
	QUINZENAL("Quinzenal"),
	MENSAL("Mensal")
	;
	
	private TipoRecorrencia(String descricao) {
		this.descricao = descricao;
	}
	private final String descricao;
	
	public String getDescricao() {
		return descricao;
	}

}
