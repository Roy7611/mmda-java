package cloud.mmda.core.file.pdf.Jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JasperReportUtil {

    public static File export(JasperPrint print, File file, String suffix) throws IOException, JRException {
        if (suffix.equalsIgnoreCase("html")) {
            return exporterHTML(print, file);
        } else if (suffix.equalsIgnoreCase("xlsx")) {
            return exporterXLSX(print, file);
        } else if (suffix.equalsIgnoreCase("xls")) {
            return exporterXSL(print, file);
        } else if (suffix.equalsIgnoreCase("csv")) {
            return exporterCSV(print, file);
        } else if (suffix.equalsIgnoreCase("docx")) {
            return exporterDOCX(print, file);
        } else {
            //默认导出pdf
            JasperExportManager.exportReportToPdfStream(print,
                    new FileOutputStream(file));
            return file;
        }
    }

    public static File exporterHTML(JasperPrint print, File file) throws IOException, JRException {
        HtmlExporter exporterHTML = new HtmlExporter();
        SimpleExporterInput exporterInput = new SimpleExporterInput(print);
        exporterHTML.setExporterInput(exporterInput);
        SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(new FileOutputStream(file));
        exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
        exporterHTML.setExporterOutput(exporterOutput);
        SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
        reportExportConfiguration.setWhitePageBackground(false);
        reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        exporterHTML.setConfiguration(reportExportConfiguration);
        exporterHTML.exportReport();
        return file;
    }

    public static File exporterXSL(JasperPrint print, File file) throws IOException, JRException {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(file)));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return file;
    }

    public static File exporterCSV(JasperPrint print, File file) throws IOException, JRException {
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(new FileOutputStream(file)));
        exporter.exportReport();
        return file;
    }

    public static File exporterDOCX(JasperPrint print, File file) throws IOException, JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(file)));
        exporter.exportReport();
        return file;
    }

    public static File exporterXLSX(JasperPrint print, File file) throws IOException, JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        ExporterInput exporterInput = new SimpleExporterInput(print);
        exporter.setExporterInput(exporterInput);
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(file)));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return file;
    }
}
