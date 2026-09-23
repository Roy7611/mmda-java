package cloud.mmda.core.file.excel;

import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.excel.bindings.BindingTable;
import cloud.mmda.core.file.exceptions.ExcelException;
import cloud.mmda.core.file.pdf.PdfConverter;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.file.util.ExcelToCsvUtil;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.utils.BaseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelPagedWriter<T extends SequencedRow,K> implements AutoCloseable {
    private static final Log logger = LogFactory.getLog(ExcelPagedWriter.class);

    private  Workbook workbook;
    private Sheet currentSheet;
    private int currentRowIndex = 0;
    private int sheetIndex = 0;
    private final int maxRowsPerSheet;
    private final ExcelExportProgressListener progressListener;
    private final Repository<T, K> repository;
    private final BindingTable<T, K> table;
    private  File templateFile;
    private Map<MetaRelation, List<Object>> optionMap;
    private final int tenantID;
    private  String format = "xlsx";
    private String filename;
    private static final ExpressionParser parser = new SpelExpressionParser();
    private final List<Row> templateRows=new ArrayList<>();


    private final AtomicInteger totalRowsWritten = new AtomicInteger(0);

    public ExcelPagedWriter(
            int tenantID,
            Repository<T, K> repository,
            List<String> colNames,
            String templateFileUrl,
            String filename,
            String format,
            Map<MetaRelation, List<Object>> optionMap,
            ExcelExportProgressListener listener
    ) {
        this.repository = repository;
        this.maxRowsPerSheet = 10000000;
        this.progressListener = listener;
        this.optionMap = optionMap;
        this.tenantID = tenantID;
        this.format = format;
        this.filename = BaseUtil.hasText(filename)? filename : repository.getMetaObject().getDisplayLabel();
        if (this.filename.length() < 3) {
            this.filename = String.format("%-3s", this.filename).replace(' ', '_'); // 补齐到3字符
        }
        createWorkbook(templateFileUrl);

        this.table= BaseUtil.hasAny(colNames)
                ? BindingTable.names(repository, colNames, tenantID,workbook)
                : BindingTable.all(repository, tenantID,workbook);
        createNewSheet(repository.getMetaObject().getDisplayLabel());

    }




    private void createNewSheet(String sheetNamePrefix) {
        if (templateFile!=null) {
            workbook.setSheetName(0, sheetNamePrefix);
            currentSheet = workbook.getSheetAt(sheetIndex);
        }else {
            sheetIndex++;
            currentSheet = workbook.createSheet(sheetNamePrefix + "_" + sheetIndex);
        }
        currentRowIndex = 0;
    }

    private void createWorkbook(String templateFileUrl){
        if (BaseUtil.hasText(templateFileUrl)) {
            File template;
            if (templateFileUrl.startsWith("http")) {
                template = ExcelFileUtil.getFileByUrl(templateFileUrl);
            } else {
                template = new File(templateFileUrl);
            }
            if (template != null) {
                try {
                    // 复制模板到临时文件
                    Path tempPath = Files.createTempFile("temp_", filename);
                    Files.copy(template.toPath(), tempPath, StandardCopyOption.REPLACE_EXISTING);
                    templateFile = tempPath.toFile();
                    this.workbook = new XSSFWorkbook(templateFile);
                } catch (Exception e) {

                }
            } else {
                this.workbook = new SXSSFWorkbook(100);
            }

        } else {
            this.workbook = new SXSSFWorkbook(100);
        }
    }


    public void write(List<T> batch) throws IOException {
        if (CollectionUtils.isEmpty(batch)) return;

        for (var t : batch) {
            writeSingle(t);
            checkSheetFull();
        }
    }

    public void writeTemplate(List<T> tList) {

        if (CollectionUtils.isEmpty(tList)) {
            return;
        }

        var names = workbook.getAllNames();
        // 遍历所有命名区域
        if (BaseUtil.hasAny(names)) {
            for (Name namedRange : names) {
                var metaRel = repository.getMetaObject().getRelations().stream().filter(metaRelation ->
                        namedRange.getNameName().startsWith(metaRelation.getRelationName())).findFirst().orElse(null);
                var rows = findTemplateRows(currentSheet, namedRange, metaRel);

                if (metaRel != null) {
                    for (var t : tList) {
                        if (metaRel.getRelationType() == MetaRelationType.HAS_MANY) {
                            var itemList = (List<?>) repository.getRelativeValue(t, metaRel);

                            writeTemplateRows(currentSheet, itemList, rows);
                            templateRows.addAll(rows);
                        }
                    }
                } else {
                    writeTemplateRows(currentSheet, tList, rows);
                    templateRows.addAll(rows);
                }
            }
        } else {
            var rows = findTemplateRows(currentSheet, null, null);
            writeTemplateRows(currentSheet, tList, rows);
            templateRows.addAll(rows);
        }

    }





    /**
     * 写入单个实体（可以是模板区域或单表写入）
     */
    private void writeSingle(T data) {
        // 使用 BindingTable 写入

        List<T> list = Collections.singletonList(data);
        table.write(currentSheet, list, currentRowIndex, currentRowIndex == 0, true);

        currentRowIndex += list.size();

        if (progressListener != null) {
            int written = totalRowsWritten.addAndGet(list.size());
            progressListener.onProgress(written, 1);
        }
    }

    /**
     * 检查当前 Sheet 是否超过最大行数，如果超过则新建 Sheet
     */
    private void checkSheetFull() {
        if (currentRowIndex >= maxRowsPerSheet) {
            createNewSheet(currentSheet.getSheetName().split("_")[0]);
        }
    }


    /**
     * 填充子表数据
     * @param sheet 工作表
     * @param list 子表对象列表
     * @param templateRows 模板行
     */
    private void writeTemplateRows(Sheet sheet, List<?> list,List<Row> templateRows) {
        if (list == null || list.isEmpty()) return;

        // -----------------  找到子表末行 -----------------
        var startRow = BaseUtil.hasAny(templateRows)?templateRows.get(templateRows.size() - 1).getRowNum():0;
         currentRowIndex = findNextEmptyRow(sheet,startRow );

        // -----------------  循环填充数据 -----------------
        for (Object item : list) {
            // 遍历模板行
            for (Row templateRow : templateRows) {
                Row newRow = sheet.getRow(currentRowIndex);
                if (newRow == null) newRow = sheet.createRow(currentRowIndex);
                //  拷贝样式 + 公式 + 合并单元格
                copyRowWithFormulasAndMergedCells(sheet, templateRow, newRow);

                //  填充数据到 SpEL 表达式
                fillRowWithSpEL(newRow, item);

                currentRowIndex++;
            }
            int written = totalRowsWritten.addAndGet(1);
            progressListener.onProgress(written, 1);
        }

    }

    // ----------------- 工具方法 -----------------
    private  int findNextEmptyRow(Sheet sheet, int startRow) {
        int rowIndex = startRow;
        while (true) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) break;
            boolean empty = true;
            for (Cell cell : row) {
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    empty = false;
                    break;
                }
            }
            if (empty) break;
            rowIndex++;
        }
        return rowIndex;
    }

    private List<Row> findTemplateRows(Sheet sheet, Name namedRange, MetaRelation metaRelation) {
        String prefix = metaRelation==null?null:metaRelation.getRelationName();
        List<Row> templateRows = new ArrayList<>();
        // -----------------获取模板行 -----------------
        if (namedRange != null) {
            // 命名区域模板
            AreaReference areaRef = new AreaReference(namedRange.getRefersToFormula(), sheet.getWorkbook().getSpreadsheetVersion());
            for (CellReference ref : areaRef.getAllReferencedCells()) {
                Row row = sheet.getRow(ref.getRow());
                if (row == null) row = sheet.createRow(ref.getRow());
                if (!templateRows.contains(row)) templateRows.add(row);
            }
        } else {
            // 无命名区域，自动查找包含 SpEL 表达式的行
            templateRows = findTemplateRowsBySpEL(sheet, 0, 10,prefix);
            if (templateRows.isEmpty()) {
                // 退回默认上一行逻辑
                int nextEmpty = findNextEmptyRow(sheet, 0);
                Row row = sheet.getRow(nextEmpty - 1);
                if (row == null) row = sheet.createRow(nextEmpty - 1);
                templateRows.add(row);
            }
        }
        return templateRows;
    }
    /**
     * 查找 sheet 中包含 SpEL 表达式的模板行
     * @param sheet 工作表
     * @param startRow 开始查找行
     * @param maxRows 最大查找行数
     * @return 模板行列表
     */
    private List<Row> findTemplateRowsBySpEL(Sheet sheet, int startRow, int maxRows, String prefix) {
        List<Row> templateRows = new ArrayList<>();
        int lastRow = sheet.getLastRowNum();
        int endRow = Math.min(startRow + maxRows, lastRow);

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            boolean hasSpEL = false;
            for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    if (value != null && (value.contains("${") || value.contains("#{"))) {

                        if (prefix == null || prefix.isEmpty()) {
                            // 不限制前缀，任何占位符都匹配
                            hasSpEL = true;
                            break;
                        } else {
                            // 只匹配指定前缀
                            Pattern p = Pattern.compile("(\\$\\{" + prefix + "\\..*?\\}|#\\{" + prefix + "\\..*?\\})");
                            Matcher m = p.matcher(value);
                            if (m.find()) {
                                hasSpEL = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (hasSpEL) {
                templateRows.add(row);
            }
        }

        return templateRows;
    }



    private static final Pattern PATTERN = Pattern.compile("(\\$\\{.*?\\}|#\\{.*?\\})");

    /**
     * 填充 Excel 行，支持混合文本中的 SP-EL 占位符
     *
     * @param row     Excel 行
     * @param rootObj 上下文对象，可为 POJO 或 Map
     */
    public static void fillRowWithSpEL(Row row, Object rootObj) {
        if (row == null || rootObj == null) return;

        StandardEvaluationContext context = new StandardEvaluationContext(rootObj);

        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();

        for (int i = first; i < last; i++) {
            Cell cell = row.getCell(i);
            if (cell == null || cell.getCellType() != CellType.STRING) continue;

            String text = cell.getStringCellValue();
            if (text == null || text.isEmpty()) continue;

            try {
                String result;

                // 如果包含占位符 ${...} 或 #{...}
                if (text.contains("${") || text.contains("#{")) {
                    Matcher matcher = PATTERN.matcher(text);
                    StringBuffer sb = new StringBuffer();

                    while (matcher.find()) {
                        String exprStr = matcher.group(); // 占位符，例如 ${vcode}
                        String exprBody = exprStr.substring(2, exprStr.length() - 1); // 去掉 ${}
                        try {
                            Object val = parser.parseExpression(exprBody).getValue(context);
                            matcher.appendReplacement(sb, val != null ? Matcher.quoteReplacement(val.toString()) : "");
                        } catch (Exception e) {
                            matcher.appendReplacement(sb, ""); // 出错置空
                        }
                    }
                    matcher.appendTail(sb);
                    result = sb.toString();

                } else {
                    // 没有占位符，尝试当作字段名或 SpEL 表达式解析
                    try {
                        Object val = parser.parseExpression(text).getValue(context);
                        result = val != null ? val.toString() : "";
                    } catch (Exception e) {
                        result = text; // 如果解析失败，保留原文本
                    }
                }

                cell.setCellValue(result);

            } catch (Exception e) {
                cell.setCellValue(""); // 整体失败置空
            }
        }
    }



    private void copyRowWithFormulasAndMergedCells(Sheet sheet, Row source, Row target) {
        target.setHeight(source.getHeight());

        for (int i = source.getFirstCellNum(); i < source.getLastCellNum(); i++) {
            Cell srcCell = source.getCell(i);
            if (srcCell == null) continue;

            Cell tgtCell = target.getCell(i);
            if (tgtCell == null) tgtCell = target.createCell(i);

            tgtCell.setCellStyle(srcCell.getCellStyle());

            switch (srcCell.getCellType()) {
                case FORMULA:
                    tgtCell.setCellFormula(srcCell.getCellFormula());
                    break;
                case STRING:
                    tgtCell.setCellValue(srcCell.getStringCellValue());
                    break;
                case NUMERIC:
                    tgtCell.setCellValue(srcCell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    tgtCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case BLANK:
                    tgtCell.setBlank();
                    break;
            }
        }

        // 处理合并单元格
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() == source.getRowNum()) {
                CellRangeAddress newRegion = new CellRangeAddress(
                        target.getRowNum(),
                        target.getRowNum() + (region.getLastRow() - region.getFirstRow()),
                        region.getFirstColumn(),
                        region.getLastColumn()
                );
                sheet.addMergedRegion(newRegion);
            }
        }
    }
    /**
     * 导出完成后统一删除模板行
     */
    private void finalizeTemplateRows() {
        if (templateRows == null || templateRows.isEmpty()) return;

        for (Row templateRow : templateRows) {
            // 清空模板行单元格内容（保留样式）
            for (int i = templateRow.getFirstCellNum(); i < templateRow.getLastCellNum(); i++) {
                Cell cell = templateRow.getCell(i);
                if (cell != null) {
                    if (cell.getCellType() == CellType.STRING) {
                        cell.setCellValue("");
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        cell.setCellValue(0);
                    } else if (cell.getCellType() == CellType.FORMULA) {
                        cell.setCellFormula(null);
                    }
                }
            }
        }
    }

    /**
     * 生成最终文件
     */
    public String finish() throws Exception {
        try {


            File file = File.createTempFile(filename, ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            File resultFile =file;

            try {
                if ("pdf".equalsIgnoreCase(format)) {
                    resultFile = File.createTempFile(filename, ".pdf");
                    PdfConverter.excelToPdf(file.getPath(), resultFile.getPath(), ".xlsx");
                }else if ("csv".equalsIgnoreCase(format)) {
                    resultFile = File.createTempFile(filename, ".csv");
                    ExcelToCsvUtil.excelToCsv(file, resultFile);
                }
            }catch (Exception e){
                logger.error("excel.conversion.error"+format,e);
                throw new ExcelException("file.conversion.error",null);
            }
            // ========= 上传 =========
            return ExcelFileUtil.uploadFile(resultFile.getName(), resultFile)
                    .block();
        }catch (ExcelException e){
            throw e;
        } catch (Exception e){
            logger.error("upload.file.error:"+filename,e);
            throw new ExcelException("upload.file.error",null);
        }

    }

    @Override
    public void close() throws IOException {
//        workbook.dispose();
        if (workbook instanceof SXSSFWorkbook w){
            w.dispose();
        }
        workbook.close();
    }
}
