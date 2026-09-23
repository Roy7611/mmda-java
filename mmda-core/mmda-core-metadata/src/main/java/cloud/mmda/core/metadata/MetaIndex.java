package cloud.mmda.core.metadata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 元索引
 */
@Setter
@Getter
@NoArgsConstructor
public class MetaIndex extends MetaEntity{
    /**
     * 数据库名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    private String dbSchema;

    /**
     * 对象名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    private String objName;

    /**
     * 索引名称
     */
    @NotBlank
    @Size(min = 1, max = 128)
    private String indexName;

    private boolean unique = false;

    /**
     * 索引定义，某些数据库能获得脚本
     */
    @NotBlank
    private String indexDef;

    private List<MetaIndexCol> cols;

    private String comment;


    @Override
    public int hashCode() {
        return Objects.hash(dbSchema, objName, indexName);
    }

}
