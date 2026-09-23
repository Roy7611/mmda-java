package cloud.mmda.core.messaging.push;

import cloud.mmda.core.messaging.Message;
import cloud.mmda.core.messaging.Sender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 短信数据模型包括发送给谁，短信模板和参数、签名和文本消息。由{@link Sender}负责发送。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessage implements Message {
    //设备类型：ANDROID,IOS,ALL
    public static final String DEVICE_ALL = "ALL";
    public static final String DEVICE_ANDROID = "ANDROID";
    public static final String DEVICE_IOS = "IOS";

    public static final String REDIRECT_APPLICATION = "APPLICATION";
    /**
     * 消息ID
     **/
    private String messageID;
    /**
     * 推送目标：ALIAS,TAG,ACCOUNT,DEVICE
     **/
    private String to;//设置接收方信息
    @Override
    public void setTo(String to) {
        this.to = to;
    }
    /**
     * 推送目标值，如按tag推送则是tag值
     **/
    private String targetValue;
    /**
     * 推送类型：MESSAGE,NOTICE
     **/
    private String pushType;
    /**
     * 设备类型：ANDROID,IOS,ALL
     **/
    private String deviceType;
    /**
     * 消息标题
     **/
    private String title;
    private String text;//设置消息文本
    @Override
    public void setText(String text) {
        this.text = text;
    }
    /**
     * 消息推送时间
     **/
    private Timestamp pushTime;
    /**
     * 扩展参数，json格式，包含redirectType,redirectTo
     **/
    private Map<String,Object> extParameters;
    private long pushID;
    private static final String PARAM_REDIRECT_TYPE = "redirectType";
    private static final String PARAM_REDIRECT_TO = "redirectTo";
    @JsonIgnore
    public String getRedirectTypePara(){
        if (extParameters == null) {
            return "NONE";
        }
        return extParameters.containsKey(PARAM_REDIRECT_TYPE)
                ? extParameters.get(PARAM_REDIRECT_TYPE).toString()
                : "NONE";
    }
}
