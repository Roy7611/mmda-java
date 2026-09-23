package cloud.mmda.core.messaging.config;

import cloud.mmda.core.messaging.ding.DingSender;
import cloud.mmda.core.messaging.ding.impl.GroupDingSender;
import cloud.mmda.core.messaging.ding.impl.RobotDingSender;
import cloud.mmda.core.messaging.phoneCall.PhoneCallSender;
import cloud.mmda.core.messaging.phoneCall.impl.AliYunPhoneCallSender;
import cloud.mmda.core.messaging.phoneCall.impl.HuaWeiPhoneCallSender;
import cloud.mmda.core.messaging.sms.SmsSender;
import cloud.mmda.core.messaging.sms.impl.AliyunSmsSender;
import cloud.mmda.core.messaging.wechat.WeChatSender;
import cloud.mmda.core.messaging.wechat.impl.WeChatOfficialAccountSender;
import cloud.mmda.core.messaging.wechat.impl.WeChatPointToPointSender;
import cloud.mmda.core.messaging.wechat.impl.WeChatServiceAccountSender;
import cloud.mmda.core.utils.BaseUtil;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 发送配制工厂
 */
@Component
public class SenderFactory {
    //region    SMS
    private static final String SMS_ALIYUN = "aliyun";
    public static SmsSender getTargetSmsSender(String provider) {
        List<SmsSender> smsSenders = SpringFactoriesLoader.loadFactories(SmsSender.class, null);
        SmsSender targetPlugin = null;
        if (BaseUtil.isNullOrEmpty(provider)) {
            return smsSenders.getFirst();
        }
        for (SmsSender smsSender : smsSenders) {
            boolean findTarget = false;
            if (provider.equals(SMS_ALIYUN)) {
                if (smsSender instanceof AliyunSmsSender) {
                    targetPlugin = smsSender;
                    findTarget = true;
                }
            }
            if (findTarget) break;
        }
        return targetPlugin;
    }
    //endregion
    //region    DING
    private static final String DING_ROBOT = "robot";
    private static final String DING_GROUP = "group";
    public static DingSender getTargetDingSender(String provider) {
        List<DingSender> dingSenders = SpringFactoriesLoader.loadFactories(DingSender.class, null);
        DingSender targetPlugin = null;
        if (BaseUtil.isNullOrEmpty(provider)) {
            return dingSenders.getFirst();
        }
        for (DingSender dingSender : dingSenders) {
            boolean findTarget = false;
            switch (provider) {
                case DING_ROBOT:
                    if (dingSender instanceof RobotDingSender) {
                        targetPlugin = dingSender;
                        findTarget = true;
                    }
                    break;
                case DING_GROUP:
                    if (dingSender instanceof GroupDingSender) {
                        targetPlugin = dingSender;
                        findTarget = true;
                    }
                    break;
            }
            if (findTarget) break;
        }
        return targetPlugin;
    }
    //endregion
    //region    WECHAT
    private static final String WECHAT_SERVICE_ACCOUNT = "serviceAccount";
    private static final String WECHAT_OFFICIAL_ACCOUNT = "officialAccount";
    private static final String POINT_TO_POINT = "pointToPoint";
    public static WeChatSender getTargetWechatSender(String provider) {
        List<WeChatSender> weChatSenders = SpringFactoriesLoader.loadFactories(WeChatSender.class, null);
        WeChatSender targetPlugin = null;
        if (BaseUtil.isNullOrEmpty(provider)) {
            return weChatSenders.getFirst();
        }
        for (WeChatSender wechatSender : weChatSenders) {
            boolean findTarget = false;
            switch (provider) {
                case WECHAT_SERVICE_ACCOUNT:
                    if (wechatSender instanceof WeChatServiceAccountSender) {
                        targetPlugin = wechatSender;
                        findTarget = true;
                    }
                    break;
                case WECHAT_OFFICIAL_ACCOUNT:
                    if (wechatSender instanceof WeChatOfficialAccountSender) {
                        targetPlugin = wechatSender;
                        findTarget = true;
                    }
                    break;
                case POINT_TO_POINT:
                    if (wechatSender instanceof WeChatPointToPointSender) {
                        targetPlugin = wechatSender;
                        findTarget = true;
                    }
                    break;
            }
            if (findTarget) break;
        }
        return targetPlugin;
    }
    //endregion
    //region    PHONECALL
    private static final String PHONECALL_ALIYUN = "aliyun";
    private static final String PHONECALL_ = "huawei";
    public static PhoneCallSender getTargetPhoneCallSender(String provider) {
        List<PhoneCallSender> phoneCallSenders = SpringFactoriesLoader.loadFactories(PhoneCallSender.class, null);
        PhoneCallSender targetPlugin = null;
        if (BaseUtil.isNullOrEmpty(provider)) {
            return phoneCallSenders.getFirst();
        }
        for (PhoneCallSender phoneCallSender : phoneCallSenders) {
            boolean findTarget = false;
            switch (provider) {
                case PHONECALL_ALIYUN:
                    if (phoneCallSender instanceof AliYunPhoneCallSender) {
                        targetPlugin = phoneCallSender;
                        findTarget = true;
                    }
                    break;
                case PHONECALL_:
                    if (phoneCallSender instanceof HuaWeiPhoneCallSender) {
                        targetPlugin = phoneCallSender;
                        findTarget = true;
                    }
                    break;
            }
            if (findTarget) break;
        }
        return targetPlugin;
    }
    //endregion
}