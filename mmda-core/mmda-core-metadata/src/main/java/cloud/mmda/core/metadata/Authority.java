package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.ModuleAuthScope;
import cloud.mmda.core.utils.BaseUtil;

import java.util.Optional;
import java.util.Set;

/**
 * 功能权限
 */
public interface Authority {
    boolean isAllowRead();
    void setAllowRead(boolean value);

    boolean isAllowCreate();
    void setAllowCreate(boolean value);

    boolean isAllowEdit();
    void setAllowEdit(boolean value);

    boolean isAllowDelete();
    void setAllowDelete(boolean value);
    boolean isAllowPrint();
    void setAllowPrint(boolean value);
    boolean isAllowImport();
    void setAllowImport(boolean value);

    boolean isAllowExport();
    void setAllowExport(boolean value);

    boolean isAllowUpload();
    void setAllowUpload(boolean value);

    ModuleAuthScope getAuthScope();
    void setAuthScope(ModuleAuthScope scope);

    Set<ModuleAction> getAuthorizedActions();
    void setAuthorizedActions(Set<ModuleAction> actions);
    default void addAuthorizedActions(Set<ModuleAction> actions){
        this.getAuthorizedActions().addAll(actions);
    }
    default Optional<ModuleAction> findAuthorizedActionByName(String actionName){
        return getAuthorizedActions().stream()
                .filter(a->a.getActionName().equals(actionName))
                .findFirst();
    }


    String getAuthRule();
    void setAuthRule(String authRule);

    default Authority add(Authority other){
        this.setAllowRead(this.isAllowRead() || other.isAllowRead());
        this.setAllowCreate(this.isAllowCreate() || other.isAllowCreate());
        this.setAllowEdit(this.isAllowEdit() || other.isAllowEdit());
        this.setAllowDelete(this.isAllowDelete() || other.isAllowDelete());
        this.setAllowPrint(this.isAllowPrint() || other.isAllowPrint());
        this.setAllowImport(this.isAllowImport() || other.isAllowImport());
        this.setAllowExport(this.isAllowExport() || other.isAllowExport());
        this.setAllowUpload(this.isAllowUpload() || other.isAllowUpload());
        if(other.getAuthScope().compareTo(this.getAuthScope()) > 0) this.setAuthScope(other.getAuthScope());
        if (BaseUtil.hasAny(other.getAuthorizedActions()))
            this.addAuthorizedActions(other.getAuthorizedActions());
        return this;
    }

    public static Authority NONE = new AuthorityImpl();
    public static Authority READ_ONLY = new AuthorityImpl(true);
}
