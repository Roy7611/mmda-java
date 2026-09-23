package cloud.mmda.core.file.pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * https://www.baeldung.com/java-convert-excel-files-pdf
 */
@Slf4j
public class PdfConverter {


//    public static void main(String[] argList) {
//        String excelFilePath = "/Users/cuixuan/Desktop/mes/未命名文件夹/boms/WK403-输送线+移载机外购件.xlsx";
//        String pdfFilePath = "/Users/cuixuan/Desktop/mes/未命名文件夹/boms/合同_宁波.pdf";
//        excelToPdf(excelFilePath, pdfFilePath, ".xlsx");
//        System.out.println("Excel文件已成功转换为PDF！");
//    }

    /**
     * Excel转PDF
     *
     * @param excelPath   Excel文件路径
     * @param pdfPath     PDF文件路径
     * @param excelSuffix Excel文件后缀
     */
    public static void excelToPdf(String excelPath, String pdfPath, String excelSuffix){
        InputStream in = null;
        OutputStream out = null;
        try {
            in = Files.newInputStream(Paths.get(excelPath));
            out = Files.newOutputStream(Paths.get(pdfPath));
            PdfConverter.excelToPdf(in, out, excelSuffix);

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }finally {
            try {
                if(in != null){
                    in.close();
                }
                if(out != null){
                    out.close();
                }
            }catch (Exception e){
                log.info(e.getMessage());
            }

        }
    }

    private static String getServerType() {
        // 这里只是示例，实际需根据你的获取方式来返回正确的服务器类型
        return System.getProperty("os.name").toLowerCase().contains("win") ? "windows" : "linux";
    }

    /**
     * Excel转PDF并写入输出流
     *
     * @param inStream    Excel输入流
     * @param outStream   PDF输出流
     * @param excelSuffix Excel类型 .xls 和 .xlsx
     * @throws Exception 异常信息
     */
    public static void excelToPdf(InputStream inStream, OutputStream outStream, String excelSuffix) {
        // 输入流转workbook，获取sheet
        Sheet sheet = null;
        try {
            sheet = getPoiSheetByFileStream(inStream, 0, excelSuffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取列宽度占比
        float[] widths = getColWidth(sheet);
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        table.setSplitLate(false);
        int colCount = widths.length;
        //设置基本字体
        BaseFont baseFont = null;
        try {
//            String serverType = getServerType();
//
//            if ("windows".equals(serverType)) {
//                baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            } else {
//                baseFont = BaseFont.createFont("/usr/share/fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            }
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 遍历行
        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (Objects.isNull(row)) {
                // 插入空对象
                for (int i = 0; i < colCount; i++) {
                    com.itextpdf.text.Font pdFont = new com.itextpdf.text.Font(baseFont, 13f, 0, BaseColor.BLACK);
                    table.addCell(createPdfPCell("", 0, 13f, pdFont, null));
                }
            } else {
                // 遍历单元格
                Set<String> cellSet = new HashSet<>();
                for (int columnIndex = 0; (columnIndex < colCount) && columnIndex > -1; columnIndex++) {
                    PdfPCell pCell = null;
                    try {
                        pCell = excelCellToPdfCell(sheet, row.getCell(columnIndex), baseFont,cellSet,widths);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    // 是否合并单元格
                    if (isMergedRegion(sheet, rowIndex, columnIndex)) {
                        int[] span = getMergedSpan(sheet, rowIndex, columnIndex);
                        //忽略合并过的单元格
                        boolean mergedCell = span[0] == 1 && span[1] == 1;
                        if (mergedCell) {
                            pCell.setPhrase(null);
                            continue;
                        }else {
                            pCell.setRowspan(span[0]);
                            pCell.setColspan(span[1]);
                        }

                    }
                    table.addCell(pCell);
                }
            }
        }

        // 初始化PDF文档对象
        createPdfTableAndWriteDocument(outStream, table);
    }

    /**
     * 单元格转换，poi cell 转换为 itext cell
     *
     * @param sheet     poi sheet页
     * @param excelCell poi 单元格
     * @param baseFont  基础字体
     * @return PDF单元格
     */
    private static PdfPCell excelCellToPdfCell(Sheet sheet, Cell excelCell, BaseFont baseFont,Set<String> cellSet,float[] widths) {
        if (Objects.isNull(excelCell)) {
            com.itextpdf.text.Font pdFont = new com.itextpdf.text.Font(baseFont, 13f, 0, BaseColor.BLACK);
//            table.addCell(createPdfPCell("", 0, 13f, pdFont, null));
            return createPdfPCell("", 0, 13f, pdFont, null);
        }
        int rowIndex = excelCell.getRowIndex();
        int columnIndex = excelCell.getColumnIndex();
        // 获取单元格背景色（更通用的获取及转换方式开始）
        short bgColorIndex = excelCell.getCellStyle().getFillForegroundColor();
        BaseColor backgroundColor = null;
        if (bgColorIndex != IndexedColors.WHITE.getIndex()) {
            if (sheet instanceof HSSFSheet) {
                HSSFWorkbook workbook = (HSSFWorkbook) sheet.getWorkbook();
                HSSFPalette palette = workbook.getCustomPalette();
                HSSFColor hssfColor = palette.getColor(bgColorIndex);
                if (hssfColor != null) {
                    short[] argb = hssfColor.getTriplet();
                    byte r = (byte) Math.min(255, Math.max(0, argb[0]));
                    byte g = (byte) Math.min(255, Math.max(0, argb[1]));
                    byte b = (byte) Math.min(255, Math.max(0, argb[2]));
                    backgroundColor = new BaseColor(r, g, b);
                }
            } else if (sheet instanceof XSSFSheet) {
                XSSFColor xssfColor = ((XSSFCell) excelCell).getCellStyle().getFillForegroundXSSFColor();
                if (xssfColor != null) {
                    byte[] argb = xssfColor.getARGB();
                    if (argb != null && argb.length >= 4) {
                        // 将有符号的byte类型转换为无符号的int类型，并确保范围在0 - 255
                        int alpha = Math.min(255, Math.max(0, (argb[0] & 0xFF)));
                        int red = Math.min(255, Math.max(0, (argb[1] & 0xFF)));
                        int green = Math.min(255, Math.max(0, (argb[2] & 0xFF)));
                        int blue = Math.min(255, Math.max(0, (argb[3] & 0xFF)));

                        backgroundColor = new BaseColor(red, green, blue, alpha);
                    }else {
                        // 设置为默认的灰色
                        backgroundColor = new BaseColor(200, 200, 200);
                    }
                }
            }
        }
        // 获取单元格字体颜色（新增代码部分开始）
        BaseColor fontColor = null;
        short fontColorIndex = -1;
        if (sheet instanceof HSSFSheet) {
            fontColorIndex = ((HSSFCell) excelCell).getCellStyle().getFont(sheet.getWorkbook()).getColor();
        } else if (sheet instanceof XSSFSheet) {
            fontColorIndex = ((XSSFCell) excelCell).getCellStyle().getFont().getColor();
        }
        if (fontColorIndex != IndexedColors.AUTOMATIC.getIndex()) {
            if (fontColorIndex == IndexedColors.BLACK.getIndex()) {
                fontColor = BaseColor.BLACK;
            } else if (fontColorIndex == IndexedColors.RED.getIndex()) {
                fontColor = BaseColor.RED;
            } else if (fontColorIndex == IndexedColors.BLUE.getIndex()) {
                fontColor = BaseColor.BLUE;
            }
            // 这里可以继续添加更多颜色判断，列举常见颜色，类似处理背景色时的方式，也可采用更通用的颜色转换方式（如下面注释掉的代码部分）
            // 以下是一种更通用的通过RGB值转换的示例（但需要进一步处理可能的异常情况等，暂注释掉先展示简单常见颜色判断方式）
//            byte[] argb = getColorArgbFromIndex(fontColorIndex, sheet);
//            // 假设定义了这个方法来获取RGB值，暂未实现
//            if (argb == null) {
//                fontColor = new BaseColor(0, 0, 0);
//            } else {
//                fontColor = new BaseColor(argb[1], argb[2], argb[3]);
//            }
        }
        // 图片信息
        List<PicturesInfo> infos = null;
        try {
            infos = getAllPictureInfos(sheet, rowIndex, rowIndex, columnIndex, columnIndex, false,cellSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PdfPCell pCell = new PdfPCell();
        Font excelFont = getExcelFont(sheet, excelCell);
        //设置单元格字体
        com.itextpdf.text.Font pdFont = new com.itextpdf.text.Font(baseFont, excelFont.getFontHeightInPoints(), excelFont.getBold() ? 1 : 0, fontColor != null ? fontColor : BaseColor.BLACK);


        if (CollectionUtils.isNotEmpty(infos)) {
            Image image = null;
            Paragraph paragraph = new Paragraph();
            for (PicturesInfo picturesInfo : infos) {
                try {
                    image = Image.getInstance(picturesInfo.getPictureData());
                } catch (BadElementException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // 调整图片大小
                float cellWidth = sheet.getColumnWidth(excelCell.getColumnIndex()) / 264f;
                float cellHeight = excelCell.getRow().getHeightInPoints();

                // 计算图片在单元格内的相对偏移比例（范围0-1）
                float widthRatio = (picturesInfo.getDx2() - picturesInfo.getDx1()) / 12700f;
                float heightRatio = (picturesInfo.getDy2() - picturesInfo.getDy1()) / 12700f;

                if (isMergedRegion(sheet, rowIndex, columnIndex)) {
                    int[] span = getMergedSpan(sheet, rowIndex, columnIndex);

                    for (int i = excelCell.getColumnIndex(); i <= span[1]; i++) {
                        widthRatio -= (sheet.getColumnWidth(i) / 264f);
                    }

                }

                // 计算图片占单元格的显示比例（例如50%）
                float scaleX = widthRatio / cellWidth;

                float scaleY = heightRatio / cellHeight;

                float width = widths[excelCell.getColumnIndex()] * PageSize.A4.getWidth() / 100f;
                ;

                float minWidth = Math.min(scaleX * width, width);
                float minHeight = Math.min(scaleY * pCell.getHeight(), pCell.getHeight());

                image.scaleAbsolute(minWidth, 40f);
                image.scaleToFit(minWidth, 40f);
                image.setScaleToFitLineWhenOverflow(false);

//            pCell.setImage(image);
//            pCell.addElement(image);
                Chunk imageChunk = new Chunk(image, 0, 0);
                paragraph.add(imageChunk);
                pCell.setPadding(pCell.getBorderWidth() / 2.0F);

            }
            String excelCellValue = getExcelCellValue(excelCell);

            if(StringUtils.isNotEmpty(excelCellValue) && !"null".equals(excelCellValue)) {
//                Phrase elements = new Phrase(excelCellValue, pdFont);
//                pCell.addElement(elements);
                paragraph.add(new Chunk(excelCellValue,pdFont));
            }
            paragraph.setAlignment(Element.ALIGN_LEFT);
            pCell.addElement(paragraph);
            pCell.setPadding(5);

        } else {//            Integer border = hasBorder(excelCell) ? null : 0;
            Integer border = 0;
            if (hasTopBorder(excelCell)) {
                border |= Rectangle.TOP;
            }
            if (hasBottomBorder(excelCell)) {
                border |= Rectangle.BOTTOM;
            }
            if (hasLeftBorder(excelCell)) {
                border |= Rectangle.LEFT;
            }
            if (hasRightBorder(excelCell)) {
                border |= Rectangle.RIGHT;
            }
            String excelCellValue = getExcelCellValue(excelCell);
            pCell = createPdfPCell(excelCellValue, border, excelCell.getRow().getHeightInPoints(), pdFont, backgroundColor);
        }
//        if (ObjectUtils.isEmpty(pCell) || ObjectUtils.isEmpty(pCell.getLeft())) {
//            pCell = createPdfPCell(null, 0, 13f, null, null);
//        }
        // 水平居中
        pCell.setHorizontalAlignment(getHorAlign(excelCell.getCellStyle().getAlignment().getCode()));
        // 垂直对齐
        pCell.setVerticalAlignment(getVerAlign(excelCell.getCellStyle().getVerticalAlignment().getCode()));
        return pCell;
    }

    private static byte[] getColorArgbFromIndex(short fontColorIndex, Sheet sheet) {
        byte[] argb = new byte[4];
        if (sheet instanceof HSSFSheet) {
            HSSFWorkbook workbook = (HSSFWorkbook) sheet.getWorkbook();
            HSSFPalette palette = workbook.getCustomPalette();
            HSSFColor hssfColor = palette.getColor(fontColorIndex);
            if (hssfColor != null) {
                short[] triplet = hssfColor.getTriplet();
                argb[0] = (byte) 255; // Alpha通道，暂设为不透明
                argb[1] = (byte) triplet[0];
                argb[2] = (byte) triplet[1];
                argb[3] = (byte) triplet[2];
            }
        } else if (sheet instanceof XSSFSheet) {
            XSSFColor xssfColor = new XSSFColor(IndexedColors.fromInt(fontColorIndex), null);
            argb = xssfColor.getARGB();
        }
//        if (argb != null && argb.length >= 4) {
//            // 将有符号的byte类型转换为无符号的int类型，并确保范围在0 - 255
//            argb[0] = (byte) (argb[0] & 0xFF);
//            argb[1] = (byte) (argb[1] & 0xFF);
//            argb[2] = (byte) (argb[2] & 0xFF);
//            argb[3] = (byte) (argb[3] & 0xFF);
//
//            argb[0] = (byte) Math.min(255, Math.max(0, argb[0]));
//            argb[1] = (byte) Math.min(255, Math.max(0, argb[1]));
//            argb[2] = (byte) Math.min(255, Math.max(0, argb[2]));
//            argb[3] = (byte) Math.min(255, Math.max(0, argb[3]));
//        }
        return argb;
    }

    /**
     * 创建pdf文档，并添加表格
     *
     * @param outStream 输出流，目标文档
     * @param table     表格
     * @throws DocumentException 异常信息
     */
    private static void createPdfTableAndWriteDocument(OutputStream outStream, PdfPTable table) {
        //设置pdf纸张大小 PageSize.A4 A4横向
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outStream);
            //设置页边距 宽
//            document.setMargins(20, 20, 40, 40);
            document.open();
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Excel文档输入流转换为对应的workbook及获取对应的sheet
     *
     * @param inputStream Excel文档输入流
     * @param sheetNo     sheet编号，默认0 第一个sheet
     * @param excelSuffix 文件类型 .xls和.xlsx
     * @return poi sheet
     * @throws IOException 异常
     */
    public static Sheet getPoiSheetByFileStream(InputStream inputStream, int sheetNo, String excelSuffix) throws IOException {
        Workbook workbook;
        if (excelSuffix.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        return workbook.getSheetAt(sheetNo);
    }

    /**
     * 创建itext pdf 单元格
     *
     * @param content       单元格内容
     * @param border        边框
     * @param minimumHeight 高度
     * @param pdFont        字体
     * @return pdf cell
     */
    private static PdfPCell createPdfPCell(String content, Integer border, Float minimumHeight, com.itextpdf.text.Font pdFont, BaseColor backgroundColor) {
        String contentValue = content == null ? "" : content;
        com.itextpdf.text.Font pdFontNew = pdFont == null ? new com.itextpdf.text.Font() : pdFont;
        PdfPCell pCell = new PdfPCell(new Phrase(contentValue, pdFontNew));
        if (Objects.nonNull(border)) {
            pCell.setBorder(border);
        }
        if (Objects.nonNull(minimumHeight)) {
            pCell.setMinimumHeight(minimumHeight);
        }
        if (backgroundColor != null) {
            pCell.setBackgroundColor(backgroundColor);
        }
        return pCell;
    }

    /**
     * excel垂直对齐方式映射到pdf对齐方式
     *
     * @param align 对齐
     * @return 结果
     */
    private static int getVerAlign(int align) {
        switch (align) {
            case 2:
                return Element.ALIGN_BOTTOM;
            case 3:
                return Element.ALIGN_TOP;
            default:
                return Element.ALIGN_MIDDLE;
        }
    }

    /**
     * excel水平对齐方式映射到pdf水平对齐方式
     *
     * @param align 对齐
     * @return 结果
     */
    private static int getHorAlign(int align) {
        switch (align) {
            case 1:
                return Element.ALIGN_LEFT;
            case 3:
                return Element.ALIGN_RIGHT;
            default:
                return Element.ALIGN_CENTER;
        }
    }

    /*============================================== POI获取图片及文本内容工具方法 ==============================================*/

    /**
     * 获取字体
     *
     * @param sheet excel 转换的sheet页
     * @param cell  单元格
     * @return 字体
     */
    private static Font getExcelFont(Sheet sheet, Cell cell) {
        // xls
        if (sheet instanceof HSSFSheet) {
            Workbook workbook = sheet.getWorkbook();
            return ((HSSFCell) cell).getCellStyle().getFont(workbook);
        }
        // xlsx
        return ((XSSFCell) cell).getCellStyle().getFont();
    }

    /**
     * 判断excel单元格是否有边框
     *
     * @param excelCell 单元格
     * @return 结果
     */
    private static boolean hasBorder(Cell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop().getCode();
        short bottom = excelCell.getCellStyle().getBorderBottom().getCode();
        short left = excelCell.getCellStyle().getBorderLeft().getCode();
        short right = excelCell.getCellStyle().getBorderRight().getCode();
        return top + bottom + left + right > 2;
    }

    private static boolean hasTopBorder(Cell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop().getCode();
        return top > 0;
    }

    private static boolean hasBottomBorder(Cell excelCell) {
        short bottom = excelCell.getCellStyle().getBorderBottom().getCode();
        return bottom > 0;
    }

    private static boolean hasLeftBorder(Cell excelCell) {
        short left = excelCell.getCellStyle().getBorderLeft().getCode();
        return left > 0;
    }

    private static boolean hasRightBorder(Cell excelCell) {
        short right = excelCell.getCellStyle().getBorderRight().getCode();
        return right > 0;
    }

    /**
     * 判断单元格是否是合并单元格
     *
     * @param sheet  表
     * @param row    行
     * @param column 列
     * @return 结果
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断指定单元格是否为合并单元格
     *
     * @param sheet  当前sheet页
     * @param row    行号
     * @param column 列号
     * @return 是否为合并单元格
     */
    private static boolean isMergedCell(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            // 判断当前单元格是否在合并区域内，并区分跨行、跨列等情况
            if (row >= firstRow && row <= lastRow && column >= firstColumn && column <= lastColumn) {
                return true;
            }
        }
        return false;
    }
    /**
     * 计算合并单元格合并的跨行跨列数
     *
     * @param sheet  表
     * @param row    行
     * @param column 列
     * @return 结果
     */
    private static int[] getMergedSpan(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        int[] span = {1, 1};
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (firstColumn == column && firstRow == row) {
                span[0] = lastRow - firstRow + 1;
                span[1] = lastColumn - firstColumn + 1;
                break;
            }
        }
        return span;
    }

    /**
     * 获取excel中每列宽度的占比
     *
     * @param sheet 表
     * @return 结果
     */
    private static float[] getColWidth(Sheet sheet) {
        int rowNum = getMaxColRowNum(sheet);
        Row row = sheet.getRow(rowNum);
        int cellCount = row.getPhysicalNumberOfCells();
        int[] colWidths = new int[cellCount];
        int sum = 0;

        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                colWidths[i] = sheet.getColumnWidth(i);
                sum += sheet.getColumnWidth(i);
            }
        }

        float[] colWidthPer = new float[cellCount];
        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            colWidthPer[i] = (float) colWidths[i] / sum * 100;
        }
        return colWidthPer;
    }

    /**
     * 获取excel中列数最多的行号
     *
     * @param sheet 表
     * @return 结果
     */
    private static int getMaxColRowNum(Sheet sheet) {
        int rowNum = 0;
        int maxCol = 0;
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            Row row = sheet.getRow(r);
            if (row != null && maxCol < row.getPhysicalNumberOfCells()) {
                maxCol = row.getPhysicalNumberOfCells();
                rowNum = r;
            }
        }
        return rowNum;
    }

    /**
     * poi 根据单元格类型获取单元格内容
     *
     * @param excelCell poi单元格
     * @return 单元格内容文本
     */
    public static String getExcelCellValue(Cell excelCell) {
        if (excelCell == null) {
            return "";
        }
        // 判断数据的类型
        CellType cellType = excelCell.getCellType();

        if (cellType == CellType.STRING) {
            return excelCell.getStringCellValue();
        }
        if (cellType == CellType.BOOLEAN) {
            return String.valueOf(excelCell.getBooleanCellValue());
        }
        if (cellType == CellType.FORMULA) {
            try {
                FormulaEvaluator formulaEvaluator = excelCell.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue evaluate = formulaEvaluator.evaluate(excelCell);
                return String.valueOf(getValueByCellValue(evaluate));
            }catch (Exception e) {
                return "";
            }

        }
        if (cellType == CellType.NUMERIC) {
            // 处理日期格式、时间格式
            if (DateUtil.isCellDateFormatted(excelCell)) {
                SimpleDateFormat sdf;
                // 验证short值
                if (excelCell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd");
                } else if (excelCell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (excelCell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                } else {
                    DataFormatter formatter = new DataFormatter();
                    return formatter.formatCellValue(excelCell);
//                    throw new RuntimeException("日期格式错误!!!");
                }
                Date date = excelCell.getDateCellValue();
                return sdf.format(date);
            } else if (excelCell.getCellStyle().getDataFormat() == 0) {
                //处理数值格式
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(excelCell);
            }
        }
        if (cellType == CellType.ERROR) {
            return "非法字符";
        }
        return "";
    }

    public static Object getValueByCellValue(CellValue cellValue) {
        return switch (cellValue.getCellType()) {
            case NUMERIC -> cellValue.getNumberValue(); // 数值类型结果:ml-citation{ref="2" data="citationList"}
            case STRING  -> cellValue.getStringValue();  // 字符串类型结果:ml-citation{ref="2,5" data="citationList"}
            case BOOLEAN -> cellValue.getBooleanValue();  // 布尔类型结果:ml-citation{ref="5" data="citationList"}
            default -> "";
        };
    }

    /**
     * 获取sheet内的所有图片信息
     *
     * @param sheet        sheet表
     * @param onlyInternal 单元格内部
     * @return 照片集合
     * @throws Exception 异常
     */
    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, boolean onlyInternal) throws Exception {
        Set<String> cellSet = new HashSet<>();
        return getAllPictureInfos(sheet, null, null, null, null, onlyInternal,cellSet);
    }

    /**
     * 根据sheet和单元格信息获取图片
     *
     * @param sheet        sheet表
     * @param minRow       最小行
     * @param maxRow       最大行
     * @param minCol       最小列
     * @param maxCol       最大列
     * @param onlyInternal 是否内部
     * @return 图片集合
     * @throws Exception 异常
     */
    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, Integer minRow, Integer maxRow, Integer minCol,
                                                        Integer maxCol, boolean onlyInternal,Set<String> cellSet) throws Exception {
        if (sheet instanceof HSSFSheet) {
            return getXLSAllPictureInfos((HSSFSheet) sheet, minRow, maxRow, minCol, maxCol, onlyInternal,cellSet);
        } else if (sheet instanceof XSSFSheet) {
            return getXLSXAllPictureInfos((XSSFSheet) sheet, minRow, maxRow, minCol, maxCol, onlyInternal,cellSet);
        } else {
            throw new Exception("未处理类型，没有为该类型添加：GetAllPicturesInfos()扩展方法！");
        }
    }

    /**
     * 获取XLS图片信息
     *
     * @param sheet        表
     * @param minRow       最小行
     * @param maxRow       最大行
     * @param minCol       最小列
     * @param maxCol       最大列
     * @param onlyInternal 只在内部
     * @return 图片信息列表
     */
    private static List<PicturesInfo> getXLSAllPictureInfos(HSSFSheet sheet, Integer minRow, Integer maxRow,
                                                            Integer minCol, Integer maxCol, Boolean onlyInternal,Set<String> cellSet) {
        List<PicturesInfo> picturesInfoList = new ArrayList<>();
        HSSFShapeContainer shapeContainer = sheet.getDrawingPatriarch();
        if (shapeContainer == null) {
            return picturesInfoList;
        }
        List<HSSFShape> shapeList = shapeContainer.getChildren();
        for (HSSFShape shape : shapeList) {
            if (shape instanceof HSSFPicture && shape.getAnchor() instanceof HSSFClientAnchor) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();

                if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(),
                        anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
                    String item = anchor.getRow1()+","+anchor.getRow2()+","+anchor.getCol1()+","+anchor.getCol2();
                    if (cellSet.contains(item)) {
                        continue;
                    }
                    cellSet.add(item);
                    HSSFPictureData pictureData = picture.getPictureData();
                    picturesInfoList.add(new PicturesInfo()
                            .setMinRow(anchor.getRow1())
                            .setMaxRow(anchor.getRow2())
                            .setMinCol(anchor.getCol1())
                            .setMaxCol(anchor.getCol2())
                            .setDx1(anchor.getDx1())
                            .setDx2(anchor.getDx2())
                            .setDy1(anchor.getDy1())
                            .setDy2(anchor.getDy2())
                            .setPictureData(pictureData.getData())
                            .setExt(pictureData.getMimeType()));
                }
            }
        }
        return picturesInfoList;
    }

    /**
     * 获取XLSX图片信息
     *
     * @param sheet        表
     * @param minRow       最小行
     * @param maxRow       最大行
     * @param minCol       最小列
     * @param maxCol       最大列
     * @param onlyInternal 只在内部
     * @return 图片信息列表
     */
    private static List<PicturesInfo> getXLSXAllPictureInfos(XSSFSheet sheet, Integer minRow, Integer maxRow,
                                                             Integer minCol, Integer maxCol, Boolean onlyInternal,Set<String> cellSet) {
        List<PicturesInfo> picturesInfoList = new ArrayList<>();

        List<POIXMLDocumentPart> documentPartList = sheet.getRelations();
        for (POIXMLDocumentPart documentPart : documentPartList) {
            if (documentPart instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) documentPart;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    if (shape instanceof XSSFPicture) {
                        XSSFPicture picture = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = picture.getPreferredSize();

                        if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(),
                                anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
                            String item = anchor.getRow1()+","+anchor.getRow2()+","+anchor.getCol1()+","+anchor.getCol2();

                            if (cellSet.contains(item)) {
                                continue;
                            }
                            cellSet.add(item);
                            XSSFPictureData pictureData = picture.getPictureData();
                            picturesInfoList.add(new PicturesInfo()
                                    .setMinRow(anchor.getRow1())
                                    .setMaxRow(anchor.getRow2())
                                    .setMinCol(anchor.getCol1())
                                    .setMaxCol(anchor.getCol2())
                                    .setDx1(anchor.getDx1())
                                    .setDx2(anchor.getDx2())
                                    .setDy1(anchor.getDy1())
                                    .setDy2(anchor.getDy2())
                                    .setPictureData(pictureData.getData())
                                    .setExt(pictureData.getMimeType()));
                        }
                    }
                }
            }
        }

        return picturesInfoList;
    }

    /**
     * 是内部的或相交的
     *
     * @param rangeMinRow   最小行范围
     * @param rangeMaxRow   最大行范围
     * @param rangeMinCol   最小列范围
     * @param rangeMaxCol   最大列范围
     * @param pictureMinRow 图片最小行
     * @param pictureMaxRow 图片最大行
     * @param pictureMinCol 图片最小列
     * @param pictureMaxCol 图片最大列
     * @param onlyInternal  只在内部
     * @return 结果
     */
    private static boolean isInternalOrIntersect(Integer rangeMinRow, Integer rangeMaxRow, Integer rangeMinCol,
                                                 Integer rangeMaxCol, int pictureMinRow, int pictureMaxRow, int pictureMinCol, int pictureMaxCol,
                                                 Boolean onlyInternal) {
        int _rangeMinRow = rangeMinRow == null ? pictureMinRow : rangeMinRow;
        int _rangeMaxRow = rangeMaxRow == null ? pictureMaxRow : rangeMaxRow;
        int _rangeMinCol = rangeMinCol == null ? pictureMinCol : rangeMinCol;
        int _rangeMaxCol = rangeMaxCol == null ? pictureMaxCol : rangeMaxCol;

        if (onlyInternal) {
            return (_rangeMinRow <= pictureMinRow && _rangeMaxRow >= pictureMaxRow && _rangeMinCol <= pictureMinCol
                    && _rangeMaxCol >= pictureMaxCol);
        } else {
            return ((Math.abs(_rangeMaxRow - _rangeMinRow) + Math.abs(pictureMaxRow - pictureMinRow) >= Math
                    .abs(_rangeMaxRow + _rangeMinRow - pictureMaxRow - pictureMinRow))
                    && (Math.abs(_rangeMaxCol - _rangeMinCol) + Math.abs(pictureMaxCol - pictureMinCol) >= Math
                    .abs(_rangeMaxCol + _rangeMinCol - pictureMaxCol - pictureMinCol)));
        }
    }

}
