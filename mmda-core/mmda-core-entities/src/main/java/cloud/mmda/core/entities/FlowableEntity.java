package cloud.mmda.core.entities;

import cloud.mmda.core.models.FlowTrail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 支持流程追踪的实体抽象类是多租户的，主键类型<code>Long</code>
 *
 * @param <S> 实体状态枚举类型
 * @see AbstractEntity 抽象实体
 * @see TenancyEntity 多租户实体
 * @see MasterTenancyEntity 多租户主实体
 */
public abstract class FlowableEntity<S extends Enum<S>> extends MasterTenancyEntity
        implements Flowable<S> {
    /**
     * 获取状态
     * @return
     */
    public abstract Enum<S> getStatus();

    /**
     * 流程追踪
     */
    @Getter
    @Setter
    private List<FlowTrail> flowTrails;

    @Getter
    @Setter
    private Long newChangeLogID;

    @Getter
    @Setter
    private String changeLogTags;

    @Setter
    @Getter
    private boolean loggable;

    /**
     * 标识当前操作是否作为业务动作记录日志（可由业务代码显式设置）。
     */
    private boolean logAsAction;

    /**
     * 设置当前操作是否应作为业务动作记录日志。
     * @param logAsAction true 表示强制记录日志（业务动作）
     */
    public final void setLogAsAction(boolean logAsAction) {
        this.logAsAction = logAsAction;
    }

    /**
     * 判断是否应记录日志。
     * 若已设置 loggable 或 logAsAction 任一为 true，则记录日志。
     * @return true 表示需要记录日志
     */
    public final boolean shouldLog() {
        return isLoggable() || logAsAction;
    }

}
