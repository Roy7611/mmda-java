package cloud.mmda.core.messaging.wechat;

import lombok.Data;

import java.util.Map;


/**
 * @Description 微信公众号模板消息请求对象
 */
@Data
public class TemplateMessage {
    /**
     * 发送消息用户的openid
     */
    private String touser;
    /**
     * 模板消息id
     */
    private String template_id;
    /**
     * key为模板中参数内容"xx.DATA"的xx,value为参数对应具体的值和颜色
     */
    private Map<Object, Map<String,String>> data;
    public TemplateMessage() {
    }
    public TemplateMessage(String touser, String templateId, Map<Object,  Map<String,String>> data) {
        this.touser = touser;
        this.template_id = templateId;
        this.data = data;
    }
}