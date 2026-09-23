package cloud.mmda.core.messaging.phoneCall;

import cloud.mmda.core.messaging.Message;
import cloud.mmda.core.messaging.Sender;
import lombok.*;

/**
 * 短信数据模型包括发送给谁，短信模板和参数、签名和文本消息。由{@link Sender}负责发送。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneCallMessage implements Message {
    //外部扩展字段,此ID将在回执消息中带回给调用方
    private String id;
    //主叫号码:158********  （华为云用）
    @NonNull
    private String displayNbr;
    //被叫手机号:158********
    @NonNull
    private String to;
    //语音通知消息 （阿里云用）
    @NonNull
    private String template;
    //模版参数
    @NonNull
    private String text;
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
