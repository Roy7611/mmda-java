package cloud.mmda.core.file.excel.excelname;

import lombok.Getter;

public class SheetItemName {
    @Getter
    private String itemsName;
    @Getter
    private SheetName sheetName;
    public SheetItemName(String itemsName, SheetName sheetName) {
        this.itemsName = itemsName;
        this.sheetName = sheetName;
    }
}
