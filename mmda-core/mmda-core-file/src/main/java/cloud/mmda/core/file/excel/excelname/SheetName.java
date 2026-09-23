package cloud.mmda.core.file.excel.excelname;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetName {
    @Getter
    private List<Cell> cells;
    @Getter
    private int maxRowNum = 0;
    @Getter
    private int minRowNum = 0;
    @Getter
    private int maxColNum = 0;
    @Getter
    private int minColNum = 0;
    private Sheet sheet;
    @Getter
    private Name name;
    @Getter
    private Map<Integer,CellRangeAddress> mergedRegionsMap = new HashMap<>();

    public SheetName(Sheet sheet, List<Cell> cells) {
        this.sheet = sheet;
        this.cells = cells;
        this.maxRowNum = CollectionUtils.isEmpty(cells) ? 0 : cells.stream().map(cell -> cell.getRow().getRowNum()).distinct()
                .max(Integer::compareTo).orElse(0);
        this.minRowNum = CollectionUtils.isEmpty(cells) ? 0 : cells.stream().map(cell -> cell.getRow().getRowNum()).distinct()
                .min(Integer::compareTo).orElse(0);
        this.maxColNum = CollectionUtils.isEmpty(cells) ? 0 : cells.stream().map(Cell::getColumnIndex).distinct()
                .max(Integer::compareTo).orElse(0);
        this.minColNum = CollectionUtils.isEmpty(cells) ? 0 : cells.stream().map(Cell::getColumnIndex).distinct()
                .min(Integer::compareTo).orElse(0);
    }

    public SheetName(Sheet sheet, Name name) {
        this.sheet = sheet;
        this.name = name;
        cells = parseNamedRangeFormula(name, sheet);
//        this.maxRowNum = cells.stream().map(cell -> cell.getRow().getRowNum()).distinct()
//                .max(Integer::compareTo).orElse(0);
//        this.minRowNum = cells.stream().map(cell -> cell.getRow().getRowNum()).distinct()
//                .min(Integer::compareTo).orElse(0);
//        this.maxColNum = cells.stream().map(Cell::getColumnIndex).distinct()
//                .max(Integer::compareTo).orElse(0);
//        this.minColNum = cells.stream().map(Cell::getColumnIndex).distinct()
//                .min(Integer::compareTo).orElse(0);
        setRange();
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (Cell cell : cells) {
            for (CellRangeAddress mergedRegion : mergedRegions) {
                int rowIndex = cell.getRowIndex();
                int colIndex = cell.getColumnIndex();
                if((rowIndex >= mergedRegion.getFirstRow() && rowIndex <= mergedRegion.getLastRow())
                        && (colIndex >= mergedRegion.getFirstColumn() && colIndex <= mergedRegion.getLastColumn())) {
                    mergedRegionsMap.put(colIndex, mergedRegion);
                    break;
                }
            }
        }

    }

    public static List<Cell> parseNamedRangeFormula(Name name, Sheet sheet) {

        List<Cell> cells = new ArrayList<>();
        // 使用正则表达式匹配单元格范围
        //Sheet1!$B$2:$E$4
        String refersToFormula = name.getRefersToFormula();
        if (refersToFormula.contains("#REF!")) return cells;

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

    public void setRange() {
        String refersToFormula = this.name.getRefersToFormula();
        if (refersToFormula.contains("#REF!")) return;

        if (refersToFormula.split("\\$").length == 3) {
            String str = refersToFormula.replace("$", "").split("!")[1];
            this.minRowNum = Integer.parseInt(String.valueOf(str.charAt(1))) - 1;
            this.minColNum = Integer.parseInt(String.valueOf(str.charAt(0)));
            this.maxRowNum = Integer.parseInt(String.valueOf(str.charAt(1))) - 1;
            this.maxColNum = Integer.parseInt(String.valueOf(str.charAt(0)));
            return;
        }
        String[] split = refersToFormula.replace("$", "").split("!")[1].split(":");
        // 解析起始和结束单元格的行列号
        this.minRowNum = Integer.parseInt(split[0].substring(1)) - 1;
        this.minColNum = CellReference.convertColStringToIndex(String.valueOf(split[0].charAt(0)));
        this.maxRowNum = Integer.parseInt(split[1].substring(1)) - 1;
        this.maxColNum = CellReference.convertColStringToIndex(String.valueOf(split[1].charAt(0)));
    }

    public void addRowNum(int rowNum) {
        this.minRowNum += rowNum;
        this.maxRowNum += rowNum;
        List<Cell> cells1 = new ArrayList<>();
        for(int i = minRowNum; i<=maxRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) row = sheet.createRow(i);
            for(int j = minColNum; j<=maxColNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) cell = row.createCell(j);
                cells1.add(cell);
            }
        }
        this.cells = cells1;
    }
}
