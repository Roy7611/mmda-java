package cloud.mmda.core.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeParam {
    /**
     * 执行操作的人
     */
    @NotBlank
    private String userName;
    /**
     * 执行的action中文名，如：提交
     */
    @NotBlank
    private String actionName;
    /**
     * 实体名称，如：Contract
     */
    @NotBlank
    private String objName;
}
