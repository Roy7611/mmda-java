package cloud.mmda.core.caching.redis;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TenancyEntityCacheProvider<T extends TenancyEntity<K>,K>
        extends EntityCacheProvider<T,K> {
    public TenancyEntityCacheProvider(RedisConnectionFactory factory, Class<T> tClass, Class<K> kClass){
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
        return getListCacheKey(tenantID)+KEY_SEPERATOR+ joinCacheKey(args);
    }

    public void removeList(int tenantID, String listName){
        Objects.requireNonNull(listName);
        String cacheKey=getListCacheKey(tenantID, listName);
        super.removeList(cacheKey);
    }

    public void updateList(int tenantID, String listName, Collection<T> list, Duration cacheTimeout){
        Objects.requireNonNull(listName);
        String cacheKey=getListCacheKey(tenantID,listName);
        if(Boolean.TRUE == tRedisOps.delete(cacheKey)){
            putList(cacheKey,list,cacheTimeout);
        }
    }

    public List<T> getList(int tenantID, String listCacheKey){
        String cacheKey=getListCacheKey(tenantID, listCacheKey);
        return super.getList(cacheKey);
    }

    @Override
    public T withRefProperty(T t, String propName, String refSet, String refKey){
        Objects.requireNonNull(propName);
        Objects.requireNonNull(refSet);

        String cacheKey=getListCacheKey(t.getTenantID(), refSet);
        String s = stringRedisHashOps.get(cacheKey,refKey);
        t.setRefProperty(propName,s);
        return t;
    }
}
