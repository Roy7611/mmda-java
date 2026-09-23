package cloud.mmda.core.messaging.push;

import cloud.mmda.core.messaging.Sender;
import cloud.mmda.core.messaging.config.MessagingConfiguration;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.utils.ParameterHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * 阿里云短信发送器
 */
@Service
public class PushSender implements Sender<PushMessage> {
    private final static Log logger = LogFactory.getLog(PushSender.class);

    @Resource
    private MessagingConfiguration configuration;
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * https://help.aliyun.com/knowledge_detail/48089.html?spm=a2c4g.11186631.2.5.501224e1eSCtm1
     *
     * @param push :
     *             如何向一群用户推送消息或通知
     *             给这些用户 绑定上相同的 Alias 或者 Tag ;
     *             小范围的 绑定 Alias , 大范围的 绑定 Tag ;
     *             客户端 和 服务端 都有相应的绑定的API 。
     */
    @Override
    public boolean send(PushMessage push) {
        try {
            IClientProfile profile = DefaultProfile.getProfile(
                    configuration.getPush().getProvider(),
                    configuration.getPush().getAccessKeyId(),
                    configuration.getPush().getAccessKeySecret()
            );
            DefaultAcsClient client = new DefaultAcsClient(profile);
            PushRequest pushRequest = new PushRequest();
            // 推送目标

            pushRequest.setTarget(push.getTo()); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
            pushRequest.setTargetValue(push.getTargetValue()); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            //pushRequest.setTarget("ALL"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALL: 推送给全部
            //pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            pushRequest.setPushType(push.getPushType()); // 消息类型 MESSAGE NOTICE
            pushRequest.setDeviceType(push.getDeviceType()); // 设备类型 ANDROID iOS ALL.
            // 推送配置
            pushRequest.setTitle(push.getTitle()); // 消息的标题
            pushRequest.setBody(push.getText()); // 消息的内容
            // 推送控制
            if (push.getPushTime() != null) {
                Date pushDate = new Date(push.getPushTime().getTime()); // 30秒之间的时间点, 也可以设置成你指定固定时间
                String pushTime = ParameterHelper.getISO8601Time(pushDate);
                pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
            }
            String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
            pushRequest.setExpireTime(expireTime);
            pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
            if (push.getDeviceType().equalsIgnoreCase(PushMessage.DEVICE_IOS)) {//ios
                pushToIOS(push, pushRequest);
                clientPush(push, client, pushRequest);
            } else if (push.getDeviceType().equalsIgnoreCase(PushMessage.DEVICE_ANDROID)) {//andorid
                pushToAndroid(push, pushRequest);
                clientPush(push, client, pushRequest);
            } else {//all
                pushToIOS(push, pushRequest);
                clientPush(push, client, pushRequest);
                push.setPushID(0);
                pushToAndroid(push, pushRequest);
                clientPush(push, client, pushRequest);
            }
        } catch (ClientException e) {
            logger.error("阿里推送客户端错误", e);
        } catch (Exception e) {
            logger.error("推送最外围错误", e);
        }
        return true;
    }
    private void pushToIOS(PushMessage push, PushRequest pushRequest) {
        // 推送配置: iOS
        pushRequest.setAppKey(configuration.getPush().getIOSAppkey());
        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
        pushRequest.setIOSMusic("default"); // iOS通知声音
        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容

        if (isProd()) {
            pushRequest.setIOSApnsEnv("PRODUCT");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        } else {
            pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        }
        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
        pushRequest.setIOSRemindBody(push.getText());//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效

        try {
            String extParam = objectMapper.writeValueAsString(push.getExtParameters());
            pushRequest.setIOSExtParameters(extParam); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
        } catch (JsonProcessingException e) {
            logger.error("解析推送扩展属性出错IOS", e);
        }
    }
    private boolean isProd() {
        return configuration.getPush().getEnv().equals("res");
    }
    private void clientPush(PushMessage push,  DefaultAcsClient client, PushRequest pushRequest) throws ClientException {
        try {
            //PushResponse pushResponse = client.getAcsResponse(AcsRequest.concatQueryString(JsonUtils.parseJsonAsMapT(JsonUtils.toJson(pushRequest))));
            //System.out.printf("RequestId: %s, MessageId: %s\n",
            //        pushResponse.getRequestId(), pushResponse.getMessageId());
            //push.setMessageID(pushResponse.getMessageId());
        } catch (Exception e) {

        }
    }
    private void pushToAndroid(PushMessage push, PushRequest pushRequest) {
        // 推送配置: Android

        if (isProd()) {
            pushRequest.setAppKey(configuration.getPush().getAndroidAppkeyPro());
        } else {
            pushRequest.setAppKey(configuration.getPush().getAndroidAppkeyTest());
        }
        pushRequest.setAndroidNotifyType("BOTH");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        //Android 推送跳转
        pushRequest.setAndroidOpenType(PushMessage.REDIRECT_APPLICATION);//APPLICATION
        pushRequest.setAndroidMusic("default"); // Android通知音乐
        pushRequest.setAndroidPopupTitle(push.getTitle());//"Popup Title"
        pushRequest.setAndroidPopupBody(push.getText());//"Popup Body"
        pushRequest.setAndroidNotificationChannel("innetsect push");//8.0 通知不到
        try {
            String extParam = objectMapper.writeValueAsString(push.getExtParameters());
            pushRequest.setAndroidExtParameters(extParam); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
        } catch (JsonProcessingException e) {

            logger.error("解析推送扩展属性出错Android", e);
        }
    }
}