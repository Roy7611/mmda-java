package cloud.mmda.core.messaging.wechat.impl;

import cloud.mmda.core.messaging.wechat.WeChatMessage;
import cloud.mmda.core.messaging.wechat.WeChatSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * 微信公众号Sender
 * lkj
 * 2024-05-22 23:00
 **/
@Service
public class WeChatOfficialAccountSender extends WeChatSender {
    private final static Log log = LogFactory.getLog(WeChatServiceAccountSender.class);
    @Override
    public boolean send(WeChatMessage wechatMessage) {
        return false;
    }
}
