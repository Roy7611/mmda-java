package cloud.mmda.core.entities;

import cloud.mmda.core.models.Attachment;

import java.util.List;

/**
 * 实体附件接口，单个Long主键的实体支持
 */
public interface Attachable {
    List<? extends Attachment> getAttachments();
    void setAttachments(List<? extends Attachment> attachments);
}
