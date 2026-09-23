package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.data.jdbc.repository.TenancyRepository;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.entities.TenancyEntity;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.app.ApplicationContextExcelProvider;
import cloud.mmda.core.file.excel.PagedListSupplier;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.file.excel.excelname.SheetItemName;
import cloud.mmda.core.file.excel.excelname.SheetName;
import cloud.mmda.core.file.excel.excelname.SheetNameInfo;
import cloud.mmda.core.file.util.ReflectionUtil;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NamingUtil;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.StringUtil;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 绑定表格
 * <p>
 * 绑定Excel Sheet中的一个区域，区域中可包含标题行、数据行和合计行。它使用{@link BindingRow}定义每行的绑定关系。
 * 可限定数据表格最大行数，用于套打或者导入固定模板的单据。默认自增长的表格行数不能超过10000。
 * </p>
 * 它有以下静态构造函数：
 * <ul>
 *     <li>{@link BindingTable#all(Repository)}创建包含所有字段按原生顺序的数据表格</li>
 *     <li>{@link BindingTable#listed(Repository)}创建包含列出字段按原生顺序的数据表格</li>
 *     <li>{@link BindingTable#names(Repository, Collection)}创建包含字段名和顺序的数据表格</li>
 *     <li>{@link BindingTable#fromSheet(Repository, Sheet, int)}从一个Sheet中某一行读取绑定模板的数据表格，根据单元格的内容和批注判定绑定哪个字段{@link BindingCell}</li>
 * </ul>
 * <p>
 *     你可以使用它读取{@link BindingTable#read(Sheet, int, Integer)}一个Excel Sheet数据，获得一个实体列表{@code List<T>}，
 *     或者将一个实体列表数据写入{@link BindingTable#write(Sheet, List, int)}到一个Excel Sheet。
 * </p>
 * 你可以在Sheet中指定命名区域，例如子表items对应名称为items的区域。
 *
 * @param <T> 行实体数据类型
 * @param <K> 行实体主键类型
 * @see <a href="https://poi.apache.org/components/spreadsheet/quick-guide.html#NamedRanges">参考命名区域</a>
 */
public class BindingTable<T extends SequencedRow, K> {
    private static final Log logger = LogFactory.getLog(BindingTable.class);
    private static final int MAX_ROWS = 10000;
    private final BindingRow<T, K> bindingRow;
    private List<BindingName<T, K>> bindingNames;

    //region 构造函数
    protected BindingTable(final BindingRow<T, K> bindingRow) {
        this.bindingRow = bindingRow;
    }

    //region 构造函数
    protected BindingTable(BindingRow<T, K> bindingRow, final List<BindingName<T, K>> bindingNames) {
        this.bindingRow = bindingRow;
        this.bindingNames = bindingNames;
    }

    /**
     * 创建包含所有字段的导出器
     *
     * @param repository
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T extends SequencedRow, K> BindingTable all(final Repository<T, K> repository, int tenantID,Workbook workbook) {
        WorkbookStyleRegistry styleRegistry = new WorkbookStyleRegistry(workbook);
        return new BindingTable(BindingRow.all(repository, tenantID,styleRegistry));
    }

    /**
     * 创建包含列出字段的导出器
     *
     * @param repository
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T extends SequencedRow, K> BindingTable listed(final Repository<T, K> repository, int tenantID,Workbook workbook) {
        Predicate<MetaCol> colFilter = (metaCol -> metaCol.isListed());
        WorkbookStyleRegistry styleRegistry = new WorkbookStyleRegistry(workbook);
        return new BindingTable(BindingRow.filtered(repository, colFilter, 0, tenantID,styleRegistry));
    }

    /**
     * 创建包含在colNames集合中的导出器
     *
     * @param repository
     * @param colNames
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T extends SequencedRow, K> BindingTable names(final Repository<T, K> repository, final Collection<String> colNames, int tenantID,Workbook workbook) {
       WorkbookStyleRegistry styleRegistry = new WorkbookStyleRegistry(workbook);
        var bindingRow = BindingRow.names(repository, colNames, 0, tenantID,styleRegistry);
        return new BindingTable(bindingRow);
    }

    public void writeTemplate(Sheet sheet) {
        for (BindingName<T, K> bindingName : this.bindingNames) {
            bindingName.writeLabel(sheet);
        }
    }

    public void readTemplate(final Repository<T, K> repository, Sheet sheet, T t,int tenantID) throws Exception {
        for (BindingName<T, K> bindingName : this.bindingNames) {
            bindingName.reader(sheet, t);
        }

        setRel(repository, t,tenantID);
    }

    public void setRel(final Repository<T, K> repository, T t,int tenantID) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(t);
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>) getValueByParser(t, "customProperties");
        } catch (Exception e) {
            logger.error(e);
        }
        List<MetaRelation> relations = repository.getMetaObject().getRelations();


        /*for (MetaRelation relation : relations) {
            String relationName = relation.getJoinOn().split("=@")[1];
            List datas = new  ArrayList<>();
            if(repository instanceof TenancyRepository){
                datas = ((TenancyRepository) repository).findAllRefItems(tenantID, relation);
            }else{
                datas = ((TenancyRepository) repository).findAllRefItems((short) 0, relation);
            }
            String relativeDbName = relation.getRelativeDbName();
            MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
            if (relation.getRelationType() == MetaRelation.REF && map != null) {
                Map<String, Object> finalMap = map;
                String mapValue = String.valueOf(finalMap.get("$".concat(relationName)));
                datas.forEach(x -> {
                    String dataValue = String.valueOf(getValueByParser(x, "[".concat(relation.getLabelColName()).concat("]")));
                    List<String> list = Arrays.asList(mapValue.split(" "));
                    if (dataValue.equals(mapValue) || dataValue.strip().equals(mapValue.strip()) || list.contains(dataValue)) {
                        setValueByParser(t, getValueByParser(x, "[".concat(relation.getValueColName()).concat("]")), relationName);
                    }
                });
            } else if (relation.getRelationType() == MetaRelation.HAS_ONE) {
                String colName = relation.getJoinOn().split("@")[1];
                MetaCol col = repository.getMetaObject().getCol(colName);

                String[] split = col.getEnumSet().split(" AS ");
                String hasOneName = "";
                if (split.length == 2) {
                    Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<readOnly>READONLY))?$");
                    Matcher matcher = enumSetPattern.matcher(col.getEnumSet());
                    if(matcher.matches()){
                        hasOneName = matcher.group("relationName");
                    }
                } else {
                    String objName = metaObject.getObjName();
                    hasOneName = Character.toLowerCase(objName.charAt(0)) + objName.substring(1);
                }
                String Valye = String.valueOf(getValueByParser(t, hasOneName.concat(".").concat(relation.getLabelColName())));
                datas.forEach(x -> {
                    String dataValue = String.valueOf(getValueByParser(x, "[".concat(relation.getLabelColName()).concat("]")));
                    List<String> list = Arrays.asList(Valye.split(" "));
                    if (dataValue.equals(Valye) || dataValue.strip().equals(Valye.strip()) || list.contains(dataValue)) {
                        setValueByParser(t, getValueByParser(x, "[".concat(relation.getValueColName()).concat("]")), relationName);
                    }
                });
            }
        }*/
        for (MetaRelation relation : relations) {
            String relationName = relation.getJoinOn().split("=@")[1];
            List datas = new  ArrayList<>();

            MetaObject currObject = repository.getMetaObject(relation.getRelativeDbSchema() == null ? relation.getDbSchema() : relation.getRelativeDbSchema(), relation.getRelativeObjName());
            Object entityObject = null;
            try {
                Class<?> entityClass = Class.forName(currObject.getNameSpace() + "." + currObject.getObjName());
                entityObject = entityClass.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

            if (repository instanceof TenancyRepository repo) {
                datas = repo.findAllRefItems(tenantID, relation);
            } else {
                datas = repository.findAllRefItems(relation);
            }
            String relativeDbName = relation.getRelativeDbSchema();
            MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());

            if (BaseUtil.hasAny(datas) && relation.getValueColName() != null) {
                if (entityObject instanceof TenancyEntity entity) {
                    datas = datas.stream().filter(x -> {
                        Object value = getValueByParser(x, "[".concat(relation.getValueColName()).concat("]"));
                        entity.setPartitionID(Long.parseLong(String.valueOf(value)));
                        return Objects.equals(entity.getTenantID(), tenantID);
                    }).toList();
                }
            }
            if (relation.getRelationType() == MetaRelationType.REF && map != null) {
                Map<String, Object> finalMap = map;
                String mapValue = String.valueOf(finalMap.get("$".concat(relationName)));
                datas.forEach(x -> {
                    String dataValue = String.valueOf(getValueByParser(x, "[".concat(relation.getLabelColName()).concat("]")));
                    List<String> list = Arrays.asList(mapValue.split(" "));
                    if (dataValue.equals(mapValue) || dataValue.strip().equals(mapValue.strip()) || list.contains(dataValue)) {
                        setValueByParser(t, getValueByParser(x, "[".concat(relation.getValueColName()).concat("]")), relationName);
                    }
                });
            } else if (relation.getRelationType() == MetaRelationType.HAS_ONE) {
                String colName = relation.getJoinOn().split("@")[1];
                MetaCol col = repository.getMetaObject().getCol(colName);

                String[] split = col.getEnumSet().split(" AS ");
                String hasOneName = "";
                if (split.length == 2) {
                    //Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<readOnly>READONLY))?$");
                    Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<shape>LIST|TREE|HIERARCHY|PHOTO))?(\\s+(?<readOnly>READONLY))?(\\s+(?<oneTime>ONETIME))?$");
                    Matcher matcher = enumSetPattern.matcher(col.getEnumSet());
                    if (matcher.matches()) {
                        hasOneName = matcher.group("relationName");
                    }
                } else {
                    String objName = metaObject.getObjName();
                    hasOneName = Character.toLowerCase(objName.charAt(0)) + objName.substring(1);
                }
                String Valye = String.valueOf(getValueByParser(t, hasOneName.concat(".").concat(relation.getLabelColName())));
                datas.forEach(x -> {
                    String dataValue = String.valueOf(getValueByParser(x, "[".concat(relation.getLabelColName()).concat("]")));
                    List<String> list = Arrays.asList(Valye.split(" "));
                    if (dataValue.equals(Valye) || dataValue.strip().equals(Valye.strip()) || list.contains(dataValue)) {
                        setValueByParser(t, getValueByParser(x, "[".concat(relation.getValueColName()).concat("]")), relationName);
                    }
                });
            }
        }
    }

    public void setValueByParser(Object t, Object value, String parseExpression) {
        getParser(t).parseExpression(parseExpression).setValue(t, value);
    }

    public Object getValueByParser(Object t, String parseExpression) {
        return getParser(t).parseExpression(parseExpression).getValue(t);
    }

    public ExpressionParser getParser(Object t) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(t);
        return parser;
    }

    public static <T extends SequencedRow, K> BindingTable fromTemplate(final Repository<T, K> repository, final Sheet sheet, final Sheet sheetData, int tenantID, T t, SheetNameInfo nextSimpleDataName, SheetNameInfo sheetNameInfoData) {
        List<BindingName<T, K>> bindingNames = new ArrayList<>();


        Boolean aBoolean = buildData(nextSimpleDataName, sheetData, repository, tenantID, t, bindingNames, sheetNameInfoData);
        if(!aBoolean){
            return null;
        }
        buildItem(sheet, t, nextSimpleDataName, repository, tenantID, sheetData, bindingNames, nextSimpleDataName.getSheetNameInfoName() == null ? 0 :nextSimpleDataName.getSheetNameInfoName().getMaxRowNum(), sheetNameInfoData);
        return new BindingTable(null, bindingNames);
    }

    public static <T extends SequencedRow, K> Boolean buildData(SheetNameInfo nextSimpleDataName, Sheet sheetData, Repository<T, K> repository, int tenantID, T t, List<BindingName<T, K>> bindingNames, SheetNameInfo sheetNameInfoData) {
        List<Cell> dataCells = nextSimpleDataName.getDataCells();
        List<Cell> cells = dataCells.isEmpty() ? nextSimpleDataName.getCells() : dataCells;


        cells = cells.stream().filter(cell -> cell.getCellType() == CellType.STRING &&cell.getStringCellValue().contains("t.")).toList();
        var bindingCells = new ArrayList<BindingCell>();
        for (Cell cell : cells) {
            String stringCellValue = cell.getStringCellValue().replaceFirst("t.", "");

            if (sheetData != null) {

                Integer orderNumByDataInfoRowNum = nextSimpleDataName.getOrderNumByDataInfoRowNum(cell.getRow().getRowNum());
                Integer rowNum = sheetNameInfoData.getRowNumByDataInfoOrderNum(orderNumByDataInfoRowNum);

                Cell cellData = sheetData.getRow(rowNum).getCell(cell.getColumnIndex());
                if(sheetNameInfoData.checkNotHandle(cellData))return false;
                addBindingCell(repository, t, cellData, stringCellValue, bindingCells);

            } else {
                addBindingCell(repository, t, cell, stringCellValue, bindingCells);

            }
        }
        bindingNames.add(new BindingName(repository, bindingCells, tenantID, false, null));
        return true;
    }

    private static <T extends SequencedRow, K> void addBindingCell(Repository<T, K> repository, T t, Cell cell, String stringCellValue, ArrayList<BindingCell> bindingCells) {
        if (cell == null) return;
        String nameCell = stringCellValue
                .replace("$", "")
                .replace("customProperties[", "")
                .replace("]", "");
        MetaCol col = repository.getMetaObject().getCol(nameCell);
        MetaObjectAccess<T, K> metaObjectAccess = repository.getMetaObjectAccess();
        ExpressionBindingCell build = ExpressionBindingCell.<T, K>builder().cell(cell).with(t).metaCol(col, metaObjectAccess).<K>build(nameCell, stringCellValue);
        bindingCells.add(build);
    }

    public static <T extends SequencedRow, K> void buildItem(Sheet sheet, T t, SheetNameInfo nextSimpleDataName, Repository<T, K> repository, int tenantID, Sheet sheetData, List<BindingName<T, K>> bindingNames, int maxRowNum, SheetNameInfo sheetNameInfoData) {
        List<SheetItemName> dataInfoItemsNames = nextSimpleDataName.getDataInfoItemsName();
        int allMinRow = 0;
        int allsize = 0;
        for (SheetItemName dataInfoItemsName : dataInfoItemsNames) {
            SheetName itemSheetName = dataInfoItemsName.getSheetName();
            maxRowNum = itemSheetName.getMaxRowNum();
            if(allsize > 0 && itemSheetName.getMinRowNum() >= allMinRow){
                itemSheetName.addRowNum(allsize);
                maxRowNum = itemSheetName.getMaxRowNum();
            }
            List<Cell> itemsCells = nextSimpleDataName.getItemsCells(dataInfoItemsName.getItemsName());
            Name name = itemSheetName.getName();
            var bindingCells = new ArrayList<BindingCell>();
            if (sheetData == null) {
                ExpressionParser parser = new SpelExpressionParser();

                // 创建评估上下文
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setRootObject(t);
                List<Object> list = (List<Object>) parser.parseExpression(dataInfoItemsName.getItemsName()).getValue(context);
                if (CollectionUtils.isEmpty(list)) {
                    itemsCells.forEach(cell -> {
                        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains(dataInfoItemsName.getItemsName().concat("."))) {
                            cell.setCellValue("");
                        }
                    });
                    createName(sheet.getWorkbook(), name.getNameName().concat("Data"), name.getRefersToFormula(), 0);
                    continue;
                }
                int lastRowNum = Math.max(sheet.getLastRowNum(), (maxRowNum + 1));
                //asdjklasdjkl

                if(list.size() > 1){
                    Map<Integer[],String> map = new HashMap<>();
                    for (Row row : sheet) {
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.FORMULA) {
                                Integer[] index = {cell.getRowIndex(),cell.getColumnIndex()};
                                String formula = cell.getCellFormula();
                                map.put(index,formula);
                            }
                        }
                    }
                    sheet.shiftRows(maxRowNum + 1, lastRowNum, list.size()-1);//因为items要循环写数据，需要把items区域下面的数据下移items的数量
                    map.forEach((k,v) -> {
                        Row row = sheet.getRow(k[0]);
                        if(row == null) row = sheet.createRow(k[0]);
                        Cell cell1 = row.getCell(k[1]);
                        if(cell1 == null) cell1 = row.createCell(k[1]);
                        if(cell1.getCellType() == CellType.FORMULA){
                            cell1.setCellFormula(v);
                        }
                    });
                }
                createName(sheet.getWorkbook(), name.getNameName().concat("Data"), name.getRefersToFormula(), list.size()-1);
                List<Cell> listCell = itemsCells.stream().filter(cell -> cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains(dataInfoItemsName.getItemsName().concat("."))).toList();
                allsize += list.size();
                allMinRow = maxRowNum + 1;

                if(CollectionUtils.isEmpty(listCell))return;

                Row row1 = listCell.get(0).getRow();
                int size = list.size();
                int num = 0;
                for (int i = 0; i < size; i++) {
                    Row row = i == 0 ? listCell.get(0).getRow() : sheet.createRow(listCell.get(0).getRowIndex() + i);
                    if(nextSimpleDataName.checkNotHandleByRow(row.getRowNum())){
                        size++;
                        continue;
                    }

                    for (Cell cell : listCell) {
                        Cell cell1 = row.getCell(cell.getColumnIndex());
                        cell1 = cell1 != null ? cell1 : row.createCell(cell.getColumnIndex());
                        Cell c = i == 0 ? cell : cell1;
                        c.setCellStyle(row1.getCell(cell.getColumnIndex()).getCellStyle());
                        ExpressionBindingCell.Builder<T, K> builder = ExpressionBindingCell.<T, K>builder().with(t).cell(c);
                        try {
                            String fieldName = cell.getStringCellValue();
                            String[] split = fieldName.split("\\.");
                            fieldName = split[1];
                            List<MetaRelation> list1 = repository.getMetaObject().getRelations().stream().filter(r -> {
                                return r.getRelationName().equals(split[0]);
                            }).toList();
                            MetaRelation relation = list1.getFirst();
                            String relativeDbName = relation.getRelativeDbSchema();
                            MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
                            String finalFieldName2 = fieldName;
                            List<MetaCol> cols = metaObject.getCols().stream().filter(col -> col.getColName().equals(finalFieldName2)).toList();
                            if (!CollectionUtils.isEmpty(cols)) {
                                MetaCol metaCol = cols.getFirst();

                                var repositoryName= NamingUtil.firstLetterLower(relation.getRelativeObjName()) + "Repository";
                                Repository<T, K> subRepository = (Repository<T, K>) ApplicationContextExcelProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);
                                MetaObjectAccess<T, K> metaObjectAccess = subRepository.getMetaObjectAccess();
                                builder.metaCol(metaCol,metaObjectAccess);
                            };
                        }catch (Exception e){
                            logger.info(e.getMessage());
                        }

                        ExpressionBindingCell build = builder.<K>build("t"
                                , cell.getStringCellValue().replace(dataInfoItemsName.getItemsName().concat("."), dataInfoItemsName.getItemsName().concat("[").concat(String.valueOf(num)).concat("].")));
                        bindingCells.add(build);
                    }
                    num++;
                }
                bindingNames.add(new BindingName(repository, bindingCells, tenantID, true, null));
//                createName(sheet.getWorkbook(), name.getNameName().concat("Data"), name.getRefersToFormula(), list.size());
            } else {
                SheetItemName itemNameData = sheetNameInfoData.getItemNameData(dataInfoItemsName.getItemsName());
                if (itemNameData == null) return;
                Name name1 = itemNameData.getSheetName().getName();
                Map<Integer, List<Cell>> map = itemsCells.stream().filter(cell -> cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains(dataInfoItemsName.getItemsName().concat("."))).collect(Collectors.groupingBy(Cell::getColumnIndex));
                getBindingNameByRangeFormula(repository, sheetData, name1, map, tenantID, bindingNames, dataInfoItemsName.getItemsName(),sheetNameInfoData);
            }

        }
    }

    /**
     * 从命名区域获取最后一行
     *
     * @param name
     * @param sheet
     * @return
     */
    public static Integer maxRowNumForName(Name name, Sheet sheet) {
        List<Cell> cells = parseNamedRangeFormula(name, sheet);
        return cells.stream().map(cell -> cell.getRow().getRowNum()).distinct().max(Integer::compareTo).get();
    }

    /**
     * 从命名区域获取第一行
     *
     * @param name
     * @param sheet
     * @return
     */
    public static Integer minRowNumForName(Name name, Sheet sheet) {
        List<Cell> cells = parseNamedRangeFormula(name, sheet);
        return cells.stream().map(cell -> cell.getRow().getRowNum()).distinct().min(Integer::compareTo).get();
    }

    private static <T extends SequencedRow, K> void getBindingNameByRangeFormula(final Repository<T, K> repository, Sheet sheetData, Name name, Map<Integer, List<Cell>> map, Integer tenantID, List<BindingName<T, K>> bindingNames, String itemName,SheetNameInfo sheetNameInfoData) {
        String[] split = name.getRefersToFormula().replace("$", "").split("!")[1].split(":");
        int startRow = Integer.parseInt(split[0].substring(1)) + 1;
        int endRow = Integer.parseInt(split[1].substring(1));

        int index = 0;
        var bindingCells = new ArrayList<BindingCell>();

        Map<String, List<MetaCol>> colsMap = new HashMap<>();

        List<MetaRelation> relations = repository.getMetaObject().getRelations();
        if(!CollectionUtils.isEmpty(relations)){
            List<MetaRelation> list = relations.stream().filter(metaRelation -> metaRelation.getRelationName().equals(itemName)).toList();
            if(!CollectionUtils.isEmpty(list)){
                MetaRelation metaRelation = list.get(0);
                String dbSchema = StringUtil.isBlank(metaRelation.getRelativeDbSchema()) ? metaRelation.getDbSchema() : metaRelation.getRelativeDbSchema();
                MetaObject metaObject = repository.getMetaObject(dbSchema, metaRelation.getRelativeObjName());
               if(metaObject != null){
                   colsMap = metaObject.getCols().stream().collect(Collectors.groupingBy(MetaCol::getColName));

               }
            }
        }

        for (int i = startRow; i <= endRow; i++) {
            Row row = sheetData.getRow(i - 1);
            if (row == null || sheetNameInfoData.checkNotHandleByRow(row.getRowNum())) continue;
            for (Map.Entry<Integer, List<Cell>> entry : map.entrySet()) {
                Cell cell = row.getCell(entry.getKey());
                if (cell == null || checkCellIsNull(cell)) continue;
                String replace = entry.getValue().get(0).getStringCellValue().replace(itemName.concat("."), "");
                MetaCol refCol = null;
                List<MetaCol> metaCols = colsMap.get(replace);
                if(!CollectionUtils.isEmpty(metaCols)){
                    refCol = metaCols.get(0);
                }
                ExpressionBindingCell build = ExpressionBindingCell.<T, K>builder().with(null).cell(cell).metaCol(refCol).<K>build("t"
                        , entry.getValue().get(0).getStringCellValue().replace(itemName.concat("."), itemName.concat("[").concat(String.valueOf(index)).concat("].")));
                bindingCells.add(build);
            }
            index++;
        }

        BindingName bindingName = new BindingName(repository, bindingCells, tenantID, true, itemName);
        bindingNames.add(bindingName);
    }

    public static Boolean checkCellIsNull(Cell cell) {
        return  switch (cell.getCellType()) {
            case STRING -> StringUtil.isBlank(cell.getStringCellValue());
            case NUMERIC -> Double.isNaN(cell.getNumericCellValue());
            default -> true;
        };
    }


    private static Name createName(Workbook workbook, String name, String refersToFormula, int rowIndex) {
        String[] split = refersToFormula.split("!")[1].split(":");
        String start = split[0];
        int endRow = Integer.parseInt(split[1].replace("$", "").substring(1)) + rowIndex;
        String endCol = String.valueOf(split[1].replace("$", "").charAt(0));
        refersToFormula = refersToFormula.split("!")[0].concat("!").concat(start).concat(":").concat("$").concat(endCol).concat("$").concat(String.valueOf(endRow));
        Name name1 = workbook.createName();
        name1.setRefersToFormula(refersToFormula);
        name1.setNameName(name);
        return name1;
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


    /**
     * 从一个Sheet解析字段列表创建导出器
     *
     * @param repository
     * @param sheet         Excel Sheet
     * @param startRowIndex 标题行
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T extends SequencedRow, K> BindingTable fromSheet(final Repository<T, K> repository, final Sheet sheet, int startRowIndex, int tenantID) {
        var row = sheet.getRow(startRowIndex);
        var metaObj = repository.getMetaObject();
        var bindingCells = new ArrayList<BindingCell<T>>();
        var styleRegistry=new WorkbookStyleRegistry(sheet.getWorkbook());
        var bindingCellBuilder = SimpleBindingCell.builder(repository.getMetaObjectAccess(),styleRegistry);
        for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
            //ignore blank cells
            var cell = row.getCell(i, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
            if (cell == null || cell.getCellType() == CellType.BLANK) continue;

            var cellValue = cell.getStringCellValue();
            if (cellValue == null || StringUtil.isBlank(cellValue)) continue;

            //find matched meta col
            MetaCol metaCol = null;
            if (cellValue.charAt(0) == '#') {
                var colName = cellValue.substring(1);
                metaCol = metaObj.getCol(colName);
                if (metaCol == null) {
                    logger.warn("Column '" + colName + "' doesn't exists in " + metaObj.getObjName());
                    continue;
                }
            } else {
                var foundCol = metaObj.findColByLabel(cellValue);
                if (foundCol.isPresent()) {
                    metaCol = foundCol.get();
                } else {
                    var comment = cell.getCellComment();
                    if (comment != null) {
                        var commentText = comment.getString().getString();
                        var matcher = BindingPatterns.FIRST_VARIABLE.matcher(commentText);
                        if (matcher.matches()) {
                            metaCol = metaObj.getCol(matcher.group(1));
                        }
                    }
                }
            }
            if (metaCol == null) continue;
            var bindingCell = bindingCellBuilder.metaCol(metaCol).cell(cell).build();

            bindingCells.add(bindingCell);
        }
        var bindingRow = new BindingRow<>(repository, bindingCells, tenantID,styleRegistry);

        return new BindingTable(bindingRow);
    }

    //endregion

    //region 读取
    private BiFunction<T, Integer, Boolean> maxRowsLimiter(int maxRows) {
        return (t, i) -> i >= maxRows;
    }

    /**
     * 从某一行开始读取一个sheet
     *
     * @param sheet
     * @param startRowIndex 数据开始行
     * @param afterRowRead  数据行读取后拦截器，(实体,行索引)=>是否继续，默认限制10000行
     * @return 实体T列表
     */
    public List<T> read(final Sheet sheet, int startRowIndex, final BiFunction<T, Integer, Boolean> afterRowRead) {
        int r = startRowIndex;
        List<T> result = new ArrayList<>();
        var interceptor = afterRowRead == null ? maxRowsLimiter(MAX_ROWS) : afterRowRead;

        while (true) {
            Row row = sheet.getRow(r++);
            Optional<T> data = bindingRow.read(row);
            if (data.isEmpty()) break;
            T t = data.get();
            try{
                Method compute = t.getClass().getMethod("compute");
                compute.invoke(t);
            }catch (Exception e){
                logger.info("没有compute方法");
            }
            try {
                ReflectionUtil.setT(t, "rowNum", row.getRowNum() + 1);
            }catch (Exception e){
                logger.info(e.getMessage());
            }
            result.add(t);
            if (interceptor.apply(data.get(), r)) {
                break;
            }
        }
        return result;
    }

    /**
     * 从某一行开始读取一个sheet，最多读取maxRows
     *
     * @param sheet
     * @param startRowIndex 数据开始行
     * @param maxRows       读取最大行数
     * @return 实体数据列表
     */
    public List<T> read(final Sheet sheet, int startRowIndex, Integer maxRows) {
        return read(sheet, startRowIndex, maxRowsLimiter(startRowIndex + maxRows));
    }

    /**
     * 异步读取一个sheet
     *
     * @param sheet
     * @param startRowIndex 数据开始行
     * @param afterRowRead  数据行读取后拦截器，(实体,行索引)=>是否继续，默认限制10000行
     * @return
     */
    public Flux<T> readAsync(Sheet sheet, int startRowIndex, final BiFunction<T, Integer, Boolean> afterRowRead) {
        var interceptor = afterRowRead == null ? maxRowsLimiter(MAX_ROWS) : afterRowRead;
        return Flux.<T>create(emitter -> {
            int r = startRowIndex;
            while (true) {
                Row row = sheet.getRow(r++);
                Optional<T> data = bindingRow.read(row);
                if (data.isEmpty()) break;

                emitter.next(data.get());
                if (!interceptor.apply(data.get(), r)) break;
            }
            emitter.complete();
        });
    }

    /**
     * 异步读取一个sheet，从startRowIndex开始，最多maxRows
     *
     * @param sheet
     * @param startRowIndex 数据开始行
     * @param maxRows       读取最大行数
     * @return
     */
    public Flux<T> readAsync(final Sheet sheet, int startRowIndex, Integer maxRows) {
        return readAsync(sheet, startRowIndex, maxRowsLimiter(startRowIndex + maxRows));
    }
    //endregion

    //region 写入

    /**
     * 将实体列表list写入sheet，从第startRowIndex行开始
     *
     * @param sheet
     * @param list          实体数据列表
     * @param startRowIndex 开始行
     * @param writeLabels   是否写入标题
     */
    public void write(final Sheet sheet, final List<T> list, int startRowIndex, boolean writeLabels) {
        write(sheet, list, startRowIndex, writeLabels, false);
    }

    /**
     * 将实体列表list写入sheet，从第startRowIndex行开始
     *
     * @param sheet
     * @param list                       实体数据列表
     * @param startRowIndex              开始行
     * @param writeLabels                是否写入标题
     * @param byConstraintSetLabelsStyle 是否通过元数据约束设置标题格式
     */
    public void write(final Sheet sheet, final List<T> list, int startRowIndex, boolean writeLabels, Boolean byConstraintSetLabelsStyle) {
        int r = startRowIndex;
        if (writeLabels) {
            var headCellStyle = createDefaultHeadCellStyle(sheet.getWorkbook());
            var headRow = sheet.createRow(r++);
            bindingRow.writeLabel(headRow, headCellStyle, byConstraintSetLabelsStyle);
        }

        bindingRow.writeRelationTypeHasMany(sheet, list);


        for (T item : list) {
            Row dataRow = sheet.createRow(r++);
            bindingRow.write(dataRow, item);
        }


    }

    /**
     * 将实体列表list写入sheet，从第startRowIndex行开始写标题
     *
     * @param sheet
     * @param list          实体数据列表
     * @param startRowIndex 开始行
     */
    public void write(final Sheet sheet, final List<T> list, int startRowIndex) {
        write(sheet, list, startRowIndex, true);
    }

    /**
     * 从分页数据提供者获取实体数据页写入sheet，从第startRowIndex行开始
     *
     * @param sheet
     * @param pagedDataSupplier 分页数据提供者
     * @param startRowIndex     开始行
     * @param writeLabels       是否写入标题
     */
    public void write(final Sheet sheet, final PagedListSupplier<T> pagedDataSupplier, int startRowIndex, boolean writeLabels) {
        //headers
        int r = startRowIndex;
        if (writeLabels) {
            Row headRow = sheet.createRow(r++);
            bindingRow.writeLabel(headRow, createDefaultHeadCellStyle(sheet.getWorkbook()), true);
        }


        //data
        int pageNo = 1;
        var page = pagedDataSupplier.page(pageNo);
        do {
            bindingRow.writeRelationTypeHasMany(sheet, page.getData());
            for (T item : page.getData()) {
                Row dataRow = sheet.createRow(r++);
                bindingRow.write(dataRow, item);
            }
            //next page
            page = pagedDataSupplier.page(++pageNo);
        } while (page.hasNextPage());
    }

    /**
     * 将数据流stream异步写入sheet，从第startRowIndex行开始
     *
     * @param sheet
     * @param stream        数据流
     * @param startRowIndex 开始行
     * @param writeLabels   是否写入标题
     */
    public void writeAsync(final Sheet sheet, final Flux<T> stream, int startRowIndex, boolean writeLabels, int tenantID) {
        //headers
        if (writeLabels) {
            Row headRow = sheet.createRow(startRowIndex);
            bindingRow.writeLabel(headRow, createDefaultHeadCellStyle(sheet.getWorkbook()));
        }

        //data
        stream.subscribe(item -> {
            int rowIndex = startRowIndex + (int) item.getRowNum();
            Row dataRow = sheet.createRow(rowIndex);
            bindingRow.write(dataRow, item);
        });
    }

    /**
     * 创建默认标题栏单元格风格
     *
     * @param workbook
     * @return
     */
    private CellStyle createDefaultHeadCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(font);
        cellStyle.setLocked(true);
        return cellStyle;
    }
    //endregion

    /**
     * 通过元数据约束设置标题样式
     *
     * @param cellStyle
     * @param repository
     * @return
     */
    private CellStyle byConstraintSetLabelsStyle(CellStyle cellStyle, final Repository<T, K> repository) {
        MetaObject metaObject = repository.getMetaObject();
        return cellStyle;
    }

}
