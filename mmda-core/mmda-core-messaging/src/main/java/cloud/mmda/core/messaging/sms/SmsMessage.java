package cloud.mmda.core.messaging.sms;

import cloud.mmda.core.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 通用的短消息接口，允许你设置关键的短消息属性。
 *
 * @author Roy Luo
 * @since 2024.07
 */
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessage implements Message {
    @Getter
    private String to;//获取短信接收方手机号码，多个号码使用英文逗号隔开
    @Getter
    private String text;//获取短信文本
    @Getter @Setter
    private String signature;//获取短信签名
    @Getter @Setter
    private String templateCode;//获取短信模板编号
    @Getter @Setter
    private String templateParams;//获取短信模板参数（JSON格式）
    @Getter @Setter
    private String outID;//外部流水扩展字段。
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }
}