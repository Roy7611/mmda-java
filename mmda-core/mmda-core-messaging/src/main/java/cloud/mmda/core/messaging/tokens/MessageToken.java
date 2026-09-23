package cloud.mmda.core.messaging.tokens;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

/**
 * 短消息令牌
 * 用于短信验证，5分钟有效
 * @author lcl
 *
 */
public class MessageToken {

    public static final long EXPIRED_SECONDS = 1800; //5 min

    /**
     * 接收方
     */
    private final String to;
    /**
     * 验证码，即令牌
     */
    private final String token;

    /**
     * 发送时间
     */
    private final long sentTime;

    public MessageToken(String to, String token) {
        this.to = to;
        this.token = token;
        this.sentTime = Instant.now().getEpochSecond();
    }

    public String getTo() {
        return to;
    }

    public String getToken() {
        return token;
    }

    @JsonIgnore
    public boolean isExpired(){
        long seconds = Instant.now().getEpochSecond() - sentTime;
        return seconds>EXPIRED_SECONDS;
    }

    @JsonIgnore
    public Long getExpireSeconds(){
        return sentTime-Instant.now().getEpochSecond();
    }
}
