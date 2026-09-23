package cloud.mmda.core.messaging.tokens;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis to令牌存储
 * 需提供RedisConnectionFactory类型的Bean
 *
 */
@Component
public class RedisMessageTokenStore implements MessageTokenStore {
    private static final String MESSAGES_TOKENS = "messages:tokens:";
    private RedisTemplate<String,String> redisTemplate;


    public RedisMessageTokenStore(RedisConnectionFactory redisConnectionFactory){
        this.redisTemplate = new StringRedisTemplate();
        this.redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.redisTemplate.afterPropertiesSet();
    }

    /**
     * 存入令牌
     * @param token
     */
    public void store(MessageToken token) {
        redisTemplate.opsForValue().set(
                MESSAGES_TOKENS +token.getTo(),
                token.getToken(),
                MessageToken.EXPIRED_SECONDS,
                TimeUnit.SECONDS);
    }

    /**
     * 移除令牌
     * @param to
     */
    public boolean remove(String to) {
        return redisTemplate.delete(MESSAGES_TOKENS +to);
    }

    /**
     * 判断是否存在
     * @param to
     * @return
     */
    public boolean exists(String to) {
        return redisTemplate.hasKey(MESSAGES_TOKENS +to);
    }

    /**
     * 判断令牌是否有效
     * @param to
     * @param token
     * @return
     */
    public boolean isValid(String to, String token) {
        if(!exists(to)) return false;
        return redisTemplate.opsForValue().get(MESSAGES_TOKENS +to).equals(token);
    }

    /**
     * 限制同一接收方1分钟内重发
     * @param to
     * @return
     */
    public boolean sendable(String to){
        Long expireSeconds = redisTemplate.getExpire(MESSAGES_TOKENS +to);
        if(expireSeconds==null) return true;
        return MessageToken.EXPIRED_SECONDS - expireSeconds>60;
    }
}
