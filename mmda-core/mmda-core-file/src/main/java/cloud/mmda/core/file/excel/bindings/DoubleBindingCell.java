package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.excel.ExcelConstant;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.metadata.MetaRelation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DoubleBindingCell<T, K> extends SimpleBindingCell<T, K> {
    public DoubleBindingCell(MetaObjectAccess<T, K> metaObjAccess, MetaCol metaCol, WorkbookStyleRegistry registry) {
        super(metaObjAccess, metaCol, registry);
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            Object property = getProperty(t);
            if (property == null) return;
            Double value = Double.parseDouble(String.valueOf(property));
            var relationType = super.metaCol.getRelationType();
            if (relationType.hasOneOrRef()) {
                String valueByOption = getValueByOption(cell, String.valueOf(value.intValue()));
                cell.setCellValue(valueByOption);
            }else if (t instanceof Entity o && o.getRefProperty(metaCol.getColName())!=null){
                cell.setCellValue(o.getRefProperty(metaCol.getColName()));
            }
            else {
                Field declaredField = null;
                try {
                    declaredField = t.getClass().getDeclaredField(super.metaCol.getColName());
                } catch (NoSuchFieldException e) {
                    try {
                        declaredField = t.getClass().getSuperclass().getDeclaredField(super.metaCol.getColName());
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                declaredField.setAccessible(true);
                Integer rate =  metaCol.getColName().contains("Rate") ? 100 : 1;
                if (declaredField.getType().equals(Long.class) ||
                        declaredField.getType().equals(long.class)) {
                    cell.setCellValue((double) value.longValue() * rate);
                } else if (declaredField.getType().equals(Integer.class) ||
                        declaredField.getType().equals(int.class)) {
                    cell.setCellValue((double) value.intValue() * rate);
                } else if(declaredField.getType().equals(BigDecimal.class)){
                    cell.setCellValue( new BigDecimal(value.toString()).multiply(new BigDecimal(rate)).toString());
                } else {
                    cell.setCellValue(String.valueOf(property));
                }
            }
            if (hasStyle()) cell.setCellStyle(getCellStyle());
        };
    }

    @Override
    public BiFunction<Cell, T, BindingResult> reader() {
        return (cell, t) -> {
            try {
                var value = BindingCell.readFormulaValueAsDouble(cell);
                if (ObjectUtils.isEmpty(value)) {
                    return BindingResult.OK;
                }
                Field declaredField = null;
                try {
                    declaredField = t.getClass().getDeclaredField(super.metaCol.getColName());

                } catch (NoSuchFieldException e) {
                    try {
                        declaredField = t.getClass().getSuperclass().getDeclaredField(super.metaCol.getColName());
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                Integer rate = metaCol.getColName().contains("Rate") ? 100 : 1;


                declaredField.setAccessible(true);
                if (declaredField.getType().equals(Long.class) ||
                        declaredField.getType().equals(long.class)) {
                    setProperty(t, value.longValue()/rate);
                } else if (declaredField.getType().equals(Integer.class) ||
                        declaredField.getType().equals(int.class)) {
                    setProperty(t, value.intValue()/rate);
                } else if (declaredField.getType().equals(BigDecimal.class)) {
                    setProperty(t, new BigDecimal(value).divide(new BigDecimal(rate.toString()), 2, BigDecimal.ROUND_HALF_UP));
                } else if (declaredField.getType().equals(Short.class) ||
                        declaredField.getType().equals(short.class)) {
                    setProperty(t, Short.parseShort(String.valueOf(value.intValue()))/rate);
                } else if (declaredField.getType().equals(Byte.class) ||
                        declaredField.getType().equals(byte.class)) {
                    setProperty(t, value.byteValue()/rate);
                } else {
                    setProperty(t, value/rate);
                }

            }catch (Exception e) {
                return BindingResult.error(e.getMessage());
            }

            return BindingResult.OK;
        };
    }


}
