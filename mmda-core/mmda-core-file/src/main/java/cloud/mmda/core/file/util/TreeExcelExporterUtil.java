package cloud.mmda.core.file.util;

import cloud.mmda.core.file.excel.modul.TreeData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeExcelExporterUtil {


    /**
     * 导出树形结构到Excel并创建分组
     *
     * @param rootNodes 根节点列表
     * @param fileName  文件名称
     * @param writeChildrenTitle 子结构是否写标题
     * @param allWriteTitle 同一层级的多条数据是否每个都在数据上面显示标题
     */
    public static File exportWithGroups(List<TreeData> rootNodes, String fileName,boolean writeChildrenTitle,boolean allWriteTitle) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(fileName);



            // 导出树节点并创建分组
            int currentRow = 0; // 从第二行开始
            int index = 1;
            for (TreeData root : rootNodes) {
                currentRow = exportNode(
                        sheet,
                        root,
                        0,
                        currentRow,
                        writeChildrenTitle,
                        index++,
                        allWriteTitle
                );
            }

            // 自动调整列宽
            autoSizeColumns(sheet, 3);

            // 保存文件
            File file = Files.createTempFile(fileName, ".xlsx").toFile();
            try (FileOutputStream out = new FileOutputStream(file.getPath())) {
                workbook.write(out);
                return file;
            }
        }
    }

    private static int exportNode(Sheet sheet, TreeData node, int level, int rowNum,boolean writeChildrenTitle,int index,boolean allWriteTitle) {

        if( (level == 0 || writeChildrenTitle) && (index == 1 || allWriteTitle) ) {
            Row headerRow = sheet.createRow(rowNum++);
            // 创建标题行
            createHeaderRow(sheet,node,headerRow);

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

            // 应用标题样式
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }
        }


        // 创建行和单元格
        Row row = sheet.createRow(rowNum);

        Map<String, Object> data = node.getData();

        AtomicInteger i = new AtomicInteger(-1);
        data.forEach((k, v) -> {
            String str = v == null ? "" : String.valueOf(v);
            int i1 = i.incrementAndGet();
            Cell nameCell = row.createCell(i1);
            String indent = "";
            if(i1 == 0){
                indent = "  ".repeat(level)+(level > 0 ? "└─ " : "");
            }
            nameCell.setCellValue(indent+str);
        });
        int startChildRow = rowNum + 1;
        int currentRow = rowNum + 1;

        // 创建分组（如果有子节点）
        if (!CollectionUtils.isEmpty(node.getChildren())) {
            // 处理子节点
            int indexChild = 1;
            for (TreeData child : node.getChildren()) {
                currentRow = exportNode(
                        sheet,
                        child,
                        level + 1,
                        currentRow,
                        writeChildrenTitle,
                        indexChild++,
                        allWriteTitle
                );
            }
            sheet.groupRow(startChildRow, currentRow - 1);

            // 默认折叠层级（大于1级的节点）
            if (level > 0) {
                sheet.setRowGroupCollapsed(startChildRow - 1, true);
            }
        }

        return currentRow;
    }

    private static void createHeaderRow(Sheet sheet,TreeData treeData,Row headerRow) {
        AtomicInteger i = new AtomicInteger(-1);
        treeData.getData().forEach((k, v) -> {
            int i1 = i.incrementAndGet();
            headerRow.createCell(i1).setCellValue(k);
        });
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static CellStyle createNodeStyle(Workbook workbook, short color) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        // 缩进文本对齐
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
