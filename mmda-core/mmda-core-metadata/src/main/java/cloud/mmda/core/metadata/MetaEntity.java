package cloud.mmda.core.metadata;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

public abstract class MetaEntity {
    public static final String COMPOSITE_KEY_SEPARATOR = ",";//复合主键字符串分割

    /**
     * 行号，从数据库中取出时的顺序
     */
    @Getter
    @Setter
    private long rowNum;


    /**
     * 实体状态，指在应用中的临时修改状态，用于控制更新数据库时的行为
     */
    @Getter @Setter
    private int entityState;

    protected <T> boolean hasAny(Collection<T> tCollection){
        return tCollection!=null && !tCollection.isEmpty();
    }
}
