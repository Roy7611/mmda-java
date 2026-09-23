package cloud.mmda.core.file.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Getter
@EqualsAndHashCode
public class CellStyleKey {

    // 基础
    private final String dataFormat;
    private final HorizontalAlignment hAlign;
    private final VerticalAlignment vAlign;

    // 字体
    private final Short fontSize;
    private final Boolean bold;

    // 边框
    private final Boolean border;

    private FillPatternType fillPattern;

    private Short fillForegroundColor;

    private CellStyleKey(Builder b) {
        this.dataFormat = b.dataFormat;
        this.hAlign = b.hAlign;
        this.vAlign = b.vAlign;
        this.fontSize = b.fontSize;
        this.bold = b.bold;
        this.border = b.border;
        this.fillPattern = b.fillPattern;
        this.fillForegroundColor = b.fillForegroundColor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String dataFormat;
        private HorizontalAlignment hAlign;
        private VerticalAlignment vAlign;
        private Short fontSize;
        private Boolean bold;
        private Boolean border;
        private FillPatternType fillPattern;
        private Short fillForegroundColor;

        public Builder dataFormat(String v) { this.dataFormat = v; return this; }
        public Builder hAlign(HorizontalAlignment v) { this.hAlign = v; return this; }
        public Builder vAlign(VerticalAlignment v) { this.vAlign = v; return this; }
        public Builder fontSize(short v) { this.fontSize = v; return this; }
        public Builder bold(boolean v) { this.bold = v; return this; }
        public Builder border(boolean v) { this.border = v; return this; }
        public Builder fillPattern(FillPatternType fp) { this.fillPattern = fp; return this; }
        public Builder fillForegroundColor(short color) { this.fillForegroundColor = color; return this; }

        public CellStyleKey build() {
            return new CellStyleKey(this);
        }
    }
}

