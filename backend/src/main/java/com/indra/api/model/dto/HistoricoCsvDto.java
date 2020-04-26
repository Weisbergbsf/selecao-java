package com.indra.api.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indra.api.model.enuns.RegiaoEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoCsvDto {

	private String regiao;
	private String siglaEstado;
	private String municipio;
	private String nomeRevendedora;
	private String cnpjRevendedora;
	private String nomeProduto;
	private LocalDate dataColeta;
	private BigDecimal valorVenda;
	private BigDecimal valorCompra;
	private String unidadeMedida;
	private String bandeira;

	@JsonIgnore
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public HistoricoCsvDto(long total) {}

	public HistoricoCsvDto(RegiaoEnum regiao, String siglaEstado, String municipio, String nomeRevendedora,
			String cnpjRevendedora, String nomeProduto, LocalDate dataColeta, BigDecimal valorVenda,
			BigDecimal valorCompra, String unidadeMedida, String bandeira) {
		this.regiao = regiao.getSigla();
		this.siglaEstado = siglaEstado;
		this.municipio = municipio;
		this.nomeRevendedora = nomeRevendedora;
		this.cnpjRevendedora = cnpjRevendedora;
		this.nomeProduto = nomeProduto;
		this.dataColeta = dataColeta;
		this.valorVenda = valorVenda;
		this.valorCompra = valorCompra;
		this.unidadeMedida = unidadeMedida;
		this.bandeira = bandeira;
	}

	public HistoricoCsvDto(String regiao, String siglaEstado, String municipio, String nomeRevendedora,
			String cnpjRevendedora, String nomeProduto, String dataColeta, String valorVenda, String valorCompra,
			String unidadeMedida, String bandeira) {
		super();
		this.regiao = regiao;
		this.siglaEstado = siglaEstado;
		this.municipio = municipio;
		this.nomeRevendedora = nomeRevendedora;
		this.cnpjRevendedora = cnpjRevendedora;
		this.nomeProduto = nomeProduto;
		this.dataColeta = LocalDate.parse(dataColeta, df);
		this.valorVenda = conveteStringParaBigDecimal(valorVenda.isEmpty() ? "0,0" : valorVenda);
		this.valorCompra = conveteStringParaBigDecimal(valorCompra.isEmpty() ? "0,0" : valorCompra);
		this.unidadeMedida = unidadeMedida;
		this.bandeira = bandeira;
	}

	private BigDecimal conveteStringParaBigDecimal(String valor) {
		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		double numero = 0.0;
		try {
			numero = nf.parse(valor).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new BigDecimal(numero).setScale(2, RoundingMode.HALF_EVEN);
	}

}
