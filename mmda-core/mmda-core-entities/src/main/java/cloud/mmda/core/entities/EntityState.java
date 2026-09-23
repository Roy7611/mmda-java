package cloud.mmda.core.entities;

/**
 * 实体状态
 *
 * 多行数据提交时需要标识每一行的更改状态
 */
public final class EntityState {
    /**
     * 默认，从数据源获取后未更改
     */
    public static final int DEFAULT = 0;
    /**
     * 已修改
     */
    public static final int MODIFIED = 1;
    /**
     * 客户端新增
     */
    public static final int CREATED = 2;
    /**
     * 客户端新增后，做了修改
     */
    public static final int NEW_MODIFIED = CREATED | MODIFIED;
    /**
     * 客户端已删除
     */
    public static final int DELETED = 4;


    /**
     * 是否被删除，
     * 客户端有可能修改后再删除。
     * @return
     */
    public static boolean isDeleted(int state){
        return (state & DELETED)>0;
    }
    public static boolean isCreated(int state){
        return (state & CREATED)>0;
    }
    public static boolean isModified(int state){
        return !isCreated (state) && ((state & MODIFIED)>0);
    }
    private EntityState(){}
}
