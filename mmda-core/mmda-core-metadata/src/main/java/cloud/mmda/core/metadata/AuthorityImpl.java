package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.ModuleAuthScope;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class AuthorityImpl implements Authority {

    /**
     * 读取
     */
    @NotNull
    @Getter
    @Setter
    private boolean allowRead;
    /**
     * 创建
     */
    @NotNull
    @Getter @Setter
    private boolean allowCreate;
    /**
     * 编辑
     */
    @NotNull
    @Getter @Setter
    private boolean allowEdit;
    /**
     * 删除
     */
    @NotNull
    @Getter @Setter
    private boolean allowDelete;
    /**
     * 打印
     */
    @NotNull
    @Getter @Setter
    private boolean allowPrint;
    /**
     * 导入
     */
    @NotNull
    @Getter @Setter
    private boolean allowImport;
    /**
     * 导出
     */
    @NotNull
    @Getter @Setter
    private boolean allowExport;

    /**
     * 上传模板
     */
    @NotNull
    @Getter @Setter
    private boolean allowUpload;
    /**
     * 权限范围：0;SELF;本人|1;GROUP;组|2;DEPARTMENT;部门|4;DIVISION;子公司|8;ALL;全局
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private ModuleAuthScope authScope;

    /**
     * 权限操作
     */
    @Getter @Setter
    @Valid
    @NotEmpty
    private Set<ModuleAction> authorizedActions;

    /**
     * 授权规则
     */
    @Size(max=255)
    @Getter @Setter
    private String authRule;


    public AuthorityImpl(){
        this.authScope = ModuleAuthScope.SELF;
        this.authorizedActions = new HashSet<ModuleAction>();
    }

    public AuthorityImpl(boolean allowRead){
        if (allowRead)
            this.allowRead = true;
        this.authScope = ModuleAuthScope.ALL;
        this.authorizedActions = new HashSet<ModuleAction>();
    }
}
