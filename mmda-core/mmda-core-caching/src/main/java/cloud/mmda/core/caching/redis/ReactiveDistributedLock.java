package cloud.mmda.core.caching.redis;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * 响应式Redis分布式锁
 * @author roshion
 * @see DistributedLock
 */
public class ReactiveDistributedLock {

    private final String redisLockKey;
    private String redisLockID;
    private ReactiveRedisTemplate<String,String> reactiveRedisTemplate;

    public ReactiveDistributedLock(String lockKey, ReactiveRedisTemplate<String,String> redisTemplate){
        this.redisLockKey = "{Locks}:"+lockKey;
        this.redisLockID = null;
        this.reactiveRedisTemplate = redisTemplate;
    }

    public Mono<Boolean> tryLock(long timeoutMillis){
        if(redisLockID!=null) return Mono.just(false);
        if(timeoutMillis<=0) timeoutMillis=DistributedLock.DEFAULT_EXPIRATION; //避免死锁

        //使用时间戳，然后GETSET避免异常导致的死锁
        //GETSET lock.foo <current Unix timestamp + lock timeout + 1>
        long millis = System.currentTimeMillis()+timeoutMillis+1L;
        redisLockID = String.valueOf(millis);
        //redisLockID = UUID.randomUUID().toString();

        return reactiveRedisTemplate.execute(DistributedLock.LOCK_SCRIPT, singletonList(redisLockKey), asList(redisLockID, String.valueOf(timeoutMillis)))
                .single()
                .switchIfEmpty(Mono.just(Boolean.FALSE))
                .doOnError(throwable -> {
                    System.out.print(throwable);
                })
                .onErrorReturn(Boolean.FALSE);
    }
    /**
     * 尝试获得锁
     * @param duration 过期时间，为了避免死锁
     * @return
     */
    public Mono<Boolean> tryLock(Duration duration){
        return tryLock(duration.toMillis());
    }

    /**
     * 尝试释放锁
     * @return
     */
    public Mono<Boolean> tryRelease(){
        if(redisLockID==null) return Mono.just(Boolean.FALSE);
        return reactiveRedisTemplate.execute(DistributedLock.RELEASE_SCRIPT, singletonList(redisLockKey), singletonList(redisLockID))
                .single()
                .switchIfEmpty(Mono.just(Boolean.FALSE))
                .onErrorReturn(Boolean.FALSE)
                .doOnError(throwable -> {
                    System.out.print(throwable);
                })
                .doFinally(signalType->{
                    if(signalType== SignalType.ON_COMPLETE) redisLockID = null;
                });
    }

}
