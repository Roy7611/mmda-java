package cloud.mmda.core.entities;

import cloud.mmda.core.models.Attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户相关的主实体是id为长整形的业务主数据，例如订单主表。支持附件上传、下载。
 * 主键类型<code>Long</code>，它可能有子表一般会继承{@link CompositeTenancyEntity}
 * @see AbstractEntity 抽象实体
 * @see TenancyEntity 多租户实体
 * @see FlowableEntity 具有流程属性的多租户实体
 */
public abstract class MasterTenancyEntity extends TenancyEntity<Long> implements Attachable{
    private List<? extends Attachment> attachments = new ArrayList<>();
    @Override

    public List<? extends Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public void setAttachments(List<? extends Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public long getPartitionID() {
        return getId();//主键即租户分区键
    }

    @Override
    public Class<Long> getIdClass() {
        return Long.class;
    }
}
