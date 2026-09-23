package cloud.mmda.core.metadata;


import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.utils.CollectionUtil;

import java.util.*;

/**
 * 元数据提供者接口
 */
public interface MetadataProvider {
    /**
     * 查找元对象（分页，不包含子对象）
     * @param pager 分页器
     * @return 元对象列表
     */
    List<MetaObject> findMetaObjects(Paginator pager);
    List<MetaObject> findMetaObjects(String dbSchema, Paginator pager);
    /**
     * 获取元数据类型对应的元数据类型类，用于多语言或多种数据库类型映射
     * @param dataType
     * @return
     */
    @Deprecated
    MetaDataType getMetadataType(Integer dataType);

    Module getModule(String moduleCode, final String locale);
    Module getModule(MetaObject metaObj, final String locale);
    List<Module> getModules(final String systemCode, ModuleType moduleType);

    /**
     * 凭实体对象类{@code objClass}获取元对象{@link MetaObject}，
     * 实际上是根据命名空间和类名获取
     * @param objClass 对象类
     * @return 元对象
     * @param <T> 实体类型
     */
    <T> MetaObject getMetaObject(Class<T> objClass);

    /**
     * 根据对象名称获取元对象，建议不要在多个数据库中有重名。
     * 如果有重名，请务必使用{@link #getMetaObject(String, String)}指定数据库模式
     * @param objName 对象名称可以是表名或者视图名称
     * @return 元对象
     */
    MetaObject getMetaObject(String objName);

    /**
     * 根据对象全称获取元对象，对象全称类似{@code mes.ProductionOrder}
     * @param dbSchema 数据库模式
     * @param objName 对象名称
     * @return 元对象
     */
    MetaObject getMetaObject(String dbSchema, String objName);

    /**
     * 凭
     * @param relation 元关系
     * @return
     */
    default MetaObject getRelativeMetaObject(MetaRelation relation){
        return getMetaObject(relation.getDbSchema(), relation.getObjName());
    }


    /**
     * 将一个元对象构建为元上下文，包含所有的关联元对象
     * @param metaObj 元上下文
     * @return 如果没有关联关系，返回本身，否则为包含关系后的{@link SimpleMetaContext}
     */
    default MetaContext buildMetaContext(MetaObject metaObj){
        if(metaObj.hasRelations()){
            var relatives = new LinkedHashMap<MetaRelation, MetaObject>();
            metaObj.getRelations().forEach(relation -> {
                if(!relation.hasOneOrMany()) return;
                var relMetaObj = getRelativeMetaObject(relation);
                relatives.put(relation, relMetaObj);
            });
            if(CollectionUtil.hasAny(relatives)) return SimpleMetaContext.create(metaObj, relatives);
        }
        return metaObj;
    }
    /**
     * 获取元枚举
     * @param enumClassName 元枚举类名称
     * @return
     */
    MetaEnum getMetaEnum(String enumClassName);
    default <E extends Enum<E>> MetaEnum getMetaEnum(Class<E> enumClass){
        return getMetaEnum(enumClass.getSimpleName());
    }

    List<MetaEnum> getMetaEnums();
    /**
     * 获取元对象的元索引
     * @param metaObject 元对象
     * @return 索引集合
     */
    Collection<MetaIndex> getIndexes(MetaObject metaObject);
    /**
     * 获取数据库模式的所有元外键
     * @param dbSchema 数据库模式
     * @return 元外键集合
     */
    Collection<MetaForeignKey> getForeignKeys(String dbSchema);
    Collection<MetaForeignKey> getForeignKeys(final MetaObject metaObject);

    //region ui
    MetaUi getMetaUi(final String dbName,final String objName, final String locale, boolean reload);
    MetaUi getMetaUi(final String objName, final String locale, boolean reload);
    MetaUi getTenancyMetaUi(int tenantID, final String dbName, final String objName, String locale, boolean reload);
    Optional<MetaUiField> getMetaUiField(MetaCol col);
    //endregion of ui

    /**
     * 获取多对一关系列表，用于为其生成loadAll函数
     * @param dbName
     * @param objName
     * @return
     */
    List<MetaRelation> getManyToOneRelations(String dbName, String objName);

    /**
     * 检查是否有扩展子类
     * @param dbName 数据库名称
     * @param objName 对象名称
     * @return
     */
    boolean hasExtensions(String dbName, String objName);


}
