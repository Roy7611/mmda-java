package cloud.mmda.core.file.excel;

import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.file.excel.bindings.BindingTable;
import cloud.mmda.core.file.excel.excelname.SheetNameData;
import cloud.mmda.core.file.excel.excelname.SheetNameInfo;
import cloud.mmda.core.file.exceptions.ExcelException;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.file.util.ReflectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Excel文件读取
 * <p>
 *
 * </p>
 *
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 * @author Roy Luo
 * @version 4.0
 * @since 2022.11
 */
public class ExcelFileReader<T extends SequencedRow, K> {
    private static final Log logger = LogFactory.getLog(ExcelFileReader.class);
    private final Repository<T, K> repository;

    public ExcelFileReader(final Repository<T, K> repository) {
        this.repository = repository;
    }

    /**
     * 读取所有表格数据，默认不超过10000行。
     * <p>
     * 列顺序在没有标题行时默认按照原生的数据库字段顺序。
     * 如果有标题行，尝试根据标题行文本匹配字段的显示标签（displayLabel），你可以使用“#字段名”作为标题文本，这样按字段名称准确匹配。
     * 或者在标题单元格添加“#字段名”批注来避免字段显示标签重复和不可靠的情况。
     * </p>
     *
     * @param file          文件
     * @param sheetIndex    哪一个Sheet，默认0为第一个
     * @param startRowIndex 从哪一行开始，默认0为第一行
     * @param hasHeadRow    是否包含标题行，若包含则会自动根据行标题内容匹配字段
     * @param afterRowRead  读取每一行后拦截器，例如你可以对行数据进一步加工，然后保存到数据库。传入null代表不作每行拦截处理。
     *                      (实体,行索引)=>是否继续
     * @return
     * @throws IOException              文件打开异常
     * @throws InvalidFormatException   文件格式非法
     * @throws IllegalArgumentException 参数不合法，例如file参数为null, sheetIndex不存在
     */
    public List<T> readAll(final File file, int sheetIndex, int startRowIndex, boolean hasHeadRow,
                           final BiFunction<T, Integer, Boolean> afterRowRead,int tenantID)
            throws IOException, IllegalArgumentException {
        Assert.notNull(file, "file must not be null");
        FileInputStream fileInputStream = new FileInputStream(file.getPath());
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (final Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            if(checkIsNullWorkBook(workbook)) return new ArrayList<>();
            checkTemplate(null,workbook);
            Sheet dateSheetTemplate = workbook.getSheet("_TEMPLATE_ID");
            if(dateSheetTemplate != null) throw ExcelException.CodeConstant.TEMPLATE_NOT_SAME;
            var sheet = workbook.getSheetAt(sheetIndex);
            ExcelFileUtil.findAndUploadPic(workbook);
            var dataTable = hasHeadRow
                    ? BindingTable.fromSheet(repository, sheet, startRowIndex, (short) -1)
                    : BindingTable.all(repository, (short) -1,workbook);
            return dataTable.read(sheet, hasHeadRow ? startRowIndex + 1 : startRowIndex, afterRowRead);
        }
    }

    /**
     * 检测文件是否为空
     * @param workbook
     * @return Boolean
     */
    public Boolean checkIsNullWorkBook(Workbook workbook){
        if (workbook.getNumberOfSheets() == 0) return true;
        for (Sheet sheet : workbook) {
            // 检查物理行数
            if (sheet.getPhysicalNumberOfRows() == 0) continue;

            // 遍历行（快速终止）
            for (Row row : sheet) {
                if (!isRowEmpty(row)) return false;
            }
        }
        return true;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 按模板 读取Excel 文件
     * @param tenantID
     * @param file
     * @param templateFilePath
     * @return
     * @throws Exception
     */
    public List<T> readAll(int tenantID, final File file, final String templateFilePath) throws Exception {
        Assert.notNull(file, "file must not be null");
        Assert.notNull(templateFilePath, "templateFile must not be null");

        File templateFile;
        if (templateFilePath.startsWith("http")) {
            templateFile = getFileByUrl(templateFilePath);
        } else {
            templateFile = new File(templateFilePath);
        }

        if (templateFile == null) return null;

        FileInputStream fileInputStream = new FileInputStream(file.getPath());
        FileInputStream templateFileInputStream = new FileInputStream(templateFile.getPath());

        List<T> list = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (final Workbook workbook = new XSSFWorkbook(fileInputStream);
             final Workbook templateWorkbook = new XSSFWorkbook(templateFileInputStream)) {
            if (checkIsNullWorkBook(workbook)) return new ArrayList<>();

            Sheet sheet = templateWorkbook.getSheetAt(0);
            Sheet sheetAt = workbook.getSheetAt(0);
            ExcelFileUtil.findAndUploadPic(sheetAt);
            checkTemplate(templateWorkbook, workbook);


            SheetNameData sheetNameDataTemplate = new SheetNameData(sheet);
            SheetNameInfo sheetNameInfo = sheetNameDataTemplate.getSheetNameInfo(0);

            SheetNameData sheetNameData = new SheetNameData(sheetAt);
            long rowNum = 1;
            for (int i = 0; i <= sheetNameData.getNameDataSize(); i++) {
                SheetNameInfo sheetNameInfoData = sheetNameData.getSheetNameInfo(i);

                if (sheetNameInfoData == null) {
                    continue;
                }
                var dataTable = BindingTable.fromTemplate(repository, sheet, sheetAt, (short) 0, null, sheetNameInfo, sheetNameInfoData);
                if (dataTable == null) continue;
                T t = repository.create();
                dataTable.readTemplate(repository, sheetAt, t, tenantID);
                String uniqueKey = repository.getMetaObject().getUniqueKey();
                Object t1 = ReflectionUtil.getT(t, uniqueKey);
                if (StringUtil.isNotBlank(uniqueKey) && (t1 == null || !StringUtil.isNotBlank(t1.toString()))) {
                    ReflectionUtil.setT(t, uniqueKey, "@");
                }
                ReflectionUtil.executeMethod(t, "compute");
                ReflectionUtil.setT(t, "rowNum", rowNum++);
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 检查 Excel是否模版
     * @param templateWorkbook
     * @param workbook
     */
    private static void checkTemplate(Workbook templateWorkbook, Workbook workbook) {

        Sheet dateSheetTemplate = null;
        dateSheetTemplate = workbook.getSheet("_TEMPLATE_ID");
        if (templateWorkbook == null && dateSheetTemplate != null)
            throw ExcelException.CodeConstant.TEMPLATE_NOT_DEFAULT;
        Sheet sheetTemplate = null;

        if (templateWorkbook == null || (sheetTemplate = templateWorkbook.getSheet("_TEMPLATE_ID")) == null) return;
        try {
            Assert.notNull(dateSheetTemplate, "数据文件-模版标识丢失");

            Row row = sheetTemplate.getRow(0);
            Row dataRow = dateSheetTemplate.getRow(0);
            Assert.notNull(row, "模版文件-模版标识丢失");
            Assert.notNull(dataRow, "数据文件-模版标识丢失");

            Cell cell1 = row.getCell(0);
            Cell Datacell1 = dataRow.getCell(0);
            Assert.notNull(cell1, "模版文件-模版标识丢失");
            Assert.notNull(Datacell1, "数据文件-模版标识丢失");

            if (!cell1.getStringCellValue().equals(Datacell1.getStringCellValue())) {
                throw new IllegalArgumentException("模版文件-上传的数据模版与选择的模版不一致");
            }
        } catch (Exception e) {
            throw ExcelException.CodeConstant.TEMPLATE_NOT_SAME;
        }
    }

    public File getFileByUrl(String urlStr) throws IOException {
        File temp = Files.createTempFile("temp", repository.getMetaObject().getDisplayLabel()).toFile();
        String[] split = urlStr.split("/");
        String fileName = split[split.length - 1];
        String encodedChinesePart = URLEncoder.encode(fileName, "UTF-8");
        urlStr = urlStr.replace(fileName, encodedChinesePart);
        URL url = new URL(urlStr);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) return null;
        InputStream inputStream = httpConn.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(temp);
        // 创建一个缓冲区
        byte[] buffer = new byte[1024];
        int bytesRead;

        // 从输入流读取数据并写入输出流
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        // 关闭流
        fileOutputStream.close();
        inputStream.close();
        return temp;
    }

    /**
     * 默认读取所有表格数据(无模板)
     * @param tenantID
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @throws IllegalArgumentException
     */
    public List<T> readAll(int tenantID,final File file) throws IOException, InvalidFormatException, IllegalArgumentException {
        return readAll(file, 0, 0, true, null,tenantID);
    }

    /**
     * 读取所有表格数据流，是{@link ExcelFileReader#readAll(File, int, int, boolean, BiFunction)}的响应式版本。
     *
     * @param file
     * @param sheetIndex
     * @param startRowIndex
     * @param hasHeadRow
     * @param afterRowRead
     * @return
     */
    public Flux<T> readAllAsync(final File file, int sheetIndex, int startRowIndex, boolean hasHeadRow,
                                final BiFunction<T, Integer, Boolean> afterRowRead) {
        try (final Workbook workbook = new XSSFWorkbook(file)) {
            var sheet = workbook.getSheetAt(sheetIndex);
            var dataTable = hasHeadRow
                    ? BindingTable.fromSheet(repository, sheet, startRowIndex, (short) -1)
                    : BindingTable.all(repository, (short) -1,workbook);
            return dataTable.readAsync(sheet, hasHeadRow ? startRowIndex + 1 : startRowIndex, afterRowRead);
        } catch (Exception e) {
            logger.error(e);
            return Flux.error(e);
        }
    }
}
