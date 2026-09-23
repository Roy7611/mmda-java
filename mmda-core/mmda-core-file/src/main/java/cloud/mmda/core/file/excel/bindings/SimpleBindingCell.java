package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObjectAccess;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.util.List;

/**
 * 单个字段绑定单元格抽象基类。
 * <p>复杂表达式绑定单元格请使用{@link ExpressionBindingCell}，它利用了Spring Expression技术。</p>
 * 单个字段绑定单元格被以下类继承：
 * @see StringBindingCell
 * @see DoubleBindingCell
 * @see DateBindingCell
 * @see BooleanBindingCell
 * @see EnumBindingCell
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
public abstract class SimpleBindingCell<T,K> implements BindingCell<T> {
    protected final MetaObjectAccess<T,K> metaObjAccess;
    @Getter
    protected final MetaCol metaCol;

    protected final Class<?> requiredType;

    @Setter
    protected  List<String> relationValues;
    @Override
    public final String getName(){
        return metaCol.getColName();
    }
    @Override
    public final String getDisplayLabel(){
        return metaCol.getDisplayLabel();
    }
    @Getter @Setter
    private int rowIndex;
    @Getter @Setter
    private int columnIndex;
    @Getter @Setter
    private CellStyle cellStyle;
    @Getter @Setter
    private Row.MissingCellPolicy missingCellPolicy;

    @Getter @Setter
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();

    private WorkbookStyleRegistry styleRegistry;


    public SimpleBindingCell(final MetaObjectAccess<T,K> metaObjAccess, final MetaCol metaCol,final WorkbookStyleRegistry styleRegistry) {
        this.metaObjAccess = metaObjAccess;
        this.metaCol = metaCol;
        this.requiredType = BindingCell.requiredType(metaCol);
        this.styleRegistry =styleRegistry;
    }

    protected Object getProperty(T t) {
        return metaObjAccess.getProperty(t, metaCol);
    }
    protected <U> U getProperty(T t, Class<U> type) {
        var result = metaObjAccess.getProperty(t, metaCol);
        if (result != null && this.requiredType != null && !this.requiredType.isInstance(result)) {
            // try to convert it.
            try {
                return (U) convertValueToRequiredType(result, this.requiredType);
            }
            catch (IllegalArgumentException ex) {
                throw new RuntimeException(
                        "Type mismatch : " + ex.getMessage());
            }
        }
        return (U) result;
    }
    protected void setProperty(T t, Object value) {
        metaObjAccess.setProperty(t, metaCol, value);
    }

    protected Object convertValueToRequiredType(Object value, Class<?> requiredType) {
        if (String.class == requiredType) {
            return value.toString();
        }
        else if (Number.class.isAssignableFrom(requiredType)) {
            if (value instanceof Number number) {
                // Convert original Number to target Number class.
                return NumberUtils.convertNumberToTargetClass(number, (Class<Number>) requiredType);
            }
            else {
                // Convert stringified value to target Number class.
                return NumberUtils.parseNumber(value.toString(),(Class<Number>) requiredType);
            }
        }
        else if (this.conversionService != null && this.conversionService.canConvert(value.getClass(), requiredType)) {
            return this.conversionService.convert(value, requiredType);
        }
        else {
            throw new IllegalArgumentException(
                    "Value [" + value + "] is of type [" + value.getClass().getName() +
                            "] and cannot be converted to required type [" + requiredType.getName() + "]");
        }
    }

    public static <T,K> Builder<T,K> builder(MetaObjectAccess<T,K> metaObjAccess,WorkbookStyleRegistry registry){
        return new Builder(metaObjAccess, registry);
    }

    public static class Builder<T,K> {
        private final MetaObjectAccess<T,K> metaObjAccess;
        private MetaCol metaCol;
        private int rowIndex;
        private int columnIndex;
        private CellStyle cellStyle;
        private Row.MissingCellPolicy missingCellPolicy;
        private WorkbookStyleRegistry styleRegistry;


        public Builder(MetaObjectAccess<T,K> metaObjAccess,WorkbookStyleRegistry registry) {
            this.metaObjAccess = metaObjAccess;
            this.styleRegistry = registry;
            this.rowIndex = 0;
        }
        public Builder<T,K> metaCol(MetaCol metaCol) {
            this.metaCol = metaCol;
            this.columnIndex = metaCol.getColIdx();
            if(metaCol.isNullable())
                this.missingCellPolicy = Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
            else
                this.missingCellPolicy = Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
            return this;
        }
        public Builder<T,K> cell(Cell cell) {
            this.rowIndex = cell.getRowIndex();
            this.columnIndex = cell.getColumnIndex();
            this.cellStyle = cell.getCellStyle();
            return this;
        }
        public Builder<T,K> cell(int rowIndex, int columnIndex, CellStyle cellStyle) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.cellStyle = cellStyle;
            return this;
        }
        public Builder<T,K> cell(int rowIndex, int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.cellStyle = null;
            return this;
        }
        public Builder<T,K> columnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }
        public BindingCell<T> build() {
            SimpleBindingCell<T,K> bindingCell = null;
            if(this.metaCol.isEnumType()){
                bindingCell = new EnumBindingCell<T,K>(metaObjAccess,metaCol,styleRegistry);
            }
            else if(DataType.BOOL == metaCol.getDataType()){
                bindingCell = new BooleanBindingCell<T,K>(metaObjAccess,metaCol,styleRegistry);
            }
            else if(metaCol.getDataType().hasDatePart()){
                bindingCell = new DateBindingCell<T,K>(metaObjAccess,metaCol,styleRegistry);
            }
            else if(metaCol.getDataType().isNumber()){
                bindingCell = new DoubleBindingCell<T,K>(metaObjAccess,metaCol,styleRegistry);
            }
            else{
                bindingCell = new StringBindingCell<T,K>(metaObjAccess,metaCol,styleRegistry);
            }
            bindingCell.setRowIndex(rowIndex);
            bindingCell.setColumnIndex(columnIndex);
            bindingCell.setCellStyle(cellStyle);
            bindingCell.setMissingCellPolicy(missingCellPolicy);
            return bindingCell;
        }
    }

    /**
     * 给单元格设置下拉框
     * @param cell
     * @param data
     */
    protected void setOption(Cell cell,String[] data){
        if(data == null || data.length <= 0){
            return;
        }
        DataValidationHelper dataValidationHelper = cell.getSheet().getDataValidationHelper();
        DataValidationConstraint explicitListConstraint = dataValidationHelper.createExplicitListConstraint(data);
//        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()); // A1
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(0, 10, cell.getColumnIndex(), cell.getColumnIndex()); // A1

        DataValidation dataValidation = dataValidationHelper.createValidation(explicitListConstraint,cellRangeAddressList);
        cell.getSheet().addValidationData(dataValidation);
    }

    /**
     * 设置单元格时间格式
     * @param cell
     */
    protected void setCellStyleForDate(Cell cell){
//        CellStyle cellStyle = getCellStyle();
//        if(!hasStyle()) cellStyle = cell.getSheet().getWorkbook().createCellStyle();
//        CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
//        String format = "";
//        if(MetaDataType.isDateTime(metaCol.getDataType())){
//            format = "yyyy-MM-dd HH:mm:ss";
//        }else {
//            format = "yyyy-MM-dd";
//        }
//        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(format));
//        setCellStyle(cellStyle);
        CellStyle style = metaCol.getDataType().isDateTime()
                ? styleRegistry.dateTime()
                : styleRegistry.date();

        cell.setCellStyle(style);
    }

    //根据其他关联sheet设置的下拉框获取前端显示内容
    public String getValueByOption(Cell cell,String value){
        if(CollectionUtils.isEmpty(relationValues))return "";
        for (String relationValue : relationValues) {
            String[] split = relationValue.split(";");
            if(value.equals(split[0])){
                return split[1];
            }
        }

        return "";
    }


}
