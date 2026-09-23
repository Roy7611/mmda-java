package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.MetaForeignKeyAction;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * 元外键
 */
public class MetaForeignKey extends MetaConstraint {

    public MetaForeignKey() {
        setConstraintType(FOREIGN_KEY);
    }

    @Getter @Setter
    private LinkedHashMap<String,String> colRefs;

    /**
     * 是否已创建关联索引
     */
    @Getter @Setter
    private boolean indexed = false;

    /**
     * 索引名称
     */
    @Getter @Setter
    private String indexName;

    /**
     * 引用数据库模式
     */
    @Getter @Setter
    private String refDbSchema;

    /**
     * 引用表名称
     */
    @Getter @Setter
    private String refObjName;

    /**
     * 删除操作
     */
    @Getter @Setter
    private MetaForeignKeyAction onDeleteAction = MetaForeignKeyAction.NO_ACTION;

    /**
     * 更新操作
     */
    @Getter @Setter
    private MetaForeignKeyAction onUpdateAction = MetaForeignKeyAction.NO_ACTION;

}
