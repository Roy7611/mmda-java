package cloud.mmda.core.messaging.ding;

import cloud.mmda.core.messaging.Message;
import cloud.mmda.core.messaging.Sender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 短信数据模型包括发送给谁，短信模板和参数、签名和文本消息。由{@link Sender}负责发送。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingMessage implements Message {
    /**
     * -- GETTER --
     *  获取设置接收方信息
     */
    private String to;//设置接收方信息   多个用英文(,)隔开
    /**
     * -- GETTER --
     *  设置消息文本
     */
    private String text;//设置消息文本

    private String outID;//额外参数
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
