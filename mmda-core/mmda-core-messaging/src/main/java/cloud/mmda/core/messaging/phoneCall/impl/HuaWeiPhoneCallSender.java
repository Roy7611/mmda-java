package cloud.mmda.core.messaging.phoneCall.impl;

import cloud.mmda.core.messaging.phoneCall.PhoneCallMessage;
import cloud.mmda.core.messaging.phoneCall.PhoneCallSender;
import org.springframework.stereotype.Service;

/**
 * 华为电话发送器
 */
@Service
public class HuaWeiPhoneCallSender extends PhoneCallSender {
    //https://rtccall.cn-north-1.myhuaweicloud.cn:443/rest/httpsessions/callnotify/{version}
    @Override
    public boolean send(PhoneCallMessage phoneCallMessage) {
        return false;
    }
}
