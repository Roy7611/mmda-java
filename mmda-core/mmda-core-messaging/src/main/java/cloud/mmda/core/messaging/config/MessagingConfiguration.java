package cloud.mmda.core.messaging.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 短信服务配置
 * <p>
 * 使得你可以在application.yml配置短信发送平台、发送和批量发送端点以及用于获取令牌的客户端身份。
 * </p>
 *
 * @author Roy Luo
 * @version 4.0
 * @see <a href="https://www.baeldung.com/configuration-properties-in-spring-boot">Guide to @ConfigurationProperties in Spring Boot</a>
 * @since 2024.07
 */
@Data
@Configuration //自动注册为Bean组件，你就能在发送短信时获取配置知道怎么干
@ConfigurationProperties(prefix = "mmda") //首先这是一个属性配置，限定以mmda.sms开头
//@EnableConfigurationProperties(MessagingConfiguration.class) //包含了这个库就可以配置
public class MessagingConfiguration {
    //region    短信 || SMS
    private final static Sms sms = new Sms();
    @Getter
    @Setter
    public static class Sms {
        //短信通知方式，aliyun 或者 huawei
        @NotBlank
        private String provider;
        //接入地址（URL）
        @NotBlank
        private String host;
        //发送端点（相对URL）
        @NotBlank
        private String endpoint;
        //分批或者批量发送端点（相对URL）
        private String batchEndpoint;
        //获取短信模板编号
        private String templateCode;
        //Http方法，默认POST
        private String httpMethod;
        //在短信平台中注册的应用标识
        private String clientId;
        //在短信平台中注册的应用密钥
        private String clientSecret;
        //短信签名
        private String signature;
        //我方回执确认URL，供第三方短信平台调用
        //@Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
        private String ackReceiptUrl;

    }
    public Sms getSms() {
        return sms;
    }
    //endregion
    //region    钉钉 || DING
    private final static Ding ding = new Ding();
    @Getter
    @Setter
    public static class Ding {
        //钉钉通知方式，robot 或者 group
        @NotBlank
        private String provider;
        //企业内部应用相关配制
        @NotBlank
        private String key;
        @NotBlank
        private String secret;
        @NotBlank
        private String getByMobileServiceUrl;
        @NotBlank
        private String getTokenServiceUrl;
        //钉钉自定义机器人相关配置
        @NotBlank
        private String token;
        @NotBlank
        private String validateToken;
    }
    public Ding getDing() {
        return ding;
    }
    //endregion
    //region    微信 || WECHAT
    private final static Wechat wechat = new Wechat();
    @Getter
    @Setter
    public static class Wechat {
        //微信通知方式，serviceAccount 或者 officialAccount
        @NotBlank
        private String provider;
        //钉钉自定义机器人相关配置
        @NotBlank
        private String wxToken;
        @NotBlank
        private String appId;
        @NotBlank
        private String appSecret;
        @NotBlank
        private String messageTemplateCode;
        @NotBlank
        private String apiUrl;
        @NotBlank
        private String mpUrl;
    }
    public Wechat getWechat() {
        return wechat;
    }
    //endregion
    //region    电话通知 || PHONECALL
    private final static PhoneCall phoneCall = new PhoneCall();
    @Getter
    @Setter
    public static class PhoneCall {
        //电话通知方式，aliyun 或者 huawei
        @NotBlank
        private String provider;
        //阿里云Key
        @NotBlank
        private String aLiAccessKeyId;
        //阿里云Secret
        @NotBlank
        private String aLiSecret;
        //端点，如cn-hangzhou
        @NotBlank
        private String aLiEndpointName;
        //区域，如cn-hangzhou
        @NotBlank
        private String aLiRegionId;
        //短信API产品名称（短信产品名固定，无需修改）
        @NotBlank
        private String aLiProduct;
        //短信API产品域名（接口地址固定，无需修改）
        @NotBlank
        private String aLiDomain;
        //模版值
        @NotBlank
        private String aLiTemplateCode;
    }
    public PhoneCall getPhoneCall() {
        return phoneCall;
    }
    //endregion
    //region    通知 || PUSH
    private final static Push push = new Push();
    @Getter
    @Setter
    public static class Push {
        //通知方式，aliyun
        @NotBlank
        private String provider;
        @NotBlank
        private String env;
        @NotBlank
        private String regionId;
        @NotBlank
        private String accessKeyId;
        @NotBlank
        private String accessKeySecret;
        @NotBlank
        private Long IOSAppkey;
        @NotBlank
        private Long AndroidAppkeyPro;
        @NotBlank
        private Long AndroidAppkeyTest;
    }
    public Push getPush() {
        return push;
    }
    //region
    //region    邮件 || MAIL
    private final static Mail mail = new Mail();
    @Getter
    @Setter
    public static class Mail {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String host;
        @NotBlank
        private String defaultEncoding;
        @NotBlank
        private Integer port;
        @NotBlank
        private String smtpSocketFactoryClass;
        @NotBlank
        private Boolean smtpSocketFactoryFallback;
        @NotBlank
        private Integer smtpSocketFactoryPort;
        @NotBlank
        private Boolean smtpAuth;
        @NotBlank
        private Boolean smtpStarttlsEnable;
        @NotBlank
        private Boolean smtpStarttlsRequired;
    }
    public Mail getMail() {
        return mail;
    }
    //region
}