package cloud.mmda.core.entities;

import cloud.mmda.core.models.ChangeLog;

import java.io.Serializable;

/**
 * 修改日志标识实体支持修改追踪，每次修改都存储{@link ChangeLog}，并且支持从日志中恢复
 */
public interface ChangeLoggable extends Serializable {
    /**
     * 获取变更日志ID
     * @return
     */
    Long getChangeLogID();

    /**
     * 设置视图的变更日志ID
     * @param logID
     */
    void setChangeLogID(Long logID);

    /**
     * 可以开始记录日志否，例如本次修改属于变更才允许开始记录修改记录
     * @return
     */
    default boolean isLoggable() {
        return true;
    }


    default String getChangeLogTags(){
        return null;
    };

    default void  setChangeLogTags(String tags){}
}
