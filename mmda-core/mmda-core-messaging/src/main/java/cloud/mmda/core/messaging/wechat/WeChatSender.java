package cloud.mmda.core.messaging.wechat;

import cloud.mmda.core.messaging.Sender;
import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class WeChatSender implements Sender<WeChatMessage>, InitializingBean {
    private final static Log logger = LogFactory.getLog(WeChatSender.class);
    private static AccessToken at;
    @Autowired
    protected MessagingConfiguration messagingConfiguration;
    protected String wxToken;
    protected String appId;
    protected String appSecret;
    protected String messageTemplateCode;
    protected String apiUrl;
    protected String mpUrl;

    @Override
    public void afterPropertiesSet() {
        this.wxToken = messagingConfiguration.getWechat().getWxToken();
        this.appId = messagingConfiguration.getWechat().getAppId();
        this.appSecret = messagingConfiguration.getWechat().getAppSecret();
        this.messageTemplateCode = messagingConfiguration.getWechat().getMessageTemplateCode();
        this.apiUrl = messagingConfiguration.getWechat().getApiUrl();
        this.mpUrl = messagingConfiguration.getWechat().getMpUrl();
    }

    //region    checkURL||验证URL||用SHA1算法生成安全签名
    public String checkURL(String signature, String timestamp, String nonce, String echostr) {
        try {
            String sha1 = getSHA1(wxToken, timestamp, nonce, "");
            // 和signature进行对比
            if (sha1.equals(signature)) {
                // 返回echostr给微信
                return echostr;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
    //endregion
    //region    getPermanentQrCode||获取二维码
    public String getPermanentQrCode(long userID) {
        try {
            QrCode qrCode = new QrCode(86400, "QR_STR_SCENE", new Scene(new SceneStr(String.valueOf(userID))));
            //永久二维码（获取）
            String url = mpUrl + "showqrcode?ticket=TICKET";
            return getTicket(qrCode, url);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    private String getTicket(QrCode qrCode, String imageUrl) {
        String url = "qrcode/create" + "?access_token=" + getToken();
        String field = "ticket";
        return imageUrl.replace("TICKET", String.valueOf(postData(url, qrCode, field)));
    }
    private final String WECHAT_KEY="wechat_token";
    @Autowired
    private StringRedisTemplate restTemplate;
    private void getAccessToken() {
        // 发送请求获取token
        JSONObject jsonObject = null;
        try {
            String url = apiUrl + "token?grant_type=client_credential" + "&appid=" + appId + "&secret=" + appSecret;
            jsonObject = getData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error(JsonUtil.toJson(jsonObject));
        assert jsonObject != null;
        String accessToken = (String) jsonObject.get("access_token");
        Integer expiresIn = (Integer) jsonObject.get("expires_in");
        // 创建token对象，并存储
        if (!BaseUtil.isNullOrEmpty(accessToken) && !BaseUtil.isNullOrEmpty(JsonUtil.toJson(expiresIn))) {
            at = new AccessToken(accessToken, String.valueOf(expiresIn));
            if (Boolean.FALSE.equals(restTemplate.hasKey(WECHAT_KEY)))
                restTemplate.opsForValue().set(WECHAT_KEY, Objects.requireNonNull(JsonUtil.toJson(at)), at.getExpiresTime(), TimeUnit.SECONDS);
        }
    }
    //  获取token, 本地缓存有就直接返回，没有就发送请求获取（wx官方api获取token每天有限制，因此需做缓存)
    public String getToken() {
        if (at==null && Boolean.TRUE.equals(restTemplate.hasKey(WECHAT_KEY)))
            at = JsonUtil.fromJson(restTemplate.opsForValue().get(WECHAT_KEY), AccessToken.class);

        if (at == null || at.isExpired()) {
            logger.error("开始获取TOKEN");
            getAccessToken();
        }
        logger.error("已有TOKEN");
        return at.getAccessToken();
    }
    //endregion
    //region    POST||GET请求
    public JSONObject getData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, JSONObject.class);
    }
    private Object postData(String url, Object qrCode, String field) {
        String requestUrl = apiUrl + url;
        JSONObject jsonObject = postData(requestUrl, qrCode);
        if (BaseUtil.isNullOrEmpty(field)) {
            return jsonObject;
        }
        return jsonObject.get(field);
    }
    protected JSONObject postData(String url, Object qrCode) {
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<Object> httpEntity = new HttpEntity<>(JsonUtil.toJson(qrCode), headers);
        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        logger.error(JsonUtil.toJson(jsonObject));
        return jsonObject;
    }
    //endregion
}
