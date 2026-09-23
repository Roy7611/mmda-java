package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.utils.NameValue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

/**
 * 表达式绑定单元格是在单个单元格中绑定了一个复杂的Spring SpEL表达式。
 * <p>
 * 你应使用{@link Builder}创建表达式绑定单元格
 * <pre>
 *     {@code
 *         var expBindingCell = ExpressionBindingCell.builder()
 *          .with(t) //给定一个实体作为数据上下文
 *          .cell(cell) //绑定到一个表格
 *          .build("name", "quantity * price"); //名称和SpEL表达式
 *     }
 *     </pre>
 * </p>
 *
 * @param <T> 实体类型
 * @param <E> 表达式返回数据类型
 */
@Slf4j
public class ExpressionBindingCell<T, E> implements BindingCell<T> {
    private final EvaluationContext evalContext;
    private final Expression expression;

    private Class<T> tClass;
    private Class<E> expressionValueClass;


    @Setter
    protected List<String> relationValues;

    @Getter
    private final String name;
    @Getter
    @Setter
    private String displayLabel;

    @Getter
    @Setter
    private int rowIndex;
    @Getter
    @Setter
    private int columnIndex;
    @Getter
    @Setter
    private CellStyle cellStyle;
    @Getter
    @Setter
    private Row.MissingCellPolicy missingCellPolicy;

    @Getter
    @Setter
    private T t;

    @Getter
    @Setter
    private Cell cell;

    @Getter
    @Setter
    private MetaCol metaCol;

    @Getter
    @Setter
    private String expressionStr;

    @Getter
    @Setter
    private MetaObjectAccess<T, E> metaObjectAccess;

    public ExpressionBindingCell(final EvaluationContext evalContext, final String name, final Expression expression) {
        this.evalContext = evalContext;
        this.name = name;
        this.expression = expression;
    }

    public void init() {
//        ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
//        tClass = (Class<T>)pt.getActualTypeArguments()[0];
//        expressionValueClass = (Class<E>)pt.getActualTypeArguments()[1];
    }

    public boolean isPic(String name) {
        List<String> picList = List.of("pic", "pica", "picb", "logo");
        return StringUtil.isNotBlank(name) && ((picList.stream().anyMatch(pic -> name.toLowerCase().endsWith(pic)) ||
                name.equalsIgnoreCase("avatar") || name.equalsIgnoreCase("certificate")));
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            Cell c = (cell == null ? this.cell : cell);
            if(metaCol != null && metaCol.getEnumMap() != null){
                if (getValueByEnum((t == null ? this.t : t), c)) return;
            }else if((metaCol != null && isPic(metaCol.getColName())) || isPic(expressionStr) ){
                String value = String.valueOf(getValue(t == null ? this.t : t));
                c.setCellValue("null".equals(value) ? "" : value);
                if(value != null && value.startsWith("http")){
                    writePic(c, value);
                }

            }else if(metaCol != null && metaCol.getColName().contains("Rate")){
                String value = String.valueOf(getValue(t == null ? this.t : t));
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    bigDecimal = bigDecimal.multiply(new BigDecimal(100));
                    c.setCellValue(bigDecimal.doubleValue()+"%");
                }catch (Exception e){
                    c.setCellValue(value.equals("null") ? null : value);
                }

            }else {
                String value = String.valueOf(getValue(t == null ? this.t : t));
                if("false".equals(value)){
                    value = "否";
                }else if("true".equals(value)){
                    value = "是";
                }
                c.setCellValue(value.equals("null") ? null : value);
            }
            if (hasStyle()) c.setCellStyle(getCellStyle());

        };
    }

    private static void writePic(Cell cell, String value) {
        try {
//            File fileByUrl = ExcelFileUtil.getFileByUrl(value);
//            if(fileByUrl == null) {
//                cell.setCellValue(value);
//            }else {
//                ExcelFileUtil.setExcelPic(cell, fileByUrl);
//
//            }

            ExcelFileUtil.setExcelPics(cell, value);
        } catch (Exception e) {
            log.info(value +"文件下载失败 error:"+e.getMessage());
        }
    }

    private boolean getValueByEnum(T t, Cell c) {
        Map<String, NameValue<String, String>> enumMap = metaCol.getEnumMap();

        Object obj = null;
        try {
            obj = getProperty(t == null ? this.t : t);
        }catch (Exception e){
            try {
                obj = getValue(t == null ? this.t : t);
            }catch (Exception e1){
                log.info(e1.getMessage());
            }
        }

        if(obj == null) return true;
        if(obj instanceof EnumBitSet){
            EnumBitSet<?> enumBitSet = (EnumBitSet<?>) obj;
            List<String> list = enumBitSet.getEnumSet().stream().map(Enum::name).toList();

            List<String> values = enumMap.values().stream()
                    .filter(stringStringNameValue -> list.contains(String.valueOf(stringStringNameValue).split("=")[0]))
                    .map(stringStringNameValue -> String.valueOf(stringStringNameValue).split("=")[1]).toList();
            String valueStr = String.join(",", values);


            c.setCellValue(valueStr);
        }else if(obj instanceof EnumValue){
            Object value = ((EnumValue<?>)obj).getValue();
            NameValue<String, String> stringStringNameValue = enumMap.get(String.valueOf(value));
            c.setCellValue(stringStringNameValue.getValue());
        }
        return false;
    }

    protected E getValue(T t) {
        try {
            if (evalContext != null) return expression.getValue(evalContext, expressionValueClass);
            return expression.getValue(t, expressionValueClass);
        }catch (Exception e){
            return null;
        }
    }

    protected void setValue(T t,Object value) {
        if (evalContext != null) expression.setValue(t, value);
        expression.setValue(t, value);
    }

    protected void setDateValue(T t,Date value) {
        Field declaredField = null;
        try {
            declaredField = t.getClass().getDeclaredField(metaCol.getColName());

        } catch (NoSuchFieldException e) {
            try {
                declaredField = t.getClass().getSuperclass().getDeclaredField(metaCol.getColName());
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }
        declaredField.setAccessible(true);
        if (declaredField.getType().equals(java.util.Date.class)) {
            setValue(t, value);
        } else if (declaredField.getType().equals(java.sql.Date.class)) {
            setValue(t, new java.sql.Date(value.getTime()));
        } else if (declaredField.getType().equals(Timestamp.class)) {
            setValue(t, new Timestamp(value.getTime()));
        }
    }

    @Override
    public BiFunction<Cell, T, BindingResult> reader() {

        return (cell, t) -> {
            try {
                if (metaCol != null && !StringUtil.isBlank(metaCol.getEnumSet()) && !metaCol.getEnumSet().contains("REF")) {
                    setValueByEnum(cell, t);
                }else {
                    Object value ;
                    if(metaCol != null && metaCol.getDataType().isDateOrTime()){
                        Date date = BindingCell.readDate(cell);
                        setDateValue(t, date);
                    }else {
                        if(cell.getCellType() == NUMERIC){
                            value = cell.getNumericCellValue();
                            if((metaCol != null && metaCol.getColName().contains("Rate")) || String.valueOf(value).contains("%")){
                                try {
                                    String v = String.valueOf(value).replaceAll("%","");
                                    value = new BigDecimal(v).divide(new BigDecimal("100"));
                                }catch (Exception e){
                                    log.info(e.getMessage());
                                }

                            }

                        }else if (cell.getCellType() == BOOLEAN || (metaCol != null && DataType.BOOL == metaCol.getDataType())){
                            value = BindingCell.readBoolean(cell);
                        } else{
                            value = cell.getStringCellValue();
                            if(value == "是"){
                                value = true;
                            }else if(value == "否"){
                                value = false;
                            }
                            if((metaCol != null && metaCol.getColName().contains("Rate")) || String.valueOf(value).contains("%")){
                                try {
                                    String v = String.valueOf(value).replaceAll("%","");
                                    value = new BigDecimal(v).divide(new BigDecimal("100"));
                                }catch (Exception e){
                                    log.info(e.getMessage());
                                }

                            }
                        }
                        setValue(t, value);
                    }

                }
            }catch (Exception e){
                return BindingResult.error(e.getMessage());
            }
            return BindingResult.OK;
        };
    }

    private void setValueByEnum(Cell cell, T t) throws Exception {
        Class<?> enumClass = Class.forName(metaCol.getMetaEnum().getNamespace().concat(".").concat(metaCol.getMetaEnum().getEnumClass()));
        if (metaCol.isBitSet()) {
            EnumBitSet enumBitSet = BindingCell.readEnumBit(cell, metaCol);
            try {
                setProperty(t, enumBitSet);
            }catch (Exception e){
                try {
                    setValue(t, enumBitSet);
                }catch (Exception e1){
                    log.info(e1.getMessage());
                }
            }
        } else {
            var value = BindingCell.readEnum(cell, metaCol);
            EnumValue[] enumByKey = (EnumValue[]) enumClass.getMethod("values").invoke(enumClass);
            for (EnumValue enumValue : enumByKey) {
                Object invoke = enumValue.getValue();
                if (String.valueOf(value).equals(String.valueOf(invoke))) {
                    try {
                        setProperty(t, enumValue);
                    }catch (Exception e){
                        try {
                            setValue(t, enumValue);
                        }catch (Exception e1){
                            log.info(e1.getMessage());
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void read(final Cell cell, final T t) {
        //not support
    }

    protected Object getProperty(T t) {
        return metaObjectAccess.getProperty(t, metaCol);
    }

    protected void setProperty(T t, Object value) {
        metaObjectAccess.setProperty(t, metaCol, value);
    }

    public static <T,E> Builder<T,E> builder() {
        return new Builder<>();
    }

    public static class Builder<T,E> {
        private final SpelParserConfiguration config;
        private final StandardEvaluationContext evalContext;

        private final ExpressionParser parser;

        private int rowIndex;
        private int columnIndex;
        private CellStyle cellStyle;
        private Row.MissingCellPolicy missingCellPolicy;
        @Getter
        @Setter
        private T t;

        @Getter
        @Setter
        private Cell cell;

        @Getter
        @Setter
        private MetaCol metaCol;

        @Getter
        @Setter
        private MetaObjectAccess metaObjectAccess;

        public Builder() {
            config = new SpelParserConfiguration(SpelCompilerMode.MIXED, this.getClass().getClassLoader());
            parser = new SpelExpressionParser(config);
            evalContext = new StandardEvaluationContext();
        }

        public Builder<T,E> with(T t) {
            this.t = t;
            evalContext.setRootObject(t);
            return this;
        }

        public Builder<T,E> metaCol(MetaCol metaCol, MetaObjectAccess<T, E> metaObjectAccess) {
            this.metaCol = metaCol;
            this.metaObjectAccess = metaObjectAccess;
            return this;
        }

        public Builder<T,E> metaCol(MetaCol metaCol) {
            this.metaCol = metaCol;
            return this;
        }

        public Builder<T,E> withVariable(String name, Object value) {
            evalContext.setVariable(name, value);
            return this;
        }

        public Builder<T,E> cell(Cell cell) {
            this.cell = cell;
            this.rowIndex = cell.getRowIndex();
            this.columnIndex = cell.getColumnIndex();
            this.cellStyle = cell.getCellStyle();
            return this;
        }

        public <E> ExpressionBindingCell<T, E> build(final String name, final String expression) {
            var expr = parser.parseExpression(expression);
            var bindingCell = new ExpressionBindingCell<T, E>(evalContext, name, expr);
            bindingCell.setColumnIndex(columnIndex);
            bindingCell.setRowIndex(rowIndex);
            bindingCell.setMissingCellPolicy(missingCellPolicy);
            bindingCell.setCellStyle(cellStyle);
            bindingCell.setCell(cell);
            bindingCell.setT(t);
            bindingCell.setMetaCol(metaCol);
            bindingCell.setMetaObjectAccess(metaObjectAccess);
            bindingCell.setExpressionStr(expression);
            bindingCell.init();
            return bindingCell;
        }
    }
}
