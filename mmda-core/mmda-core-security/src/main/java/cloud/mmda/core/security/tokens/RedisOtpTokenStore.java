package cloud.mmda.core.security.tokens;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Redis based implementation of one-time password token store.
 */
@Component
public class RedisOtpTokenStore implements OtpTokenStore {
    private static final String OTP_TOKENS = "mmda:otp:tokens:";
    private StringRedisTemplate redisTemplate;
    private PasswordEncoder passwordEncoder;

    public RedisOtpTokenStore(final StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
    }

    @Override
    public void store(OtpToken token) {
        Assert.notNull(token, "token must not be null");
        redisTemplate.opsForValue().set(
                OTP_TOKENS + token.getUsername(),
                token.getPassword(),
                token.getTimeout(),
                TimeUnit.SECONDS);
    }

    @Override
    public String retrieve(String name) {
        Assert.notNull(name, "name must not be null");
        return redisTemplate.opsForValue().get(OTP_TOKENS + name);
    }

    @Override
    public boolean remove(String name) {
        Assert.notNull(name, "name must not be null");
        return redisTemplate.delete(OTP_TOKENS + name);
    }

    @Override
    public boolean exists(String name) {
        Assert.notNull(name, "name must not be null");
        return redisTemplate.hasKey(OTP_TOKENS + name);
    }

}
