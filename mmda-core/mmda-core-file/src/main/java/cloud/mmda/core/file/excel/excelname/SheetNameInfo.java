package cloud.mmda.core.file.excel.excelname;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SheetNameInfo {
    private final static String DATA_INFO_NAME = "dataInfoName";
    private final static String DATA_INFO_ITEMS_NAME = "dataInfoItemsName";
    private final static String SIMPLE_NAME = "simpleData";
    private final static String NOT_HANDLE = "notHandle";

    private Sheet sheet;
    @Getter
    private List<Cell> cells;
    @Getter
    private Map<Integer, List<Cell>> cellsMap;
    @Getter
    private List<SheetName> dataInfoName;
    @Getter
    private List<SheetItemName> dataInfoItemsName;
    private Map<String, SheetItemName> dataInfoItemsNameMap;

    @Getter
    private List<SheetItemName> dataInfoItemsDataName;
    private Map<String, SheetItemName> dataInfoItemsDataNameMap;

    @Getter
    private SheetName SheetNameInfoName;
    @Getter
    private String no;

    @Getter
    private List<SheetName> dataInfoNotHandles;



    public SheetNameInfo(Sheet sheet,List<Cell> cells) {
        this.cells = CollectionUtils.isEmpty(cells) ? new ArrayList<>() : cells;
        this.sheet = sheet;
        this.dataInfoName = new ArrayList<>();
        this.dataInfoItemsName = new ArrayList<>();
        this.dataInfoItemsDataName = new ArrayList<>();
        this.dataInfoItemsNameMap = new HashMap<>();
        this.dataInfoItemsDataNameMap = new HashMap<>();
        this.dataInfoNotHandles = new ArrayList<>();
        cellsMap = CollectionUtils.isEmpty(cells) ? new HashMap<>() : cells.stream().collect(Collectors.groupingBy(x -> x.getRow().getRowNum()));
        SheetName sheetName = new SheetName(sheet, cells);
        this.dataInfoName.add(sheetName);
        for (Name allName : sheet.getWorkbook().getAllNames()) {
            if (allName.getNameName().contains(NOT_HANDLE)) {
                this.dataInfoNotHandles.add(new SheetName(sheet, allName));
            }
        }
    }

    public SheetNameInfo(Sheet sheet, String no, Name name) {
        this.cells = SheetName.parseNamedRangeFormula(name, sheet);
        this.no = no;
        this.SheetNameInfoName = new SheetName(sheet, name);
        this.sheet = sheet;
        this.dataInfoName = new ArrayList<>();
        this.dataInfoItemsName = new ArrayList<>();
        this.dataInfoItemsDataName = new ArrayList<>();
        this.dataInfoItemsNameMap = new HashMap<>();
        this.dataInfoItemsDataNameMap = new HashMap<>();
        this.dataInfoNotHandles = new ArrayList<>();

        for (Name allName : sheet.getWorkbook().getAllNames()) {
            SheetName sheetName = new SheetName(sheet, allName);
            if (allName.getNameName().contains(DATA_INFO_NAME.concat(no))) {
                this.dataInfoName.add(sheetName);
            } else if (allName.getNameName().contains(DATA_INFO_ITEMS_NAME.concat(no))
                    && !allName.getNameName().contains(DATA_INFO_ITEMS_NAME.concat(no).concat("Data"))
                    && !allName.getNameName().contains(DATA_INFO_ITEMS_NAME.concat(no).concat("data"))) {
                String itemName = allName.getNameName().replace(DATA_INFO_ITEMS_NAME.concat(no), "");
                dataInfoItemsName.add(new SheetItemName(itemName, sheetName));
            } else if (allName.getNameName().contains(DATA_INFO_ITEMS_NAME.concat(no).concat("Data"))
                       || allName.getNameName().contains(DATA_INFO_ITEMS_NAME.concat(no).concat("data"))) {
                String itemName = allName.getNameName().replace(DATA_INFO_ITEMS_NAME.concat(no).concat("Data"), "");
                dataInfoItemsDataName.add(new SheetItemName(itemName, sheetName));
            }else if (allName.getNameName().contains(NOT_HANDLE)) {
                this.dataInfoNotHandles.add(new SheetName(sheet, allName));
            }
        }

        if (!CollectionUtils.isEmpty(dataInfoItemsName)) {
            for (SheetItemName sheetItemName : dataInfoItemsName) {
                dataInfoItemsNameMap.put(sheetItemName.getItemsName(), sheetItemName);
            }
        }
        if (!CollectionUtils.isEmpty(dataInfoItemsDataName)) {
            for (SheetItemName sheetItemName : dataInfoItemsDataName) {
                dataInfoItemsDataNameMap.put(sheetItemName.getItemsName(), sheetItemName);
            }
        }

        if (dataInfoName.isEmpty()) {
            cellsMap = cells.stream().collect(Collectors.groupingBy(x -> x.getRow().getRowNum()));
        }

    }

    public SheetItemName getItemName(String itemsName) {
        return dataInfoItemsNameMap.get(itemsName);
    }

    public SheetItemName getItemNameData(String itemsName) {
        return dataInfoItemsDataNameMap.get(itemsName);
    }

    public List<Cell> getItemsCells(String itemsName) {
        SheetItemName sheetItemName = dataInfoItemsNameMap.get(itemsName);
        return sheetItemName == null ? null : sheetItemName.getSheetName().getCells();
    }

    public List<Cell> getDataCells() {
        List<Cell> cells = new ArrayList<>();

        dataInfoName.forEach(cell -> cells.addAll(cell.getCells() == null ? new ArrayList<>() : cell.getCells()));
        return cells;
    }

    public Integer getOrderNumByDataInfoRowNum(int rowNum) {
        List<Cell> dataCells = getDataCells();
        List<Integer> list = dataCells.stream().map(c -> c.getRow().getRowNum()).distinct().sorted().toList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(rowNum)) {
                return i;
            }
        }
        return -1;
    }

    public Integer getRowNumByDataInfoOrderNum(int orderNum) {
        List<Cell> dataCells = getDataCells();
        List<Integer> list = dataCells.stream().map(c -> c.getRow().getRowNum()).distinct().sorted().toList();
        return list.get(orderNum);
    }

    public SheetNameInfo createSheetNameInfoByCopy(Integer offSetRowNum, String no, SheetNameInfo sheetNameInfoFirst) {
        SheetName sheetNameInfoName = this.getSheetNameInfoName();
        List<Cell> cells1 = new ArrayList<>();
        for (int i = sheetNameInfoName.getMinRowNum(); i <= sheetNameInfoName.getMaxRowNum(); i++) {

//            sheet.shiftRows(i + offSetRowNum, i+ offSetRowNum+1, 1);
            Row row = sheet.getRow(i + offSetRowNum);
            row = row != null ? row : sheet.createRow(i + offSetRowNum);

            for (int j = sheetNameInfoName.getMinColNum(); j <= sheetNameInfoName.getMaxColNum(); j++) {
                Cell cell1 = row.getCell(j);
                cell1 = cell1 != null ? cell1 : row.createCell(j);
                Row row1 = sheet.getRow(i);
                row1 = row1 != null ? row1 : sheet.createRow(i);
                Cell cell2 = row1.getCell(j);
                copyCell(cell2, cell1);
                cells1.add(cell1);
            }
        }

        if (!sheetNameInfoFirst.getDataInfoName().isEmpty()) {
            String nameName = SIMPLE_NAME.concat(no);
            Name name = SheetNameData.createName(nameName, sheetNameInfoFirst.getSheetNameInfoName().getName().getRefersToFormula(), offSetRowNum, sheet);
            dataInfoItemsName.forEach(x ->
                    SheetNameData.createName(x.getItemsName().concat(DATA_INFO_ITEMS_NAME).concat(no), x.getSheetName().getName().getRefersToFormula(), offSetRowNum, sheet));

            for (SheetName sheetName : dataInfoName) {
                String dataInfoNum = sheetName.getName().getNameName().split("序号")[1];
                SheetNameData.createName(DATA_INFO_NAME.concat(no).concat("序号").concat(dataInfoNum), sheetName.getName().getRefersToFormula(), offSetRowNum, sheet);

            }
            return new SheetNameInfo(sheet, no, name);
        }else {
            return new SheetNameInfo(sheet,cells1);
        }

    }

    private void copyCell(Cell sourceCell, Cell newCell) {
        if (sourceCell != null) {
            if(sourceCell.getCellType() == CellType.FORMULA){

                newCell.setCellFormula(sourceCell.getCellFormula());
            }else {
                newCell.setCellType(sourceCell.getCellType());
            }
            // 复制值
            switch (sourceCell.getCellType()) {
                case STRING:
                    newCell.setCellValue(sourceCell.getStringCellValue());
                    break;
                case NUMERIC:
                    newCell.setCellValue(sourceCell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(sourceCell.getBooleanCellValue());
                    break;
            }
            // 复制样式
            CellStyle newStyle = newCell.getSheet().getWorkbook().createCellStyle();
            newStyle.cloneStyleFrom(sourceCell.getCellStyle());
            newCell.setCellStyle(newStyle);
        }


    }

    public Boolean checkNotHandle(Cell cell) {
        if(CollectionUtils.isEmpty(dataInfoNotHandles))return false;

        int rowNum = cell.getRow().getRowNum();
        int columnIndex = cell.getColumnIndex();

        for (SheetName dataInfoNotHandle : dataInfoNotHandles) {
            boolean rowCheck = rowNum >= dataInfoNotHandle.getMinRowNum() && rowNum <= dataInfoNotHandle.getMaxRowNum();
            boolean colCheck = columnIndex >= dataInfoNotHandle.getMinColNum() && columnIndex <= dataInfoNotHandle.getMaxColNum();
            if(rowCheck && colCheck)return true;

        }
        return false;
    }

    public Boolean checkNotHandleByRow(int rowNum) {
        if(CollectionUtils.isEmpty(dataInfoNotHandles))return false;


        for (SheetName dataInfoNotHandle : dataInfoNotHandles) {
            boolean rowCheck = rowNum >= dataInfoNotHandle.getMinRowNum() && rowNum <= dataInfoNotHandle.getMaxRowNum();
            if(rowCheck)return true;

        }
        return false;
    }
}
