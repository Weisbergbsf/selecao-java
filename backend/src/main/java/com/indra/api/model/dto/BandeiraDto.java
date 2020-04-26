package com.indra.api.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BandeiraDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bandeira;
	private BigDecimal valorVenda = BigDecimal.ZERO;
	private BigDecimal valorCompra = BigDecimal.ZERO;
	
	public BandeiraDto(long total) {}

	public BandeiraDto(String bandeira, double valorVenda, double valorCompra) {
		super();
		this.bandeira = bandeira;
		this.valorVenda = conveteDoubleParaBigDecimal(valorVenda);
		this.valorCompra = conveteDoubleParaBigDecimal(valorCompra);
	}

	private BigDecimal conveteDoubleParaBigDecimal(double valor) {
		return new BigDecimal(valor).setScale(2, RoundingMode.HALF_EVEN);
	}

}
