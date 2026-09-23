package cloud.mmda.core.file.excel.bindings;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.Date;

public enum BindingCellType {
    STRING(CellType.STRING,String.class),
    BOOLEAN(CellType.BOOLEAN, Boolean.class),
    DOUBLE(CellType.NUMERIC, Double.class),
    DATE(CellType.NUMERIC, Date.class),
    ;
    private CellType cellType;
    private Class<?> requiredType;

    BindingCellType(final CellType cellType, Class<?> requiredType) {
        this.cellType = cellType;
        this.requiredType = requiredType;
    }
    public CellType getCellType() {
        return cellType;
    }
    public Class<?> getRequiredType() {
        return requiredType;
    }

}
