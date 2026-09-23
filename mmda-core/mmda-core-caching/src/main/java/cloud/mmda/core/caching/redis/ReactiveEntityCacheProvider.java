package cloud.mmda.core.caching.redis;

import cloud.mmda.core.caching.ReactiveCacheProvider;
import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NamingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 响应式实体对象缓存提供者
 *
 * 与
 * @param <T>
 * @param <K>
 */
public class ReactiveEntityCacheProvider<T extends Entity<K>,K> implements ReactiveCacheProvider<T,K> {
    private static final Log logger = LogFactory.getLog(ReactiveEntityCacheProvider.class);

    protected final Class<T> tClass;
    protected final Class<K> kClass;

    //缓存Key配置
    protected final String cacheKeyPrefix, cacheKeyListPrefix;
    protected final String cacheKeyListSet, cacheKeyListSetRemoved;

    protected final String simpleTClassName;
    protected final boolean isTenancy;
    protected final boolean evictAllListCacheOnChange;
    /**
     * Redis缓存操作
     */
    protected final ReactiveRedisOperations<String, T> tRedisOps;
    protected final ReactiveRedisOperations<String, MetaObject> metadataRedisOps;

    protected final ReactiveRedisTemplate<String,String> stringRedisTemplate;
    protected final ReactiveHashOperations<String, String,String> stringRedisHashOps;//引用字典

    public ReactiveEntityCacheProvider(ReactiveRedisConnectionFactory factory, Class<T> tClass, Class<K> kClass){
        this.tClass = tClass;
        this.kClass = kClass;

        this.simpleTClassName = tClass.getSimpleName();
        this.isTenancy = tClass.isAssignableFrom(Tenancy.class);

        this.cacheKeyPrefix = NamingUtil.makePlural(simpleTClassName);   //Partners 缓存单个对象
        this.cacheKeyListPrefix = simpleTClassName + "Lists";           //PartnerLists 缓存列表
        this.cacheKeyListSet = simpleTClassName+"ListSet";
        this.cacheKeyListSetRemoved = cacheKeyListSet+"Removed";

        this.tRedisOps = createRedisOperations(factory);
        this.metadataRedisOps = createMetadataRedisOperations(factory);
        this.stringRedisTemplate =new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
        this.stringRedisHashOps = stringRedisTemplate.opsForHash();
        //默认行为是创建和修改后移除所有列表缓存
        this.evictAllListCacheOnChange = true;
    }

//    @PostConstruct
//    protected void initialize(){
//    }

    private ReactiveRedisOperations<String, T> createRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(tClass);
        RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, T> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<String, T>(factory, context);
    }
    private ReactiveRedisOperations<String, MetaObject> createMetadataRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<MetaObject> serializer = new Jackson2JsonRedisSerializer<>(MetaObject.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, MetaObject> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, MetaObject> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<String, MetaObject>(factory, context);
    }

    /**
     * 获取底层的StringRedisTemplate
     * @return
     */
    public final ReactiveRedisTemplate<String,String> getStringRedisTemplate(){
        return stringRedisTemplate;
    }
    public final ReactiveHashOperations<String, String,String> getStringRedisHashOps(){
        return stringRedisHashOps;
    }

    /**
     * 给一个主键，获得缓存键
     * @param k 主键值
     * @return
     */
    @Override
    public String getCacheKey(K k){
        return isTenancy
                ? joinCacheKey(String.valueOf(Tenancy.toTenantID(k)),cacheKeyPrefix, String.valueOf(k))
                : joinCacheKey(cacheKeyPrefix, String.valueOf(k));
    }

    @Override
    public final String getListCacheKey() {
        return cacheKeyListPrefix;
    }

    /**
     * 放入缓存并设置过期时间
     * @param t 实体对象
     * @param timeout 过期时间
     * @return
     */
    @Override
    public Mono<Boolean> put(@NonNull T t, Duration timeout){
        String cachKey = getCacheKeyOf(t);
        return tRedisOps.opsForValue().set(cachKey, t, timeout)
                .doOnError(e->logger.error(simpleTClassName+"存入缓存失败（put）", e));
    }

    @Override
    public Mono<Boolean> putIfPresent(@NonNull T t, Duration timeout){
        String cachKey = getCacheKeyOf(t);
        return tRedisOps.opsForValue().setIfPresent(cachKey, t)
                .doOnSuccess(ok->{
                    if(BaseUtil.isTrue(ok)) tRedisOps.expire(cachKey,timeout).subscribe();
                })
                .doOnError(e->logger.error(simpleTClassName+"存入缓存失败（putIfPresent）", e));
    }
    @Override
    public Mono<Boolean> putIfAbsent(@NonNull T t, Duration timeout){
        String cachKey = getCacheKeyOf(t);
        return tRedisOps.opsForValue().setIfAbsent(cachKey, t)
                .doOnSuccess(ok->{
                    if(BaseUtil.isTrue(ok)) tRedisOps.expire(cachKey,timeout).subscribe();
                })
                .doOnError(e->logger.error(simpleTClassName+"存入缓存失败（putIfAbsent）", e));
    }


    @Override
    public Mono<Boolean> putList(String listCacheKey, Collection<T> list, Duration cacheTimeout){
        Objects.requireNonNull(listCacheKey,"缓存键值不能为空");
        if(list == null || list.isEmpty()) return Mono.just(false);
        ReactiveDistributedLock redisLock = new ReactiveDistributedLock(listCacheKey, stringRedisTemplate);
        return redisLock.tryLock(Duration.ofSeconds(30))
                .flatMap(locked->{
                    if(BaseUtil.isTrue(locked)){
                        return tRedisOps.opsForList().rightPushAll(listCacheKey, list)
                                //记录此列表已放入缓存，便于之后移除（这样避免扫描全表）
                                .then(stringRedisTemplate.opsForSet().add(cacheKeyListSet,listCacheKey))
                                .then(tRedisOps.expire(listCacheKey, cacheTimeout));
                    }
                    return Mono.just(false);
                })
                .doFinally(s->

                        redisLock.tryRelease().doOnError(e->{
                            logger.error("redis分布式锁释放报错");
                        }).subscribe()
                );
    }

    /**
     * 从缓存中删除
     * @param k 实体主键
     * @return
     */
    @Override
    public Mono<Boolean> removeByKey(@NonNull K k){
        String cachKey = getCacheKey(k);
        return tRedisOps.opsForValue().delete(cachKey);
    }


    /**
     * 移除单个列表缓存
     * @param listCacheKey
     */
    @Override
    public void removeList(String listCacheKey){
        tRedisOps.delete(listCacheKey)
                .then(stringRedisTemplate.opsForSet().remove(cacheKeyListSet,listCacheKey))
                .subscribe();
    }
    @Override
    public void removeAllListBy(String listCacheKeyPrefix){
        ReactiveSetOperations<String,String> tRedisSetOps = stringRedisTemplate.opsForSet();

        tRedisSetOps.members(cacheKeyListSet)
                .filter(k->k.startsWith(listCacheKeyPrefix))
                .flatMap(k->
                    tRedisSetOps.add(cacheKeyListSetRemoved,k)
                            .then(tRedisOps.delete(k))
                )
                .doOnComplete(
                        ()->tRedisSetOps.differenceAndStore(cacheKeyListSet,cacheKeyListSet,cacheKeyListSetRemoved)
                                .then(tRedisSetOps.delete(cacheKeyListSetRemoved))
                                .subscribe()
                )
                .subscribe();

        /**
         //Fix:scan超时问题
        ScanOptions options = BaseUtils.isNullOrEmpty(listCacheKeyPattern)
                ? ScanOptions.NONE
                : ScanOptions.scanOptions().match(listCacheKeyPattern).build();
        tRedisSetOps.scan(cacheKeyListSet,options)
                .flatMap(k->
                        tRedisSetOps.add(cacheKeyListSetRemoved,k)
                                .then(tRedisOps.delete(k))
                )
                .doOnComplete(
                        ()->tRedisSetOps.differenceAndStore(cacheKeyListSet,cacheKeyListSet,cacheKeyListSetRemoved)
                                .then(tRedisSetOps.delete(cacheKeyListSetRemoved))
                                .subscribe()
                )
                .subscribe();
        */
    }
    /**
     * 更新当个列表缓存，先删除再设置
     * @param listCacheKey
     * @param list
     */
    @Override
    public void updateList(String listCacheKey, Collection<T> list, Duration cacheTimeout){
        tRedisOps.delete(listCacheKey)
                .then(
                        putList(listCacheKey,list,cacheTimeout)
                )
                .subscribe();
    }

    /**
     * 判断一个实体主键是否在缓存中存在
     * @param k
     * @return
     */
    @Override
    public Mono<Boolean> existsKey(@NonNull K k){
        String cacheKey = getCacheKey(k);
        return tRedisOps.hasKey(cacheKey);
    }
    /**
     * 从缓存中获取（根据主键）
     * @param k 实体主键
     * @return 如果不存在
     */
    @Override
    public Mono<T> getByKey(@NonNull K k){
        String cachKey = getCacheKey(k);
        return tRedisOps.opsForValue().get(cachKey);
    }

    /**
     * 从缓存中获取列表
     * @param listCacheKey 列表缓存键
     * @return
     */
    @Override
    public Flux<T> getList(String listCacheKey){
        if(BaseUtil.isNullOrWhitesapce(listCacheKey)) return Flux.empty();
        return tRedisOps.opsForList().size(listCacheKey)
                .flatMapMany(size -> {
                    if(size>0){
                        return tRedisOps.opsForList().range(listCacheKey, 0, size-1);
                    }
                    return Flux.empty();
                });
    }


    @Override
    public Mono<T> withRefProperty(T t, String propName, String refSet, String refKey){
        return stringRedisHashOps.get(refSet,refKey)
                .map(s -> {
                    t.setRefProperty(propName,s);
                    return t;
                });
    }
    @Override
    public Mono<Boolean> existsRefMap(String refSet){
        return stringRedisTemplate.hasKey(refSet);
    }

    @Override
    public Mono<Boolean> putRefMap(String refSet, Map<String,String> refEnumMap){
        return stringRedisHashOps.putAll(refSet,refEnumMap);
    }
    @Override
    public Mono<String> getRefText(String refSet, String refKey){
        return stringRedisHashOps.get(refSet,refKey);
    }
    @Override
    public Mono<Boolean> setRefText(String refSet, String refKey, String refText){
        return stringRedisHashOps.put(refSet,refKey,refText);
    }
    @Override
    public Mono<Boolean> putMetaObject(MetaObject metaObject){
        String cacheKey = joinCacheKey("MetaObjects",metaObject.getDbSchema().replaceAll("^\\w+_",""),metaObject.getObjName());
        return metadataRedisOps.opsForValue().set(cacheKey,metaObject);
    }

    @Override
    public Mono<MetaObject> getMetaObject(String dbSchema, String objName){
        String cacheKey = joinCacheKey("MetaObjects",dbSchema.replaceAll("^\\w+_",""),objName);
        return metadataRedisOps.opsForValue().get(cacheKey);
    }
}
