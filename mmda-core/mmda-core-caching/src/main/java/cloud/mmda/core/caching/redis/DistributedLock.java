package cloud.mmda.core.caching.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;

import static java.util.Collections.singletonList;

/**
 * Redis分布式锁
 * @author roshion
 * @remark
 * <p>
 * 设计原则：目前只实现单节点模式
 * <ol>
 *     <li>同一时刻只能有一个客户端持有锁</li>
 *     <li>避免死锁</li>
 *     <li>容错性，只要主节点在或者重启了，都能获取和释放锁</li>
 * </ol>
 * </p>
 *
 * <p>
 * 单节点的情况，正确的做法：
 * 获取锁 <code></>SET lock_key random_value NX PX 30000</code>。设置过期时间自动释放，避免死锁。
 * 释放锁使用Lua脚本
 * <code>
 * if redis.call("get",KEYS[1]) == ARGV[1] then
 *     return redis.call("del",KEYS[1])
 * else
 *     return 0
 * end
 * </code>
 * </p>
 *
 * <p>
 * 分布式的情形如何考虑如下情况极为重要：
 * <ol>
 *     <li>客户端A从主节点获取锁</li>
 *     <li>主节点在将键值复制到从节点前崩溃</li>
 *     <li>从节点晋升为主节点</li>
 *     <li>客户端B从可获取相同键值的锁，此时A,B同时获得锁</li>
 * </ol>
 * </p>
 * http://redis.io/commands/setnx
 * https://redis.io/commands/set 可使用SET替代SETNX
 * https://redis.io/topics/distlock 分布式锁实现
 * https://wudashan.cn/2017/10/23/Redis-Distributed-Lock-Implement/
 */
public class DistributedLock {
    private static final String LUA_RELEASE_SCRIPT = "if redis.call('GET',KEYS[1]) == ARGV[1] then return redis.call('DEL',KEYS[1]) else return false end";
    private static final String LUA_LOCK_SCRIPT = "if redis.call('SETNX',KEYS[1],ARGV[1]) then return redis.call('EXPIRE', KEYS[1], ARGV[2]) else return false end";
    //private static final String LUA_LOCK_SCRIPT = "if redis.call('SET',KEYS[1],ARGV[1],'NX','PX',ARGV[2]) then return 1 else return 0 end";
    static final long DEFAULT_EXPIRATION = 5000L;

    static final RedisScript<Boolean> LOCK_SCRIPT = RedisScript.of(LUA_LOCK_SCRIPT,Boolean.class);
    static final RedisScript<Boolean> RELEASE_SCRIPT = RedisScript.of(LUA_RELEASE_SCRIPT,Boolean.class);

    private final String redisLockKey;
    private String redisLockID;
    private StringRedisTemplate redisTemplate;

    public DistributedLock(String lockKey, StringRedisTemplate redisTemplate){
        this.redisLockKey = "{Locks}:"+lockKey;
        this.redisLockID = null;
        this.redisTemplate = redisTemplate;
    }

    public Boolean tryLock(long timeoutMillis){
        if(redisLockID!=null) return Boolean.FALSE;
        if(timeoutMillis<=0) timeoutMillis=DEFAULT_EXPIRATION; //避免死锁

        //使用时间戳，然后GETSET避免异常导致的死锁
        //GETSET lock.foo <current Unix timestamp + lock timeout + 1>
        long millis = System.currentTimeMillis()+timeoutMillis+1L;
        redisLockID = String.valueOf(millis);
        //redisLockID = UUID.randomUUID().toString();

        return redisTemplate.execute(LOCK_SCRIPT, singletonList(redisLockKey), redisLockID, String.valueOf(timeoutMillis));
    }
    /**
     * 尝试获得锁
     * @param duration 过期时间，为了避免死锁
     * @return
     */
    public Boolean tryLock(Duration duration){
        return tryLock(duration.toMillis());
    }

    /**
     * 尝试释放锁
     * @return
     */
    public Boolean tryRelease(){
        if(redisLockID==null) return Boolean.FALSE;
        Boolean released = redisTemplate.execute(RELEASE_SCRIPT, singletonList(redisLockKey), redisLockID);
        if(released.booleanValue()) redisLockID = null;
        return released;
    }
}
