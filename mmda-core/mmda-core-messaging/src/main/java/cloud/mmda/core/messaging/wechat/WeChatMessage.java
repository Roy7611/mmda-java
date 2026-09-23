package cloud.mmda.core.messaging.wechat;

import cloud.mmda.core.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 微信消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatMessage implements Message {
    private int templateCodeIndex;//模版项次 用 ( ; ) 分割
    private String to;//设置接收方信息   多个用英文(,)隔开
    private String text;//设置消息文本
    private String content;
    private String jumpUrl;
    private int type;
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
