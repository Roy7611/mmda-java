package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * 引用键
 */
@NoArgsConstructor
@AllArgsConstructor
public class RefKey implements CompositeTenancyKey {
    /**
     * 引用名称
     */
    @NotNull
    @Size(min=1,max=30)
    @Getter @Setter
    protected String refName;
    /**
     * 单据标识
     */
    @NotNull
    @Getter @Setter
    protected long refID;

    /**
     * 获取租户分区ID
     * 实现{@link Tenancy#getPartitionID()}接口
     */
    @JsonIgnore
    @Override
    public long getPartitionID(){
        return refID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefKey refKey)) return false;
        return refID == refKey.refID && Objects.equals(refName, refKey.refName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refName, refID);
    }

    @Override
    public String toString() {
        return join(refName,refID);
    }

    public void parse(String s){
        Objects.requireNonNull(s);
        String[] ks = s.split(CompositeKey.KEY_DELIMITER);
        this.refName = ks[0];
        this.refID = Long.parseLong(ks[1]);
    }

    public static RefKey valueOf(String s){
        Objects.requireNonNull(s);
        String[] ks = s.split(CompositeKey.KEY_DELIMITER);
        return new RefKey(ks[0],Long.parseLong(ks[1]));
    }
}
