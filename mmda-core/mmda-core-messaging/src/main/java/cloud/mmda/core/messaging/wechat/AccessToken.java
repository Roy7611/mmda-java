package cloud.mmda.core.messaging.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description access_token缓存类
 */
@Setter
@Getter
public class AccessToken {

    private String accessToken;
    //过期时间 当前系统时间+微信传来的过期时间
    private Long expiresTime;

    public AccessToken(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + Integer.parseInt(expiresIn) * 1000L;
    }

    /**
     * 判断token是否过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expiresTime;
    }

    public AccessToken(String accessToken, Long expiresTime) {
        this.accessToken = accessToken;
        this.expiresTime = expiresTime;
    }

    public AccessToken() {
    }

}