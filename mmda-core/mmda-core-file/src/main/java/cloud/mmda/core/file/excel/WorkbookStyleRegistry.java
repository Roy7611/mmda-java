package cloud.mmda.core.file.excel;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class WorkbookStyleRegistry {

    private final Workbook workbook;
    private final Map<CellStyleKey, CellStyle> styleCache = new HashMap<>();
    private final Map<CellStyleKey, Font> fontCache = new HashMap<>();

    public WorkbookStyleRegistry(Workbook workbook) {
        this.workbook = workbook;
    }

    public CellStyle getStyle(CellStyleKey key) {
        return styleCache.computeIfAbsent(key, k -> createStyle(k));
    }

    private CellStyle createStyle(CellStyleKey k) {
        CellStyle style = workbook.createCellStyle();

        // ===== data format =====
        if (k.getDataFormat() != null) {
            style.setDataFormat(
                    workbook.createDataFormat().getFormat(k.getDataFormat())
            );
        }

        // ===== alignment =====
        if (k.getHAlign() != null) style.setAlignment(k.getHAlign());
        if (k.getVAlign() != null) style.setVerticalAlignment(k.getVAlign());

        // ===== border =====
        if (Boolean.TRUE.equals(k.getBorder())) {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
        // ===== 填充颜色 =====
        if (k.getFillPattern() != null) style.setFillPattern(k.getFillPattern());
        if (k.getFillForegroundColor() != null) style.setFillForegroundColor(k.getFillForegroundColor());

        // ===== font =====
        if (k.getFontSize() != null || k.getBold() != null) {
            style.setFont(getFont(k));
        }

        return style;
    }

    private Font getFont(CellStyleKey k) {
        return fontCache.computeIfAbsent(k, key -> {
            Font font = workbook.createFont();
            if (key.getFontSize() != null) {
                font.setFontHeightInPoints(key.getFontSize());
            }
            if (Boolean.TRUE.equals(key.getBold())) {
                font.setBold(true);
            }
            return font;
        });
    }

    /* ===== 便捷方法 ===== */

    public CellStyle date() {
        return getStyle(CellStyleKey.builder()
                .dataFormat("yyyy-MM-dd")
                .build());
    }

    public CellStyle dateTime() {
        return getStyle(CellStyleKey.builder()
                .dataFormat("yyyy-MM-dd HH:mm:ss")
                .build());
    }

    public CellStyle number() {
        return getStyle(CellStyleKey.builder()
                .dataFormat("#,##0.00")
                .build());
    }

    public CellStyle header() {
        return getStyle(CellStyleKey.builder()
                .bold(true)
                .border(true)
                .hAlign(HorizontalAlignment.CENTER)
                .vAlign(VerticalAlignment.CENTER)
                .build());
    }
}
