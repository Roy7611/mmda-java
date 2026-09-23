package cloud.mmda.core.entities;

import cloud.mmda.core.utils.BaseUtil;
import lombok.Data;

import java.util.List;

/**
 * 实体引用参数，原来的CreateParam，用于客户端勾选多行后提交的引用参数
 * <p>
 *     引用整个实体时只有refName和refID，引用实体明细表记录时refName为主表名称，refID为主表键值，
 *     而refItemKeys存储子表的主键值集合。
 * </p>
 */
@Data
public class RefParam {
    private String refName;
    private Long refID;
    private List<RefItemKey> refItemKeys;

    private RefParam(){}
    private RefParam(String refName, Long refID){
        this.refName = refName;
        this.refID = refID;
    }
    private RefParam(String refName, Long refID, List<RefItemKey> refItemKeys){
        this(refName,refID);
        this.refItemKeys = refItemKeys;
    }

    public boolean isReferring(final String objName){
        BaseUtil.requireNonBlank(objName,"objName");
        return objName.equals(this.refName);
    }
    public boolean isReferring(Class<?> entityClass){
        BaseUtil.requireNonNull(entityClass,"entityClass");
        return entityClass.getClass().getSimpleName().equals(this.refName);
    }

    public static final RefParam NONE = new RefParam();
    public static final RefParam ref(String refName, long refID){
        return new RefParam(refName,refID);
    }
    public static final RefParam ref(String refName, List<RefItemKey> refItemKeys){
        return new RefParam(refName,null, refItemKeys);
    }
}
