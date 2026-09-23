package cloud.mmda.core.data.jdbc.repository;




import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.data.sql.SqlQuery;
import cloud.mmda.core.entities.EntityFilter;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.data.pagination.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仓储模式泛型接口
 * 需元数据的支持
 * @author roshion
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 */
public interface Repository<T, K> {
	DbMetadataProvider getMetadataProvider();
	/**
	 * 获取元对象
	 * @return
	 */
	MetaObject getMetaObject();
	MetaObjectAccess<T,K> getMetaObjectAccess();
	MetaObject getMetaObject(String objName);
	MetaObject getMetaObject(String dbSchema,String objName);

	Class<T> getTClass();
	Class<K> getKClass();

	String getTClassName();

	/**
	 * 获取元字段
	 * @param colName 字段名称
	 * @return
	 */
	MetaCol col(String colName);

	/**
	 * 构建布尔过滤器
	 * @param col 字段
	 * @param trueLabel true时显示文本，默认为是
	 * @param falseLabel false时显示文本，默认为否
	 * @return
	 */
	EntityFilter buildBoolFilter(MetaCol col, String trueLabel, String falseLabel);
	EntityFilter buildBoolFilter(MetaCol col);
	default EntityFilter buildBoolFilter(String colName, String trueLabel, String falseLabel){
		return buildBoolFilter(col(colName),trueLabel,falseLabel);
	}
	default EntityFilter buildBoolFilter(String colName){
		return buildBoolFilter(col(colName));
	}

	EntityFilter buildEnumFilter(MetaCol col);
	default EntityFilter buildEnumFilter(String colName){
		return buildEnumFilter(col(colName));
	}
	EntityFilter buildDateFilter(MetaCol col);
	default EntityFilter buildDateFilter(String colName){
		return buildDateFilter(col(colName));
	}
	EntityFilter buildRefFilter(MetaCol col);
	default EntityFilter buildRefFilter(String colName){
		return buildRefFilter(col(colName));
	}

	/**
	 * 根据属性名称获取实体属性
	 * @param t 实体对象
	 * @param propName 属性名称
	 * @return 属性值
	 */
	Object getPropertyValue(T t, String propName);
	void setPropertyValue(T t, String propName, Object v);

	Object getColValue(T t, MetaCol col);
	void setColValue(final T t, MetaCol col, Object v);

	Object getColText(T t, MetaCol col) throws IllegalAccessException, InvocationTargetException;
	Object getColText(T t, String colName) throws InvocationTargetException, IllegalAccessException;

	/**
	 * 获取唯一键值
	 * @param t 实体对象
	 * @return
	 */
	Object getUniqueKeyValue(final T t);
	void setUniqueKeyValue(final T t, final Object v);
	/**
	 * 获取关联对象属性
	 * @param t
	 * @param rel 关联关系
	 * @return
	 */
	Object getRelativeValue(T t, MetaRelation rel);
	void setRelativeValue(T t, MetaRelation rel, Object v);

	/**
	 * 组装枚举属性的显示文本
	 *
	 * 共给客户端方便显示
	 * @param t
	 * @return
	 */
	T assembleEnumProperties(T t);

	/**
	 * 创建实体，使用默认值
	 * @return
	 */
	T create();

	/**
	 * 克隆实体，拷贝属性值
	 * @param source 源实体
	 * @param includeRelation 是否包含HAS_ONE/HAS_MANY关联对象
	 * @return 返回新的实体对象
	 */
	T clone(T source, boolean includeRelation);
	default T clone(T source){
		return clone(source,false);
	}

	/**
	 * 从别的实体拷贝同名属性
	 * @param fromMetaObj 从元对象
	 * @param from 源数据对象
	 * @param to 目的实体
	 * @param includeRelation
	 * @return
	 */
	void copy(MetaObject fromMetaObj, Object from, T to, boolean includeRelation);
	default void copy(MetaObject fromMetaObj, Object from, T to){
		copy(fromMetaObj,from, to,false);
	}
	/**
	 * 将等式集合转化为SqlExpression表达式
	 * @param attributes 等式集合
	 * @return SqlExpression
	 */
	SqlExpression buildExpression(Map<String, Object> attributes);

	/**
	 * 将实体属性集合转化为SqlExpression表达式
	 * @param t 实体对象
	 * @return SqlExpression
	 */
	SqlExpression buildExpression(T t);

	SqlExpression.SqlExpressionBuilder expressionBuilder();
	/**
	 * 构建条件语句，findAllBy函数使用
	 * @param condition 条件表达式
	 * @return
	 */
	String buildCondition(SqlExpression condition, char placeholder);

	Sort getDefaultSort();
//	K findKey(SqlExpression condition);

	/**
	 * 插入一个实体对象
	 * @param t 实体对象
	 * @return 返回成功否
	 */
	int insert(T t);
	default T insertAndGet(T t){
		insert(t);
		return t;
	}

	/**
	 * 插入多个实体集合，如果没用自增字段采用批量方法，否则逐个调用insert。
	 * 不论怎样都会调用beforeInsert计算Computable实体
	 * @param tCollection 实体集合
	 * @return
	 */
	int[] insertMany(Collection<T> tCollection);






	/**
	 * 根据主键k查找一个实体对象
	 * @param k 主键值
	 * @return 实体T
	 */
	T find(K k);

	/**
	 * 根据唯一索引字段值查找一个实体对象
	 * @param codeNo 单据号或唯一编码
	 * @return
	 */
	T findByUniqueKey(String codeNo);

	/**
	 * 查找第一个符合条件condition等式集合的T实体对象
	 * @param condition 等式集合，可被转化为SqlExpression
	 * @return T实体对象或者null
	 */
	default T findFirst(Map<String, Object> condition) {
		return findFirst(buildExpression(condition));
	};
	default T findFirst(Map<String, Object> condition, Sort sort) {
		return findFirst(buildExpression(condition),sort);
	};
	/**
	 * 查找第一个符合条件condition等式集合的T实体对象
	 * @param condition 条件子句，其中有几个?就要去提供几个参数值args
	 * @param args 参数值数组
	 * @return T实体对象或者null
	 */
	T findFirst(String condition, Object... args);
	T findFirst(String condition, Sort sort, Object... args);

	/**
	 * 查找第一个符合条件condition等式集合的T实体对象
	 * @param condition 条件表达式，可使用this.expressionBuilder构建
	 * @return T实体对象或者null
	 */
	T findFirst(SqlExpression condition);
	T findFirst(SqlExpression condition, Sort sort);

	/**
	 * 查找单个字段值
	 * @param colName 字段名称
	 * @param condition 条件表达式，可使用this.expressionBuilder构建
	 * @return U类型的单值
	 */
	<U> U findScalar(String colName, SqlExpression condition, Class<U> requiredType);
	/**
	 * 查找所有T实体对象
	 * @return
	 */
	List<T> findAll();
	List<T> findAll(Sort sort);

	/**
	 * 查找所有实体，条件是某个字段值在指定列表中，并可按列表顺序排序
	 * @param field
	 * @param list
	 * @param ordered
	 * @return
	 */
	List<T> findAllIn(String field, String list, boolean ordered);
	List<T> findAllIn(String field, String list, boolean ordered, String condition, Object... args);
	PagedList<T> findAllNotIn(Paginator paginator, String field, String list);
	PagedList<T> findAllNotIn(Paginator paginator, String field, String list, String condition, Object... args);
	/**
	 * 查找所有T实体对象
	 * @param paginator 分页器
	 * @return 分页后的实体T列表PagedList<T>
	 */
	PagedList<T> findAll(Paginator paginator);

	/**
	 * 查找符合条件condition的所有实体
	 * @param condition 等式集合
	 * @return 实体T列表List<T>
	 */
	default List<T> findAllBy(Map<String, Object> condition) {
		return findAllBy(buildExpression(condition));
	};
//	default List<T> findAllBy(Map<String, Object> condition, Sort sort) {
//		return findAllBy(buildExpression(condition),sort);
//	};
	/**
	 * 查找符合条件condition的所有实体
	 * @param condition 条件子句，其中包含几个?就要去提供几个args
	 * @param sort 排序字段和升降
	 * @param args 参数值数组
	 * @return 实体T列表List<T>
	 */
	List<T> findAllBy(String condition, Sort sort, Object... args);
	default List<T> findAllBy(String condition, Object... args){
		return findAllBy(condition,null,args);
	};
	/**
	 * 查找符合条件condition的所有实体
	 * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
	 * @return 实体T列表List<T>
	 */
	List<T> findAllBy(SqlExpression condition, Sort sort);
	List<T> findAllBy(SqlExpression condition);

	/**
	 * 查找符合条件condition的所有实体
	 * @param paginator 分页器
	 * @param condition 等式集合
	 * @return 分页后的实体T列表PagedList<T>
	 */
	default PagedList<T> findAllBy(Paginator paginator, Map<String, Object> condition) {
		return findAllBy(paginator, buildExpression(condition));
	};

	/**
	 * 查找符合条件condition的所有实体
	 * @param paginator 分页器
	 * @param condition 条件子句，其中包含几个?就要去提供几个args
	 * @param args 参数值数组
	 * @return
	 */
	PagedList<T> findAllBy(Paginator paginator, String condition, Object... args);

	/**
	 * 查找符合条件condition的所有实体
	 * @param paginator 分页器
	 * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
	 * @return
	 */
	PagedList<T> findAllBy(Paginator paginator, SqlExpression condition);

	/**
	 * 载入引用数据
	 * @param refRelation
	 * @return
	 */
	List<MetaEnumMember> findAllRefMap(MetaRelation refRelation);
	String findRefTextByValue(MetaRelation refRelation, Object value);

	/**
	 * 查找所有引用关系数据，用于导出Excel
	 * @param refRelation
	 * @return
	 */
	List<Map<String, Object>> findAllRefItems(MetaRelation refRelation);

	/**
	 * 模糊搜索，全表搜索
	 * @param paginator
	 * @param word 关键字
	 * @return
	 */
	PagedList<T> searchAll(Paginator paginator, String word);
	PagedList<T> searchAll(Paginator paginator, String word, String condition, Object...args);

	List<T> searchAll(String word, Sort sort);//导出使用
	List<T> searchAll(String word, Sort sort, String condition, Object...args);//导出使用

	default String getFilterCondition(List<EntityFilter> filters){
		return filters.stream()
				.map(f->"("+f.getActiveCondition()+")")
				.collect(Collectors.joining(SqlExpression.AND));
	}


	/**
	 * 过滤
	 * @param paginator
	 * @param filters
	 * @param args
	 * @return
	 */
	default PagedList<T> filterAll(Paginator paginator, List<EntityFilter> filters, Object...args){
		return findAllBy(paginator,getFilterCondition(filters),args);
	}
	default List<T> filterAll(List<EntityFilter> filters, Object...args){
		return findAllBy(getFilterCondition(filters),args);
	}

	/**
	 * 查找主键集
	 * @param condition
	 * @param args
	 * @return
	 */
	List<K> findAllKeysBy(String condition, Object...args);
	List<K> findAllKeysBy(SqlExpression condition);
	default List<K> findAllKeysBy(Map<String,Object> condition){
		return findAllKeysBy(buildExpression(condition));
	}

	/**
	 * 计数
	 * @return
	 */
	int count();

	/**
	 * 计算符合条件condition的实体T数量
	 * @param condition
	 * @return
	 */
	default int count(Map<String, Object> condition) {
		return count(buildExpression(condition));
	};

	/**
	 * 计算符合条件condition的实体T数量
	 * @param condition Sql条件子句,其中有几个?就要提供相同数量的参数args
	 * @param args 参数列表
	 * @return
	 */
	int count(String condition, Object... args);


	int count(String word,String condition, Object... args);

	int countBySearchWord(String word);
	/**
	 * 计算符合条件condition的实体T数量
	 * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
	 * @return
	 */
	int count(SqlExpression condition);

	/**
	 * 判断是否存在主键为k的实体对象
	 * @param k 主键值
	 * @return 存在返回true
	 */
	boolean exists(K k);

	int execute(SqlQuery sqlQuery, List<Object> args);
	/**
	 * 更新一个实体，包括所有属性
	 * @param t 实体T对象
	 * @return 返回1表示更新成功，0表示实体不存在
	 */
	int update(T t);
	default T updateAndGet(T t){
		update(t);
		return t;
	}

	/**
	 * 根据主键k更新部分实体T部分属性集attributes
	 * @param k 主键
	 * @param attributes 属性集合表达式，即name=value,name1=value1,...
	 * @return 返回1表示更新成功，0表示实体不存在
	 */
	int update(K k, SqlExpression attributes);
	default int update(K k, Map<String,Object> attributes){
		return update(k, buildExpression(attributes));
	}
	/**
	 * 根据主键k和条件conditions满足时更新部分实体T部分属性集attributes
	 * @param k 主键
	 * @param attributes 属性集合表达式，即name=value,name1=value1,...
	 * @param conditions 条件表达式
	 * @return 返回1表示更新成功，0表示实体不存在或者条件不满足
	 */
	int update(K k, SqlExpression attributes, SqlExpression conditions);
	default int update(K k, Map<String, Object> attribures, Map<String, Object> conditions){
		return update(k, buildExpression(attribures), buildExpression(conditions));
	}

	/**
	 * 根据唯一编码更新实体
	 * @param codeNo 实体或单据的唯一编码
	 * @param attributes 更新属性集合表达式
	 * @return
	 */
	int updateByUniqueKey(String codeNo, SqlExpression attributes);
	int updateByUniqueKey(String codeNo, SqlExpression attributes, SqlExpression condition);
	/**
	 * 更新符合条件conditions部分实体T部分属性集attributes
	 * @param attributes 属性集合
	 * @param conditions 条件集合，多个条件为and连接
	 * @return 返回影响的记录数
	 */
	default int updateAll(Map<String, Object> attributes, Map<String, Object> conditions) {
		return updateAll(buildExpression(attributes), buildExpression(conditions));
	};

	/**
	 * 更新符合条件conditions部分实体T部分属性集attributes
	 * @param attributes 属性集合表达式，即name=value,name1=value1,...
	 * @param conditions 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
	 * @return
	 */
	int updateAll(SqlExpression attributes, SqlExpression conditions);

	/**
	 * 批量更新多个实体
	 * @param tList
	 * @return
	 */
	int[] updateAll(Collection<T> tList);

	/**
	 * 批量更新多个实体的部分属性
	 * @param listOfKeyAndAttributes 对象属性列表
	 * @param updatedKeys 更新成功的主键集合
	 * @return
	 */
	int[] updateAll(Collection<Map<String,Object>> listOfKeyAndAttributes, List<K> updatedKeys);
	/**
	 * 删除一个主键值为k的实体
	 * @param k 主键值
	 * @return 返回1表示更新成功，0表示实体不存在
	 */
	int delete(K k);
	/**
	 * 删除一个主键值为k且符合条件conditions的实体
	 * @param k 主键值
	 * @param conditions 条件表达式
	 * @return 返回1表示更新成功，0表示实体不存在或者不符合条件
	 */
	int delete(K k, SqlExpression conditions);
	/**
	 * 删除符合条件conditions的所有实体
	 * @param conditions 条件集合，多个条件为and连接
	 * @return 返回影响的记录数
	 */
	default int deleteAll(Map<String, Object> conditions) {
		return deleteAll(buildExpression(conditions));
	};

	/**
	 * 删除符合条件condition的所有实体
	 * @param condition 条件子句，其中包含几个参数?就要提供几个args
	 * @param args 参数值数组
	 * @return 返回影响的记录数
	 */
	int deleteAll(String condition, Object... args);

	/**
	 * 删除符合条件condition的所有实体
	 * @param condition 条件表达式，可使用this.expressionBuilder.exp(colName).equal(value).and...构建
	 * @return 返回影响的记录数
	 */
	int deleteAll(SqlExpression condition);

	/**
	 * 批量删除
	 * @param keys 主键集合
	 * @param andCondition 额外限制条件
	 * @return
	 */
	int[] deleteAll(Collection<K> keys,String andCondition);
	/**
	 * 根据实体状态保存数据
	 *
	 * 可能插入、更新或删除，成功后实体状态恢复默认0
	 * @param t
	 * @return
	 */
	T save(T t);

	/**
	 * 执行sql语句，并返回影响记录数
	 * @param sql 查询语句
	 * @param args 参数数组
	 * @return 影响的记录数
	 */
	int execute(String sql, Object... args);
	/**
	 * 执行sql语句，并返回实体结果集
	 * @param sql 查询语句
	 * @param args 参数数组
	 * @return 实体列表
	 */
	List<T> executeForList(String sql, Object... args);
	T executeForObject(String sql, Object... args);

	void call(String sql, Object... args);

	/**
	 * 获取租户相关的元界面数据
	 * @param tenantID 租户标识
	 * @param locale 语言区域，如en,zh,zh-Hant
	 * @param reload 是否从数据库载入，然后更新缓存，相当于重新载入
	 * @return
	 */
	MetaUi getMetaUi(int tenantID, String locale, boolean reload);

}
