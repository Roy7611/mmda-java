package cloud.mmda.core.data.sql;

import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.data.pagination.Paginator;

import java.util.List;


/**
 * SQL语言构建器
 *
 * @author roshion
 *
 * 使用例子{@code 
 * 	from(metaObj).as("a")
 *   .join(metaObj2, INNER_JOIN).as("b", expression)
 *   .join(metaRelation1).as("c")
 *   .where(expression)
 *   .select(colList)
 *   .build();
 * }
 */
public interface SqlBuilder {
	public static final byte PARAMETER_INPUT=1;
	public static final byte PARAMETER_OUTPUT=2;
	public static final byte PARAMETER_INPUT_OUTPUT=3;//输入输出，双向
	

	char getParamPlaceholder();
	String quote(String name);
	
	DbMetadataProvider getMetadataProvider();
	//////////////////////////////////////////////////////////////////////////
	// select
	//////////////////////////////////////////////////////////////////////////	
	/**
	 * 使用例子{@code 
	 * 	from(metaObj).as("a")
	 *   .join(metaObj2, INNER_JOIN).as("b", expression)
	 *   .join(metaRelation1).as("c")
	 *   .where(expression)
	 *   .select(colList)
	 *   .build();
	 * }
	 */
	SqlBuilder from(MetaObject o);
	SqlBuilder as(String alias);
	
	SqlBuilder join(MetaRelation r);
	SqlBuilder join(String joinType, SqlExpression e);
	
	SqlBuilder where(SqlExpression e);
	
	SqlBuilder select(List<MetaCol> cols);
	SqlBuilder select(MetaObject obj);
	SqlBuilder select(MetaObject... objs);
	SqlBuilder select(MetaCol col);
	SqlBuilder select();
	
	//////////////////////////////////////////////////////////////////////////
	// count
	//////////////////////////////////////////////////////////////////////////	
	/**
	 * 使用例子{@code 
	 * 	from(metaObj).as("a")
	 *   .join(metaObj2, INNER_JOIN).as("b", expression)
	 *   .join(metaRelation1).as("c")
	 *   .where(expression)
	 *   .count()
	 *   .build();
	 * }
	 */
	SqlBuilder count();
	SqlBuilder distinctCount();
	
	//////////////////////////////////////////////////////////////////////////
	// insert
	//////////////////////////////////////////////////////////////////////////	
	/**
	 * 使用例子{@code 
	 * 	insert(metaObj)
	 *   .set(expression)
	 *   .where(expression)
	 *   .build();
	 * }
	 */
	SqlBuilder insert(MetaObject o);
	SqlBuilder set(SqlExpression e);
//	SqlBuilder set(List<MetaCol> cols);	
//	SqlBuilder values(Object e);
	
	//////////////////////////////////////////////////////////////////////////
	// update
	//////////////////////////////////////////////////////////////////////////	
	/**
	 * 使用例子{@code 
	 * 	update(metaObj)
	 *   .set(expression)
	 *   .where(expression)
	 *   .build();
	 * }
	 */
	SqlBuilder update(MetaObject o);
	
	//////////////////////////////////////////////////////////////////////////
	// delete
	//////////////////////////////////////////////////////////////////////////	
	SqlBuilder delete(MetaObject o);
	/**
	 * 使用例子{@code 
	 * 	delete(metaObj)
	 *   .where(expression)
	 *   .build();
	 * }
	 */
	//////////////////////////////////////////////////////////////////////////
	// build
	//////////////////////////////////////////////////////////////////////////	
	/**
	 * 构建SQL查询
	 * @return SqlQuery
	 */
	SqlQuery build();
	
	/**
	 * 得知总记录数后，构建最后分页查询语句
	 * 相当于sqlQuery.getSql()+buildPager(pager)
	 * @param sqlQuery 分页查询
	 * @param paginator 分页器
	 * @return 最终的分页查询SQL
	 */
	String build(SqlQuery sqlQuery, Paginator paginator);
	
	/**
	 * 构建分页子句
	 * 你可以连接sqlQuery.getSql()+buildPager(pager)作为最后查询语句
	 * @param paginator 分页器
	 * @return Sql order by a limit 8,9
	 */
	String buildPager(Paginator paginator);
	
	/**
	 * 限制记录数
	 * @param count 记录数
	 * @return SQL子句 limit ?
	 */
	String buildLimit(int count);
	
	/**
	 * 构建调用存储过程的语句，不同的数据库关键词不一样
	 * @param sql
	 * @return
	 */
	String buildProc(String sql);
}
