package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.SqlDateTime;
import cloud.mmda.core.entities.EntityFilterCondition;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.metadata.Module;
import cloud.mmda.core.utils.NameValue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 元数据提供者接口
 * @author roshion
 *
 */
public interface DbMetadataProvider extends MetadataProvider {
	static final String AND = "AND";
	/**
	 * 返回数据库名称前缀，例如mmda_
	 * @return
	 */
	String getDbPrefix();
	String getCurrentDbName();
	/**
	 * 关键字加引号，不同的数据库不一样
	 * @return
	 */
	String quoteName(String name);

	default String quoteValue(String value){
		return "'"+value+"'";
	}
	/**
	 * 根据字段类型确定是否加单引号
	 *
	 * 例如varchar类型字段值 abc => 'abc'
	 * 而数值类型值则直接返回 100
	 * @param col 字段
	 * @param value 字段值的字符串形式
	 * @return
	 */
	default String quoteColValue(MetaCol col, String value){
		if(value==null || value.indexOf("null")!=-1 || value.indexOf("NULL")!=-1) return value;

		boolean q = col.getDataType().hasDatePart() || col.getDataType().isString();
		return q ? quoteValue(value) : value;
	}
	/**
	 * 参数占位符，不同的数据库不一样，如?
	 * @return
	 */
	char getParamPlaceholder();

	/**
	 * 日期时间函数
	 * @return
	 */
	SqlDateTime getSqlDateTime();
	default String getCurrentTimestampFunc(){
		return getSqlDateTime().getCurrentTimestamp();
	}
	/**
	 * 获取元对象，使用默认数据库
	 * @param objName 对象名称
	 * @return MetaObject
	 */
	//MetaObject getMetaObject(String objName);//基础接口已定义

	/**
	 * 获取元对象
	 * @param dbSchema 数据库名称
	 * @param objName 对象名称
	 * @param reload 是否从数据库重新加载
	 * @return
	 */
	MetaObject getMetaObject(String dbSchema, String objName, boolean reload);
	default MetaObject getMetaObject(String dbSchema, String objName){
		return getMetaObject(dbSchema, objName,false);
	}

	/**
	 * 获取元对象映射的表名全称用于跨数据库查询
	 * @param objName 对象名如 Country 或者 sycloud_common.Country
	 * @return 表名全称，比如MySql为sycloud_common.Country，而SqlServer则为sycloud_common.dbo.Country
	 */
	default String getFrom(String objName){
		return quoteName(objName);
	}

	/**
	 * 获取元对象映射的表名全称，用于跨数据库查询
	 * @param metaObj
	 * @return MySql的 `mmda_common`.`Country` 或者 SQL Server 的[mmda_common].[dbo].[Country]
	 */
	default String getFullObjName(MetaObject metaObj){
		return quoteName(metaObj.getDbSchema())+"."+quoteName(metaObj.getObjName());
	}

	/**
	 * 获取元对象简单对象名称，用于跨数据库关联附件和流程追踪记录
	 * @param metaObj
	 * @return 无前缀的数据库和对象名称，例如crm.Partner
	 */
	default String getSimpleObjName(MetaObject metaObj){
		return metaObj.getDbSchema().substring(getDbPrefix().length())
				+"."
				+metaObj.getObjName();
	}

	/**
	 * 获取元数据类型对应的元数据类型类，用于多语言或多种数据库类型映射
	 * @param dataType
	 * @return
	 */
	MetaDataType getMetadataType(Integer dataType);

	/**
	 * 获取元数据类型对应的JDBC数据类型
	 * @param dataType 数据类型
	 * @return java.sql.Types
	 */
	default Integer getJdbcType(Integer dataType){
		return getMetadataType(dataType).getJdbcType();
	}

	/**
	 * 获取元数据库
	 * @param dbSchema 数据库名称
	 * @return
	 */
	MetaDb getMetaDb(String dbSchema);
	/**
	 * 获取元枚举
	 * @param enumClass
	 * @return
	 */
//	MetaEnum getMetaEnum(String enumClass);//已定义在基础接口
	/**
	 * 查找元列，在默认数据库中查找
	 * @param colName 列名称
	 * @param inObjs 对象名称集合
	 * @return MetaCol
	 */
	MetaCol findCol(String colName, Collection<String> inObjs);

	/**
	 * 检查是否有扩展子类
	 * @param dbSchema 数据库名称
	 * @param objName 对象名称
	 * @return
	 */
	boolean hasExtensions(String dbSchema, String objName);

	/**
	 * 行号Sql表达式模板
	 * @param sort
	 * @param partition
	 * @return
	 */
	String toRowNumSql(Sort sort, String partition);

	default String toRowNumSql(Sort sort){
		return toRowNumSql(sort,null);
	}
	/**
	 * 将分页器转换为sql，每种数据库不一样
	 * @param paginator
	 * @return
	 */
	String toPagerSql(Paginator paginator);

	/**
	 * ISNULL函数名称，各种数据库不一样
	 * @return
	 */
	String isNullFuncName();
	/**
	 * 调用存储过程或函数
	 * 不同的数据库语法不一样
	 * @param func 存储过程或函数
	 * @return 返回调用sql
	 */
	String call(String func, int argsNum);

	/**
	 * 查找元对象（分页，不包含子对象）
	 * @param paginator 分页器
	 * @return 元对象列表
	 */
	List<MetaObject> findMetaObjects(Paginator paginator);
	List<MetaObject> findMetaObjects(String dbSchema, Paginator paginator);
	/**
	 * 搜索元对象（分页，不包含子对象）
	 * @param searchText 搜索文本
	 * @param paginator 分页器
	 * @return 元对象列表
	 */
	List<MetaObject> searchMetaObjects(Paginator paginator, String searchText);

	/**
	 * 是否支持命名表分区
	 * @return
	 */
	boolean supportNamedPartition();

	/**
	 * 是否支持ORDER BY FIELD， MySql Only
	 * @return
	 */
	boolean supportOrderByField();
	/**
	 * 获取租户的表分区名称
	 * @param tenantID 租户ID
	 * @return 分区名称，SqlServer不支持
	 */
	default String getPartitionName(int tenantID){
		return supportNamedPartition() ? " PARTITION(p"+tenantID+") " : " ";
	}

	/**
	 * 获取多对一关系列表，用于为其生成loadAll函数
	 * @param dbSchema
	 * @param objName
	 * @return
	 */
	List<MetaRelation> getManyToOneRelations(String dbSchema, String objName);

	/**
	 * 将表关系联接表达式 partnerID=@partnerID and itemID=@itemID 解析为
	 * {
	 *     {"partnerID", "partnerID"}
	 *     {"itemID", "itemID"}
	 * }
	 * @param joinOn
	 * @return
	 */
	default Map<String,String> parseJoinOnToMap(String joinOn){
		Objects.requireNonNull(joinOn);
		return parseJoinOnToList(joinOn).stream()
				.collect(Collectors.toMap(NameValue::getName,NameValue::getValue));
	}
	default List<NameValue<String,String>> parseJoinOnToList(String joinOn){
		Objects.requireNonNull(joinOn);
		ArrayList<NameValue<String,String>> expressions = new ArrayList<>();
		if(joinOn.indexOf(AND)!=-1){
			String[] joinConds = joinOn.split(AND);
			for (String cond : joinConds){
				String[] cols = cond.replaceAll(" ","").split("=@");
				expressions.add(new NameValue(cols[0],cols[1]));
			}
		}
		else if(joinOn.indexOf(AND.toLowerCase())!=-1){
			String[] joinConds = joinOn.split(AND.toLowerCase());
			for (String cond : joinConds){
				String[] cols = cond.replaceAll(" ","").split("=@");
				expressions.add(new NameValue(cols[0],cols[1]));
			}
		}
		else{
			String[] cols = joinOn.replaceAll(" ","").split("=@");
			expressions.add(new NameValue(cols[0],cols[1]));
		}
		return expressions;
	}

	/**
	 * 构建日期字段过滤条件列表
	 * @param dateColName 日期字段名称
	 * @return 查询条件
	 */
	List<EntityFilterCondition> buildDateFilterConditions(String dateColName);

	//region ui
	MetaUi getMetaUi(final String dbSchema,final String objName, final String locale, boolean reload);
	MetaUi getMetaUi(final String objName, final String locale, boolean reload);
	MetaUi getTenancyMetaUi(int tenantID, final String dbSchema, final String objName, String locale, boolean reload);
	Optional<MetaUiField> getMetaUiField(MetaCol col);
	//endregion of ui

	//region module
	List<Module> getTenancyModules(int tenantID, final String locale, final String moduleCode, boolean reload);
	List<Module> getTenancySystemModules(int tenantID, final String locale,  boolean reload);
	Module getTenancyModule(int tenantID, final String locale, final String moduleCode);
	List<Module> getModules(final String systemCode, ModuleType moduleType);
	Module getModule(final String moduleCode, final String locale);
	Module getModule(final MetaObject metaObj, final String locale);
	void clearCacheOfTenancyModules(int tenantID, final String systemCode);
	//endregion of module


	default String escapeSpecialCharacters(String value) {
		Pattern pattern1 = Pattern.compile("['\"\\\\]");
		Matcher matcher1 = pattern1.matcher(value);
		if (matcher1.find()) {
			value = value.replaceAll("(['\"\\\\])", "\\\\$1");
		}
		Pattern pattern2 = Pattern.compile("[%_]");
		Matcher matcher2 = pattern2.matcher(value);
		if (matcher2.find()) {
			value = value.replaceAll("([%_])", "\\\\\\\\$0");
		}
		return value;
	}
}
