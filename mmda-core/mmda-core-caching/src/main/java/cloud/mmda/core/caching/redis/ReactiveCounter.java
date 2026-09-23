package cloud.mmda.core.caching.redis;

import cloud.mmda.core.entities.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.support.collections.RedisZSet;

/**
 * 响应式计数器，基于redis实现
 * @author roshion
 * @since 2020-12-18
 *
 * 根据文档https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:support
 * 所描述org.springframework.data.redis.support包中有现成的，包括RedisSet 和 RedisZSet 类
 */
public class ReactiveCounter<T extends Entity, K> {

//    @Autowired
    private RedisZSet<T> redisZSet;


}
