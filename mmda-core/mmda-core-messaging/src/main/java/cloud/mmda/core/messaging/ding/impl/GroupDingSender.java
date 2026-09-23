package cloud.mmda.core.messaging.ding.impl;

import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.messaging.ding.DingMessage;
import cloud.mmda.core.messaging.ding.DingSender;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import com.aliyun.credentials.utils.StringUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class GroupDingSender extends DingSender {
    private final static Log logger = LogFactory.getLog(GroupDingSender.class);

    private static final String MSG_TYPE_TEXT = "text";
    private static DingTalkClient client;
    @Resource
    private MessagingConfiguration configuration;
    //region    INIT
    public static String ACCESS_TOKEN;
    public static String SECRET;
    @PostConstruct
    public void init() {
        ACCESS_TOKEN = configuration.getDing().getToken();
        SECRET = configuration.getDing().getValidateToken();
        try {
            client = new DefaultDingTalkClient(ACCESS_TOKEN + sign());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取签名
     * 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）。
     * timestamp 当前时间戳，单位是毫秒，与请求调用时间误差不能超过1小时。
     * secret 密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符串。
     * @return java.lang.String
     */
    private static String sign() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + SECRET;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }
    //endregion
    @Override
    public boolean send(DingMessage dingMessage) {
        try {
            OapiRobotSendResponse oapiRobotSendResponse = sendMessageByText(dingMessage.getTo(), dingMessage.getText());
            return oapiRobotSendResponse != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private OapiRobotSendResponse sendMessageByText(String phone, String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        String[] array = phone.split(",");
        List<String> mobileList = Arrays.asList(array);
        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：text
        //content	String	是	消息内容
        //atMobiles	Array	否	被@人的手机号(在content里添加@人的手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        if (BaseUtil.hasAny(mobileList)) {
            // 发送消息并@ 以下手机号联系人
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(mobileList);
            at.setIsAtAll(false);
            request.setAt(at);
        }
        request.setMsgtype(MSG_TYPE_TEXT);
        request.setText(text);

        OapiRobotSendResponse response;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            logger.error(e);
            return null;
        }
        logger.error(JsonUtil.toJson(response));
        return response;
    }
}
