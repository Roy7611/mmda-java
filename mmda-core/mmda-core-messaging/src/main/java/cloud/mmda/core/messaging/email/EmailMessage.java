package cloud.mmda.core.messaging.email;

import cloud.mmda.core.messaging.Message;
import cloud.mmda.core.messaging.Sender;
import lombok.*;

import java.util.List;

/**
 * 短信数据模型包括发送给谁，短信模板和参数、签名和文本消息。由{@link Sender}负责发送。
 */
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Message {
    /**
     * -- GETTER --
     *  获取设置接收方信息
     */
    @Getter
    private String to;//设置接收方信息
    /**
     * -- GETTER --
     *  获取设置消息标题
     */
    @Setter
    @Getter
    private String title;//设置消息标题
    /**
     * -- GETTER --
     *  设置消息文本
     */
    @Getter
    private String text;//设置消息文本
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Getter
    @Setter
    private List<String> attachments;//附件
}
