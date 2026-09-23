package cloud.mmda.core.messaging.phoneCall.impl;

import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.messaging.phoneCall.PhoneCallMessage;
import cloud.mmda.core.messaging.phoneCall.PhoneCallSender;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
/**
 * 阿里云电话发送器
 */
@Service
public class AliYunPhoneCallSender extends PhoneCallSender {
    private final static Log logger = LogFactory.getLog(AliYunPhoneCallSender.class);
    @Resource
    private MessagingConfiguration configuration;
    @Override
    public boolean send(PhoneCallMessage message) {
        try {
            //1.初始化acsClient实例 暂时不支持多region
            DefaultProfile profile =
                    DefaultProfile.getProfile(configuration.getPhoneCall().getALiRegionId(),
                                              configuration.getPhoneCall().getALiAccessKeyId(),
                                              configuration.getPhoneCall().getALiSecret());
            DefaultProfile.addEndpoint(configuration.getPhoneCall().getALiRegionId(), configuration.getPhoneCall().getALiProduct(), configuration.getPhoneCall().getALiEndpointName());
            SingleCallByTtsResponse singleCallByTtsResponse = getTtsResponse(message, profile);
            /*
            {
                "Code": "OK",
                "Message": "OK",
                "RequestId": "D9CB3933-9FE3-4870-BA8E-2BEE91B69D23",
                "CallId": "116012354148^10281378****"
            }
            */
            logger.error("SingleCallByTtsResponse    ########    "+ JsonUtil.toJson(singleCallByTtsResponse));
            if(singleCallByTtsResponse.getCode() != null && singleCallByTtsResponse.getCode().equals("OK")) {
                //请求成功
                logger.info(String.format("processing sendCVoice success！RequestId = %s , Code = %s , phone = %s",
                        singleCallByTtsResponse.getRequestId(), singleCallByTtsResponse.getCode(), message.getTo()));
                return true;
            }
            //return singleCallByTtsResponse;
        } catch (Exception ex) {
            logger.error(ex);
            return false;
        }
        return false;
    }
    private static SingleCallByTtsResponse getTtsResponse(PhoneCallMessage temp, DefaultProfile profile) throws ClientException {
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //2.创建请求并设置参数
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫号码
        if (!BaseUtil.isNullOrEmpty(temp.getTo())) request.setCalledNumber(temp.getTo());
        //必填-Tts模板ID
        if (!BaseUtil.isNullOrEmpty(temp.getTemplate())) request.setTtsCode(temp.getTemplate());
        //语音模板中的变量参数 示例：{"name":"123456","rainfall":50}
        if (!BaseUtil.isNullOrEmpty(temp.getText())) request.setTtsParam(temp.getText());
        //可选-音量 取值范围 0--100 默认取值 100
        request.setVolume(80);
        //可选-播放次数 默认取3
        request.setPlayTimes(3);
        //可选-语音通话的语速。取值范围为：-500~500
        //request.setSpeed(5);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        if (!BaseUtil.isNullOrEmpty(temp.getId())) request.setOutId(temp.getId());
        //3.发送请求并获取响应
        return acsClient.getAcsResponse(request);
    }
}