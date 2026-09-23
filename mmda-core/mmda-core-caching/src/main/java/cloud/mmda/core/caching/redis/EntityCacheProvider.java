package cloud.mmda.core.caching.redis;

import cloud.mmda.core.caching.CacheProvider;
import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NamingUtil;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 实体对象缓存提供者
 *
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
@Component
public class EntityCacheProvider<T extends Entity<K>,K> implements CacheProvider<T,K> {
    private static final Log logger = LogFactory.getLog(EntityCacheProvider.class);
    private static final String MSG_K_NULL = "缓存键不能为空";
    private static final String MSG_V_NULL = "缓存值不能为空";
    private static final String MSG_T_NULL = "缓存过期时间不能为空";

    private final List<T> EMPTY_LIST;

    protected final Class<T> tClass;
    protected final Class<K> kClass;

    //缓存Key配置
    protected final String cacheKeyPrefix, cacheKeyListPrefix;
    protected final String cacheKeyListSet, cacheKeyListSetRemoved;

    protected final String simpleTClassName;
    protected final boolean isTenancy;
    protected boolean evictAllListCacheOnChange;

    //缓存启用
    protected boolean enabled;
    public Function<T,Boolean> beforePut = (t)->true;

    public final void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    /**
     * Redis缓存操作
     */
    protected final RedisTemplate<String, T> tRedisOps;
    protected final RedisTemplate<String, MetaObject> metadataRedisOps;

    protected final StringRedisTemplate stringRedisTemplate;
    protected final HashOperations<String, String,String> stringRedisHashOps;//引用字典
    @Autowired
    public EntityCacheProvider(RedisConnectionFactory factory, Class<T> tClass, Class<K> kClass){
        this.tClass = tClass;
        this.kClass = kClass;

        this.simpleTClassName = tClass.getSimpleName();
        this.isTenancy = tClass.isAssignableFrom(Tenancy.class);

        this.cacheKeyPrefix = NamingUtil.makePlural(simpleTClassName);   //Partners 缓存单个对象
        this.cacheKeyListPrefix = simpleTClassName + "Lists";           //PartnerLists 缓存列表
        this.cacheKeyListSet = simpleTClassName+"ListSet";
        this.cacheKeyListSetRemoved = cacheKeyListSet+"Removed";

        this.EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>());
        this.tRedisOps = createRedisOperations(factory);
        this.metadataRedisOps = createMetadataRedisOperations(factory);
        this.stringRedisTemplate =new StringRedisTemplate(factory);
        this.stringRedisHashOps = stringRedisTemplate.opsForHash();
        //默认行为是创建和修改后移除所有列表缓存
        this.evictAllListCacheOnChange = true;
        this.enabled = true;
    }


    private RedisTemplate<String, T> createRedisOperations(RedisConnectionFactory factory) {
        RedisTemplate<String,T> redisTemplate = new RedisTemplate<String, T>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(tClass);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    private RedisTemplate<String, MetaObject> createMetadataRedisOperations(RedisConnectionFactory factory) {

        RedisTemplate<String, MetaObject> redisTemplate = new RedisTemplate<String, MetaObject>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        Jackson2JsonRedisSerializer<MetaObject> serializer = new Jackson2JsonRedisSerializer<>(MetaObject.class);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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
    public void put(@NonNull final T t, @NonNull final Duration timeout){
        Objects.requireNonNull(t,MSG_V_NULL);
        Objects.requireNonNull(timeout,MSG_T_NULL);

        //如果未启用缓存，或者不值得缓存，忽略
        if(!enabled || !beforePut.apply(t)) return;

        String cachKey = getCacheKeyOf(t);
        tRedisOps.opsForValue().set(cachKey, t, timeout);
    }

    @Override
    public void putAny(String cacheKey, T t, Duration timeout) {
        Objects.requireNonNull(cacheKey,MSG_K_NULL);
        Objects.requireNonNull(t,MSG_V_NULL);
        Objects.requireNonNull(timeout,MSG_T_NULL);

        //如果未启用缓存，或者不值得缓存，忽略
        if(!enabled || !beforePut.apply(t)) return;

//        String cachKey = getCacheKeyOf(t);
        tRedisOps.opsForValue().set(cacheKey, t, timeout);
    }

    @Override
    public Boolean putIfPresent(@NonNull final T t, @NonNull final Duration timeout){
        Objects.requireNonNull(t,MSG_V_NULL);
        Objects.requireNonNull(timeout,MSG_T_NULL);

        //如果未启用缓存，或者不值得缓存，忽略
        if(!enabled || !beforePut.apply(t)) return Boolean.FALSE;

        String cachKey = getCacheKeyOf(t);
        return tRedisOps.opsForValue().setIfPresent(cachKey, t, timeout);
    }
    @Override
    public Boolean putIfAbsent(@NonNull final T t, @NonNull final Duration timeout){
        Objects.requireNonNull(t,MSG_V_NULL);
        Objects.requireNonNull(timeout,MSG_T_NULL);

        //如果未启用缓存，或者不值得缓存，忽略
        if(!enabled || !beforePut.apply(t)) return Boolean.FALSE;

        String cachKey = getCacheKeyOf(t);
        return tRedisOps.opsForValue().setIfAbsent(cachKey, t, timeout);
    }


    @Override
    public Boolean putList(@NonNull final String listCacheKey, @NotEmpty final Collection<T> list, @NonNull final Duration cacheTimeout){
        Objects.requireNonNull(listCacheKey,MSG_K_NULL);
        Objects.requireNonNull(cacheTimeout,MSG_T_NULL);
        if(list == null || list.isEmpty()) return Boolean.FALSE;

        DistributedLock redisLock = new DistributedLock(listCacheKey, stringRedisTemplate);
        Boolean locked = redisLock.tryLock(Duration.ofSeconds(30));
        if(BaseUtil.isTrue(locked)) {
            //TODO 使用管道提升性能
//            tRedisOps.execute(
//                    (RedisCallback<? extends Object>) conn->{
//                        conn.
//                        return null;
//                    }
//            );
            list.forEach(t->tRedisOps.boundListOps(listCacheKey).rightPush(t));
            stringRedisTemplate.opsForSet().add(cacheKeyListSet,listCacheKey);
            tRedisOps.expire(listCacheKey, cacheTimeout.getSeconds(), TimeUnit.SECONDS);
            return redisLock.tryRelease();
        }
        return Boolean.FALSE;
    }

    /**
     * 从缓存中删除
     * @param k 实体主键
     * @return
     */
    @Override
    public Boolean removeByKey(@NonNull K k){
        String cachKey = getCacheKey(k);
        return tRedisOps.delete(cachKey);
    }

    /**
     * 从缓存中删除多个
     * @param keys
     * @return
     */
    @Override
    public Long removeAllByKeys(Collection<K> keys){
        Collection<String> cacheKeys = keys.stream()
                .map(k -> getCacheKey(k))
                .collect(Collectors.toSet());
        return tRedisOps.delete(cacheKeys);
    }
    /**
     * 移除单个列表缓存
     * @param listCacheKey
     */
    @Override
    public void removeList(@NonNull String listCacheKey){
        Boolean deleted = tRedisOps.delete(listCacheKey);
        if(BaseUtil.isTrue(deleted)){
            stringRedisTemplate.opsForSet().remove(cacheKeyListSet,listCacheKey);
        }
    }
    @Override
    public void removeAllListBy(@NonNull String listCacheKeyPrefix){
        BoundSetOperations<String,String> setOps = stringRedisTemplate.boundSetOps(cacheKeyListSet);
        for (String k : setOps.members()) {
            if(k.startsWith(listCacheKeyPrefix)){
                tRedisOps.delete(k);
                setOps.remove(k);
            }
        }
    }
    /**
     * 更新当个列表缓存，先删除再设置
     * @param listCacheKey
     * @param list
     */
    @Override
    public void updateList(@NonNull String listCacheKey, Collection<T> list, @NonNull Duration cacheTimeout){
        Boolean deleted = tRedisOps.delete(listCacheKey);
        if(BaseUtil.isTrue(deleted)){
            putList(listCacheKey,list,cacheTimeout);
        }
    }

    /**
     * 判断一个实体主键是否在缓存中存在
     * @param k
     * @return
     */
    @Override
    public Boolean existsKey(@NonNull K k){
        String cacheKey = getCacheKey(k);
        return tRedisOps.hasKey(cacheKey);
    }
    /**
     * 从缓存中获取（根据主键）
     * @param k 实体主键
     * @return 如果不存在
     */
    @Override
    public T getByKey(@NonNull K k){
        String cachKey = getCacheKey(k);
        return tRedisOps.opsForValue().get(cachKey);
    }

    @Override
    public T getAny(String cacheKey) {
        return tRedisOps.opsForValue().get(cacheKey);
    }

    /**
     * 从缓存中获取列表
     * @param listCacheKey 列表缓存键
     * @return
     */
    @Override
    public List<T> getList(@NonNull String listCacheKey){
        if(BaseUtil.isNullOrWhitesapce(listCacheKey)) return EMPTY_LIST;
        BoundListOperations<String,T> listOps = tRedisOps.boundListOps(listCacheKey);
        Long size = listOps.size();
        if(!BaseUtil.isNullOrZero(size)){
            return listOps.range(0, size - 1);
        }
        return EMPTY_LIST;
    }


    @Override
    public T withRefProperty(@NonNull T t, @NonNull String propName, String refSet, String refKey){
        String s = stringRedisHashOps.get(refSet,refKey);
        t.setRefProperty(propName,s);
        return t;
    }
    @Override
    public Boolean existsRefMap(String refSet){
        return stringRedisTemplate.hasKey(refSet);
    }

    @Override
    public void putRefMap(String refSet, Map<String,String> refEnumMap){
        stringRedisHashOps.putAll(refSet,refEnumMap);
    }
    @Override
    public String getRefText(String refSet, String refKey){
        return stringRedisHashOps.get(refSet,refKey);
    }
    @Override
    public void setRefEnumText(String refSet, String refKey, String refText){
        stringRedisHashOps.put(refSet,refKey,refText);
    }
    @Override
    public void evictRefKey(String refSet, String refKey){
        if(existsRefMap(refSet)){
            stringRedisHashOps.delete(refSet,refKey);
        }
    }
    @Override
    public void putMetaObject(MetaObject metaObject){
        String cacheKey = joinCacheKey("MetaObjects",metaObject.getDbSchema().replaceAll("^\\w+_",""),metaObject.getObjName());
        metadataRedisOps.opsForValue().set(cacheKey,metaObject);
    }

    @Override
    public MetaObject getMetaObject(String dbSchema, String objName){
        String cacheKey = joinCacheKey("MetaObjects",dbSchema.replaceAll("^\\w+_",""),objName);
        return metadataRedisOps.opsForValue().get(cacheKey);
    }
}
