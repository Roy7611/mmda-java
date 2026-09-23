package cloud.mmda.core.file.excel.modul;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeData {
    private LinkedHashMap<String,Object> data;
    private List<TreeData> children;
}
