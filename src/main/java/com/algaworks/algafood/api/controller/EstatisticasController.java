package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {
	private final VendaQueryService vendaQueryService;
	private final VendaReportService vendaReportService;

	public EstatisticasController(VendaQueryService vendaQueryService, VendaReportService vendaReportService) {
		this.vendaQueryService = vendaQueryService;
		this.vendaReportService = vendaReportService;
	}

	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<?>> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter,
			@RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
		return ResponseEntity.ok(this.vendaQueryService.consultarVendasDiarias(vendaDiariaFilter, timeOffset));
	}

	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<?> consultarVendasDiariasPdf(VendaDiariaFilter vendaDiariaFilter,
			@RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
		var headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).headers(headers)
				.body(this.vendaReportService.emitirVendasDiarias(vendaDiariaFilter, timeOffset));
	}
}
