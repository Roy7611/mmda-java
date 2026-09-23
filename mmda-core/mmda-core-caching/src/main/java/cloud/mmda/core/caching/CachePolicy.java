package cloud.mmda.core.caching;


import cloud.mmda.core.Tenancy;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 缓存策略
 *
 * @author roshion
 * @since 2020/6/20
 *
 * 2020.10.09 增加cacheKey用于指定缓存键值，当使用SqlExpression时无法先计算条件哈希，由缓存策略自行指定
 */
public class CachePolicy {
    //缓存策略常数
    /**
     * 不缓存
     *
     */
    public static final int NONE = 0;
    /**
     * Read Through 策略
     *
     * <p>
     *     读取时先检查缓存，若未命中（hit）从数据源载入并缓存以备下次从缓存读取。
     *     适用于多次请求相同数据的场景，需缓存预热。
     *     在进行大量读取时，可以减少数据源上的负载，也对缓存服务的故障具备一定的弹性。
     *     如果缓存服务挂了，则仍然可以通过直接转到数据源来进行操作。
     * </p>
     *
     */
    public static final int READ_THRU = 1;//读并
    /**
     * Write Through 策略
     *
     * <p>
     *     写入时同时写数据库和缓存，缓存与数据源保持一致，数据写入速度较慢。
     *     但是，当与 Read-Through 配合使用时，我们将获得 Read-Through 的所有好处，
     *     并且还可以获得数据一致性保证，从而使我们免于使用缓存失效技术。
     * </p>
     *
     */
    public static final int WRITE_THRU = 2;
    /**
     * 综合上面两种
     */
    public static final int BOTH_THRU = READ_THRU | WRITE_THRU;

    /**
     * Write Behind 策略
     *
     * <p>
     *     在数据更新时，先写入缓存，然后由后台定期更新至数据源。
     *     优点是数据写入速度快，适用于繁重的写工作负载。
     *     与 Read Through 配合使用，可以很好地用于混合工作负载，最近更新和访问的数据总是在缓存中可用。
     *     它可以抵抗数据源故障，并可以容忍某些数据源停机时间。如果支持批处理或合并，
     *     如果没有强一致性要求，我们可以简单地使缓存的更新请求入队，则可以减少对数据源的总体写入，从而减少了负载并降低了成本。
     *     一旦更新后的缓存数据还未被写入数据源时，出现系统断电的情况，数据将无法找回。
     * </p>
     */
    public static final int WRITE_BEHIND = 4;

    public static final Duration DEF_EXPIRED_5_MINUTES = Duration.ofMinutes(5);
    public static final Duration DEF_EXPIRED_10_MINUTES = Duration.ofMinutes(10);
    public static final Duration DEF_EXPIRED_2_HOURS = Duration.ofHours(2);
    public static final Duration DEF_EXPIRED_1_DAY = Duration.ofDays(1);
    public static final Duration DEF_EXPIRED_1_WEEK = Duration.ofDays(7);

    /**
     * 租户ID，用于分片/分区
     */
    private int tenantID;
    /**
     * 缓存策略
     */
    private int policy;
    /**
     * 缓存失效时间
     * Write Behind 策略下指刷新后的失效时间
     */
    private Duration expiryTimeout;

    /**
     * 只要数据改变，自动清所有列表缓存，
     * 小心设置此选项，很可能导致性能问题，用于小量数据的情况
     */
    private boolean evictAllListOnChanged;

    /**
     * 指定缓存键值
     */
    private String cacheKey;
    ///////////////////////////////////////////////
    // 以下三个参数只作用于Write Behind 策略
    ///////////////////////////////////////////////
    /**
     * 启动刷新缓存的级别，个数
     */
    private int flushingStartLevel;
    /**
     * 停止刷新缓存的级别
     */
    private int flushingStopLevel;
    /**
     * 延迟刷新缓存
     */
    private Duration flushingDelay;

    private CachePolicy(int tenantID, int policy, Duration expiryTimeout){
        this.policy = policy;
        this.expiryTimeout = expiryTimeout;
        this.tenantID=tenantID;
    }
    private CachePolicy(int tenantID, int policy, Duration expiryTimeout, String cacheKey){
        this.policy = policy;
        this.expiryTimeout = expiryTimeout;
        this.tenantID=tenantID;
        this.cacheKey=cacheKey;
    }
    private CachePolicy(int tenantID, int flushingStartLevel,int flushingStopLevel,Duration flushingDelay, Duration expiryTimeout){
        this.policy = WRITE_BEHIND;
        this.expiryTimeout = expiryTimeout;
        this.flushingStartLevel = flushingStartLevel;
        this.flushingStopLevel = flushingStopLevel;
        this.flushingDelay = flushingDelay;
    }
    private CachePolicy(int tenantID, int flushingStartLevel,int flushingStopLevel,Duration flushingDelay, Duration expiryTimeout, String cacheKey){
        this.policy = WRITE_BEHIND;
        this.expiryTimeout = expiryTimeout;
        this.flushingStartLevel = flushingStartLevel;
        this.flushingStopLevel = flushingStopLevel;
        this.flushingDelay = flushingDelay;
        this.cacheKey=cacheKey;
    }


    public final int getPolicy(){return policy;}
    public final boolean cached(){return policy != NONE;}
    public final boolean readThrough(){ return (policy & READ_THRU)>0;}
    public final boolean writeThrough(){ return (policy & WRITE_THRU)>0;}
    public final boolean writeBehind(){ return (policy & WRITE_BEHIND)>0;}
    public final Duration timeout(){return expiryTimeout;}
    public final int getTenantID(){return tenantID;}
    public final boolean isEvictAllListOnChanged(){return evictAllListOnChanged;}
    public final void setEvictAllListOnChanged(boolean v){evictAllListOnChanged=v;}
    public final int getFlushingStartLevel(){return flushingStartLevel;}
    public final int getFlushingStopLevel(){return flushingStopLevel;}
    public final Duration getFlushingDelay(){return flushingDelay;}
    public final String getCacheKey(){return cacheKey;}
    public final void setCacheKey(String k){ cacheKey = k; }
    public final void setCacheKeyIfAbsent(Supplier<String> keySupplier){
        if(!cached() || cacheKey!=null) return;
        cacheKey = keySupplier.get();
    }
    /**
     * 克隆一个其他租户的缓存策略
     * @param tenantId 租户ID
     * @return
     */
    public CachePolicy ofTenantID(int tenantId){
        if(this.policy == WRITE_BEHIND)
            return new CachePolicy(tenantId,this.flushingStartLevel,this.flushingStopLevel,this.flushingDelay,this.expiryTimeout);
        return new CachePolicy(tenantId,this.policy,this.expiryTimeout);
    }
    public CachePolicy ofDuration(Duration expiryTimeout){
        return new CachePolicy(this.tenantID, this.policy, expiryTimeout);
    }
    public CachePolicy ofNone(){
        return new CachePolicy(this.tenantID,NONE,Duration.ZERO);
    }

    /**
     * 不使用缓存，仅可用于不区分租户的情形
     */
    public static final CachePolicy CACHE_NONE = new CachePolicy(Tenancy.NO_TENANT_ID,NONE,Duration.ofSeconds(0));
    /**
     * 全部载入缓存，仅可用于不区分租户而且少量基础数据的情形
     */
    public static final CachePolicy CACHE_ALL = new CachePolicy(Tenancy.NO_TENANT_ID,BOTH_THRU, DEF_EXPIRED_1_WEEK);
    public static CachePolicy of(int tenantID){
        return new CachePolicy(tenantID, NONE,Duration.ZERO);
    }
    public static CachePolicy of(int tenantID, int policy, Duration expiryTimeout){
        return new CachePolicy(tenantID, policy, expiryTimeout);
    }
    public static CachePolicy of(int tenantID, int policy, Duration expiryTimeout,String cacheKey){
        return new CachePolicy(tenantID, policy, expiryTimeout, cacheKey);
    }
    public static CachePolicy of(int tenantID, int flushingStartLevel,int flushingStopLevel,Duration flushingDelay, Duration expiryTimeout){
        return new CachePolicy(tenantID, flushingStartLevel,flushingStopLevel,flushingDelay, expiryTimeout);
    }
    public static CachePolicy of(int tenantID, int flushingStartLevel,int flushingStopLevel,Duration flushingDelay, Duration expiryTimeout,String cacheKey){
        return new CachePolicy(tenantID, flushingStartLevel,flushingStopLevel,flushingDelay, expiryTimeout,cacheKey);
    }
}
