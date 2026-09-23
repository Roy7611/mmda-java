package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.*;
import cloud.mmda.core.sql.expressions.SqlExp;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.expressions.SqlOp;
import cloud.mmda.core.utils.BaseUtil;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * SQL方言接口定义了基于元数据的SQL语句生成函数，包括：
 * <ul>
 *     <li>{@link #select(MetaObject, List, SqlCriteriaExp, Sort) SELECT} 查询语句</li>
 *     <li>{@link #insert(MetaObject, boolean) INSERT} 插入语句</li>
 *     <li>{@link #delete(MetaObject) DELETE} 删除语句</li>
 *     <li>{@link #update(MetaObject, SqlCriteriaExp, boolean) UPDATE} 更新语句</li>
 * </ul>
 * <p>
 *     复杂的SQL查询语句需要借助{@link SqlQuery}，先使用{@link SqlQuery#create(MetadataProvider)}创建查询，
 *     通过{@link SqlQuery#compile(SqlDialect)}来编译为各种SQL方言。
 * </p>
 * <p>
 *     命令语句返回{@link SqlCmd}，查询语句返回{@link SqlQueryable}，包含参数化可执行SQL语句和参数列表，
 *     编译结果应采用适当的缓存机制优化性能，可提供实例参数值然后交给<code>JdbcTemplate</code>执行。
 * </p>
 * 不同方言的具体实现有：{@link MySqlDialect}, {@link OracleDialect}, {@link SqlServerDialect},
 * {@link PostgreSqlDialect}, {@link KingbaseDialect}, {@link DmDialect}
 *
 */
public interface SqlDialect {
    String LINE_SEPARATOR = System.lineSeparator();
    String T = "t"; //本身别名
    String T_DOT = T + DOT;

    /**
     * 方言名称
     * @return
     */
    String name();

    /**
     * 获取数据库厂商定义的数据类型
     * @param genericDataType 通用数据类型
     * @return 数据库厂商的对应数据类型
     */
    DbDataType getDbDataType(DataType genericDataType);


    //region Quote 标识符
    String quote(String identifier);
    String unquote(String identifier);

    /**
     * 仅仅给字段名称加引号，不会包含模式、对象名
     * @param col 元字段
     * @return 返回加引号的字段名
     */
    default String quote(MetaCol col){
        return quote(col.getColName());
    }

    default String quoteValue(String value) {
        return "'" + value.replace("'", "''") + "'";
    }
    /**
     * 给数据库对象加引号，包括数据库模式
     * @param schema 数据库模式，默认schema和别名不需要引号
     * @param identifier 标识符，加引号
     * @return 例如 base."Partner"
     */
    default String quoteObject(String schema, String identifier){
        return schema + DOT + quote(identifier);
    }

    /**
     * 给元对象加上引号的全名，不同数据库的引用方式不一样。例如
     * MySql 是 mes.`Product`，它的mes是数据库名称
     * 而PostgreSql是 mes."Product"，它的mes是schema
     * @param metaObj 元对象
     * @return
     */
    default String quoteObject(MetaObject metaObj){
        return quoteObject(metaObj.getDbSchema(), metaObj.getObjName());
    }

    default List<String> quote(Map<String,MetaCol> namedCols){
        return namedCols.entrySet().stream()
                .map(e -> quoteObject(e.getKey(), e.getValue().getColName()))
                .toList();
    }

    /**
     * 给字段加引号，包括模式、对象和字段全名
     * @param schema 数据库模式
     * @param objName 对象名称
     * @param colName 字段名称
     * @return 返回 base.`Partner`.`partnerID`
     */
    default String quoteCol(String schema, String objName, String colName){
        //默认schema和别名不需要引号，例如 base."Partner"."partnerID"
        return schema + DOT + quote(objName)+ DOT + quote(colName);
    }

    /**
     * 给字段{@code col}全名加引号，调用{@link #quoteCol(String, String, String)}
     * @param col 元列字段，例如 Partner.partnerID
     * @return 返回 base.`Partner`.`partnerID`
     */
    default String quoteCol(MetaCol col){
        return quoteCol(col.getDbSchema(), col.getObjName(), col.getColName());
    }

    /**
     * 给定元对象上下文{@code context}，给字段{@code col}加引号。用于SQL语句中的字段列表
     * @param col 元列字段，例如 Partner.partnerID, 子表 Contactor.contactorName
     * @param context 元对象上下文
     * @param as 元列别名，为空则不指定别名
     * @return 返回 {@code t.`partnerID` as `pid`, contactors.`contactorName` AS `cname`}
     */
    default String quoteCol(MetaCol col, MetaContext context, String as){
        var objAlias = context.getObjectAlias(col.getObjName());
        var tColName = objAlias + DOT + quote(col.getColName());
        if(BaseUtil.hasText(as) && !as.equalsIgnoreCase(col.getColName())) tColName += AS + quote(as);
        return tColName;
    }

    /**
     * 给定元对象上下文{@code context}，给字段{@code col}加引号。用于SQL语句中的字段列表，不指定别名
     * @param col 元列字段
     * @param context 元对象上下文
     * @return 返回 {@code t.`partnerID`, contactors.`contactorName`}，注意没有 AS 别名
     */
    default String quoteCol(MetaCol col, MetaContext context){
        return quoteCol(col, context, null);
    }

    /**
     * 给所有主键加引号，并用逗号连接
     * @param metaObj 元对象
     * @return 例如 t.`orderID`,t.`itemID`
     */
    default String quoteKeyCols(MetaObject metaObj){
        return metaObj.getKeyCols().stream()
                .map(k -> quoteObject(T,k.getColName()))
                .collect(Collectors.joining(COMMA));
    }

    /**
     * 给元关系的连接表达式中字段加别名、引号
     * <pre>
     *     {@code
     *     //元关系的连接表达式
     *     orderID=@orderID
     *     //转化为
     *     items.`orderID`=t.`orderID`
     *     }
     * </pre>
     * @param metaJoinExp 元关系的连接表达式
     * @param relationName 元关系名称，即关联对象的别名
     * @return 加上别名和引号的SQL连接表达式
     */
    default String quoteJoinExp(final String metaJoinExp, final String relationName){
        return SqlPatterns.META_JOIN_EXP_PATTERN.matcher(metaJoinExp).replaceAll(mr -> {
            if(mr.group().startsWith("@")) return T_DOT + quote(mr.group().substring(1));
            return relationName + DOT + quote(mr.group());
        });
    }

    /**
     * 给元关系的连接表达式中字段加别名、引号，是{@link #quoteJoinExp(String, String)}的快捷方式
     * @param relation 元关系
     * @return 加上别名和引号的SQL连接表达式
     */
    default String quoteJoinExp(MetaRelation relation){
        return quoteJoinExp(relation.getJoinOn(),relation.getRelationName());
    }

    //endregion of quote

    /**
     * 是否支持ORDER BY FIELD， MySql Only
     * @return
     */
    default boolean supportOrderByField(){
        return false;
    }
    /**
     * 是否支持命名表分区
     * @return
     */
    default boolean supportNamedPartition(){
        return true;
    }


    /**
     * 元数据提供者，撰写SQL语句需要加载关联对象
     * @return
     */
    MetadataProvider getMetadataProvider();

    //region From 语句

    /**
     * 构建多表 FROM 语句
     * <pre>
     *     {@code
     *     FROM table0 AS t
     *      INNER JOIN table1 AS b ON t.id = b.id
     *      ...
     *     }
     * </pre>
     * @param context 源上下文，包含主对象和多表关联元关系列表，如果为空则是单表查询，可以是{@link MetaView}
     * @return SQL FROM语句
     */
    String fromClause(MetaContext context);


    //endregion of from

    //region Insert 插入语句
    /**
     * 生成元对象的 INSERT 命令，可进一步编译为{@link java.sql.PreparedStatement}
     * @param metaObj 元对象
     * @param namedParameter 命名参数否，目前仅支持 false
     * @return 返回命令
     */
    SqlCmd insert(MetaObject metaObj, boolean namedParameter);

    //endregion of insert

    //region Select 查询语句

    /**
     * 创建可执行的SQL查询
     * <p>
     *     可执行的SQL查询可加上实际参数交给<code>JdbcTemplate</code>执行并返回结果。
     *     它提供{@link SqlQueryable#pageOf(SqlDialect, int, int)}函数添加分页限制语句作分页查询，
     *     提供{@link SqlQueryable#orderBy(SqlDialect, MetaContext, Sort)}函数改变排序规则克隆出新的可执行SQL查询。
     *     这两个函数都无需重新编译{@link SqlQuery}
     * </p>
     * @param source 查询源的元对象
     * @param withRelations 需要饿加载的关联关系，为空则变为单表查询
     * @param condition 查询条件
     * @param orderBy 排序规则
     * @return 可执行的SQL查询
     */
    SqlQueryable select(final MetaObject source,
                        final @Nullable List<MetaRelation> withRelations,
                        final @Nullable SqlCriteriaExp condition,
                        final @Nullable Sort orderBy);

    /**
     * 创建单表可执行的单表SQL查询
     * @param source 查询源的元对象
     * @param condition 查询条件
     * @param orderBy 排序规则
     * @return 可执行的单表SQL查询
     */
    default SqlQueryable select(final MetaObject source,
                        final @Nullable SqlCriteriaExp condition,
                        final @Nullable Sort orderBy){
        return select(source, null, condition, orderBy);
    }
    /**
     * 创建可执行的无条件全表加载SQL查询
     * @param source 查询源的元对象
     * @param orderBy 排序规则
     * @return 可执行的SQL查询<code>SELECT * FROM table ORDER BY ……</code>
     */
    default SqlQueryable select(final MetaObject source, final @Nullable Sort orderBy){
        return select(source,null,null,orderBy);
    }
    //endregion of select

    //region Update 更新语句

    /**
     * 构建单表所有字段的更新SQL命令
     * @param target 目标元对象
     * @param condition 更新条件
     * @param namedParameter 是否使用命名参数，目前仅支持 false
     * @return SQL更新命令
     */
    default SqlCmd update(MetaObject target, SqlCriteriaExp condition, boolean namedParameter){
        Objects.requireNonNull(target, "target must not be null");
        var assignments = new LinkedHashMap<MetaCol,Object>();
        for(var col : target.getInsertableCols()){
            assignments.put(col, SqlExp.PARAM_VALUE);
        }
        return update(target, assignments, condition, namedParameter);
    }


    /**
     * 创建单表更新命令
     * @param target 更新目标元对象
     * @param assignments 更新字段赋值表达式
     * @param condition 更新条件
     * @param namedParameter 是否使用命名参数
     * @return 可执行的SQL命令
     */
    SqlCmd update(MetaObject target, LinkedHashMap<MetaCol,Object> assignments, SqlCriteriaExp condition, boolean namedParameter);

    /**
     * 创建多表关联更新命令
     * @param context 上下文包括更新目标元对象和关联关系
     * @param assignments 更新字段赋值表达式
     * @param condition 更新条件
     * @param namedParameter 是否使用命名参数
     * @return 可执行的SQL命令
     */
    SqlCmd update(SqlMetaContext context, LinkedHashMap<MetaCol,Object> assignments, SqlCriteriaExp condition, boolean namedParameter);
    //endregion of update

    //region Delete 删除语句
    default StringBuilder delete(MetaObject target){
        return new StringBuilder(DELETE_FROM)
                .append(quoteObject(target));
    }

    /**
     * 创建单表条件删除SQL命令
     * @param target 目标元对象
     * @param condition 删除条件
     * @return 可执行的SQL删除命令
     */
    SqlCmd delete(MetaObject target, SqlCriteriaExp condition);

    /**
     * 创建多表关联的SQL删除命令
     * @param context 上下文包括要删除的目标元对象和关联的源关系
     * @param condition 删除条件
     * @return 可执行的SQL删除命令
     */
    SqlCmd delete(SqlMetaContext context, SqlCriteriaExp condition);

    //endregion of delete

    //region Where 条件表达式

    /**
     * 构建元对象的主键查询条件表达式
     * @param metaObj 元对象
     * @return 返回主键等式，形如 id = ?
     */
    default SqlCriteriaExp keyCriteria(MetaObject metaObj){
        return SqlExp.where(metaObj.getKeyCols(), SqlOp.EQUAL, SqlOp.AND);
    }



//    String whereClause(MetaObject target, SqlCriteriaExp whereExp, List<SqlArg> argList, boolean useContextAlias);
    //endregion of where

    //region Limit & Order by


    default String limitClause(int pageSize, int pageNo){
//        return "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        long offset = (pageNo - 1) * pageSize;
        return String.format(" OFFSET %1$d ROWS FETCH NEXT %2$d ROWS ONLY", offset, pageSize);
    }

    String orderByClause(Sort sort, MetaContext context);

    default String rowNumClause(String orderByClause){
        return String.format(",ROW_NUMBER() OVER (ORDER BY %s) AS rowNum",orderByClause);
    }

//    default SqlQuery.Script orderBy(MetaObject metaObj, final SqlQuery.Script queryable, Sort sort){
//        return queryable.orderBy(this, metaObj, sort);
//    }
//    default String page(final SqlQuery.Script queryable, int pageSize, int pageNo){
//        return queryable.sql() + limit(pageSize, pageNo);
//    }
    //endregion of Limit & Order by


    //region DDL
    //region TABLE 表

    /**
     * 生成元对象的 DROP IF EXISTS语句
     * @param metaObj 元对象
     * @param ifExists 是否加IF EXISTS
     * @return 移除数据库对象的SQL语句
     */
    String drop(MetaObject metaObj, boolean ifExists);

    /**
     * 生成创建表的 CREATE TABLE 语句
     * @param metaObj 元对象
     * @param ifNotExists 是否加 IF EXISTS
     * @param dropIfExists 是否加 DROP IF EXISTS
     * @param withComment 是否生成注释
     * @return {@link StringBuilder}
     */
    StringBuilder createTable(MetaObject metaObj, boolean ifNotExists, boolean dropIfExists, boolean withComment);

    //endregion of TABLE

    //region INDEX & FOREIGN KEY

    /**
     * 移除索引 DROP INDEX IF EXISTS base."IDX_Partner_code_base"
     * @param sb
     * @param index
     * @return
     */
    default StringBuilder dropIndex(StringBuilder sb, MetaIndex index,  boolean dropIfExists){
        return sb.append(DROP_INDEX).append(dropIfExists ? IF_EXISTS : EMPTY)
                .append(quoteObject(index.getDbSchema(),index.getIndexName()));
    }

    /**
     * 创建索引
     * @param sb 字符串构建器用于写入文本
     * @param index 索引
     * @param ifNotExists 仅当索引不存在才创建
     * @param dropIfExists 如果存在先删除索引
     * @return 写入内容后的 sb
     */
    default StringBuilder createIndex(StringBuilder sb, MetaIndex index, boolean ifNotExists, boolean dropIfExists){
        if(dropIfExists) dropIndex(sb, index, true);
        return sb.append(CREATE_INDEX).append(quote(index.getIndexName()))
                .append(ifNotExists? SPACE + IF_NOT_EXISTS : SPACE)
                .append(ON).append(quoteObject(index.getDbSchema(),index.getObjName()))
                .append(PARENTHESES_START)
                .append(index.getCols().stream().map(c -> quote(c.colName())).collect(Collectors.joining(COMMA)))
                .append(PARENTHESES_END);
    }
    /**
     * 创建外键
     * <pre>
     *     {@code
     *     ALTER TABLE `address`
     * 	    ADD CONSTRAINT `FK_address_user` FOREIGN KEY (`ownerID`) REFERENCES `user` (`userID`) ON UPDATE NO ACTION ON DELETE NO ACTION;
     *     }
     * </pre>
     * @param sb
     * @param foreignKey
     * @return
     */
    StringBuilder addForeignKey(StringBuilder sb, MetaForeignKey foreignKey);
    //endregion of INDEX & FOREIGN KEY

    //region VIEW 视图

    /**
     * 从{@code sql}语句解析出元视图（逆向工程）
     * @param sql SQL语句
     * @return 元视图
     */
    MetaView parseView(String sql);

    /**
     * 创建视图
     * @param metaView 元视图
     * @param ifNotExists 生成 IF NOT EXISTS
     * @param dropIfExists 创建前生成 DROP IF EXISTS语句
     * @param withComment 带注释
     * @return 带此视图创建SQL语句的字符串构建器
     */
    StringBuilder createView(MetaView metaView, boolean ifNotExists, boolean dropIfExists, boolean withComment);
    //endregion of VIEW

    //endregion of DDL
}
