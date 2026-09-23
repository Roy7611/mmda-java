package cloud.mmda.core.metadata;

import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.CollectionUtil;
import cloud.mmda.core.utils.MapUtil;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * 元上下文包含一个主元对象和多个关联对象及他们的元关系。每个元对象有自己的别名，是一个构建多表关联查询语句的环境
 *
 * @see MetaObject 元对象本身作为一个上下文，用来构建单表查询
 * @see SimpleMetaContext 包装元对象和需要加载的关联关系，用来构建多表联合查询
 * @see MetaView 你可以代码构建一个元视图，用来构建复杂的多表联合查询
 */
public interface MetaContext {
    /**
     * 主元对象
     * @return
     */
    MetaObject get();

    /**
     * 关联关系对应的关联元对象
     * @return
     */
    Map<MetaRelation,MetaObject> getRelatives();

    default boolean hasRelatives(){
        return MapUtil.hasAny(getRelatives());
    }
    /**
     * 获取元对象在此上下文中的别名
     * @param metaObjName 元对象名称
     * @return 别名
     */
    default String getObjectAlias(final String metaObjName){
        return MetaObject.getObjectAlias(metaObjName, get(), getRelatives().keySet());
    }

    /**
     * 根据名称在此上下文中查找元列
     * @param colName 列名称，例如： orderID, items.orderID
     * @return 元列或者空
     */
    default Optional<MetaCol> findCol(final String colName){
        //如果传入字段名称带表名或表别名
        if(colName.indexOf('.') != -1){
            var names = colName.split("\\.");
            return findCol(names[1], names[0]);
        }

        var col = get().getCol(colName);
        if(col != null) return Optional.of(col);
        if(CollectionUtil.hasAny(getRelatives())){
            for(var rel : getRelatives().values()){
                col = rel.getCol(colName);
                if(col != null) return Optional.of(col);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据列名称和对象名称和别名查找元列
     * @param colName 列名称，不带前缀
     * @param objNameOrAlias 对象名称或者别名，例如 Partner, t
     * @return 元列或者空
     */
    default Optional<MetaCol> findCol(final String colName, @Nullable final String objNameOrAlias){
        //主对象中的列
        if(!BaseUtil.hasText(objNameOrAlias) || objNameOrAlias.equals("t")){
            var col = get().getCol(colName);
            if(col != null) return Optional.of(col);
            return Optional.empty();
        }

        //关联对象中的列
        var r = getRelatives().entrySet().stream()
                .filter(e -> objNameOrAlias.equals(e.getKey().getRelationName()) || objNameOrAlias.equals(e.getKey().getObjName()))
                .findFirst();
        if(r.isPresent()){
            var col = r.get().getValue().getCol(colName);
            if(col != null) return Optional.of(col);
        }
        return Optional.empty();
    }

    /**
     * 根据名称或者对象别名在此上下文中查找元对象
     * @param objNameOrAlias 对象名称或者别名，例如 Partner, contactors
     * @return 元对象或者空
     */
    default Optional<MetaObject> findObject(String objNameOrAlias){
        if(BaseUtil.isNullOrEmpty(objNameOrAlias) || "t".equalsIgnoreCase(objNameOrAlias)) return Optional.of(get());
        var entry = getRelatives().entrySet().stream()
                .filter(e -> objNameOrAlias.equalsIgnoreCase(e.getKey().getRelationName()) || objNameOrAlias.equalsIgnoreCase(e.getKey().getRelativeObjName()))
                .findFirst();
        if(entry.isEmpty()) return Optional.empty();
        return Optional.of(entry.get().getValue());
    }



}
