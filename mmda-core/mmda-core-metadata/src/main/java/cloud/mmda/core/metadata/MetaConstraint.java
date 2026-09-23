package cloud.mmda.core.metadata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 元约束，例如主键、外键、CHECK和UNIQUE约束
 */
public class MetaConstraint extends MetaEntity {
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String FOREIGN_KEY = "FOREIGN KEY";
    public static final String UNIQUE = "UNIQUE";
    public static final String CHECK = "CHECK";

    /**
     * 数据库名称或者模式
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String dbSchema;

    /**
     * 对象名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String objName;
    /**
     * 约束名称
     */
    @NotBlank
    @Getter @Setter
    private String constraintName;

    /**
     * 约束类型：PRIMARY KEY|FOREIGN KEY|UNIQUE|CHECK
     */
    @NotBlank
    @Getter @Setter
    private String constraintType;

    //level: Table,Column

    @Getter @Setter
    private String comment;
}
