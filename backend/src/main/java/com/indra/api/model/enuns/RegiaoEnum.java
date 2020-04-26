package com.indra.api.model.enuns;

public enum RegiaoEnum {

	N("N", "Norte"), S("S", "Sul"), CO("CO", "Centro Oeste"), NE("NE", "Nordeste"), SE("SE", "Sudeste");

	private String sigla;
	private String descricao;

	private RegiaoEnum(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static RegiaoEnum toEnum(String sigla) {
		if (sigla == null || sigla.isEmpty()) {
			return null;
		}
		for (RegiaoEnum regiao : RegiaoEnum.values()) {
			sigla = sigla.trim();
			if (sigla.equals(regiao.getSigla())) {
				return regiao;
			}
		}
		throw new IllegalArgumentException("Sigla inválida: " + sigla);
	}
	
	public static String toEnumString(String sigla) {
		if (sigla == null || sigla.isEmpty()) {
			return null;
		}
		for (RegiaoEnum regiao : RegiaoEnum.values()) {
			sigla = sigla.trim();
			if (sigla.equals(regiao.getSigla())) {
				return sigla;
			}
		}
		throw new IllegalArgumentException("Sigla inválida: " + sigla);
	}

}
