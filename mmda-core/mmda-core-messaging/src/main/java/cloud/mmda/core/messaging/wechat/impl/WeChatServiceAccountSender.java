package cloud.mmda.core.messaging.wechat.impl;

import cloud.mmda.core.messaging.wechat.TemplateMessage;
import cloud.mmda.core.messaging.wechat.WeChatMessage;
import cloud.mmda.core.messaging.wechat.WeChatSender;
import cloud.mmda.core.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信服务号Sender
 */
@Service
public class WeChatServiceAccountSender extends WeChatSender {
    private final static Log logger = LogFactory.getLog(WeChatServiceAccountSender.class);

    @Override
    public boolean send(WeChatMessage wechatMessage) {
        TemplateMessage templateMessage = new TemplateMessage();
        String messageTo = wechatMessage.getTo();
        String actualUser = messageTo.replace("\t", "");
        templateMessage.setTouser(actualUser);

        Map<String, Object> objectMap = JsonUtil.parseJsonAsMap(wechatMessage.getText());
        if (objectMap==null || objectMap.isEmpty()) {
            return false;
        }
        int index = wechatMessage.getTemplateCodeIndex();
        String[] split = messageTemplateCode.split(";");
        templateMessage.setTemplate_id(split[index]);

        // key对应创建模板内容中的形参
        //{{title.DATA}} {{username.DATA}} {{quote.DATA}} {{date.DATA}}
        // WeChatTemplateMsg对应实参和字体颜色
        Map<Object, Map<String,String>> data = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            data.put(entry.getKey(), Map.of("value", String.valueOf(entry.getValue())));
        }
        templateMessage.setData(data);
        logger.error(JsonUtil.toJson(templateMessage));
        String requestUrl = apiUrl + "message/template/send" + "?access_token=" + getToken();
        logger.error(getToken());
        logger.error(requestUrl);
        postData(requestUrl, templateMessage);
        return true;
    }
}