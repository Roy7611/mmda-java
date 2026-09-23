package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.file.excel.ExcelConstant;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaDataType;
import cloud.mmda.core.metadata.MetaObjectAccess;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DateBindingCell<T, K> extends SimpleBindingCell<T, K> {
    public DateBindingCell(MetaObjectAccess<T, K> metaObjAccess, MetaCol metaCol, WorkbookStyleRegistry styleRegistry) {
        super(metaObjAccess, metaCol, styleRegistry);
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            setCellStyleForDate(cell);
            Object property = getProperty(t);
            if (property == null) return;

            cell.setCellValue((java.util.Date) property);
        };
    }

    @Override
    public BiFunction<Cell, T, BindingResult> reader() {
        return (cell, t) -> {
            var value = BindingCell.readDate(cell);
            if (ObjectUtils.isEmpty(value)) return BindingResult.OK;

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
            if (declaredField.getType().equals(java.util.Date.class)) {
                setProperty(t, value);
            } else if (declaredField.getType().equals(java.sql.Date.class)) {
                setProperty(t, new java.sql.Date(value.getTime()));
            } else if (declaredField.getType().equals(Timestamp.class)) {
                setProperty(t, new Timestamp(value.getTime()));
            }

            return BindingResult.OK;
        };
    }

}
