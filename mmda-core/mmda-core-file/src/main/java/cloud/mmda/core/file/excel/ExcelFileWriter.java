package cloud.mmda.core.file.excel;

import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.entities.FileImportErrorInfo;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.entities.ValidationError;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.app.ApplicationContextExcelProvider;
import cloud.mmda.core.file.excel.bindings.BindingTable;
import cloud.mmda.core.file.excel.excelname.SheetItemName;
import cloud.mmda.core.file.excel.excelname.SheetName;
import cloud.mmda.core.file.excel.excelname.SheetNameData;
import cloud.mmda.core.file.excel.excelname.SheetNameInfo;
import cloud.mmda.core.file.pdf.PdfConverter;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.file.util.ReflectionUtil;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaDataType;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NameValue;
import cloud.mmda.core.utils.NamingUtil;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Excel文件写入
 * <p>
 * 首先读取数据，我们通过定义数据源Supplier/Publisher。
 * 然后定义数据转换器Function<T,R>，在写入单元格前拦截，例如格式化。
 * 最后写入单元格。
 *
 * </p>
 * <p>
 * 基于 https://poi.apache.org
 * 参考 https://github.com/eugenp/tutorials/tree/master/apache-poi
 * </p>
 *
 * @see <a href="https://www.baeldung.com/java-write-to-file">文件操作</a>
 */
public class ExcelFileWriter<T extends SequencedRow, K> {
    private static final Log logger = LogFactory.getLog(ExcelFileWriter.class);
    private final Repository<T, K> repository;
    private  static WorkbookStyleRegistry styleRegistry;

    public ExcelFileWriter(final Repository<T, K> repository) {
        this.repository = repository;
    }

    /**
     * 将数据列表 list 写入文件名 filename
     *
     * @param list          数据列表
     * @param colNames      导出字段列表
     * @param filename      文件名
     * @param startRowIndex 开始行，默认0
     * @param writeHeadRow  是否写入标题行
     * @return 临时文件
     * @throws IOException
     */
    public File writeAll(final List<T> list, final Collection<String> colNames, final String filename,
                         int startRowIndex, boolean writeHeadRow, int tenantID) throws IOException {
        if (colNames == null || colNames.isEmpty()) {
            return writeAll(list, filename, tenantID);
        }
        return writeAll(list, colNames, filename, startRowIndex, writeHeadRow, true, tenantID);

    }

    public File writeAll(final List<T> list, final String filename, int tenantID) throws IOException {
        List<String> colNames = repository.getMetaObject().getCols().stream().map(MetaCol::getColName).toList();
        return writeAll(list, colNames, filename, 0, true, true, tenantID);

    }

    /**
     * 将数据列表 list 写入文件名 filename
     *
     * @param list                       数据列表
     * @param colNames                   导出字段列表
     * @param filename                   文件名
     * @param startRowIndex              开始行，默认0
     * @param writeHeadRow               是否写入标题行
     * @param byConstraintSetLabelsStyle 是否通过元数据约束设置标题格式
     * @return 临时文件
     * @throws IOException
     */
    public File writeAll(final List<T> list, final Collection<String> colNames, final String filename,
                         int startRowIndex, boolean writeHeadRow, Boolean byConstraintSetLabelsStyle, int tenantID) throws IOException {
        try (final Workbook workbook = new XSSFWorkbook()) {
            var dataTable = BaseUtil.hasAny(colNames)
                    ? BindingTable.names(repository, colNames, tenantID,workbook)
                    : BindingTable.all(repository, tenantID,workbook);
            Sheet sheet = workbook.createSheet(repository.getMetaObject().getDisplayLabel());
            dataTable.write(sheet, list, startRowIndex, writeHeadRow, byConstraintSetLabelsStyle);
            Row row = sheet.getRow(0);
            if(row != null){
                sheet.createFreezePane(0, 1);
            }

            String dataName = "";
            if(!CollectionUtils.isEmpty(list) && list.size() == 1) {
                T t = list.getFirst();
                try {
                    Object o = ReflectionUtil.executeMethodAndGet(t, "getExportFileName");
                    if(o instanceof String){
                        dataName =  o +"_";
                    }
                }catch (Exception e){
                    logger.info(e.getMessage());
                }

            }
            File file = File.createTempFile(repository.getMetaObject().getDisplayLabel().concat("_").concat(dataName), ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            try {
                Mono<File> mono = ExcelFileUtil.uploadExcelFile(file.getName(), file);
                CompletableFuture<File> future = mono.toFuture();
                return future.get();
            }catch (Exception e) {
                logger.info(e.getMessage());
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File writeAll(final List<T> list, final Collection<String> colNames, final String filename, int tenantID,String format) throws IOException {
        return writeAll(list, colNames, filename, 0, true, tenantID);
    }

    public File writeAll(final List<T> list, final Collection<String> colNames, final String filename, int tenantID) throws IOException {
        return writeAll(list, colNames, filename, 0, true, tenantID);
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
     * 模版写入基础配置数据
     *
     * @param templateFile
     * @param map
     * @return
     * @throws IOException
     */
    public File writeBasicDataForTemplate(String templateFile, final Map<MetaRelation, List<Object>> map) throws IOException {
        Assert.notNull(templateFile, "templateFile must not be null");
        File file;
        if (templateFile.startsWith("http")) {
            file = getFileByUrl(templateFile);
        } else {
            file = new File(templateFile);
        }

        if (file == null) return null;
        Map<String, List<MetaRelation>> collect = CollectionUtils.isEmpty(map) ? new HashMap<>() : (map.entrySet().stream()
                .collect(Collectors.groupingBy(entry -> {
                    MetaRelation key = entry.getKey();
                    if(key.getRelationType() == MetaRelationType.REF){
                        String colName = key.getJoinOn().split("@")[1];
                        return key.getObjName().concat(",").concat(colName);
                    }else {
                        return key.getObjName().concat(",").concat(key.getRelationName());
                    }
                }, Collectors.mapping(Map.Entry::getKey, Collectors.toList()))));

        Map<String, List<MetaRelation>> collect1 = repository.getMetaObject().getRelations().stream().collect(Collectors.groupingBy(MetaRelation::getRelationName));


        try (final Workbook workbook = new XSSFWorkbook(file.getPath())) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                int lastCellNum = row.getLastCellNum();
                for (int j = 0; j <= lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null || cell.getCellType() != CellType.STRING) continue;
                    String stringCellValue = cell.getStringCellValue()
                            .replace("$", "")
                            .replace("customProperties[", "")
                            .replace("]", "");
                    String fieldName = stringCellValue;
                    String finalFieldName = fieldName;
                    boolean check = repository.getMetaObject().getRelations().stream()
                            .map(relation -> relation.getRelationName().concat(".")).anyMatch(name -> finalFieldName.contains(name));
                    /**
                     * 主表 t.name 或者 has_one: t.project.projectName
                     * 子表 item.itemId
                     */
                    if (fieldName.length()<2)
                        continue;

                    boolean equals = "t.".equals(fieldName.substring(0, 2));//判断是否是主表
                    if(!check && !equals) {
                        continue;
                    }
                    cell.setCellValue("");

                    String objName;
                    MetaCol metaCol = null;
                    MetaObject metaObject = null;
                    if (equals) {
                        metaObject = repository.getMetaObject();
                        fieldName = fieldName.replaceFirst("t.", "").split("\\.")[0];
                        objName = repository.getMetaObject().getObjName();
                        String finalFieldName1 = fieldName;
                        List<MetaCol> list = repository.getMetaObject().getCols().stream().filter(col -> col.getColName().equals(finalFieldName1)).toList();
                        if(!CollectionUtils.isEmpty(list)) metaCol  = list.getFirst();
                    } else {
                        String[] split = fieldName.split("\\.");
                        fieldName = split[1];
                        List<MetaRelation> metaRelations = collect1.get(split[0]);
                        if (!CollectionUtils.isEmpty(metaRelations)) {
                            MetaRelation relation = metaRelations.getFirst();
                            objName = relation.getRelativeObjName();

                            String relativeDbName = relation.getRelativeDbSchema();
                            metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
                            String finalFieldName2 = fieldName;
                            List<MetaCol> list = metaObject.getCols().stream().filter(col -> col.getColName().equals(finalFieldName2)).toList();
                            if(!CollectionUtils.isEmpty(list)) metaCol  = list.getFirst();
                        } else {
                            objName = "";
                        }
                    }

                    if(metaCol != null
                            && metaCol.getRelationType() == MetaRelationType.ENUM && metaObject != null){
                        Map<String, NameValue<String, String>> enumMap = metaCol.getEnumMap();
                        String[] array = enumMap.entrySet().stream().map(entry -> {
                            NameValue<String, String> value = entry.getValue();
                            return value.getValue();
                        }).filter(Objects::nonNull).toArray(String[]::new);
//                        generateCombinations
                        ExcelFileUtil.setOption(cell, array);
                    }else if(metaCol != null
                            && metaCol.getDataType() == DataType.BOOL){
                        String[] array = Stream.of("是", "否").toArray(String[]::new);
                        ExcelFileUtil.setOption(cell, array);
                    } else {

                        List<MetaRelation> metaRelations = collect.get(objName.concat(",").concat(fieldName));
                        if (CollectionUtils.isEmpty(metaRelations)) continue;
                        MetaRelation metaRelation = metaRelations.getFirst();
                        List<Object> objects = map.get(metaRelation);


                        if (CollectionUtils.isEmpty(objects)) continue;
                        String valueColName = metaRelation.getLabelColName();
                        String labelColExtra = metaRelation.getLabelColExtra();

                        String[] array = objects.stream().map(o -> {
                            try {
                                if (BaseUtil.isNullOrEmpty(labelColExtra))
                                    return String.valueOf(((Map) o).get(valueColName));
                                else{
                                    StringBuilder value = new StringBuilder(String.valueOf(((Map) o).get(valueColName)));
                                    String[] split = labelColExtra.split(",");
                                    for (String string : split) {
                                        value.append(" ").append(((Map) o).get(string));
                                    }
                                    return value.toString();
                                }
                                //return String.valueOf(((Map) o).get(valueColName));
                            } catch (Exception e) {
                                try {
                                    Field declaredField = o.getClass().getDeclaredField(valueColName);
                                    declaredField.setAccessible(true);
                                    //return String.valueOf(declaredField.get(o));
                                    String string = String.valueOf(declaredField.get(o));
                                    if (BaseUtil.isNullOrEmpty(labelColExtra))
                                        return string;
                                    else{
                                        StringBuilder value = new StringBuilder(string);
                                        String[] split = labelColExtra.split(",");
                                        for (String value1 : split) {
                                            Field declaredField2 = o.getClass().getDeclaredField(value1);
                                            declaredField2.setAccessible(true);
                                            Object o1 = declaredField2.get(o);
                                            if (o1 != null) {
                                                String string2 = String.valueOf(o1);
                                                value.append(" ").append(string2);
                                            }
                                        }
                                        return value.toString();
                                    }
                                } catch (Exception e1) {
                                    try {
                                        Field declaredField = o.getClass().getSuperclass().getDeclaredField(valueColName);
                                        declaredField.setAccessible(true);
                                        //return String.valueOf(declaredField.get(o));
                                        String string = String.valueOf(declaredField.get(o));
                                        if (BaseUtil.isNullOrEmpty(labelColExtra))
                                            return string;
                                        else{
                                            StringBuilder value = new StringBuilder(string);
                                            String[] split = labelColExtra.split(",");
                                            for (String value1 : split) {
                                                Field declaredField2 = o.getClass().getDeclaredField(value1);
                                                declaredField2.setAccessible(true);
                                                Object o1 = declaredField2.get(o);
                                                if (o1 != null) {
                                                    String string2 = String.valueOf(o1);
                                                    value.append(" ").append(string2);
                                                }
                                            }
                                            return value.toString();
                                        }
                                    } catch (Exception ex) {
                                        logger.info(e.getMessage());
                                        return null;
                                    }


                                }
                            }
                        }).filter(Objects::nonNull).toArray(String[]::new);
/*
                        String[] array = objects.stream().map(o -> {
                            try {
                                return String.valueOf(((Map) o).get(valueColName));
                            } catch (Exception e) {
                                try {
                                    Field declaredField = o.getClass().getDeclaredField(valueColName);
                                    declaredField.setAccessible(true);
                                    return String.valueOf(declaredField.get(o));
                                } catch (Exception e1) {
                                    logger.info(e.getMessage());
                                    return null;
                                }
                            }
                        }).filter(Objects::nonNull).toArray(String[]::new);*/
                        Sheet sheet1 = workbook.getSheet(metaRelation.getDisplayLabel());
                        if (sheet1 == null) {
                            sheet1 = workbook.createSheet(metaRelation.getDisplayLabel());
                        }
                        ExcelFileUtil.setOption(cell, array, sheet1);
                    }
                }
            }

            file = File.createTempFile(repository.getMetaObject().getDisplayLabel().concat("_"), ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
        }
        return file;
    }

    public static String[] generateCombinations(String[] strings) {
        List<String> result = new ArrayList<>();
        int n = strings.length;

        // 生成所有非空子集（2^n - 1种可能）
        for (int mask = 1; mask < (1 << n); mask++) {
            StringBuilder sb = new StringBuilder();

            // 检查每个bit位
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sb.append(strings[i]);
                }
            }
            result.add(sb.toString());
        }

        return result.toArray(String[]::new);
    }

    public File writeAllNew(String templateFile, final List<T> list, final String filename,
                         int startRowIndex, boolean writeHeadRow, int tenantID) throws IOException {
        Assert.notNull(templateFile, "templateFile must not be null");
        File file1;
        if (templateFile.startsWith("http")) {
            file1 = getFileByUrl(templateFile);
        } else {
            file1 = new File(templateFile);
        }

        if (file1 == null) return null;
        Path tempFile = Files.createTempFile("temp", filename);
        Files.copy(file1.toPath(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        try (final Workbook workbook = new XSSFWorkbook(tempFile.toFile().getPath())) {

        }

        return file1;
    }

    public File writeAll(String templateFile, final List<T> list, final String filename,
                         int startRowIndex, boolean writeHeadRow, int tenantID,String format,final Map<MetaRelation, List<Object>> map,boolean hasOption) throws IOException {
        Assert.notNull(templateFile, "templateFile must not be null");
        File file1;
        if (templateFile.startsWith("http")) {
            file1 = getFileByUrl(templateFile);
        } else {
            file1 = new File(templateFile);
        }

        if (file1 == null) return null;
        Path tempFile = Files.createTempFile("temp", filename);
        Files.copy(file1.toPath(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        try (final Workbook workbook = new XSSFWorkbook(tempFile.toFile().getPath())) {
            workbook.setSheetName(0, repository.getMetaObject().getDisplayLabel());
            Sheet sheet = workbook.getSheetAt(0);
            SheetNameData sheetNameData = new SheetNameData(sheet);

            MetaObject metaObject = repository.getMetaObject();
            List<MetaCol> cols = metaObject.getCols();
            String colName= StringUtil.isNotBlank(metaObject.getNameCol()) ? metaObject.getNameCol() : metaObject.getUniqueKey();
            List<File> files = new ArrayList<>();
            String dataNameOne = "";
            for (T t : list) {
                String dataName = filename;
                try {
                    dataName = metaObject.getDisplayLabel();
                }catch (Exception e) {
                    logger.info(e.getMessage());
                }

                try {
                    Object o = ReflectionUtil.executeMethodAndGet(t, "getExportFileName");
                    if(o instanceof String){
                        dataName = dataName+"_"+ o+"_";
                    }else if(o == null){
                        String dateStr = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                        dataName = dataName+"_"+dateStr+"_";
                    }
                }catch (Exception e){
                    logger.info(e.getMessage());
                }
                dataNameOne = dataName;
                SheetNameInfo sheetNameInfo = sheetNameData.getSheetNameInfo(0);
                Map<String, List<SheetItemName>> itemMap = sheetNameInfo.getDataInfoItemsName().stream().collect(Collectors.groupingBy(SheetItemName::getItemsName));


                if ("batch".equals(sheetNameData.getSheetNameDataType()) || list.size() == 1) {
                    SheetNameInfo nextSimpleDataName = sheetNameData.createNextSimpleDataName();
                    var dataTable = BindingTable.fromTemplate(repository, sheet, null, tenantID, t, nextSimpleDataName, null);
                    dataTable.writeTemplate(sheet);
                    sheetNameData.delSheetNameInfoByNo(0, sheet);
                    setMergedRegions(sheet, itemMap);
                    setMergedRegionsMain(sheet,file1);
                    if(hasOption) {
                        setOption(sheet, templateFile, map);
                    }
                    sheet.setForceFormulaRecalculation(true);

                }else {
                    Path file2 = Files.createTempFile("temp", dataName);


                    Files.copy(file1.toPath(), file2, StandardCopyOption.REPLACE_EXISTING);
                    Workbook workbook1 = new XSSFWorkbook(file2.toFile().getPath());
                    workbook1.setSheetName(0, repository.getMetaObject().getDisplayLabel());
                    Sheet sheet1 = workbook1.getSheetAt(0);
                    SheetNameData sheetNameData1 = new SheetNameData(sheet1);
                    SheetNameInfo nextSimpleDataName = sheetNameData1.createNextSimpleDataName();
                    var dataTable = BindingTable.fromTemplate(repository, sheet1, null, tenantID, t, nextSimpleDataName, null);
                    dataTable.writeTemplate(sheet1);
                    sheetNameData1.delSheetNameInfoByNo(0, sheet1);
                    setMergedRegions(sheet1, itemMap);
                    setMergedRegionsMain(sheet1,file1);
                    if(hasOption) {
                        setOption(sheet1, templateFile, map);
                    }
                    sheet1.setForceFormulaRecalculation(true);
                    File file = File.createTempFile(dataName, ".xlsx");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    workbook1.write(outputStream);
                    files.add(file);
                }
            }

            File file = null;
            if (files.isEmpty()) {
                if(!StringUtil.isNotBlank(dataNameOne)){
                    dataNameOne = repository.getMetaObject().getDisplayLabel().concat("_");
                }
                File tempFile1 = File.createTempFile(dataNameOne, ".xlsx");
                FileOutputStream outputStream = new FileOutputStream(tempFile1);
                workbook.write(outputStream);
                if("pdf".equals(format)){
                    file = File.createTempFile(dataNameOne, ".pdf");
                    PdfConverter.excelToPdf(tempFile1.getPath(),file.getPath(),".xlsx");
                }else {
                    file = tempFile1;
                }

            } else {
                file = Files.createTempFile(repository.getMetaObject().getDisplayLabel().concat("_"),".zip").toFile();
                zipFiles(files, file);
            }

            try {
                Mono<File> mono = ExcelFileUtil.uploadExcelFile(file.getName(), file);
                CompletableFuture<File> future = mono.toFuture();
                return future.get();
            }catch (Exception e) {
                logger.info(e.getMessage());
            }

            return file;
        }

    }



    private void setOption(Sheet sheet,String templateFile, Map<MetaRelation, List<Object>> map) throws IOException {
        File file = writeBasicDataForTemplate(templateFile, map);
        try (Workbook workbook = new XSSFWorkbook(file.getPath())) {

            Workbook workbookData = sheet.getWorkbook();

            for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheetAt = workbook.getSheetAt(i);
                if(workbookData.getSheet(sheetAt.getSheetName()) != null) continue;
                Sheet sheetData = workbookData.createSheet(sheetAt.getSheetName());
                for (int i1 = 0; i1 <= sheetAt.getLastRowNum(); i1++) {
                    Row row = sheetAt.getRow(i1);
                    for (int j1 = 0; j1 < row.getLastCellNum(); j1++) {
                        Cell cell1 = row.getCell(j1);
                        sheetData.createRow(i1).createCell(j1).setCellValue(cell1.getStringCellValue());
                    }
                }
                workbookData.setSheetHidden(workbookData.getSheetIndex(sheetData.getSheetName()),true);
            }
            Sheet sheetAt = workbook.getSheetAt(0);
            //模版
            SheetNameData sheetNameTemple = new SheetNameData(sheetAt);
            //数据文件
            SheetNameData sheetNameData = new SheetNameData(sheet);

            List<SheetNameInfo> nameDataInfosTemple = sheetNameTemple.getNameDataInfos();

            List<SheetNameInfo> nameDataInfos = sheetNameData.getNameDataInfos();

            if(CollectionUtils.isEmpty(nameDataInfosTemple) || CollectionUtils.isEmpty(nameDataInfos))return;

            SheetNameInfo nameInfoTemple = nameDataInfosTemple.getFirst();
            SheetNameInfo nameInfo = nameDataInfos.getFirst();

            setMainOption(nameInfoTemple,sheet);
            setItemsOption(nameInfoTemple,nameInfo);


        }
    }

    private void setItemsOption(SheetNameInfo nameInfoTemple, SheetNameInfo nameInfo) {

        List<SheetItemName> dataInfoItemsDataNameTemple = nameInfoTemple.getDataInfoItemsDataName();
        List<SheetItemName> dataInfoItemsDataName = nameInfo.getDataInfoItemsDataName();
        if(CollectionUtils.isEmpty(dataInfoItemsDataNameTemple) || CollectionUtils.isEmpty(dataInfoItemsDataName)) return;
        Map<String, List<SheetItemName>> collect = dataInfoItemsDataName.stream().collect(Collectors.groupingBy(SheetItemName::getItemsName));
        for (SheetItemName sheetItemName : dataInfoItemsDataNameTemple) {
            List<SheetItemName> sheetItemNames = collect.get(sheetItemName.getItemsName());
            if(CollectionUtils.isEmpty(sheetItemNames)) continue;
            SheetName sheetName = sheetItemNames.getFirst().getSheetName();
            SheetName sheetNameTemple = sheetItemName.getSheetName();
            Cell cell = sheetNameTemple.getCells().getFirst();
            Row r = cell.getRow();
            Row row = r.getSheet().getRow(r.getRowNum() + 1);
            if(row == null) continue;
            HashMap<Integer, Cell> map = new HashMap<>();
            for(int i = sheetNameTemple.getMinColNum() ; i <= sheetNameTemple.getMaxColNum();i++){
                Cell cell1 = row.getCell(i);
                if(cell1 != null && hasDropdown(cell1)){
                    map.put(i,cell1);
                }
            }
            Sheet sheet = sheetName.getCells().getFirst().getSheet();

            for(int i = sheetName.getMinRowNum(); i <= sheetName.getMaxRowNum();i++){
                Row row1 = sheet.getRow(i);
                if(row1 == null) row1 = sheet.createRow(i);
                for(int j = sheetName.getMinColNum(); j <= sheetName.getMaxColNum();j++){
                    Cell cell2 = map.get(j);
                    if(cell2 == null)continue;
                    Cell cell1 = row1.getCell(j);
                    if(cell1 == null) cell1 = row1.createCell(j);
                    copyDropdown(cell2,cell1);
                }
            }


        }

    }

    private static void setMainOption(SheetNameInfo nameInfoTemple,Sheet sheet) {
        List<SheetName> dataInfoNameTemple = nameInfoTemple.getDataInfoName();
        if(CollectionUtils.isEmpty(dataInfoNameTemple) )return;
        SheetName sheetNameTemple = dataInfoNameTemple.getFirst();
        List<Cell> cells = sheetNameTemple.getCells();
        for (Cell cell : cells) {
            if(hasDropdown(cell)){
                Row row = sheet.getRow(cell.getRowIndex());
                if(row == null) continue;
                Cell cell1 = row.getCell(cell.getColumnIndex());
                if(cell1 == null) cell1 = row.createCell(cell.getColumnIndex());
                copyDropdown(cell,cell1);
            }
        }


    }

    public static boolean hasDropdown(Cell cell) {
        Sheet sheet = cell.getSheet();
        for (DataValidation dataValidation : sheet.getDataValidations()) {
            CellRangeAddressList addressList = dataValidation.getRegions();
            for (CellRangeAddress range : addressList.getCellRangeAddresses()) {
                if (range.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                    return dataValidation.getValidationConstraint()
                            .getValidationType() == DataValidationConstraint.ValidationType.LIST;
                }
            }
        }
        return false;
    }

    public static void copyDropdown(Cell sourceCell, Cell targetCell) {
        Sheet sheet = sourceCell.getSheet();
        Sheet sheet1 = targetCell.getSheet();
        for (DataValidation dataValidation : sheet.getDataValidations()) {
            CellRangeAddressList addressList = dataValidation.getRegions();
            for (CellRangeAddress range : addressList.getCellRangeAddresses()) {
                if (range.isInRange(sourceCell.getRowIndex(), sourceCell.getColumnIndex())) {
                    DataValidationConstraint constraint = dataValidation.getValidationConstraint();
                    DataValidationHelper helper = sheet.getDataValidationHelper();

                    // 创建新验证范围（仅目标单元格）
                    CellRangeAddressList newRange = new CellRangeAddressList(
                            targetCell.getRowIndex(),
                            targetCell.getRowIndex(),
                            targetCell.getColumnIndex(),
                            targetCell.getColumnIndex()
                    );

                    // 根据类型创建新验证规则
                    DataValidation newValidation;
                    if (constraint.getValidationType() == DataValidationConstraint.ValidationType.LIST) {
                        if (constraint.getFormula1() != null) {
                            // 公式型下拉框（如"=Sheet1!$A$1:$A$5"）
                            newValidation = helper.createValidation(
                                    helper.createFormulaListConstraint(constraint.getFormula1()),
                                    newRange
                            );
                        } else {
                            // 显式值下拉框（如"苹果,香蕉,橙子"）
                            String[] explicitValues = constraint.getExplicitListValues();
                            newValidation = helper.createValidation(
                                    helper.createExplicitListConstraint(explicitValues),
                                    newRange
                            );
                        }

                        // 复制验证属性
                        newValidation.setShowErrorBox(dataValidation.getShowErrorBox());
                        newValidation.setErrorStyle(dataValidation.getErrorStyle());
                        sheet1.addValidationData(newValidation);
                    }
                    return;
                }
            }
        }
    }

    private void setMergedRegionsMain(Sheet sheet,File file1) throws IOException {
        Path tempFile = Files.createTempFile("temp","option");
        Files.copy(file1.toPath(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        try (final Workbook workbook = new XSSFWorkbook(tempFile.toFile().getPath())) {
            Sheet sheet1 = workbook.getSheetAt(0);
            SheetNameData sheetNameData = new SheetNameData(sheet);
            List<Cell> list = sheetNameData.getNameDataInfos()
                    .stream()
                    .map(SheetNameInfo::getDataInfoName)
                    .flatMap(Collection::stream)
                    .map(SheetName::getCells)
                    .flatMap(Collection::stream)
                    .toList();

            List<SheetItemName> dataInfoItemsDataName = sheetNameData.getNameDataInfos().get(0).getDataInfoItemsDataName();
            List<Integer> list1 = dataInfoItemsDataName.stream().map(x -> x.getSheetName().getCells())
                    .flatMap(Collection::stream)
                    .map(Cell::getRowIndex).distinct().toList();
            List<CellRangeAddress> mergedRegions = sheet1.getMergedRegions();
            for (CellRangeAddress range : mergedRegions) {
                int count =(int) list1.stream().filter(x -> x < range.getFirstRow()).count();
                CellRangeAddress cellAddresses1 = new CellRangeAddress(range.getFirstRow()+count, range.getLastRow()+count, range.getFirstColumn(), range.getLastColumn());
                boolean b = list.stream().anyMatch(cell -> checkCellInCellRangeAddress(cell, cellAddresses1));
                if(b && !checkSame(sheet, cellAddresses1)){
                  sheet.addMergedRegion(cellAddresses1);
                }
            }
        }
    }

    private Boolean checkCellInCellRangeAddress(Cell cell,CellRangeAddress range){
        return  (cell.getRowIndex() >= range.getFirstRow() &&
                cell.getRowIndex() <= range.getLastRow() &&
                cell.getColumnIndex() >= range.getFirstColumn() &&
                cell.getColumnIndex() <= range.getLastColumn());
    }

    private static void setMergedRegions(Sheet sheet, Map<String, List<SheetItemName>> itemMap) {
        try {
        SheetNameData sheetNameData1 = new SheetNameData(sheet);
        SheetNameInfo sheetNameInfo1 = sheetNameData1.getNameDataInfos().get(0);


        for (SheetItemName sheetItemName : sheetNameInfo1.getDataInfoItemsDataName()) {

            List<SheetItemName> sheetItemNames = itemMap.get(sheetItemName.getItemsName());
            if(!CollectionUtils.isEmpty(sheetItemNames)){
                SheetItemName sheetItemName1 = sheetItemNames.get(0);
                Map<Integer, CellRangeAddress> mergedRegionsMap = sheetItemName1.getSheetName().getMergedRegionsMap();
                SheetName sheetName = sheetItemName.getSheetName();
                int minRowNum = sheetName.getMinRowNum();
                int maxRowNum = sheetName.getMaxRowNum();
                int minColNum = sheetName.getMinColNum();
                int maxColNum = sheetName.getMaxColNum();
                for (int i = minRowNum ;i<=maxRowNum;i++){
                    for (int j = minColNum;j<=maxColNum;j++){
                        CellRangeAddress cellAddresses = mergedRegionsMap.get(j);
                        if(cellAddresses != null){
                            CellRangeAddress cellAddresses1 = new CellRangeAddress(i, i, cellAddresses.getFirstColumn(), cellAddresses.getLastColumn());
                            if(!checkSame(sheet, cellAddresses1)){
                                sheet.addMergedRegion(cellAddresses1);
                                Row row = sheet.getRow(i);
                                if(row != null){
                                    Cell cell1 = row.getCell(cellAddresses.getFirstColumn());
                                    if(cell1 != null){
                                        for(int i1 = cellAddresses.getFirstColumn()+1 ; i1<=cellAddresses.getLastColumn() ; i1++){
                                            Cell cell2 = row.getCell(i1);
                                            if(cell2 == null)cell2 = row.createCell(i1);
                                            cell2.setCellStyle(cell1.getCellStyle());
                                        }
                                    }
                                }
                            };

                        }
                    }
                }


            }
        }
        }catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private static Boolean checkSame(Sheet sheet, CellRangeAddress cellAddresses1) {
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            if(cellAddresses1.getFirstRow() == mergedRegion.getFirstRow()
             && cellAddresses1.getLastRow() == mergedRegion.getLastRow()
            && cellAddresses1.getFirstColumn() == mergedRegion.getFirstColumn()
            && cellAddresses1.getLastColumn() == mergedRegion.getLastColumn()){
                return true;
            }
        }

        return false;
    }

    public void zipFiles(List<File> files, File zipFile) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                try (InputStream is = new FileInputStream(file)) {
                    zout.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        zout.write(buffer, 0, len);
                    }
                }
            }
        }
    }

    public File writeAll(String templateFile, final List<T> list, final String filename,
                         int startRowIndex, boolean writeHeadRow, int tenantID, Boolean byConstraintSetLabelsStyle) throws IOException, InvalidFormatException {
        Assert.notNull(templateFile, "templateFile must not be null");
        // 获取系统的临时目录
        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        // 构建目标临时文件路径

        File tempFile = new File(tempDir, filename);
        Files.copy(new File(templateFile).toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try (final Workbook workbook = new XSSFWorkbook(tempFile)) {
            Sheet sheet = workbook.getSheetAt(0);
            var dataTable = BindingTable.fromSheet(repository, sheet, startRowIndex, tenantID);
            dataTable.write(sheet, list, startRowIndex, writeHeadRow, byConstraintSetLabelsStyle);
            File file = File.createTempFile(filename, ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            return file;
        }
    }

    public File writeAll(String templateFile, final List<T> list, final String filename, int tenantID,String format,final Map<MetaRelation, List<Object>> map,boolean hasOption) throws IOException {
        return writeAll(templateFile, list, filename, 0, true, tenantID,format,map,hasOption);
    }

    public File writeAll(String templateFile, final List<T> list, final String filename, int tenantID,String format) throws IOException {
        return writeAll(templateFile, list, filename, 0, true, tenantID,format,null,false);
    }

    public File writeAll(String templateFile, final List<T> list, final String filename, int tenantID) throws IOException {
        return writeAll(templateFile, list, filename, 0, true, tenantID,"excel",null,false);
    }

    /**
     * 将listSupplier数据列表提供者返回的数据写入filename指定的临时文件
     *
     * @param listSupplier  数据列表提供者
     * @param filename      文件名
     * @param startRowIndex 开始行，默认0
     * @param writeHeadRow  是否写标题行
     * @return 临时文件
     * @throws IOException
     */
    public File writeAll(final PagedListSupplier<T> listSupplier, final String filename, int startRowIndex, boolean writeHeadRow, int tenantID) throws IOException {
        try (final Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            var dataTable = writeHeadRow
                    ? BindingTable.listed(repository, tenantID, workbook)
                    : BindingTable.all(repository, tenantID,workbook);
            dataTable.write(sheet, listSupplier, startRowIndex, writeHeadRow);
            File file = File.createTempFile(filename, ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            return file;
        }
    }

    /**
     * 将error信息写入excel
     *
     * @param errorDataList    error信息
     * @param file             要写入的文件
     * @param templateFilePath 如果是模版传模版路径
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public File writeError(List<FileImportErrorInfo> errorDataList, File file, String templateFilePath) throws IOException, InvalidFormatException {
        try (final Workbook workbook = new XSSFWorkbook(file)) {
            if (templateFilePath == null) {
                for (FileImportErrorInfo errorData : errorDataList) {
                    Sheet sheet = workbook.getSheet(errorData.getObjName());
                    if (sheet == null) continue;
                    Map<String, Integer> map = new HashMap<>();
                    Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        map.put(cell.getStringCellValue(), cell.getColumnIndex());
                    }
                    Map<String, List<ValidationError>> collect = errorData.getValidationErrors().stream().collect(Collectors.groupingBy(ValidationError::getField));

                    collect.forEach((k, v) -> {
                        Repository subRepository = repository;
                        if (!errorData.getObjName().equals(repository.getMetaObject().getObjName())) {
                            try {
                                var repositoryName = NamingUtil.firstLetterLower(errorData.getObjName()) + "Repository";

                                subRepository = ApplicationContextExcelProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);
                            } catch (Exception e) {
                                logger.info(e.getMessage());
                            }
                        }

                        MetaCol col = subRepository.getMetaObject().getCol(k);
                        if(col == null) return;
                        Integer columnIndex = map.get(col.getDisplayLabel());
                        Row row = sheet.getRow(errorData.getRowNum() + 1);
                        if(row == null) row = sheet.createRow(errorData.getRowNum() + 1);
                        Cell cell1 = row.getCell(columnIndex);
                        List<String> list = v.stream().map(ValidationError::getError).toList();
                        setExplanatory(cell1, String.join("\n", list));
                    });
                }
            } else {
                File fileByUrl = getFileByUrl(templateFilePath);
                Workbook templateWorkbook = new XSSFWorkbook(fileByUrl);
                Sheet sheet = templateWorkbook.getSheetAt(0);
                Sheet sheetData = workbook.getSheetAt(0);
                SheetNameData sheetNameData = new SheetNameData(sheetData);
                Map<String, ? extends List<? extends Name>> collect = templateWorkbook.getAllNames().stream().collect(Collectors.groupingBy(Name::getNameName));
                for (FileImportErrorInfo fileImportErrorInfo : errorDataList) {
                    String objName = fileImportErrorInfo.getObjName();
                    List<? extends Name> list =collect.entrySet().stream()
                            .filter(entry -> entry.getKey().contains("simpleData"))
                                    .map(entry -> entry.getValue().getFirst()).toList();
                    if (list == null) continue;
                    Map<String, List<Cell>> collect1 = parseNamedRangeFormula(list.getFirst(), sheet).stream()
                            .filter(cell -> cell.getCellType() == CellType.STRING)
                            .collect(Collectors.groupingBy(cell ->  cell.getStringCellValue().replaceFirst("t.", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("customProperties", "")
                            .replace("$", "")
                            .replace("items", "")));
                    Map<String, List<ValidationError>> map = fileImportErrorInfo.getValidationErrors().stream().collect(Collectors.groupingBy(ValidationError::getField));


                    map.forEach((k, v) -> {
                        List<Cell> cells = collect1.get(k);
                        if (cells == null) return;
                        Cell cell = cells.get(0);
                        Cell errorCell;
                        if (objName.contains("items")) {
                            errorCell = sheetData.getRow(cell.getRowIndex() + fileImportErrorInfo.getRowNum() - 1).getCell(cell.getColumnIndex());
                        } else {
                            int rowIndex = cell.getRowIndex();
                            if("batch".equals(sheetNameData.getSheetNameDataType())){
                                rowIndex += fileImportErrorInfo.getRowNum()-1;
                                SheetNameInfo sheetNameInfo = sheetNameData.getSheetNameInfo(0);
                                for (int i = 0; i < fileImportErrorInfo.getRowNum(); i++) {
                                    if(sheetNameInfo.checkNotHandleByRow(cell.getRowIndex() + i)) {
                                        rowIndex++;
                                        int n = 1;
                                        while (sheetNameInfo.checkNotHandleByRow(cell.getRowIndex() + i + (n++)))
                                            rowIndex++;
                                    }
                                }

                            }
                            errorCell = sheetData.getRow(rowIndex).getCell(cell.getColumnIndex());
                        }
                        List<String> list1 = v.stream().map(ValidationError::getError).toList();
                        setExplanatory(errorCell, String.join("\n", list1));
                    });
                }

            }

            File fileTemp = File.createTempFile(file.getName(), ".xlsx");
//                File fileTemp = new File("/Users/cuixuan/Desktop/mes/未命名文件夹/test.xlsx");
            FileOutputStream outputStream = new FileOutputStream(fileTemp);
            workbook.write(outputStream);
            return file;
        }

    }

    // 解析命名范围公式，返回单元格范围列表
    private static List<Cell> parseNamedRangeFormula(Name name, Sheet sheet) {

        List<Cell> cells = new ArrayList<>();
        // 使用正则表达式匹配单元格范围
        //Sheet1!$B$2:$E$4
        String refersToFormula = name.getRefersToFormula();

        if (refersToFormula.split("\\$").length == 3) {
            String str = refersToFormula.replace("$", "").split("!")[1];
            Row r = sheet.getRow(Integer.parseInt(String.valueOf(str.charAt(1))) - 1);
            Cell c = r.getCell(CellReference.convertColStringToIndex(String.valueOf(str.charAt(0))), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            cells.add(c);
            return cells;
        }
        String[] split = refersToFormula.replace("$", "").split("!")[1].split(":");


        // 解析起始和结束单元格的行列号
        int startRow = Integer.parseInt(split[0].substring(1)) - 1;
        int startCol = CellReference.convertColStringToIndex(String.valueOf(split[0].charAt(0)));
        int endRow = Integer.parseInt(split[1].substring(1)) - 1;
        int endCol = CellReference.convertColStringToIndex(String.valueOf(split[1].charAt(0)));

        // 创建单元格范围对象并添加到列表中
        CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);

        // 遍历范围内的所有行和列
        for (int row = address.getFirstRow(); row <= address.getLastRow(); row++) {
            Row r = sheet.getRow(row);
            if (r == null) {
                continue; // 如果行是空的，则跳过
            }
            for (int col = address.getFirstColumn(); col <= address.getLastColumn(); col++) {
                Cell c = r.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (c != null) {
                    cells.add(c);
                }
            }
        }


        return cells;
    }

    public static void setExplanatory(Cell cell, String annotations) {
        if(cell == null)return;
        CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
        Comment cellComment1 = cell.getCellComment();
        if (cellComment1 != null) {
            String concat = cellComment1.getString().getString().concat("/n").concat(annotations);
            cellComment1.setString(helper.createRichTextString(concat));
        } else {
            Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();

            ClientAnchor clientAnchor = helper.createClientAnchor();
            clientAnchor.setCol1(cell.getColumnIndex());
            clientAnchor.setCol2(cell.getColumnIndex());
            clientAnchor.setRow1(cell.getRowIndex());
            clientAnchor.setRow2(cell.getRowIndex());
            clientAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            cellComment1 = drawing.createCellComment(clientAnchor);
            cellComment1.setString(helper.createRichTextString(annotations));
            cell.setCellComment(cellComment1);
        }
        if (styleRegistry==null){
            styleRegistry = new WorkbookStyleRegistry(cell.getSheet().getWorkbook());
        }

        CellStyle cellStyle =styleRegistry.getStyle(CellStyleKey.builder()
                .fillPattern(FillPatternType.SOLID_FOREGROUND)
                .fillForegroundColor(IndexedColors.RED.getIndex())
                .build());
//        CellStyle cellStyle =cell.getSheet().getWorkbook().createCellStyle();
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());

        cell.setCellStyle(cellStyle);
    }


    public void writeAll(File templateFile, String templateFilePath, Sheet sheet, SheetNameData sheetNameData, T t, int tenantID,
                         Map<MetaRelation, List<Object>> optionMap, boolean hasOption) throws IOException {
        Assert.notNull(templateFile, "templateFile must not be null");

        SheetNameInfo sheetNameInfo = sheetNameData.getSheetNameInfo(0);
        Map<String, List<SheetItemName>> itemMap = sheetNameInfo.getDataInfoItemsName().stream().collect(Collectors.groupingBy(SheetItemName::getItemsName));

        SheetNameInfo nextSimpleDataName = sheetNameData.createNextSimpleDataName();
        var dataTable = BindingTable.fromTemplate(repository, sheet, null, tenantID, t, nextSimpleDataName, null);
        dataTable.writeTemplate(sheet);
        sheetNameData.delSheetNameInfoByNo(0, sheet);
        setMergedRegions(sheet, itemMap);
        setMergedRegionsMain(sheet, templateFile);
        if (hasOption) {
            setOption(sheet, templateFilePath, optionMap);
        }
        sheet.setForceFormulaRecalculation(true);

    }


    public void writeAll() {
    }
}
