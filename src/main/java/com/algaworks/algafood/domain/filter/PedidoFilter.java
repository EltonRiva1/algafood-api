package com.algaworks.algafood.domain.filter;

import java.time.OffsetDateTime;

public class PedidoFilter {
	private Long clienteId, restauranteId;
	private OffsetDateTime dataCriacaoInicio, dataCriacaoFim;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Long getRestauranteId() {
		return restauranteId;
	}

	public void setRestauranteId(Long restauranteId) {
		this.restauranteId = restauranteId;
	}

	public OffsetDateTime getDataCriacaoInicio() {
		return dataCriacaoInicio;
	}

	public void setDataCriacaoInicio(OffsetDateTime dataCriacaoInicio) {
		this.dataCriacaoInicio = dataCriacaoInicio;
	}

	public OffsetDateTime getDataCriacaoFim() {
		return dataCriacaoFim;
	}

	public void setDataCriacaoFim(OffsetDateTime dataCriacaoFim) {
		this.dataCriacaoFim = dataCriacaoFim;
	}
}
