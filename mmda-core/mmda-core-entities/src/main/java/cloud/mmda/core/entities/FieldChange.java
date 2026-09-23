package cloud.mmda.core.entities;

import cloud.mmda.core.enums.ChangeType;
import cloud.mmda.core.models.ChangeLog;
import lombok.Data;

@Data
public class FieldChange extends AbstractEntity{

    /**
     * 字段名
     */
    private String fieldName;

    /**
     *  变更类型
     */
    private ChangeType changeType;

    /**
     * 原始值
     */
    private Object oldValue;

    /**
     * 新值
     */
    private Object newValue;



    @Override
    public String getId() {
        return fieldName;
    }

    @Override
    public Class getIdClass() {
        return String.class;
    }

    /**
     * 元数据包括字段和子表关系名称
     */
    public static abstract class Meta {
        public static final String _fieldName = "fieldName";
        public static final String _changeType = "changeType";
        public static final String _oldValue = "oldValue";
        public static final String _newValue = "newValue";
    }

    public FieldChange(String fieldName, ChangeLog.ChangeData<?> change) {
        this.fieldName = fieldName ;
        this.changeType = change.getT();
        this.oldValue = change.getO();
        this.newValue = change.getN();
    }

}
