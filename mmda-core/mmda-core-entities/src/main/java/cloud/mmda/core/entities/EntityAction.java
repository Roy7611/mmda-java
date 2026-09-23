package cloud.mmda.core.entities;

import cloud.mmda.core.enums.MessageLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体操作组装在实体对象的actions列表中，用于客户端渲染操作按钮。
 * <p>元数据是 ModuleAction，定义了模块应具备的功能操作</p>
 */
@Data
public class EntityAction implements Serializable {
    private static final String NO_PROMPT = "NONE";
    //region 标准操作
    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String DELETE = "delete";
    public static final String SEARCH = "search";
    public static final String PRINT = "print";
    public static final String IMPORT = "import";
    public static final String EXPORT = "export";

    public static final String EXECUTE = "execute";
    public static final String REDIRECT = "redirect";
    //endregion

    final String name;
    final String label;
    final String icon;
    final String group;
    final String description;

    /**
     * 需要传递的参数，例如创建、导航
     */
    EntityActionParam param;
    private EntityAction(final String name, final String label, final String icon,final String group, final String description, final EntityActionParam param) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.group = group;
        this.description = description;
        this.param = param;
    }
    private EntityAction(final String name, final String label, final String icon, final String description, final EntityActionParam param) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.group = null;
        this.description = description;
        this.param = param;
    }
    public EntityAction withParam(EntityActionParam param){
        this.param = param;
        return this;
    }
    public EntityAction withDefaultParam(){
        this.param = EntityActionParam.ofExecute(NO_PROMPT);
        return this;
    }

    public static EntityAction of(final String name, final String label){
        return new EntityAction(name,label,name,null, EntityActionParam.ofExecute(NO_PROMPT));
    }
    public static EntityAction of(final String name, final String label, final String icon){
        return new EntityAction(name,label,icon,null, EntityActionParam.ofExecute(NO_PROMPT));
    }
    public static EntityAction of(final String name, final String label, final String icon, final String description){
        return new EntityAction(name,label,icon,description, EntityActionParam.ofExecute(NO_PROMPT));
    }
    public static EntityAction of(final String name, final String label, final String icon, final String description, MessageLevel hint){
        return new EntityAction(name,label,icon,description, EntityActionParam.ofExecute(NO_PROMPT, hint));
    }
    /**
     * 重定向动作
     * @param name 名称
     * @param label 显示文本
     * @param redirectTo 路由格式：objName:moduleUrl:action 便于客户端识别模块路由，例如"DeliveryOrder:/WMS/DeliveryOrders:create"
     * @return
     */
    public static EntityAction redirect(final String name, final String label, final EntityActionLink redirectTo){
        return new EntityAction(name,label,REDIRECT,null, EntityActionParam.redirect(redirectTo));
    }
    public static EntityAction redirectToCreate(String name, String label, String  description, final String module, final String objName, final RefParam param){
        return new EntityAction(name,label,CREATE,description, EntityActionParam.redirectToCreate(module,objName,param));
    }
    public static EntityAction redirectToCreate(String name, String label,String group, String description, final String module, final String objName, final RefParam param) {
        return new EntityAction(name, label, CREATE, group, description, EntityActionParam.redirectToCreate(module, objName, param));
    }
}
