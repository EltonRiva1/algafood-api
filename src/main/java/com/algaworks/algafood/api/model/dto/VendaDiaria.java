package com.algaworks.algafood.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VendaDiaria {
	private LocalDate data;
	private Long totalVendas;
	private BigDecimal totalFaturado;

	public VendaDiaria(LocalDate data, Long totalVendas, BigDecimal totalFaturado) {
		this.data = data;
		this.totalVendas = totalVendas;
		this.totalFaturado = totalFaturado;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Long getTotalVendas() {
		return totalVendas;
	}

	public void setTotalVendas(Long totalVendas) {
		this.totalVendas = totalVendas;
	}

	public BigDecimal getTotalFaturado() {
		return totalFaturado;
	}

	public void setTotalFaturado(BigDecimal totalFaturado) {
		this.totalFaturado = totalFaturado;
	}
}
