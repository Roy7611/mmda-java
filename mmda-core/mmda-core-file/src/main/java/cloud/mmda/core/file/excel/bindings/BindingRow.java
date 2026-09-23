package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.data.jdbc.repository.TenancyRepository;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.app.ApplicationContextExcelProvider;
import cloud.mmda.core.file.constant.Annotations;
import cloud.mmda.core.file.excel.ExcelConstant;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.utils.NameValue;
import cloud.mmda.core.utils.NamingUtil;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 绑定行
 *
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
@Slf4j
public class BindingRow<T extends SequencedRow, K> {
    protected final Repository<T, K> repository;
    private final MetaObject metaObj;
    private final List<BindingCell<T>> bindingCells;
    private final int tenantID;
    private final String[] data = {"是", "否"};
    ConcurrentMap<MetaCol, MetaRelation> colRelationMap;
    ConcurrentMap<String, Map<String, String>> refMap = new ConcurrentHashMap<>();
    private final int MAX_NUM = 10000;
    private final WorkbookStyleRegistry styleRegistry;


    /**
     * 行数据校验器，读取时如果缺项则认为是空行
     */
    @Getter
    @Setter
    private Predicate<Row> rowValidator;

    public void requiredNonBlank(final String cellName) {
        Optional<BindingCell<T>> bindingCell = bindingCells.stream()
                .filter(bc -> bc.getName().equals(cellName))
                .findFirst();
        if (bindingCell.isPresent()) {
            int columnIndex = bindingCell.get().getColumnIndex();
            Predicate<Row> requiredValidator = (row) -> {
                var cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                return cell.getCellType() != CellType.BLANK;
            };
            if (rowValidator == null) {
                rowValidator = requiredValidator;
            } else {
                rowValidator = rowValidator.and(requiredValidator);
            }
        }
    }

    public BindingRow(final Repository<T, K> repository, List<BindingCell<T>> bindingCells, final int tenantID,WorkbookStyleRegistry styleRegistry) {
        this.repository = repository;
        this.metaObj = repository.getMetaObject();
        this.bindingCells = bindingCells;
        this.tenantID = tenantID;
        this.styleRegistry=styleRegistry;
    }

    /**
     * 创建绑定行，默认包含所有字段，默认从第一列开始，0列可用于输出行号。
     *
     * @param repository
     * @param <T>        实体类型
     * @param <K>        实体主键类型
     * @return
     */
    public static <T extends SequencedRow, K> BindingRow<T, K> all(final Repository<T, K> repository, int tenantID,WorkbookStyleRegistry registry) {
        var builder = SimpleBindingCell.builder(repository.getMetaObjectAccess(),registry);
        var metaObj = repository.getMetaObject();
        var bindingCells = new ArrayList<BindingCell<T>>();
        for (final MetaCol metaCol : metaObj.getCols()) {
            if (Arrays.stream(ExcelConstant.IGNORE_FIELDS).anyMatch(ignore -> (metaCol.getColName().equalsIgnoreCase(ignore) && !metaCol.isKey())
            )) continue;
            BindingCell<T> colBindingCell = builder.metaCol(metaCol).build();
            bindingCells.add(colBindingCell);
        }
        return new BindingRow<>(repository, bindingCells, tenantID, registry);
    }

    /**
     * 构造绑定行，包含predicate判断通过的字段。例如仅绑定列出的部分字段（col->col.isListed）。
     *
     * @param repository
     * @param filter           仅绑定此测试条件通过的字段
     * @param startColumnIndex 从哪一列开始绑定
     * @param <T>              实体类型
     * @param <K>              实体主键类型
     * @return
     */
    public static <T extends SequencedRow, K> BindingRow<T, K> filtered(final Repository<T, K> repository, final Predicate<MetaCol> filter, int startColumnIndex, int tenantID,WorkbookStyleRegistry registry) {
        var builder = SimpleBindingCell.builder(repository.getMetaObjectAccess(),registry);
        int columnIndex = startColumnIndex;
        var metaObj = repository.getMetaObject();
        var bindingCells = new ArrayList<BindingCell<T>>();
        for (final MetaCol metaCol : metaObj.getCols()) {
            if (!filter.test(metaCol)) continue;
            BindingCell<T> colBindingCell = builder.metaCol(metaCol).columnIndex(columnIndex++).build();
            bindingCells.add(colBindingCell);
        }
        return new BindingRow<>(repository, bindingCells, tenantID,registry);
    }

    /**
     * 构造绑定行，包含colNames中列出的字段。用于客户端调整可见性和顺序。
     *
     * @param repository
     * @param colNames         字段名称列表
     * @param startColumnIndex 从哪一列开始绑定
     * @param <T>              实体类型
     * @param <K>              实体主键类型
     * @return
     */
    public static <T extends SequencedRow, K> BindingRow<T, K> names(final Repository<T, K> repository, final Collection<String> colNames, int startColumnIndex, int tenantID,WorkbookStyleRegistry registry) {
        var builder = SimpleBindingCell.builder(repository.getMetaObjectAccess(),registry);
        int columnIndex = startColumnIndex;
        var metaObj = repository.getMetaObject();
        var bindingCells = new ArrayList<BindingCell<T>>();
        for (final String colName : colNames) {
            var metaCol = metaObj.getCol(colName);
            if (metaCol == null) continue;
            if (Arrays.stream(ExcelConstant.IGNORE_FIELDS).anyMatch(ignore -> (metaCol.getColName().equalsIgnoreCase(ignore) && !metaCol.isKey())
            ))
                continue;

            BindingCell<T> colBindingCell = builder.metaCol(metaCol).columnIndex(columnIndex++).build();
            bindingCells.add(colBindingCell);
        }
        return new BindingRow<>(repository, bindingCells, tenantID,registry);
    }

    public Optional<T> read(final Row row) {
        if (row == null) return Optional.empty();
        if (rowValidator != null && !rowValidator.test(row)) {
            return Optional.empty();
        }
        T t = repository.create();
        for (var bindingCell : bindingCells) {
            Cell cell = row.getCell(bindingCell.getColumnIndex(), bindingCell.getMissingCellPolicy());
            if (cell == null) continue;
            setRel(cell, bindingCell, t);
            bindingCell.read(cell, t);
        }
        setHasMany(t, row);
        return Optional.of(t);
    }

    public void setHasMany(T t, Row row) {
        try {
            Workbook workbook = row.getSheet().getWorkbook();
            String uniqueKeyValue = "";
            for (MetaRelation relation : metaObj.getRelations()) {
                if (relation.getRelationType() == MetaRelationType.HAS_MANY) {
                    String relativeDbName = relation.getRelativeDbSchema();
                    MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
                    Sheet sheet = workbook.getSheet(metaObject.getDisplayLabel());
                    if (sheet == null) continue;
                    String uniqueKey = StringUtil.isNotBlank(metaObj.getNameCol()) ? metaObj.getNameCol():metaObj.getUniqueKey();
                    Map<String, List<MetaCol>> collect = metaObj.getCols().stream().collect(Collectors.groupingBy(MetaCol::getColName));
                    Map<String, List<MetaCol>> collectDis = metaObject.getCols().stream().collect(Collectors.groupingBy(MetaCol::getDisplayLabel));

                    String displayLabel = collect.get(uniqueKey).get(0).getDisplayLabel();
                    Row row1 = row.getSheet().getRow(0);
                    for (int i = 0; i < ((int) row1.getLastCellNum()); i++) {
                        if (row1.getCell(i).getStringCellValue().equals(displayLabel)) {
                            uniqueKeyValue = row.getCell(i).getStringCellValue();
                        }
                    }

                    HashMap<Integer, String> integerStringHashMap = new HashMap<>();

                    Row row3 = sheet.getRow(0);
                    for (int i = 0; i < ((int) row3.getLastCellNum()); i++) {
                        integerStringHashMap.put(i, row3.getCell(i).getStringCellValue());
                    }
                    List<Map<String, Object>> list = new ArrayList<>();
                    int lastRowNum = sheet.getLastRowNum();
                    String uniqueKeyValueCheck = uniqueKeyValue;
                    for (int i = 1; i <= lastRowNum; i++) {
                        Map<String, Object> stringObjectHashMap = new HashMap<>();
                        Row row2 = sheet.getRow(i);
                        int cellNum = row2.getPhysicalNumberOfCells();
                        for (int j = 0; j < cellNum; j++) {
                            Cell cell = row2.getCell(j);
                            if (cell == null) {
                                stringObjectHashMap.put(integerStringHashMap.get(j), null);
                                continue;
                            }
                            Object cellValue = switch (cell.getCellType()) {
                                case BOOLEAN -> cell.getBooleanCellValue();
                                case STRING -> cell.getStringCellValue();
                                case NUMERIC -> cell.getNumericCellValue();
                                default -> null;
                            };


                            List<MetaCol> metaCols = collectDis.get(integerStringHashMap.get(j));
                            if(CollectionUtils.isEmpty(metaCols))continue;
                            MetaCol metaCol = metaCols.get(0);
                            if (metaCol.getRelationType().hasOneOrRef()) {
                                MetaRelation relation1 = metaObject.getRelation(metaCol.getRelationName());
                                List<MetaEnumMember> allRefMap = repository.findAllRefMap(relation1);
                                Map<String, List<MetaEnumMember>> collect1 = allRefMap.stream().collect(Collectors.groupingBy(MetaEnumMember::getText));
                                List<MetaEnumMember> metaEnumMembers = collect1.get(cellValue);
                                if (!CollectionUtils.isEmpty(metaEnumMembers)) {
                                    cellValue = metaEnumMembers.get(0).getValue();
                                }

                            } else if (metaCol.getColName().equals(metaObj.getPartitionKey()) && cell.getStringCellValue().equals(uniqueKeyValueCheck)) {
                                Field declaredField = t.getClass().getDeclaredField(metaObj.getPartitionKey());
                                declaredField.setAccessible(true);
                                String partitionKey = String.valueOf(declaredField.get(t));
                                cellValue = partitionKey;
                                uniqueKeyValue = partitionKey;
                            }

                            stringObjectHashMap.put(metaCol.getColName(), cellValue);
                        }
                        list.add(stringObjectHashMap);
                    }

                    Class<?> aClass = Class.forName(metaObject.getNameSpace().concat(".").concat(metaObject.getObjName()));
                    List<Object> objects = itemToObject(list, metaObject, aClass, uniqueKeyValue,workbook,true);

                    if (CollectionUtils.isEmpty(objects)) continue;
                    Class<?> aClass1 = Class.forName(metaObj.getNameSpace().concat(".").concat(metaObj.getObjName()));
                    Field declaredField = aClass1.getDeclaredField(relation.getRelationName());
                    declaredField.setAccessible(true);
                    declaredField.set(t, objects);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRel(Cell cell, BindingCell<T> bindingCell, T t) {
        try {
            String stringCellValue = "";

            if (cell.getCellType() == CellType.NUMERIC) {
                stringCellValue = String.valueOf((int) cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) { // 字符串
                stringCellValue = cell.getStringCellValue();
            }

            Workbook workbook = cell.getSheet().getWorkbook();

            SimpleBindingCell simpleBindingCell = (SimpleBindingCell) bindingCell;
            MetaCol metaCol = simpleBindingCell.getMetaCol();
            if (metaCol.getRelationType().hasOneOrRef()) {
                MetaRelation relation = metaObj.getRelation(metaCol.getRelationName());
                String labelCol = "";
                Integer labelColCell = null;

                String valueCol = "";
                Integer valueColCell = null;

                MetaObject metaObject = repository.getMetaObject(relation.getRelativeDbSchema(), relation.getRelativeObjName());
                for (MetaCol col : metaObject.getCols()) {
                    if (col.getColName().equalsIgnoreCase(relation.getLabelColName())) {
                        labelCol = col.getDisplayLabel();
                    }
                    if (col.getColName().equalsIgnoreCase(relation.getValueColName())) {
                        valueCol = col.getDisplayLabel();
                    }
                }
                MetaRelation relationSheetName = metaObj.getRelation(metaCol.getRelationName());
                Sheet sheet = workbook.getSheet(relationSheetName.getDisplayLabel());
                if (sheet == null) {
                    return;
                }
                Row row1 = sheet.getRow(0);
                HashMap<Integer, String> integerStringHashMap = new HashMap<>();
                for (int i = 0; i < row1.getLastCellNum() - 1; i++) {
                    String stringCellValue1 = row1.getCell(i).getStringCellValue();
                    if (stringCellValue1.equals(labelCol)) {
                        labelColCell = i;
                    }
                    if (stringCellValue1.equals(valueCol)) {
                        valueColCell = i;
                    }
                    integerStringHashMap.put(i, stringCellValue1);
                }
                if (labelColCell == null || valueColCell == null) return;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row2 = sheet.getRow(i);
                    String stringCellValue1 = getCellStringValue(row2.getCell(labelColCell));
                    if (stringCellValue1.equals(stringCellValue)) {

                        cell.setCellValue(getCellStringValue(row2.getCell(valueColCell)));
                        if (metaCol.getRelationType() == MetaRelationType.HAS_ONE) {
                            setHasOne(metaObject, relation, row2, valueColCell, t);
                        }
                        return;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCellStringValue(Cell cell) {
        String value = "";
        try {
            value = cell.getStringCellValue();
        } catch (Exception e) {
            value = String.valueOf(cell.getNumericCellValue());
        }
        return value;
    }


    public void setHasOne(MetaObject metaObject, MetaRelation relation, Row row2, Integer valueColCell, T t) throws Exception {
        //获取关联对象
        Class<?> aClass = Class.forName(metaObject.getNameSpace().concat(".").concat(metaObject.getObjName()));

        //获取关联数据
        List<Map<String, Object>> allRefItems = repository.findAllRefItems(relation);

        //将数据赋值到关联对象里面
        String stringCellValue = row2.getCell(valueColCell).getStringCellValue();
        List<Object> objects = itemToObject(allRefItems, metaObject, aClass, stringCellValue,row2.getSheet().getWorkbook(),false);

        //将关联对象注入到主要实体对象
        if (CollectionUtils.isEmpty(objects)) return;
        Class<?> aClass1 = Class.forName(metaObj.getNameSpace().concat(".").concat(metaObj.getObjName()));
        Field declaredField = null;
        try {
            declaredField = aClass1.getDeclaredField(relation.getRelationName());
        } catch (Exception e) {
            declaredField = aClass1.getSuperclass().getDeclaredField(relation.getRelationName());
        }
        declaredField.setAccessible(true);
        declaredField.set(t, objects.get(0));

    }

    public List<Object> itemToObject(List<Map<String, Object>> allRefItems, MetaObject metaObject, Class<?> aClass, String stringCellValue,Workbook workbook,Boolean isMany) throws Exception {

        List<Object> objects = new ArrayList<>();
        List<MetaCol> cols = metaObject.getCols();
//        metaObject.getRelations().stream().collect(Collectors.groupingBy(MetaRelation::get))
        for (Map<String, Object> allRefItem : allRefItems) {
            String partitionkey = String.valueOf(allRefItem.get(metaObject.getPartitionKey()));
            if (stringCellValue.equals(partitionkey)) {
                Object newObj = aClass.getDeclaredConstructor().newInstance();
                for (MetaCol col : cols) {
                    try {
                        Field declaredField;
                        try {
                            declaredField = aClass.getDeclaredField(col.getColName());
                        } catch (Exception e) {
                            declaredField = aClass.getSuperclass().getDeclaredField(col.getColName());
                        }
                        declaredField.setAccessible(true);
                        String s = String.valueOf(allRefItem.get(col.getColName()));
                        if (StringUtil.isBlank(s) || s.equals("null")) continue;

                        boolean isNumber = s.matches("-?\\d+(\\.\\d+)?");
                        if (col.getRelationType().hasOneOrRef()) {
                            MetaRelation relation = metaObject.getRelation(col.getRelationName());
                            if (relation != null) {

                                var repositoryName= NamingUtil.firstLetterLower(relation.getRelativeObjName()) + "Repository";

                                Repository<T, K> subRepository = (Repository<T, K>) ApplicationContextExcelProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);


                                Sheet sheet = workbook.getSheet(subRepository.getMetaObject().getDisplayLabel());
                                if (sheet != null) {
                                    for (int i = 0; i < sheet.getLastRowNum(); i++) {
                                        Row row = sheet.getRow(i);
                                        if (row == null) continue;
                                        Cell cell1 = row.getCell(0);
                                        if (cell1 == null) continue;
                                        String[] split = s.split(" ");
                                        String stringCellValue1 = cell1.getStringCellValue();
                                        boolean isExit = Arrays.stream(split).anyMatch(str -> str.equals(stringCellValue1));
                                        if (stringCellValue1.equals(s) || isExit) {
                                            declaredField.set(newObj, Long.parseLong(row.getCell(0).getStringCellValue()));
                                        }
                                    }
                                }
                            }
                        } else if (declaredField.getType().equals(Integer.class) || declaredField.getType().equals(int.class)) {
                            declaredField.set(newObj, Integer.parseInt(String.valueOf(allRefItem.get(col.getColName())).split("\\.")[0]));
                        } else if (declaredField.getType().equals(Long.class) || declaredField.getType().equals(long.class)) {
                            declaredField.set(newObj, Long.parseLong(String.valueOf(allRefItem.get(col.getColName()))));
                        } else if (declaredField.getType().equals(Double.class) || declaredField.getType().equals(double.class)) {
                            declaredField.set(newObj, Double.parseDouble(String.valueOf(allRefItem.get(col.getColName()))));
                        } else if (declaredField.getType().equals(Boolean.class) || declaredField.getType().equals(boolean.class)) {
                            declaredField.set(newObj, String.valueOf(allRefItem.get(col.getColName())).equals("是"));
                        } else if (EnumValue.class.isAssignableFrom(declaredField.getType())) {

                            Map<String, NameValue<String, String>> enumMap = col.getEnumMap();
                            Integer i = matchEnumInt(enumMap, s);

                            Class<?> enumClass = Class.forName(col.getMetaEnum().getNamespace().concat(".").concat(col.getMetaEnum().getEnumClass()));
                            EnumValue[] enumByKey = (EnumValue[]) enumClass.getMethod("values").invoke(enumClass);

                            for (EnumValue value : enumByKey) {
                                Object invoke = value.getValue();
                                if (isMany && String.valueOf(i).equals(String.valueOf(invoke))) {
                                    declaredField.set(newObj, value);
                                    break;
                                } else if (!isMany && s.equals(String.valueOf(invoke))) {
                                    declaredField.set(newObj, value);
                                    break;
                                }
                            }


                        } else if (EnumBitSet.class.isAssignableFrom(declaredField.getType())) {
                            Class<?> enumSetClass = Class.forName(col.getMetaEnum().getNamespace().concat(".").concat(col.getMetaEnum().getEnumClass()).concat("Set"));
                            Class<?>[] parameterTypes = {int.class};
                            Constructor<?> constructor = enumSetClass.getDeclaredConstructor(parameterTypes);
                            constructor.setAccessible(true);

                            EnumBitSet enumBitSet = (EnumBitSet) constructor.newInstance(Integer.parseInt(String.valueOf(allRefItem.get(col.getColName()))));


                            declaredField.set(newObj, enumBitSet);
                        } else if (declaredField.getType().equals(Timestamp.class)) {
                            Long time = null;
                            if(isNumber){
                                Date javaDate = DateUtil.getJavaDate(Double.parseDouble(s));
                                time = javaDate.getTime();
                            }else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date parse = simpleDateFormat.parse(s.substring(0, 9));
                                time = parse.getTime();
                            }
                            declaredField.set(newObj, new Timestamp(time));
                        } else if (declaredField.getType().equals(java.sql.Date.class)) {
                            Long time = null;
                            if(isNumber){
                                Date javaDate = DateUtil.getJavaDate(Double.parseDouble(s));
                                time = javaDate.getTime();
                            }else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date parse = simpleDateFormat.parse(s.substring(0, 9));
                                time = parse.getTime();
                            }
                            declaredField.set(newObj, new java.sql.Date(time));
                        } else if (declaredField.getType().equals(java.util.Date.class)) {
                            Date parse = null;
                            if(isNumber){
                                parse = DateUtil.getJavaDate(Double.parseDouble(s));
                            }else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                parse = simpleDateFormat.parse(s.substring(0, 9));
                            }
                            declaredField.set(newObj, parse);
                        } else if (declaredField.getType().equals(BigDecimal.class)) {
                            BigDecimal bigDecimal = new BigDecimal(String.valueOf(allRefItem.get(col.getColName())));
                            if(col.getColName().contains("Rate")){
                                bigDecimal = bigDecimal.divide(new BigDecimal("100"),4, RoundingMode.HALF_UP);
                            }
                            declaredField.set(newObj, bigDecimal);
                        } else if (declaredField.getType().equals(Byte.class)) {
                            int i = Integer.parseInt(String.valueOf(allRefItem.get(col.getColName())));
                            declaredField.set(newObj, (byte) i);

                        } else {
                            declaredField.set(newObj, String.valueOf(allRefItem.get(col.getColName())));
                        }

                    }catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }
                objects.add(newObj);
            }
        }
        return objects;
    }

    public NameValue readEnum(String value, MetaCol metaCol) {
        Map<String, NameValue<String, String>> enumMap = metaCol.getEnumMap();

        return matchEnum(enumMap, value);
    }

    public NameValue matchEnum(Map<String, NameValue<String, String>> enumMap, String name) {
        for (String key : enumMap.keySet()) {
            NameValue<String, String> stringStringNameValue = enumMap.get(key);
            if (stringStringNameValue.getValue().equals(name)) return stringStringNameValue;
        }
        return null;
    }

    public Integer matchEnumInt(Map<String, NameValue<String, String>> enumMap, String name) {
        for (String key : enumMap.keySet()) {
            NameValue<String, String> stringStringNameValue = enumMap.get(key);
            if (stringStringNameValue.getValue().equals(name)) return Integer.parseInt(key);
        }
        return null;
    }

    public void write(final Row row, final T item) {
//        Cell cellFirst = row.createCell(0);
//
//        cellFirst.setCellValue(item.getRowNum());
        for (var bindingCell : bindingCells) {
            Cell cell = row.createCell(bindingCell.getColumnIndex());
            bindingCell.write(cell, item);
        }
    }


    public void writeRelationTypeHasMany(Sheet sheet, List<T> datas) {
        try {
            String partitionKey = "";
            String uniqueKey = "";
            Map<String, List<MetaCol>> collect = metaObj.getCols().stream().collect(Collectors.groupingBy(MetaCol::getColName));
            List<MetaRelation> relations = metaObj.getRelations();
            String partitionKeyStr = metaObj.getPartitionKey();
            String uniqueKeyStr = metaObj.getNameCol();
            uniqueKeyStr = StringUtils.isEmpty(uniqueKeyStr) ? metaObj.getUniqueKey() : uniqueKeyStr;
            if (!CollectionUtils.isEmpty(relations)
                    && !CollectionUtils.isEmpty(relations = relations.stream().filter(relation -> relation.getRelationType() == MetaRelationType.HAS_MANY).toList())) {
                int lastCellNum = sheet.getRow(0).getLastCellNum();
                for (int i = 0; i < lastCellNum; i++) {
                    String stringCellValue = sheet.getRow(0).getCell(i).getStringCellValue();
                    List<MetaCol> metaCols1 = collect.get(partitionKeyStr);
                    if (!CollectionUtils.isEmpty(metaCols1) && stringCellValue.equals(metaCols1.get(0).getDisplayLabel())) {
                        partitionKey = CellReference.convertNumToColString(i);
                    }
                    List<MetaCol> metaCols = collect.get(uniqueKeyStr);
                    if (!CollectionUtils.isEmpty(metaCols) && stringCellValue.equals(metaCols.get(0).getDisplayLabel())) {
                        uniqueKey = CellReference.convertNumToColString(i);
                    }

                }


                for (MetaRelation relation : relations) {
                    if (relation.getRelationType() != MetaRelationType.HAS_MANY) continue;
                    String relativeDbName = relation.getRelativeDbSchema();
                    MetaObject relMetaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
                    String relationName = relation.getRelationName();
                    List<Object> objects = new ArrayList<>();
                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                    for (T t : datas) {
                        Field declaredField = t.getClass().getDeclaredField(relationName);
                        declaredField.setAccessible(true);
                        Object obj = declaredField.get(t);
                        if (obj instanceof List) {
                            objects.addAll((List<T>) obj);
                        }


                        if (StringUtils.isNotEmpty(uniqueKeyStr) && StringUtils.isNotEmpty(partitionKeyStr)) {
                            Field partitionKeyField = t.getClass().getDeclaredField(partitionKeyStr);
                            partitionKeyField.setAccessible(true);
                            String partition = String.valueOf(partitionKeyField.get(t));

                            Field uniqueKeyField = t.getClass().getDeclaredField(uniqueKeyStr);
                            uniqueKeyField.setAccessible(true);
                            String unique = String.valueOf(uniqueKeyField.get(t));


                            stringStringHashMap.put(partition, unique);

                        }


                    }
                    addHasManyDataToSheet(repository,relMetaObject, relation, sheet, objects, true, uniqueKey, stringStringHashMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLabel(final Row row, final CellStyle labelStyle) {
        writeLabel(row, labelStyle, false);
    }

    public void writeLabel(final Row row, final CellStyle labelStyle, Boolean byConstraintSetLabelsStyle) {
        Map<String, List<MetaCol>> collect = metaObj.getCols().stream().collect(Collectors.groupingBy(MetaCol::getDisplayLabel));
//        Cell cellFirst = row.createCell(0, CellType.STRING);
//        cellFirst.setCellValue("序号");
//        cellFirst.setCellStyle(cellFirst.getSheet().getWorkbook().createCellStyle());

        for (var bindingCell : bindingCells) {
            Cell cell = row.createCell(bindingCell.getColumnIndex(), CellType.STRING);
            CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
            BeanUtils.copyProperties(labelStyle, cellStyle);
            String displayLabel = bindingCell.getDisplayLabel();
            MetaCol col = collect.get(displayLabel).get(0);
            if (col != null) {
                setLabelsStyle(col, cellStyle, cell, bindingCell, byConstraintSetLabelsStyle);
            }
            bindingCell.writeLabel(cell, cellStyle);
        }

    }

    //根据元数据设置标题样式
    public void setLabelsStyle(MetaCol col, CellStyle cellStyle, Cell cell, BindingCell<T> bindingCell, Boolean byConstraintSetLabelsStyle) {
        Workbook workbook = cell.getSheet().getWorkbook();
        if (byConstraintSetLabelsStyle) {
            //如果字段非空标题格为红色底色
            if (!col.isNullable()) cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());

            //设置数据库限制Excel注释
            String annotations = metaColconstraintToString(col);
            if (StringUtils.isNotEmpty(annotations)) {
                Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();
                CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
                ClientAnchor clientAnchor = helper.createClientAnchor();
                clientAnchor.setCol1(cell.getColumnIndex());
                clientAnchor.setCol2(cell.getColumnIndex());
                clientAnchor.setRow1(cell.getRowIndex());
                clientAnchor.setRow2(cell.getRowIndex());
                clientAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                Comment cellComment = drawing.createCellComment(clientAnchor);
                cellComment.setString(helper.createRichTextString(annotations));
                cell.setCellComment(cellComment);
            }

            if (col.isEnumType()) {//如果是枚举类给当前列设置下拉框
                Map<String, NameValue<String, String>> enumMap = col.getEnumMap();
                List<String> options = new ArrayList<>();
                for (String key : enumMap.keySet()) {
                    options.add(enumMap.get(key).getValue());
                }
                setOption(cell, options.toArray(String[]::new));
            } else if (DataType.BOOL == col.getDataType()) {//如果是布尔值设置是和否
                setOption(cell, data);
            }
        }

        if (col.getRelationType().hasOneOrRef()) {
            MetaRelation relation = metaObj.getRelation(col.getRelationName());
            String sheetName = getSheetName(relation.getDisplayLabel());
            Sheet relSheet = workbook.getSheet(sheetName);
            if (relSheet == null) {
                relSheet = workbook.createSheet(sheetName);
                Workbook workbook1 = relSheet.getWorkbook();
                workbook1.setSheetHidden(workbook1.getSheetIndex(sheetName),true);
                String colLetter = addRelDataToSheet(tenantID, relation, relSheet, bindingCell);
                setDropDownListValidation(cell.getSheet(), sheetName, false, MAX_NUM, colLetter, 1, MAX_NUM, cell.getColumnIndex(), cell.getColumnIndex());
            } else {
                String colRelLetter = ExcelConstant.DEFAULT_EXCEL_COL;
                setDropDownListValidation(cell.getSheet(), sheetName, false, MAX_NUM, colRelLetter, 1, MAX_NUM, cell.getColumnIndex(), cell.getColumnIndex());
            }
        }


    }

    public static Sheet setDropDownListValidation(Sheet sheet, String sheetName, boolean required, int lastRow, String col, int firstRow, int endRow, int firstCol, int endCol) {
        sheetName = getSheetName(sheetName);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        String cell = "\"" + sheetName + "!$" + col + "$2:$" + col + "$" + lastRow + "\"";

        // 这句话是关键 引用ShtDictionary 的单元格
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = dvHelper.createFormulaListConstraint("INDIRECT(" + cell + ")");
        DataValidation validation = dvHelper.createValidation(constraint, regions);
        validation.setShowPromptBox(true);
        validation.createPromptBox("输入提示!", "请选择下拉列表里的选项!");
        if (required) {
            validation.setShowErrorBox(true);
            validation.createErrorBox("无效输入!", "请选择下拉列表");
        }
        sheet.addValidationData(validation);

        return sheet;
    }


    public String addHasManyDataToSheet(Repository subRepository ,MetaObject metaObj, MetaRelation relation, Sheet sheet, List<Object> datas, Boolean isExpand, String uniqueKey, Map<String, String> map) throws Exception {
        String assistColLetter = "";
        String partitionColLetter = "";

        Sheet sheetRel = sheet.getWorkbook().getSheet(metaObj.getDisplayLabel());


        if (sheetRel != null) return assistColLetter;
        Workbook workbook = sheet.getWorkbook();
        sheetRel = workbook.createSheet(metaObj.getDisplayLabel());

        if(!isExpand){
            workbook.setSheetHidden(workbook.getSheetIndex(sheetRel.getSheetName()),true);
        }

        ConcurrentMap<MetaCol, MetaRelation> colRelationMap = new ConcurrentHashMap<>();
        ConcurrentMap<MetaCol, MetaRelation> refColRelationMap = metaObj.buildColRelationMap(MetaRelationType.REF);
        ConcurrentMap<MetaCol, MetaRelation> hasOneColRelationMap = metaObj.buildColRelationMap(MetaRelationType.HAS_ONE);
        colRelationMap.putAll(refColRelationMap);
        colRelationMap.putAll(hasOneColRelationMap);

        ConcurrentMap<String, Map<String, String>> refMap = new ConcurrentHashMap<>();


        int r = 0;
        int c = 0;
        List<MetaCol> cols = metaObj.getCols();
        Row row = sheetRel.createRow(r++);
        for (MetaCol col : cols) {

            //获取辅助名称列
            if (relation != null && !org.springframework.util.StringUtils.isEmpty(relation.getLabelColName()) && relation.getLabelColName().equalsIgnoreCase(col.getColName())) {
                assistColLetter = CellReference.convertNumToColString(c);
            }else if ( relation != null && !org.springframework.util.StringUtils.isEmpty(relation.getValueColName()) && relation.getValueColName().equalsIgnoreCase(col.getColName())) {
                //获取辅助主键值
                partitionColLetter = CellReference.convertNumToColString(c);
            } else if(!isExpand && !col.isKey() )continue;

            if (Arrays.stream(ExcelConstant.IGNORE_FIELDS).anyMatch(ignore -> (col.getColName().equalsIgnoreCase(ignore))
            ))
                continue;
            Cell cell = row.createCell(c);
            cell.setCellValue(col.getDisplayLabel());
            addComment(cell, col.getDescription(), "xlsx");
            if (isExpand) {
                if ((col.getRelationType().hasOneOrRef()) && colRelationMap.containsKey(col)) {
                    MetaRelation aRelation = colRelationMap.get(col);
                    if (!refMap.containsKey(relation.getRelativeObjName())) {
                        List<MetaEnumMember> items = new ArrayList<>();
                        try {
                            items = ((TenancyRepository) subRepository).findAllRefMap(tenantID, aRelation);
                        }catch (Exception e){
                            try {
                                items =  ((EntityRepository) subRepository).findAllRefMap(aRelation);
                            }catch (Exception e1){
                                log.info(e1.getMessage());
                            }

                        }

                        //处理空结果集
                        if (items.isEmpty()) {
                            refMap.put(relation.getRelativeObjName(), Collections.unmodifiableMap(new HashMap<>() {
                                {
                                    put("", "-");
                                }
                            }));
                        }
                        Map<String, String> refEnumMap = items.stream().collect(Collectors.toMap(MetaEnumMember::getValue, MetaEnumMember::getText,(v1, v2)-> v1));
                        refMap.put(relation.getRelativeObjName(), refEnumMap);
                    }
                }
                if (this.metaObj.getPartitionKey().equals(col.getColName())) {
                    setDropDownListValidation(sheetRel, sheet.getSheetName(), false, MAX_NUM, uniqueKey, 1, MAX_NUM, cell.getColumnIndex(), cell.getColumnIndex());
                }

                if (col.getRelationType().hasOneOrRef()) {

                    MetaRelation aRelation = colRelationMap.get(col);
                    String relativeDbName = aRelation.getRelativeDbSchema();
                    MetaObject relMetaObject = subRepository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : aRelation.getDbSchema(), aRelation.getRelativeObjName());


                    var repositoryName= NamingUtil.firstLetterLower(aRelation.getRelativeObjName()) + "Repository";
                    Repository subRepositoryA = (Repository) ApplicationContextExcelProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);

                    String colStr = addHasManyDataToSheet(subRepositoryA,relMetaObject, aRelation, sheetRel, null, false, null, null);
                    setDropDownListValidation(sheetRel, relMetaObject.getDisplayLabel(), false, MAX_NUM, colStr, 1, MAX_NUM, cell.getColumnIndex(), cell.getColumnIndex());
                }
            }

            c++;
        }

        if(datas != null && datas.size() == 0){
            return assistColLetter;
        }

        if (datas == null) {
            if(subRepository instanceof EntityRepository){
                datas = ((EntityRepository) subRepository).findAllRefItems(relation);
            }else if(subRepository instanceof TenancyRepository){
                datas = ((TenancyRepository) subRepository).findAllRefItems(tenantID, relation);
            }

        }

        String labelColName = StringUtil.isNotBlank(relation.getLabelColName())? relation.getLabelColName():"";
        String valueColName = StringUtil.isNotBlank(relation.getValueColName())? relation.getValueColName():"";


        for (Object data : datas) {
            Row rowData = sheetRel.createRow(r++);
            int a = 0;
            for (MetaCol col : cols) {
                if(!isExpand && !labelColName.equalsIgnoreCase(col.getColName()) && !valueColName.equalsIgnoreCase(col.getColName()) && !col.isKey()) {
                    continue;
                }

                Cell cell = rowData.createCell(a++);
                if (col.isKey() && !isExpand) {
                    sheetRel.setColumnWidth(cell.getColumnIndex(), 0);
                }

                Object v = null;
                if (data instanceof Map) {
                    v = ((Map<?, ?>) data).get(col.getColName());
                } else {
                    Field declaredField = data.getClass().getDeclaredField(col.getColName());
                    declaredField.setAccessible(true);
                    v = declaredField.get(data);
                    if (this.metaObj.getPartitionKey().equals(col.getColName()) && map != null) {
                        v = map.get(String.valueOf(v));
                    }
                }
                String cellValue = "";
                if (v != null) {
                    cellValue = String.valueOf(v);
                    if (col.getRelationType().hasOneOrRef()) {
                        if (!colRelationMap.isEmpty()) {
                            //组装所有引用属性值对应的文本
                            if (colRelationMap.containsKey(col)) {
                                Map<String, String> refEnumMap = refMap.get(relation.getRelativeObjName());
                                if(refEnumMap != null)cellValue = refEnumMap.get(cellValue);
                            }
                        }
                    }else if(col.getRelationType() == MetaRelationType.ENUM){
                        Map<String, NameValue<String, String>> enumMap = col.getEnumMap();
                        if(v instanceof EnumBitSet){
                            EnumBitSet<?> enumBitSet = (EnumBitSet<?>) v;
                            List<String> list = enumBitSet.getEnumSet().stream().map(Enum::name).toList();

                            List<String> values = enumMap.values().stream()
                                    .filter(stringStringNameValue -> list.contains(String.valueOf(stringStringNameValue).split("=")[0]))
                                    .map(stringStringNameValue -> String.valueOf(stringStringNameValue).split("=")[1]).toList();
                            String valueStr = String.join(",", values);


                            cellValue = valueStr;
                        }else if(v instanceof EnumValue){
                            Object value = ((EnumValue<?>)v).getValue();
                            NameValue<String, String> stringStringNameValue = enumMap.get(String.valueOf(value));
                            cellValue = stringStringNameValue.getValue();
                        }
                    }
                }
                List<String> picList = List.of("pic","pica","picb","logo");
                if (picList.stream().anyMatch(pic -> col.getColName().toLowerCase().endsWith(pic)) ||
                        col.getColName().equalsIgnoreCase("avatar") ||
                        col.getColName().equalsIgnoreCase("certificate")) {
                    ExcelFileUtil.setExcelPics(cell, cellValue);
                } else {
                    try {
                        if (col.getColName().contains("Rate")) {
                            cellValue = new BigDecimal(cellValue).multiply(new BigDecimal(100)).toString();
                        }
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                    cell.setCellValue(cellValue);
                }
//                if(isExpand){
//                    try {
//                        cell.setCellStyle(getCellStyle(sheetRel.getWorkbook(), col));
//                    }catch (Exception e){
//                        log.info(e.getMessage());
//                    }
//                }

            }
        }
        return assistColLetter;
    }

    public String addRelDataToSheet(int tenantID, MetaRelation relation, Sheet sheet, BindingCell<T> bindingCell) {
        String assistColLetter = null;
        String colLetter = "A";
        String partitionColLetter = "A";
        MetaObject relMetaObject = repository.getMetaObject(relation.getRelativeDbSchema(), relation.getRelativeObjName());
        List<MetaCol> cols = relMetaObject.getCols();
        int r = 0;
        int c = 0;
        List<MetaCol> bindingCols = new ArrayList<>();
        //写head
        Row row = sheet.createRow(r++);
        for (MetaCol col : cols) {
//			if (!col.isKey() && col.isHidden())
//				continue;
            //去除修改、新增默认
            if (Arrays.stream(ExcelConstant.IGNORE_FIELDS).anyMatch(ignore -> (col.getColName().equalsIgnoreCase(ignore) && !col.isKey())
            ))
                continue;
            if (col.isKey() && org.springframework.util.StringUtils.isEmpty(colLetter)) {
                Cell cell = row.createCell(c);
                cell.setCellValue(col.getDisplayLabel());
                addComment(cell, col.getDescription(), "xlsx");
                colLetter = CellReference.convertNumToColString(c);
                c++;
                bindingCols.add(col);
            }
            //获取辅助名称列
            if (relation != null && !org.springframework.util.StringUtils.isEmpty(relation.getLabelColName()) && relation.getLabelColName().equalsIgnoreCase(col.getColName())) {
                Cell cell = row.createCell(c);
                cell.setCellValue(col.getDisplayLabel());
                addComment(cell, col.getDescription(), "xlsx");
                assistColLetter = CellReference.convertNumToColString(c);
                c++;
                bindingCols.add(col);
            }
            //获取辅助主键值
            if (relation != null && !org.springframework.util.StringUtils.isEmpty(relation.getValueColName()) && relation.getValueColName().equalsIgnoreCase(col.getColName())) {
                Cell cell = row.createCell(c);
                cell.setCellValue(col.getDisplayLabel());
                addComment(cell, col.getDescription(), "xlsx");
                partitionColLetter = CellReference.convertNumToColString(c);
                c++;
                bindingCols.add(col);
            }
        }
        //辅助列
        if (assistColLetter != null) {
            Cell assistCell = row.createCell(c);
            assistCell.setCellValue(ExcelConstant.HELPER_COLUMN_NAME);
            colLetter = CellReference.convertNumToColString(c);
        }
        List datas = new ArrayList<>();
        try{
            MetaObject metaObject = repository.getMetaObject(relation.getRelativeDbSchema(), relation.getRelativeObjName());
            if (metaObject.getPartitionKey() != null) {
                datas = ((TenancyRepository) repository).findAllRefItems(tenantID, relation);
            } else {
                datas = repository.findAllRefItems(relation);
            }
        }catch (Exception e){
           log.info(e.getMessage());
        }
        //写值
        writeMapDataList(tenantID, sheet, relMetaObject, bindingCols, datas, partitionColLetter, assistColLetter, r, bindingCell);

        //返回下拉框数据列
        return assistColLetter;
    }

    //写子项数据
    public void writeMapDataList(int tenantID, Sheet sheet, MetaObject metaObject, List<MetaCol> cols, List<Map<String, Object>> datas, String partitionColLetter, String assistColLetter, int r, BindingCell<T> bindingCell) {
        Workbook workbook = sheet.getWorkbook();
        colRelationMap = new ConcurrentHashMap<>();
        ConcurrentMap<MetaCol, MetaRelation> refColRelationMap = metaObject.buildColRelationMap(MetaRelationType.REF);
        ConcurrentMap<MetaCol, MetaRelation> hasOneColRelationMap = metaObject.buildColRelationMap(MetaRelationType.HAS_ONE);
        colRelationMap.putAll(refColRelationMap);
        colRelationMap.putAll(hasOneColRelationMap);

        int partitionColIndex = CellReference.convertColStringToIndex(partitionColLetter);
        int assistColIndex = CellReference.convertColStringToIndex(assistColLetter);
        List<String> relationValues = new ArrayList<>();
        //写值
        Locale locale = LocaleContextHolder.getLocale();
        for (Map<String, Object> date : datas) {
            if (date.containsKey("localeCode") && !Objects.equals(String.valueOf(date.get("localeCode")),locale.getLanguage())) continue;
            Row deteilRow = sheet.createRow(r++);
            String partition = "";
            String assist = "";
            int c = 0;
            for (MetaCol col : cols) {
                Cell cell = deteilRow.createCell(c++);

                if (col.isKey()) {
                    sheet.setColumnWidth(cell.getColumnIndex(), 0);
                }
                String cellValue = "";
                Object v = date.get(col.getColName());
                if (v != null) cellValue = getColText(tenantID, col, v);
                cell.setCellValue(cellValue);
//                cell.setCellStyle(getCellStyle(workbook, col));
                if ((c - 1) == partitionColIndex) {
                    partition = cellValue;
                } else if ((c - 1) == assistColIndex) {
                    assist = cellValue;
                }
            }
            //增加辅助行
            if (!org.springframework.util.StringUtils.isEmpty(assistColLetter)) {
                Cell assistCell = deteilRow.createCell(c++);
                String assistFormula = assistColLetter != null ? String.format("TEXTJOIN(\";\",TRUE,%s%d,%s%d)", partitionColLetter, r, assistColLetter, r) : null;
                assistCell.setCellFormula(assistFormula);
                workbook.setForceFormulaRecalculation(true);
            }

            relationValues.add(partition.concat(";").concat(assist));
        }
        bindingCell.setRelationValues(relationValues);
    }


    private String getColText(int tenantID, MetaCol col, Object cellValue) {
        String value = "";
        if (!org.springframework.util.StringUtils.isEmpty(cellValue)) {
            if (col.getRelationType().hasOneOrRef()) {
                cellValue = getRefColText(tenantID, col, cellValue.toString());
            }
        }
        if (cellValue != null) value = cellValue.toString();
        return value;
    }

    public String getRefColText(int tenantID, MetaCol metaCol, String cellValue) {
        if (org.springframework.util.StringUtils.isEmpty(cellValue))
            return cellValue;
        //如果有引用属性
        if (colRelationMap != null && !colRelationMap.isEmpty()) {
            //组装所有引用属性值对应的文本
            if (colRelationMap.containsKey(metaCol)) {
                MetaRelation relation = colRelationMap.get(metaCol);
                loadRefEnumMap(tenantID, relation);
                Map<String, String> refEnumMap = refMap.get(relation.getRelativeObjName());
                String colText = refEnumMap.get(cellValue);
                cellValue = colText;
                return cellValue;
            }
        }
        return "";
    }

    protected void loadRefEnumMap(int tenantID, MetaRelation relation) {
        if (!refMap.containsKey(relation.getRelativeObjName())) {
            List<MetaEnumMember> items = new ArrayList<>();
            try {
                items = ((TenancyRepository) repository).findAllRefMap(tenantID, relation);
            }catch (Exception e){
                try {
                    items =  ((EntityRepository) repository).findAllRefMap(relation);
                }catch (Exception e1){
                    log.info(e1.getMessage());
                }

            }



            //处理空结果集
            if (items.isEmpty()) {
                refMap.put(relation.getRelativeObjName(), Collections.unmodifiableMap(new HashMap<String, String>() {
                    {
                        put("", "-");
                    }
                }));
            }else {
                Map<String, List<MetaEnumMember>> collect = items.stream().filter(metaEnumItem -> metaEnumItem.getValue() != null && metaEnumItem.getText() != null).collect(Collectors.groupingBy(MetaEnumMember::getValue));
                Map<String, String> refEnumMap = new HashMap<>();
                collect.forEach((k, v) -> {
                    refEnumMap.put(k, v.getFirst().getText());
                });
                refMap.put(relation.getRelativeObjName(), refEnumMap);
            }
        }
    }

    private CellStyle getCellStyle(Workbook workbook, MetaCol metaCol) {
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat(ExcelConstant.AT));
        if (metaCol.getRelationType() == MetaRelationType.NONE) {
            if (StringUtils.isNotEmpty(metaCol.getEnumSet())) {
                if (metaCol.getDataType().isNumber()) {
                    if (metaCol.getDataType().isDecimal()) {
                        cellStyle.setDataFormat(format.getFormat(ExcelConstant.NUMERICAL_DECIMAL_FORMAT));
                    } else if (metaCol.getDataType().isInteger()) {
                        cellStyle.setDataFormat(format.getFormat(ExcelConstant.NUMERICAL_INT_FORMAT));
                    }
                } else if (metaCol.getDataType().isDateOrTime()) {
                    if (metaCol.getDataType().isDateTime()) {
                        cellStyle.setDataFormat(format.getFormat(ExcelConstant.DATETIME_FORMAT));
                    } else {
                        cellStyle.setDataFormat(format.getFormat(ExcelConstant.DATE_FORMAT));
                    }
                } else {
                    cellStyle.setDataFormat(format.getFormat(ExcelConstant.AT));
                }
            } else
                cellStyle.setDataFormat(format.getFormat(ExcelConstant.AT));

        } else {
            cellStyle.setDataFormat(format.getFormat(ExcelConstant.AT));
        }
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);
        return cellStyle;
    }


    public static void addComment(Cell cell, String value, String extension) {
        Sheet sheet = cell.getSheet();
        cell.removeCellComment();
        if ("xls".equals(extension)) {
            ClientAnchor anchor = new HSSFClientAnchor();
            // 关键修改
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setDy1(0);
            anchor.setDy2(0);
            anchor.setCol1(cell.getColumnIndex());
            anchor.setRow1(cell.getRowIndex());
            anchor.setCol2(cell.getColumnIndex() + 5);
            anchor.setRow2(cell.getRowIndex() + 6);
            // 结束
            Drawing drawing = sheet.createDrawingPatriarch();
            Comment comment = drawing.createCellComment(anchor);
            // 输入批注信息
            comment.setString(new HSSFRichTextString(value));
            // 将批注添加到单元格对象中
            cell.setCellComment(comment);
        } else if ("xlsx".equals(extension)) {

            ClientAnchor anchor = new XSSFClientAnchor();
            // 关键修改
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setDy1(0);
            anchor.setDy2(0);
            anchor.setCol1(cell.getColumnIndex());
            anchor.setRow1(cell.getRowIndex());
            anchor.setCol2(cell.getColumnIndex() + 5);
            anchor.setRow2(cell.getRowIndex() + 6);
            // 结束
            Drawing drawing = sheet.createDrawingPatriarch();
            Comment comment = drawing.createCellComment(anchor);
            // 输入批注信息
            comment.setString(new XSSFRichTextString(value));
            // 将批注添加到单元格对象中
            cell.setCellComment(comment);
        }
    }


    public static String getSheetName(String sheetName) {
        String regex = "[^0-9a-zA-Z\u4e00-\u9fa5]";
        return sheetName.replaceAll(regex, "");
    }

    /**
     * 给单元格设置下拉框
     *
     * @param cell
     * @param data
     */
    protected void setOption(Cell cell, String[] data) {
        if (data == null || data.length <= 0) {
            return;
        }
        DataValidationHelper dataValidationHelper = cell.getSheet().getDataValidationHelper();
        DataValidationConstraint explicitListConstraint = dataValidationHelper.createExplicitListConstraint(data);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 9999, cell.getColumnIndex(), cell.getColumnIndex());

        DataValidation dataValidation = dataValidationHelper.createValidation(explicitListConstraint, cellRangeAddressList);
        cell.getSheet().addValidationData(dataValidation);
    }

    public String metaColconstraintToString(MetaCol col) {
        String annotations = "";
        if (col.getMaxLength() != null) {
            annotations += Annotations.getMaxLengthAnnotations(col.getMaxLength());
        }
        if (col.getNumericPrecision() != null) {
            annotations += Annotations.getNumericPrecisionAnnotations(col.getNumericPrecision());
        }

        if (col.getNumericScale() != null) {
            annotations += Annotations.getNumericScaleAnnotations(col.getNumericScale());
        }

        return annotations;
    }

}
