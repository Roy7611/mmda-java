package cloud.mmda.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileImportErrorInfo {
    private String objName;
    private Integer rowNum;
    private String errorMsg;

    private List<ValidationError> validationErrors;


}
