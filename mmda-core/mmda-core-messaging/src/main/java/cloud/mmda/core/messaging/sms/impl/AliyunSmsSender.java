package cloud.mmda.core.messaging.sms.impl;

import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.messaging.sms.SmsMessage;
import cloud.mmda.core.messaging.sms.SmsSender;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class AliyunSmsSender extends SmsSender {
    private static final Log logger = LogFactory.getLog(AliyunSmsSender.class);
    @Resource
    private MessagingConfiguration messagingConfiguration;
    @Override
    public boolean send(SmsMessage smsMessage) {
        try{
            //配置阿里云
            Config config = new Config()
                    .setAccessKeyId(messagingConfiguration.getSms().getClientId())
                    .setAccessKeySecret(messagingConfiguration.getSms().getClientSecret());
            //访问的域名
            config.endpoint = messagingConfiguration.getSms().getEndpoint();
            Client client =  new Client(config);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(smsMessage.getTo())
                    .setSignName(smsMessage.getSignature())
                    .setTemplateCode(smsMessage.getTemplateCode())
                    .setTemplateParam(smsMessage.getTemplateParams());
            if (smsMessage.getOutID() != null)
                sendSmsRequest.setOutId(smsMessage.getOutID());
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.getBody();
            return body.getCode().equals("OK");
        }catch(Exception e){
            logger.error(e);
            return false;
        }
    }
}
