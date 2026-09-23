package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.metadata.MetaCol;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.function.BiConsumer;

public class BindingCellX {
    @Getter
    private CellType cellType;
    @Getter
    @Setter
    private int rowIndex;
    @Getter @Setter
    private int columnIndex;
    @Getter @Setter
    private CellStyle cellStyle;

    private Class<?> requiredType;



}
