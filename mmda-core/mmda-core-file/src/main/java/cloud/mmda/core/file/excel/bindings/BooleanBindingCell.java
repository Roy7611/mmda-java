package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.file.excel.ExcelConstant;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.file.excel.converters.Converters;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObjectAccess;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BooleanBindingCell<T,K> extends SimpleBindingCell<T,K> {
    private final String[] data = {"是","否"};
    public BooleanBindingCell(MetaObjectAccess<T, K> metaObjAccess, MetaCol metaCol, WorkbookStyleRegistry styleRegistry) {
        super(metaObjAccess, metaCol, styleRegistry);
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            Object property = getProperty(t);
            if(property == null )return;
            Boolean aBoolean = (Boolean)property;
            cell.setCellValue(aBoolean?"是":"否");
            if(hasStyle()) cell.setCellStyle(getCellStyle());
        };
    }

    @Override
    public BiFunction<Cell, T, BindingResult> reader() {

        return (cell, t) -> {
            var value = BindingCell.readBoolean(cell);
            setProperty(t,value);
            return BindingResult.OK;
        };
    }
}
