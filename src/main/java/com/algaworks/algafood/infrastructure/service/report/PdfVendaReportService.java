package com.algaworks.algafood.infrastructure.service.report;

import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.algaworks.algafood.api.model.dto.VendaDiariaReport;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendaReportService {
	private final VendaQueryService vendaQueryService;

	public PdfVendaReportService(VendaQueryService vendaQueryService) {
		this.vendaQueryService = vendaQueryService;
	}

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset) {
		try {
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			return JasperExportManager
					.exportReportToPdf(
							JasperFillManager
									.fillReport(
											this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper"),
											parametros,
											new JRBeanCollectionDataSource(
													this.vendaQueryService
															.consultarVendasDiarias(vendaDiariaFilter, timeOffset)
															.stream()
															.map(v -> new VendaDiariaReport(Date.valueOf(v.getData()),
																	v.getTotalVendas(), v.getTotalFaturado()))
															.toList())));
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
	}

}
