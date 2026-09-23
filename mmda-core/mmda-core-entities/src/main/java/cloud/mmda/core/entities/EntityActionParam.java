package cloud.mmda.core.entities;

import cloud.mmda.core.enums.MessageLevel;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实体操作参数
 */
@Data
public class EntityActionParam implements Serializable {
    /**
     * 类型，比如：execute, create, redirect
     */
    final String type;
    /**
     * 提示框：0;NONE;-|1;CONFIRM;确认|2;FLOW_TO;流转
     */
    final String prompt;
    /**
     * 参数值
     */
    final Object value;
    /**
     * 消息级别，用于客户端着色
     */
    final MessageLevel hint;
    public EntityActionParam(final String type, final Object value, final String prompt, final MessageLevel hint) {
        this.type = type;
        this.value = value;
        this.prompt = prompt;
        this.hint = hint;
    }
    public EntityActionParam(final String type, final Object value, final String prompt) {
        this(type,value,prompt,MessageLevel.INFO);
    }

    /**
     * 构建执行操作参数，指示客户端调用doAction
     * @return
     */
    public static final EntityActionParam ofExecute(final String prompt){
        return new EntityActionParam(EntityAction.EXECUTE,null, prompt, MessageLevel.DANGER);
    }
    public static final EntityActionParam ofExecute(final String prompt, MessageLevel hint){
        return new EntityActionParam(EntityAction.EXECUTE,null,prompt,hint);
    }
    /**
     * 构建创建导航参数，用于从上游单据下推创建下游单据
     * @param param 引用参数，例如客户端勾选的多条记录的引用键值集合
     * @return 实体操作参数
     */
    public static final EntityActionParam redirectToCreate(final String module, final String objName, final RefParam param) {
        var to = EntityActionLink.create(module,objName);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("to", to);
        params.put("ref", param);
        return new EntityActionParam(EntityAction.REDIRECT, params, "NONE", MessageLevel.INFO);
    }

    /**
     * 构建导航参数，用于关联单据之间相互导航
     * @param link
     * @return 实体操作参数
     */
    public static final EntityActionParam redirect(final EntityActionLink link) {
        return new EntityActionParam(EntityAction.REDIRECT, link, "NONE", MessageLevel.INFO);
    }

    /**
     * 构建导航参数，用于关联单据之间相互导航
     * @param module 模块，如crm,wms
     * @param objName 对象名称，如Partner，Warehouse
     * @param objId 对象标识，如Partner.partnerID值
     * @return 实体操作参数
     */
    public static final EntityActionParam redirect(final String module, final String objName, long objId) {
        var to = EntityActionLink.details(module,objName,objId);
        return redirect(to);
    }

}
