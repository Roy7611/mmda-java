package cloud.mmda.core.services;

import cloud.mmda.core.entities.AbstractEntity;
import cloud.mmda.core.Tenancy;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


public class CustomizedCache extends AbstractEntity<String> {
    /**
     * 用户
     */
    @NonNull
    @Setter
    @Getter
    private long userID;


    /**
     * 缓存名称
     */
    @NotBlank
    @Setter
    @Getter
    private String cacheName;

    /**
     * 缓存数据
     */
    @NonNull
    @Setter
    @Getter
    private Object cacheData;

    /**
     * 缓存数据有效期，单位分钟(为null永不过期)
     */
    @Setter
    @Getter
    private Integer timeout;

    @Override
    public String getId() {
        return Tenancy.parseTenantID(userID)+":" +userID+ ":" + cacheName;
    }

    @Override
    public Class<String> getIdClass() {
        return String.class;
    }
}
