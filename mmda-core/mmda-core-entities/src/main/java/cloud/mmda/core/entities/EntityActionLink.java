package cloud.mmda.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 实体操作链接
 */
@Data @AllArgsConstructor
public class EntityActionLink {
    /**
     * 对象名称，如Partner
     */
    private String objName;
    /**
     * 内部链接，如/CRM/Partners/{id}
     */
    private String deepLink;
    /**
     * 执行操作，如 confirm, create
     */
    private String action;

    public static final EntityActionLink details(final String module, final String objName, long objId) {
        return new EntityActionLink(objName,
                String.join("/", "",module.toUpperCase(),objName,String.valueOf(objId)),
                ""
        );
    }
    public static final EntityActionLink details(final String module, final String objName, long objId,String refParam) {
        return new EntityActionLink(objName,
                String.format("%s?%s",String.join("/", "",module.toUpperCase(),objName,String.valueOf(objId)),refParam),
                ""
        );
    }

    public static final EntityActionLink create(final String module, final String objName) {
        return new EntityActionLink(objName,
                String.join("/", "",module.toUpperCase(),objName),
                EntityAction.CREATE
        );
    }
}
