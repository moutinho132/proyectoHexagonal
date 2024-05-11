package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class ReportService {

  private static final String REPORTS = "reports";
  private static final String VON_DER_HEYDEN_CONCIERGE_AUTHOR = "VON DER HEYDEN CONCIERGE";
  private static final String EXTENSION = "pdf";

  private AzureFileService azureFileService;

  public String build(final String resource, final List<ReportDetail> details, final Map<String, Object> parameters) {
    try {
      final InputStream compiled = getClass().getResourceAsStream(resource);
      final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiled);

      final JRDataSource jrDataSource = new JRBeanCollectionDataSource(details);

      final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

      final JRPdfExporter exporter = new JRPdfExporter();
      final ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfOutputStream));

      final SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
      reportConfig.setSizePageToContent(true);
      reportConfig.setForceLineBreakPolicy(false);

      final SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
      exportConfig.setMetadataAuthor(VON_DER_HEYDEN_CONCIERGE_AUTHOR);

      exporter.setConfiguration(reportConfig);
      exporter.setConfiguration(exportConfig);

      exporter.exportReport();

      return azureFileService.uploadFile(pdfOutputStream.toByteArray(), REPORTS, EXTENSION);

    } catch (final Exception e) {
      log.error("There was an error when build the report {}", e.getMessage());
      throw new BusinessRuleException("Cannot build the pdf");
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class ReportDetail {

    private String number;
    private String name;
    private String description;
    private String quantity;
  }
}
