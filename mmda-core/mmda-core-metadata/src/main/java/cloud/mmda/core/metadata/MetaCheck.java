package cloud.mmda.core.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库约束
 */
public class MetaCheck extends MetaConstraint{
    public MetaCheck() {
        this.setConstraintType(CHECK);
    }
    @Getter
    @Setter
    private String checkClause;
}
