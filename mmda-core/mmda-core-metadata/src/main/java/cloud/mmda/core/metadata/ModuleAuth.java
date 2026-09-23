package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.ModuleAllowOp;
import cloud.mmda.core.utils.BaseUtil;
import lombok.Getter;

import java.util.List;

/**
 * 功能模块权限
 */
public class ModuleAuth extends Module {

    /**
     * 功能权限
     */
    @Getter
    private Authority authority;

    public ModuleAuth(){
        this.authority = new AuthorityImpl();
    }
    public ModuleAuth(Module module, boolean createSubModuleAuths){
        this.authority = new AuthorityImpl();

        this.setModuleCode(module.getModuleCode());
        this.setModuleIcon(module.getModuleIcon());
        this.setModuleLabel(module.getModuleLabel());
        this.setShortLabel(module.getShortLabel());
        this.setModuleType(module.getModuleType());
        this.setDbSchema(module.getDbSchema());
        this.setObjName(module.getObjName());
        this.setAllowOps(module.getAllowOps());
        this.setModuleUrl(module.getModuleUrl());
        this.setRequiredCreateParam(module.isRequiredCreateParam());
        this.setDefaultFilter(module.getDefaultFilter());
        this.setDefaultSort(module.getDefaultSort());
        this.setStatus(module.getStatus());
        this.setDescription(module.getDescription());
        this.setActions(module.getActions());

        if(createSubModuleAuths && BaseUtil.hasAny(module.getSubModules())){
            for (Module subModule : module.getSubModules()) {
                this.addSubModule(subModule);
            }
        }
    }

    @Override
    public void addSubModule(Module module) {
        if(module instanceof ModuleAuth) super.addSubModule(module);
        else super.addSubModule(new ModuleAuth(module, true));
    }

    public void addAuthority(Authority auth){
        this.authority.add(auth);
        ComputeAuthority();
    }

    public void addAuthorities(List<? extends Authority> auths){
        for(Authority a : auths){
            this.authority.add(a);
        }
        ComputeAuthority();
    }

    private void ComputeAuthority(){
        if(!getAllowOps().hasFlag(ModuleAllowOp.READ)) this.authority.setAllowRead(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.EDIT)) this.authority.setAllowEdit(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.CREATE)) this.authority.setAllowCreate(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.DELETE)) this.authority.setAllowDelete(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.PRINT)) this.authority.setAllowPrint(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.IMPORT)) this.authority.setAllowImport(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.EXPORT)) this.authority.setAllowExport(false);
        if(!getAllowOps().hasFlag(ModuleAllowOp.UPLOAD)) this.authority.setAllowUpload(false);
    }
}
