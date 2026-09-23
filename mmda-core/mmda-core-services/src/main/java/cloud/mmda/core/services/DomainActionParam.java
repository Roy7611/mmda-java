package cloud.mmda.core.services;

import cloud.mmda.core.models.FlowTrail;
import cloud.mmda.core.entities.Ownable;
import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import cloud.mmda.core.security.models.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 领域操作参数，包括递交给谁，重要性、紧急性，消息等
 * <p>
 *     领域操作参数提交，执行操作成功后，自动生成流程追踪记录{@link FlowTrail}，提醒下一步负责人抓紧处理，
 *     同时发送Notice通知抄送copyTo中的用户。
 * </p>
 */
@Data
public class DomainActionParam implements Ownable,Serializable {
    private String actionName;

    private Long ownerID;
    private Long ownerDeptID;

    /**
     * 抄送给，会通过Notice知会
     */
    private List<Long> copyTo;

    private Importance importance;
    private Urgency urgency;

    private String notification;

    private Map<String,Object> payload;

    private List<User> users;

    private List<User> owners;

    public static DomainActionParam of(String actionName) {
        return new DomainActionParam(actionName);
    }

    public DomainActionParam(String actionName) {
        this.actionName = actionName;
        this.importance=Importance.UNKNOWN;
        this.urgency=Urgency.NORMAL;
        this.notification="";
    }

    public DomainActionParam() {
        this.importance=Importance.UNKNOWN;
        this.urgency=Urgency.NORMAL;
        this.notification="";
    }
}
