package com.indra.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "historico")
public class Historico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "data_coleta", nullable = false)
	private LocalDate dataColeta;

	@Column(name = "valor_venda")
	private BigDecimal valorVenda = BigDecimal.ZERO;

	@Column(name = "valor_compra")
	private BigDecimal valorCompra = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "produto")
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "revendedora")
	private Revendedora revendedora;

}
