package cloud.mmda.core.caching;

import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.metadata.MetaObject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 响应式缓存提供者
 *
 * @param <T>
 * @param <K>
 */
public interface ReactiveCacheProvider<T extends Entity<K>, K> {
    /**
     * 将多个字符串连接为Redis缓存键
     * @param args
     * @return
     */
    default String joinCacheKey(String...args){
        return String.join(CacheProvider.KEY_SEPERATOR, args);
    }

    /**
     * 给一个主键，获得缓存键
     * @param k 主键值
     * @return
     */
    String getCacheKey(@NonNull K k);

    /**
     * 给一个实体对象，获得缓存键
     * @param t 实体对象
     * @return
     */
    default String getCacheKeyOf(@NonNull T t){
        return getCacheKey(t.getId());
    }

    /**
     * 获取列表缓存键，如PartnerLists
     * @return
     */
    String getListCacheKey();
    default String getListCacheKey(int tenantID){
        return tenantID+CacheProvider.KEY_SEPERATOR+getListCacheKey();
    }
    /**
     * 获取列表缓存键，例如：PartnerLists:Approved 为已经审批过的列表
     * @param args 比如
     * @return
     */
    default String getListCacheKey(String...args){
        return getListCacheKey()+CacheProvider.KEY_SEPERATOR+ joinCacheKey(args);
    }
    default String getListCacheKey(int tenantID,String...args){
        return getListCacheKey(tenantID)+CacheProvider.KEY_SEPERATOR+ joinCacheKey(args);
    }

    /**
     * 放入缓存
     * @param t 实体对象
     * @param timeout 过期时间
     * @return
     */
    Mono<Boolean> put(@NonNull T t, @NonNull Duration timeout);
    default Mono<Boolean> put(@NonNull T t){
        return put(t,CacheProvider.DEF_TIMEOUT);
    }


    Mono<Boolean> putIfPresent(@NonNull T t, @NonNull Duration timeout);
    default Mono<Boolean> putIfPresent(@NonNull T t){
        return putIfPresent(t, CacheProvider.DEF_TIMEOUT);
    }
    Mono<Boolean> putIfAbsent(@NonNull T t, @NonNull Duration timeout);
    default Mono<Boolean> putIfAbsent(@NonNull T t){
        return putIfAbsent(t, CacheProvider.DEF_TIMEOUT);
    }

    /**
     * 将列表放入缓存
     * @param listCacheKey 列表缓存键
     * @param list 列表
     * @param cacheTimeout 失效时间
     * @return
     */
    Mono<Boolean> putList(@NonNull String listCacheKey, Collection<T> list, @NonNull Duration cacheTimeout);
    default Mono<Boolean> putList(@NonNull String listCacheKey, Collection<T> list){
        return putList(listCacheKey, list, CacheProvider.DEF_TIMEOUT);
    }

    void removeList(@NonNull String listCacheKey);
    /**
     * 移除单个列表缓存
     * @param listName 不含前缀的列表键值
     */
    default void removeListByName(@NonNull String listName){
        String listCacheKey = getListCacheKey(listName);
        removeList(listCacheKey);
    }

    default void removeListByName(int tenantID, @NonNull String listName){
        String listCacheKey = getListCacheKey(tenantID,listName);
        removeList(listCacheKey);
    }
    /**
     * 移除单个列表缓存
     * @param listName 列表名称，如ByAcctID
     * @param id 列表标识，如acctID=1
     */
    default void removeListByName(@NonNull String listName, @NonNull String id){
        String listCacheKey = getListCacheKey(listName,id);
        removeList(listCacheKey);
    }
    default void removeListByName(int tenantID,@NonNull String listName, @NonNull String id){
        String listCacheKey = getListCacheKey(tenantID,listName,id);
        removeList(listCacheKey);
    }
    /**
     * 移除列表缓存，模糊匹配键值模式，如Accounts:
     * @param listCacheKeyPrefix
     */
    void removeAllListBy(@NonNull String listCacheKeyPrefix);
    default void removeAllList(){
        String listCacheKeyPrefix = getListCacheKey();
        removeAllListBy(listCacheKeyPrefix);
    }
    default void removeAllList(int tenantID){
        String listCacheKeyPrefix = getListCacheKey(tenantID);
        removeAllListBy(listCacheKeyPrefix);
    }
    default void removeAllList(@NonNull String listName){
        String listCacheKeyPrefix = getListCacheKey(listName);
        removeAllListBy(listCacheKeyPrefix);
        //应避免使用keys全表扫描
//		tRedisOps.keys(listCacheKeyPattern)
//			.flatMap(tRedisOps::delete)
//			.subscribe();
    }
    default void removeAllList(int tenantID,@NonNull String listName){
        String listCacheKeyPrefix = getListCacheKey(tenantID,listName);
        removeAllListBy(listCacheKeyPrefix);
    }
    default void removeAllList(@NonNull String listName, @NonNull String id){
        String listCacheKeyPrefix = getListCacheKey(listName, id);
        removeAllListBy(listCacheKeyPrefix);
    }
    default void removeAllList(int tenantID,@NonNull String listName, @NonNull String id){
        String listCacheKeyPrefix = getListCacheKey(tenantID,listName, id);
        removeAllListBy(listCacheKeyPrefix);
    }
    /**
     * 更新当个列表缓存，先删除再设置
     * @param listCacheKey
     * @param list
     */
    void updateList(@NonNull String listCacheKey, Collection<T> list, @NonNull Duration cacheTimeout);

    /**
     * 判断是否存在键值
     * @param k
     * @return
     */
    Mono<Boolean> existsKey(@NonNull K k);
    default Mono<Boolean> exists(@NonNull T t){
        return existsKey(t.getId());
    }

    /**
     * 从缓存中获取（根据主键）
     * @param k 实体主键
     * @return 如果不存在
     */
    Mono<T> getByKey(@NonNull K k);
    /**
     * 从缓存中获取（根据唯一索引，由子类重写{@link #getCacheKeyOf(Entity)} ）
     * @param t 实体，用于计算唯一索引键值
     * @return
     */
    default Mono<T> get(@NonNull T t){
        return getByKey(t.getId());
    }

    /**
     * 从缓存中获取列表
     * @param listCacheKey 列表缓存键
     * @return
     */
    Flux<T> getList(@NonNull String listCacheKey);

    /**
     * 从缓存中删除
     * @param k 实体主键
     * @return
     */
    Mono<Boolean> removeByKey(@NonNull K k);
    /**
     * 从缓存中删除，根据唯一索引（由子类重写{@link #getCacheKeyOf(Entity)}）
     * @param t
     * @return
     */
    default Mono<Boolean> remove(@NonNull T t){
        return removeByKey(t.getId());
    }


    /**
     * 缓存元数据对象
     * @param metaObject
     * @return
     */
    Mono<Boolean> putMetaObject(@NonNull MetaObject metaObject);

    /**
     * 从缓存中获取元数据对象
     * @param dbSchema
     * @param objName
     * @return
     */
    Mono<MetaObject> getMetaObject(@NonNull String dbSchema, @NonNull String objName);

    /**
     * 是否存在引用字典数据
     * @param refSet 引用枚举设置，例如REF Partner(partnerID,partnerName)
     * @return
     */
    Mono<Boolean> existsRefMap(@NonNull String refSet);

    /**
     * 放入枚举引用字典数据
     * @param refSet 引用枚举设置，例如REF Partner(partnerID,partnerName)
     * @param refMap 引用枚举数据，{{1, 客户A},{2, 客户B},……}
     * @return
     */
    Mono<Boolean> putRefMap(@NonNull String refSet, Map<String,String> refMap);

    /**
     * 获取单个枚举值
     * @param refSet 引用枚举设置，例如REF Partner(partnerID,partnerName)
     * @param refKey 引用枚举键，例如 1
     * @return 返回枚举值，例如 客户A
     */
    Mono<String> getRefText(@NonNull String refSet, @NonNull String refKey);
    Mono<Boolean> setRefText(@NonNull String refSet, @NonNull String refKey, String refText);
    /**
     * 组装实体对象引用属性
     * @param t 实体对象
     * @param propName 属性名称，如userID
     * @param refSet 引用枚举设置，如REF User(userID,userName)
     * @param refKey 实体对象的属性值，即t.userID，作为引用键值
     * @return 返回组装了引用属性$userID=userName的实体对象
     */
    Mono<T> withRefProperty(@NonNull T t, @NonNull String propName, @NonNull String refSet, @NonNull String refKey);
}
