package cloud.mmda.core.metadata;

import cloud.mmda.core.utils.CollectionUtil;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 简单元上下文可将元对象{@link MetaObject}和它的关联关系{@link MetaObject#getRelations()}包装为一个上下文，
 * 这样能构建饿加载SQL查询语句，例如你可以构建订单子项和订单主数据的联合查询。
 */
public class SimpleMetaContext implements MetaContext {
    private final MetaObject metaObject;
    private final LinkedHashMap<MetaRelation,MetaObject> relatives;
    private SimpleMetaContext(final MetaObject metaObject, final LinkedHashMap<MetaRelation,MetaObject> relatives) {
        this.metaObject = metaObject;
        this.relatives = CollectionUtil.hasAny(relatives) ? relatives : new LinkedHashMap<>();
    }
    @Override
    public MetaObject get() {
        return metaObject;
    }

    @Override
    public Map<MetaRelation, MetaObject> getRelatives() {
        return relatives;
    }

    public static SimpleMetaContext create(MetaObject metaObject) {
        Objects.requireNonNull(metaObject);
        return new SimpleMetaContext(metaObject, null);
    }
    public static SimpleMetaContext create(MetaObject metaObject, @Nullable LinkedHashMap<MetaRelation,MetaObject> relatives) {
        Objects.requireNonNull(metaObject);
        return new SimpleMetaContext(metaObject, relatives);
    }
}
