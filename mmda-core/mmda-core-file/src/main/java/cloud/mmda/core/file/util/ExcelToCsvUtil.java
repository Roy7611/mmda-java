package cloud.mmda.core.file.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * XLS/XLSX 转 CSV 工具类
 */
public class ExcelToCsvUtil {

    /**
     * 将 Excel 文件中所有 Sheet 导出到同一个 CSV 文件
     *
     * @param excelFile 输入 XLSX 文件
     * @param csvFile   输出 CSV 文件
     * @throws Exception
     */
    public static void excelToCsv(File excelFile, File csvFile) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"))) {

            // 写 BOM，保证中文正常显示
            writer.write('\ufeff');

            try (Workbook workbook = WorkbookFactory.create(excelFile);
                 CSVPrinter csvPrinter = new CSVPrinter(writer,
                         CSVFormat.DEFAULT
                                 .withDelimiter(',')
                                 .withQuoteMode(QuoteMode.ALL)
                                 .withRecordSeparator("\n")
                 )) {

                DataFormatter formatter = new DataFormatter(false);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                int sheetCount = workbook.getNumberOfSheets();
                for (int i = 0; i < sheetCount; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();

                    for (Row row : sheet) {
                        List<String> rowData = new ArrayList<>();
                        int lastCellNum = row.getLastCellNum();
                        for (int c = 0; c < lastCellNum; c++) {
                            Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            String value = getMergedCellValue(sheet, cell, mergedRegions, formatter, sdf);
                            rowData.add(value);
                        }
                        csvPrinter.printRecord(rowData);
                    }

                    // 可选：每个 Sheet 之间加一行空白行
                    csvPrinter.println();
                }

                csvPrinter.flush();
            }
        }
    }

    private static String getMergedCellValue(Sheet sheet, Cell cell,
                                             List<CellRangeAddress> mergedRegions,
                                             DataFormatter formatter,
                                             SimpleDateFormat sdf) {
        for (CellRangeAddress region : mergedRegions) {
            if (region.isInRange(cell)) {
                Cell firstCell = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
                if (cell.getRowIndex() == region.getFirstRow() && cell.getColumnIndex() == region.getFirstColumn()) {
                    return formatCellValue(firstCell, formatter, sdf);
                } else {
                    return ""; // 保留列占位
                }
            }
        }
        return formatCellValue(cell, formatter, sdf);
    }

    private static String formatCellValue(Cell cell, DataFormatter formatter, SimpleDateFormat sdf) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return sdf.format(cell.getDateCellValue());
                } else {
                    return formatter.formatCellValue(cell);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return sdf.format(cell.getDateCellValue());
                        } else {
                            return formatter.formatCellValue(cell);
                        }
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    case ERROR:
                        return "ERROR";
                    default:
                        return "";
                }
            case BLANK:
                return "";
            case ERROR:
                return "ERROR";
            default:
                return formatter.formatCellValue(cell);
        }
    }

}
