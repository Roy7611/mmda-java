package cloud.mmda.core.caching.redis;

import cloud.mmda.core.caching.CacheProvider;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

public class ReactiveTenancyEntityCacheProvider<T extends TenancyEntity<K>,K> extends ReactiveEntityCacheProvider<T,K> {
    public ReactiveTenancyEntityCacheProvider(ReactiveRedisConnectionFactory factory, Class<T> tClass, Class<K> kClass){
        super(factory,tClass,kClass);
    }


    /**
     * 先按租户分离
     * @param k
     * @return
     */
    @Override
    public String getCacheKey(K k) {
        int tenantId = Tenancy.toTenantID(k);
        return joinCacheKey(String.valueOf(tenantId),cacheKeyPrefix, String.valueOf(k));
    }

//    public String getListCacheKey(int tenantID){
//        return tenantID+KEY_SEPERATOR+cacheKeyListPrefix;
//    }

    public String getListCacheKey(int tenantID, String...args){
        return getListCacheKey(tenantID)+ CacheProvider.KEY_SEPERATOR+ joinCacheKey(args);
    }

    public void removeList(int tenantID, String listName){
        String cacheKey=getListCacheKey(tenantID, listName);
        super.removeList(cacheKey);
    }

    public void updateList(int tenantID, String listName, Collection<T> list, Duration cacheTimeout){
        String cacheKey=getListCacheKey(tenantID,listName);
        tRedisOps.delete(cacheKey)
                .then(
                        putList(cacheKey,list,cacheTimeout)
                )
                .subscribe();
    }

    public Flux<T> getList(int tenantID, String listCacheKey){
        String cacheKey=getListCacheKey(tenantID, listCacheKey);
        return tRedisOps.opsForList().size(cacheKey)
                .flatMapMany(size -> {
                    if(size>0){
                        return tRedisOps.opsForList().range(cacheKey, 0, size-1);
                    }
                    return Flux.empty();
                });
    }

    @Override
    public Mono<T> withRefProperty(T t, String propName, String refSet, String refKey){
        Objects.requireNonNull(propName);
        Objects.requireNonNull(refSet);

        String cacheKey=getListCacheKey(t.getTenantID(), refSet);
        return stringRedisHashOps.get(cacheKey,refKey)
                .map(s -> {
                    t.setRefProperty(propName,s);
                    return t;
                });
    }
}
