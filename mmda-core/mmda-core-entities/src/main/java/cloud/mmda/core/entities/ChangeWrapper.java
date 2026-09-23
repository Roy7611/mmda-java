package cloud.mmda.core.entities;

import cloud.mmda.core.enums.ChangeType;
import cloud.mmda.core.models.ChangeLog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ChangeWrapper {

    /**
     * 改变对象名称
     */
    private String objName;

//    /**
//     * 改变对象ID
//     */
//    private String objID;

    /**
     * 改变类型
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

    /**
     * 改变属性
     */
    private List<FieldChange> fieldChanges;

    public ChangeWrapper(String objName, ChangeLog.ChangeData<?> change) {
        this.objName = objName;
        this.changeType = change.getT();
        this.oldValue = change.getO();
        fieldChanges = new ArrayList<>();

        switch (change.getT()) {
            case ChangeType.ADDED, ChangeType.REMOVED -> {
                this.newValue = change.getN();
            }
            case ChangeType.CHANGED -> {
                if (change.getN() instanceof Map<?, ?> changeMap) {
                    for (var entry : changeMap.entrySet()) {
                        var key = entry.getKey();
                        var value = entry.getValue();
                        if (value instanceof ChangeLog.ChangeData<?> nchange) {
                            fieldChanges.add(new FieldChange(key.toString(), nchange));
                        }
                    }
                }
            }
        }
    }


    public void addFieldChange(FieldChange f){
        if(fieldChanges == null) fieldChanges = new ArrayList<>();
        fieldChanges.add(f);
    }


}
