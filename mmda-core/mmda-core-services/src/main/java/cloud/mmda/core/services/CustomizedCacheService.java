package cloud.mmda.core.services;

import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.entities.EntityState;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.services.exceptions.OperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class CustomizedCacheService {
    String KEY_SEPERATOR = ":";
    Duration DEF_TIMEOUT = CachePolicy.DEF_EXPIRED_10_MINUTES;

    protected final RedisTemplate<String, Object> objectRedisTemplate;

    @Autowired
    public CustomizedCacheService(RedisConnectionFactory factory){
        this.objectRedisTemplate = createMetadataRedisOperations(factory);
    }

    private RedisTemplate<String, Object> createMetadataRedisOperations(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 将多个字符串连接为Redis缓存键
     * @param args
     * @return
     */
    private String joinCacheKey(String...args){
        return String.join(KEY_SEPERATOR, args);
    }
    public String getCacheKey(long userID, String cacheKey){
        return  joinCacheKey(String.valueOf(Tenancy.toTenantID(userID)), String.valueOf(userID), cacheKey);
    }
    public boolean putAny(String cacheKey, Object cacheValue, Duration timeout) {
        objectRedisTemplate.opsForValue().set(cacheKey, cacheValue, timeout);
        return true;
    }
    public boolean putAny(String cacheKey, Object cacheValue) {
        objectRedisTemplate.opsForValue().set(cacheKey, cacheValue);
        return true;
    }
    public boolean putAnyIfPresent(String cacheKey, Object cacheValue, Duration timeout) {
       return objectRedisTemplate.opsForValue().setIfPresent(cacheKey, cacheValue, timeout);
    }
    public boolean putAnyIfPresent(String cacheKey, Object cacheValue) {
        return objectRedisTemplate.opsForValue().setIfPresent(cacheKey, cacheValue);
    }
    public Object getAny(String cacheKey) {
        return objectRedisTemplate.opsForValue().get(cacheKey);
    }
    public Boolean existsKey( String cacheKey){
        return objectRedisTemplate.hasKey(cacheKey);
    }
    public Boolean removeByKey(String cacheKey){
        return objectRedisTemplate.delete(cacheKey);
    }

    public boolean putAny(CustomizedCache cache) {
        String cachKey = getCacheKey(cache.getUserID(), cache.getCacheName());
        if (cache.getTimeout() == null)
            return putAny(cachKey, cache.getCacheData());
        else
            return putAny(cachKey, cache.getCacheData(), Duration.ofMinutes(cache.getTimeout()));

    }

    public boolean putAnyIfPresent(CustomizedCache cache) {
        String cachKey = getCacheKey(cache.getUserID(), cache.getCacheName());
        if (cache.getTimeout() == null)
            return putAnyIfPresent(cachKey, cache.getCacheData());
        else
            return putAnyIfPresent(cachKey, cache.getCacheData(), Duration.ofMinutes(cache.getTimeout()));

    }
    public boolean saveCustomizedCache(CustomizedCache cache){
        try {
            return switch (cache.getEntityState()){
                case EntityState.CREATED, EntityState.NEW_MODIFIED, EntityState.MODIFIED-> putAny(cache);
                case EntityState.DELETED->removeByKey(cache.getId());
                default->false;
            };
        }catch (Exception e){
            throw new OperationFailedException("save.customizedCache.fail");
        }
    }
}
