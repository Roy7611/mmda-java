package cloud.mmda.core.enums;

import cloud.mmda.core.models.AuditTrail;

/**
 * 审计策略
 * <p>定义用户访问接口时是否生成审计追踪记录{@link AuditTrail}</p>
 */
public enum AuditPolicy {
    /**
     * 不审计
     */
    NONE,
    /**
     * 数据修改时审计，此时生成审计追踪记录
     */
    DATA_CHANGE,
    /**
     * 不论读写，访问接口就生成审计记录，可能对性能产生冲击
     */
    ALWAYS
}
