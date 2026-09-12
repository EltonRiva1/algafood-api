package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Estatísticas", description = "Consulta estatísticas do AlgaFood")
public interface EstatisticasControllerOpenApi {

	@Operation(summary = "Consulta vendas diárias", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	ResponseEntity<List<?>> consultarVendasDiarias(@ParameterObject VendaDiariaFilter vendaDiariaFilter,
			@Parameter(description = "Deslocamento de horário em relação ao UTC", example = "-03:00") String timeOffset);

	@Operation(summary = "Consulta vendas diárias em PDF", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)))
	ResponseEntity<?> consultarVendasDiariasPdf(@ParameterObject VendaDiariaFilter vendaDiariaFilter,
			@Parameter(description = "Deslocamento de horário em relação ao UTC", example = "-03:00") String timeOffset);

	@Operation(summary = "Lista os recursos de estatísticas")
	ResponseEntity<?> estatisticas();
}
