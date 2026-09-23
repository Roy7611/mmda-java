package cloud.mmda.core.file.excel.excelname;

import cloud.mmda.core.file.util.ExcelFileUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Stream;

public class SheetNameData {
    private final static String SIMPLE_NAME = "simpleData";

    private final static String NOT_HANDLE = "notHandle";

    @Getter
    private final static String SIMPLE_TYPE = "simple";
    @Getter
    private final static String BATCH_TYPE = "batch";


    private Sheet sheet;
    private List<? extends Name> nameData;
    @Getter
    private int nameDataSize;
    @Getter
    private List<SheetNameInfo> nameDataInfos = new ArrayList<>();
    private Map<Integer, SheetNameInfo> nameDataInfoMap = new HashMap<>();
    @Getter
    private String sheetNameDataType = SIMPLE_TYPE;



    public SheetNameData(Sheet sheet) {
        if (sheet == null) return;
        this.sheet = sheet;

        List<? extends Name> allNames = sheet.getWorkbook().getAllNames();

        nameData = CollectionUtils.isEmpty(allNames) ? new ArrayList<>() :
                allNames.stream().filter(name -> name.getNameName().contains(SIMPLE_NAME)).toList();
        nameDataSize = CollectionUtils.isEmpty(nameData) ? 0 : nameData.size();
        nameDataInfos.addAll(CollectionUtils.isEmpty(nameData) ? new ArrayList<>():
                nameData.stream().map(name ->
                new SheetNameInfo(sheet, name.getNameName().replace(SIMPLE_NAME, ""), name)).toList());
        nameDataInfos.forEach(nameDataInfo->nameDataInfoMap.put(getNo(nameDataInfo), nameDataInfo));


        if(nameDataSize > 0 && nameDataInfos.getFirst().getDataInfoName().isEmpty()){
            sheetNameDataType = BATCH_TYPE;
            Set<Integer> integers = nameDataInfos.getFirst().getCellsMap().keySet();
            Integer max = integers.stream().max(Integer::compareTo).orElse(0);
            Integer min = integers.stream().min(Integer::compareTo).orElse(0);
            nameDataSize = max - min +1;
        }

    }

    public boolean isSimple() {
        return this.sheetNameDataType.equals(SIMPLE_TYPE);
    }

    public SheetNameInfo getSheetNameInfo(int no){
        if(sheetNameDataType.equals(BATCH_TYPE)){
            no = no == 0 ? 1 : no;
            SheetNameInfo first = nameDataInfos.getFirst();
            int minRowNum = first.getSheetNameInfoName().getMinRowNum();
            List<Cell> cells = first.getCellsMap().get(minRowNum+no-1);
            if(cells == null || cells.isEmpty())return null;
            return new SheetNameInfo(sheet,cells);
        }
        return nameDataInfoMap.get(no);
    }

    public Boolean delSheetNameInfoByNo(int no,Sheet sheet) {
        SheetNameInfo sheetNameInfo = nameDataInfoMap.get(no);
        if(sheetNameInfo == null)return false;
        SheetName sheetNameInfoName = sheetNameInfo.getSheetNameInfoName();
        delRow(sheetNameInfoName.getMinRowNum(), sheetNameInfoName.getMaxRowNum(),sheet);
        delName(sheetNameInfo.getNo(),sheet);
        nameDataInfoMap.remove(no);
        nameDataInfos.removeIf(next -> sheetNameInfo.getSheetNameInfoName() != null
                && next.getSheetNameInfoName() != null
                && sheetNameInfo.getSheetNameInfoName().getName().getNameName().equals(next.getSheetNameInfoName().getName().getNameName()));
        return true;
    }

    public void delName(String name,Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        List<? extends Name> allNames = workbook.getAllNames();
        List<? extends Name> delNames = allNames.stream()
                .filter(allName ->allName.getNameName().contains(name)).toList();
        delNames.forEach(workbook::removeName);


    }

    public void addSheetNameInfo(SheetNameInfo sheetNameInfo){
        nameDataInfos.add(sheetNameInfo);
        nameDataInfoMap.put(getNo(sheetNameInfo), sheetNameInfo);

        List<? extends Name> allNames = sheet.getWorkbook().getAllNames();
        nameData = CollectionUtils.isEmpty(allNames) ? new ArrayList<>() :
                allNames.stream().filter(name -> name.getNameName().contains(SIMPLE_NAME)).toList();
    }

    public SheetNameInfo createNextSimpleDataName() {
        SheetNameInfo sheetNameInfoFirst = nameDataInfoMap.get(0);
        if (sheetNameInfoFirst == null) return null;
        int maxRowNum = 0;
        String MaxNoStr = "";
        if (sheetNameInfoFirst.getDataInfoName().isEmpty()) {
            int maxRowNumFirst = sheetNameInfoFirst.getSheetNameInfoName().getMaxRowNum();
            maxRowNum =maxRowNumFirst+nameDataInfos.size();
        }else {
            SheetNameInfo sheetNameInfo = nameDataInfoMap.get(getMaxNo());
            maxRowNum = sheetNameInfo.getSheetNameInfoName().getMaxRowNum()+1;
            MaxNoStr = getMaxNoStr(1);
        }


        SheetNameInfo sheetNameInfoByCopy = sheetNameInfoFirst.createSheetNameInfoByCopy(maxRowNum,MaxNoStr,sheetNameInfoFirst);
        addSheetNameInfo(sheetNameInfoByCopy);
        return sheetNameInfoByCopy;
    }

    public Integer getMaxNo() {
        Optional<Integer> max = nameDataInfos.stream().map(this::getNo).distinct().max(Integer::compareTo);
        return max.orElse(0);
    }

    public String getMaxNoStr(int offset) {
        return "括号".concat(String.valueOf(getMaxNo()+offset)).concat("括号");
    }

    public Integer getNo(SheetNameInfo nameDataInfo) {
        SheetName sheetNameInfoName = nameDataInfo.getSheetNameInfoName();
        if(sheetNameInfoName == null) return null;
        return Integer.parseInt(sheetNameInfoName.getName().getNameName().replace(SIMPLE_NAME, "")
                .replace("括号", ""));
    }

    public void delRow(int startRowNum, int endRowNum,Sheet sheet) {

        Map<Integer[],String> map = new HashMap<>();

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.FORMULA) {
                    Integer[] index = {cell.getRowIndex(),cell.getColumnIndex()};
                    String formula = cell.getCellFormula();
                    map.put(index,formula);
                }
            }
        }
        sheet.shiftRows(endRowNum+1,sheet.getLastRowNum(),-1*(endRowNum-startRowNum+1));
        map.forEach((k,v) -> {
            Row row = sheet.getRow(k[0]);
            if(row == null) row = sheet.createRow(k[0]);
            Cell cell1 = row.getCell(k[1]);
            if(cell1 == null) cell1 = row.createCell(k[1]);
            if(cell1.getCellType() == CellType.FORMULA){
                cell1.setCellFormula(v);
            }
        });



        ExcelFileUtil.updatePicAnchorWhenDelRow(sheet,endRowNum+1,-1*(endRowNum-startRowNum+1));
    }
    public static Name createName(String name, String refersToFormula, int rowIndex,Sheet sheet) {
        String[] split = refersToFormula.split("!")[1].split(":");
        int startRow = Integer.parseInt(split[0].replace("$", "").substring(1)) + rowIndex;
        String startCol = String.valueOf(split[0].replace("$", "").charAt(0));

        int endRow = Integer.parseInt(split[1].replace("$", "").substring(1)) + rowIndex;
        String endCol = String.valueOf(split[1].replace("$", "").charAt(0));
        refersToFormula = refersToFormula.split("!")[0].concat("!")
                .concat("$").concat(startCol).concat("$").concat(String.valueOf(startRow))
                .concat(":").concat("$").concat(endCol).concat("$").concat(String.valueOf(endRow));
        Name name1 = sheet.getWorkbook().createName();
        name1.setRefersToFormula(refersToFormula);
        name1.setNameName(name);
        return name1;
    }

//    public static void main(String[] argList) throws IOException {
//        Workbook workbook = new XSSFWorkbook("/Users/cuixuan/Desktop/mes/导入导出测试/aaa.xlsx");
//        Sheet sheet = workbook.getSheetAt(0);
//        Row row = sheet.createRow(2);
//        row.createCell(0).setCellValue(5);
//        File file = new File("/Users/cuixuan/Desktop/mes/导入导出测试/aaa1.xlsx");
//        FileOutputStream outputStream = new FileOutputStream(file);
//        workbook.write(outputStream);
//    }
}
