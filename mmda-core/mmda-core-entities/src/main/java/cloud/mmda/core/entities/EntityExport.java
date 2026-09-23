package cloud.mmda.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityExport {
    private String objName;        // 元对象
    private String format;          // xlsx / csv /pdf
    private Long templateId;  // 模板ID
    private boolean print;  // 是否打印

    private List<String> columns;

    private String searchWord;  // 搜索词

    private String condition;   // 条件

    private Map<String, Object> payload;

    private int recordCount;

    private int finished;

    private int pageSize;
    private int pageNo;
    private String sort;

    public EntityExport(String objName, String condition, String searchWord, List<String> columns,
                        boolean print, Long templateId, String format) {
        this.objName = objName;
        this.condition = condition;
        this.searchWord = searchWord;
        this.columns = columns;
        this.print = print;
        this.templateId = templateId;
        this.format = format;
    }
}
