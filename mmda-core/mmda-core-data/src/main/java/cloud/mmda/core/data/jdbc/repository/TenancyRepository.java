package cloud.mmda.core.data.jdbc.repository;

import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.entities.EntityFilter;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import cloud.mmda.core.metadata.MetaEnumMember;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.data.pagination.*;
import cloud.mmda.core.data.exceptions.TenancyDataAccessException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 多租户仓储模式泛型接口
 * @param <T> 实体类型
 * @param <K> 主键类型
 */
public interface TenancyRepository<T extends TenancyEntity,K>  extends Repository<T,K> {
    /**
     * 新建一个分区ID
     * @param tenantID 租户ID
     * @return
     */
    long newPartitionID(int tenantID);
    long renewPartitionID(int tenantID, long duplicatedKey);
    /**
     * 新建多个分区ID作为备用
     * @param tenantID 租户ID
     * @param count 数量
     * @return 返回最大的ID，返回值-count+1作为最小ID
     */
    long newPartitionIDs(int tenantID, long count);
    /**
     * 创建一个新实体，设置默认值
     * @param tenantID
     * @return
     */
    default T create(int tenantID){
        T t = create();
        //租户相关数据设置
        t.setPartitionID(newPartitionID(tenantID));
        return t;
    }

    default int parseTenantID(K k){
        return Tenancy.toTenantID(k);
    }
    default void ensureMatchedTenantID(int tenantID, K k){
        if(parseTenantID(k)!=tenantID) throw new TenancyDataAccessException(getTClassName()+"主键" + k + "与租户ID不匹配");
    }
    /**
     * 构建租户分区查询表达式
     * @param tenantID
     * @return
     */
    SqlExpression buildPartitionExpression(int tenantID);
    EntityFilter buildRefFilter(int tenantID, MetaCol col);
    default EntityFilter buildRefFilter(int tenantID, String colName){
        return buildRefFilter(tenantID, col(colName));
    }
    /**
     * 获取租户隔离键ID BETWEEN 限定条件
     * @param tenantID 租户ID
     * @param finalized 为true时直接输出最终字符串，否则使用默认的参数占位符
     * @return 返回字符串 {partitionKey} BETWEEN minID AND maxID
     */
    String getPartitionKeyCondition(int tenantID, boolean finalized);
    default String getPartitionKeyCondition(int tenantID){
        return getPartitionKeyCondition(tenantID,false);
    }
    /**
     * 添加租户隔离条件至expression中
     * 例如：(id between minId and maxId) AND (expression)
     * @param tenantID 租户id
     * @param expression Sql条件表达式
     * @return 多租户版本的查询条件表达式
     */
    default SqlExpression addPartitionCondition(int tenantID, SqlExpression expression){
        return buildPartitionExpression(tenantID).and(expression);
    }
    default String addPartitionCondition(int tenantID, String condition){
        return getPartitionKeyCondition(tenantID)+SqlExpression.AND+"("+condition+")";
    }

    /**
     * 将等式集合转化为SqlExpression表达式
     * @param tenantID 租户id
     * @param attributes 等式集合
     * @return SqlExpression
     */
    SqlExpression buildExpression(int tenantID, Map<String, Object> attributes);

    /**
     * 根据主键k查找一个实体对象
     * @param tenantID 租户id
     * @param k 主键值
     * @return
     */
    T find(int tenantID, K k);

    /**
     * 根据唯一索引键值查找一个实体对象
     * @param tenantID 租户id
     * @param uniqueKey 单据号或者唯一编码
     * @return
     */
    T findByUniqueKey(int tenantID, String uniqueKey);

    /**
     * 租户内唯一键值查重
     * @param id
     * @param uniqueNo
     * @return
     */
    boolean validateUniqueKey(long id, String uniqueNo);
    boolean validateUniqueKey(T t);
    MetaCol getUniqueKeyCol();
    /**
     * 查找第一个符合条件condition等式集合的T实体对象
     * @param tenantID 租户id
     * @param condition 条件表达式，可使用this.expressionBuilder构建
     * @return T实体对象或者null
     */
    T findFirst(int tenantID, SqlExpression condition);
    T findFirst(int tenantID, SqlExpression condition, Sort sort);


    /**
     * 查找第一个符合条件condition等式集合的T实体对象
     * @param tenantID 租户id
     * @param condition 等式集合，可被转化为SqlExpression
     * @return T实体对象或者null
     */
    default T findFirst(int tenantID, Map<String, Object> condition) {
        return findFirst(tenantID, buildExpression(condition));
    }
    default T findFirst(int tenantID, Map<String, Object> condition, Sort sort) {
        return findFirst(tenantID, buildExpression(condition), sort);
    }

    /**
     * 查找第一个符合条件condition等式集合的T实体对象
     * @param tenantID 租户id
     * @param condition 条件子句，其中有几个?就要去提供几个参数值args
     * @param args 参数值数组
     * @return T实体对象或者null
     */
    T findFirst(int tenantID, String condition, Object... args);
    T findFirst(int tenantID, String condition, Sort sort, Object... args);

    /**
     * 查找单个字段值
     * @param tenantID 租户id
     * @param colName 字段名称
     * @param condition 条件表达式，可使用this.expressionBuilder构建
     * @return U类型的单值
     */
     <U> U findScalar(int tenantID, String colName, SqlExpression condition, Class<U> requiredType);

    /**
     * 查找所有T实体对象
     * @param tenantID 租户id
     * @return
     */
    List<T> findAll(int tenantID);
    List<T> findAll(int tenantID, Sort sort);
    /**
     * 查找所有T实体对象
     * @param tenantID 租户id
     * @param paginator 分页器
     * @return 分页后的实体T列表PagedList<T>
     */
    PagedList<T> findAll(int tenantID, Paginator paginator);

    /**
     * 查找所有实体，条件是某个字段值在指定列表中，并可按列表顺序排序
     * @param tenantID 租户id
     * @param field 字段名称
     * @param list 给定的字段值列表，比如 1,2,3，不包含括弧
     * @param ordered 是否按给定的列表返回排序结果，仅MySql支持
     * @return
     */
    List<T> findAllIn(int tenantID, String field, String list, boolean ordered);
    List<T> findAllIn(int tenantID, String field, String list, boolean ordered, String condition, Object... args);
    PagedList<T> findAllNotIn(int tenantID, Paginator paginator, String field, String list);
    PagedList<T> findAllNotIn(int tenantID, Paginator paginator, String field, String list, String condition, Object... args);
    /**
     * 查找符合条件condition的所有实体
     * @param tenantID 租户id
     * @param condition 等式集合
     * @return 实体T列表List<T>
     */
    default List<T> findAllBy(int tenantID, Map<String, Object> condition) {
        return findAllBy(tenantID, buildExpression(condition));
    };

    /**
     * 查找符合条件condition的所有实体
     * @param tenantID 租户id
     * @param condition 条件子句，其中包含几个?就要去提供几个args
     * @param sort 排序字段和升降
     * @param args 参数值数组
     * @return 实体T列表List<T>
     */
    List<T> findAllBy(int tenantID, String condition, Sort sort, Object... args);
    List<T> findAllBy(int tenantID, String condition, Object... args);
    /**
     * 查找符合条件condition的所有实体
     * @param tenantID 租户id
     * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
     * @return 实体T列表List<T>
     */
    List<T> findAllBy(int tenantID, SqlExpression condition, Sort sort);
    List<T> findAllBy(int tenantID, SqlExpression condition);

    /**
     * 查找符合条件condition的所有实体
     * @param tenantID 租户id
     * @param paginator 分页器
     * @param condition 条件子句，其中包含几个?就要去提供几个args
     * @param args 参数值数组
     * @return
     */
    PagedList<T> findAllBy(int tenantID, Paginator paginator, String condition, Object... args);

    /**
     * 查找符合条件condition的所有实体
     * @param tenantID 租户id
     * @param paginator 分页器
     * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
     * @return
     */
    PagedList<T> findAllBy(int tenantID, Paginator paginator, SqlExpression condition);

    default PagedList<T> findAllBy(int tenantID, Paginator paginator, Map<String, Object> condition) {
        return findAllBy(tenantID, paginator, buildExpression(condition));
    };

    /**
     * 载入租户的引用数据
     * @param tenantID
     * @param refRelation
     * @return
     */
    List<MetaEnumMember> findAllRefMap(int tenantID, MetaRelation refRelation);

    /**
     * 查找所有引用关系数据，用于导出Excel
     * @param tenantID
     * @param refRelation
     * @return
     */
    List<Map<String, Object>> findAllRefItems(int tenantID, MetaRelation refRelation);
    /**
     * 模糊搜索，全表搜索
     * @param tenantID 租户id
     * @param paginator
     * @param word 关键字
     * @return
     */
    PagedList<T> searchAll(int tenantID, Paginator paginator, String word);
    PagedList<T> searchAll(int tenantID, Paginator paginator, String word, String condition, Object...args);

    List<T> searchAll(int tenantID, String word, Sort sort);//导出使用
    List<T> searchAll(int tenantID, String word, Sort sort, String condition, Object...args);//导出使用
    /**
     * 过滤
     * @param tenantID
     * @param paginator
     * @param filters 过滤器，通常有索引
     * @param args
     * @return
     */
    default PagedList<T> filterAll(int tenantID, Paginator paginator, List<EntityFilter> filters, Object...args){
        return findAllBy(tenantID, paginator,getFilterCondition(filters),args);
    }
    default List<T> filterAll(int tenantID, List<EntityFilter> filters, Object...args){
        return findAllBy(tenantID, getFilterCondition(filters),args);
    }


    /**
     * 查找主键集
     * @param condition
     * @param args
     * @return
     */
    List<K> findAllKeysBy(int tenantID, String condition, Object...args);
    List<K> findAllKeysBy(int tenantID, SqlExpression condition);
    default List<K> findAllKeysBy(int tenantID, Map<String,Object> condition){
        return findAllKeysBy(tenantID, buildExpression(condition));
    }
    /**
     * 计数
     * @param tenantID 租户id
     * @return
     */
    int count(int tenantID);

    /**
     * 计算符合条件condition的实体T数量
     * @param tenantID 租户id
     * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
     * @return
     */
    int count(int tenantID, SqlExpression condition);

    default int count(int tenantID, Map<String, Object> condition) {
        return count(tenantID, buildExpression(condition));
    };

    /**
     * 计算符合条件condition的实体T数量
     * @param tenantID 租户id
     * @param condition Sql条件子句,其中有几个?就要提供相同数量的参数args
     * @param args 参数列表
     * @return
     */
    int count(int tenantID, String condition, Object... args);
    int count(int tenantID, String condition);
    int count(int tenantID,String word, String condition, Object... args);
    int countBySearchWord(int tenantID,String word);
    /**
     * 判断是否存在主键为k的实体对象
     * @param tenantID 租户id
     * @param k 主键值
     * @return 存在返回true
     */
    boolean exists(int tenantID, K k);

    /**
     * 批量插入多个实体集合
     * @param tenantID 租户id
     * @param tCollection 实体集合
     * @return 返回影响记录数数组
     */
    int[] insertMany(final int tenantID, final Collection<T> tCollection);
    /**
     * 根据主键k更新部分实体T属性集attributes
     * @param tenantID 租户id
     * @param k 主键
     * @param attributes 属性集合
     * @return 返回1表示更新成功，0表示实体不存在
     */
    default int update(int tenantID, K k, Map<String, Object> attributes) {
        return update(tenantID, k, buildExpression(attributes));
    };

    /**
     * 根据主键k更新部分实体T部分属性集attributes
     * @param tenantID 租户id
     * @param k 主键
     * @param attributes 属性集合表达式，即name=value,name1=value1,...
     * @return 返回1表示更新成功，0表示实体不存在
     */
    default int update(int tenantID, K k, SqlExpression attributes){
        ensureMatchedTenantID(tenantID, k);
        return update(k,attributes);
    }
    /**
     * 根据主键k和条件conditions满足时更新部分实体T部分属性集attributes
     * @param tenantID 租户id
     * @param k 主键
     * @param attributes 属性集合表达式，即name=value,name1=value1,...
     * @param conditions 条件表达式
     * @return 返回1表示更新成功，0表示实体不存在或者条件不满足
     */
    default int update(int tenantID, K k, SqlExpression attributes, SqlExpression conditions){
        ensureMatchedTenantID(tenantID, k);
        return update(k,attributes,addPartitionCondition(tenantID,conditions));
    }

    /**
     * 根据单据号或唯一编码更新实体属性
     * @param tenantID 租户标识
     * @param codeNo 单据号或唯一编码
     * @param attributes 要更新的属性集合
     * @return 返回影响的记录数
     */
    int updateByUniqueKey(int tenantID, final String codeNo, SqlExpression attributes);
    int updateByUniqueKey(int tenantID, final String codeNo, SqlExpression attributes, SqlExpression condition);
    /**
     * 更新符合条件conditions部分实体T部分属性集attributes
     * @param tenantID 租户id
     * @param attributes 属性集合
     * @param conditions 条件集合，多个条件为and连接
     * @return 返回影响的记录数
     */
    default int updateAll(int tenantID, Map<String, Object> attributes, Map<String, Object> conditions) {
        return updateAll(buildExpression(attributes), buildExpression(tenantID, conditions));
    };

    /**
     * 更新符合条件conditions部分实体T部分属性集attributes
     * @param tenantID 租户id
     * @param attributes 属性集合表达式，即name=value,name1=value1,...
     * @param conditions 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
     * @return
     */
    default int updateAll(int tenantID, SqlExpression attributes, SqlExpression conditions){
        return updateAll(attributes, addPartitionCondition(tenantID,conditions));
    }


    /**
     * 批量更新多个实体
     * @param tList
     * @return
     */
    int[] updateAll(int tenantID, Collection<T> tList);

    /**
     * 批量部分更新
     * @param tenantID 租户id
     * @param listOfKeyAndAttributes 多个（主键和要更新的属性字典）
     * @param updatedKeys 更新成功的主键列表
     * @return
     */
    int[] updateAll(int tenantID, Collection<Map<String,Object>> listOfKeyAndAttributes, List<K> updatedKeys);
    /**
     * 删除一个主键值为k的实体
     * @param tenantID 租户id
     * @param k 主键值
     * @return 返回1表示更新成功，0表示实体不存在
     */
    int delete(int tenantID, K k);

    /**
     * 删除一个主键值为k且符合条件conditions的实体
     * @param tenantID 租户id
     * @param k 主键值
     * @param conditions 条件表达式
     * @return 返回1表示更新成功，0表示实体不存在或者不符合条件
     */
    int delete(int tenantID, K k, SqlExpression conditions);


    /**
     * 删除符合条件condition的所有实体
     * @param tenantID 租户id
     * @param condition 条件子句，其中包含几个参数?就要提供几个args
     * @param args 参数值数组
     * @return 返回影响的记录数
     */
    int deleteAll(int tenantID, String condition, Object... args);

    /**
     * 批量删除
     * @param tenantID 租户id
     * @param keys 主键集合
     * @param andCondition 额外限制条件
     * @return
     */
    int[] deleteAll(int tenantID, Collection<K> keys,String andCondition);

    /**
     * 删除符合条件condition的所有实体
     * @param tenantID 租户id
     * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
     * @return 返回影响的记录数
     */
    int deleteAll(int tenantID, SqlExpression condition);

    default int deleteAll(int tenantID, Map<String, Object> conditions) {
        return deleteAll(tenantID, buildExpression(conditions));
    };


}
