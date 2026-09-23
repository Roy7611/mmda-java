/**
 * 
 */
package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.MySqlDateTime;
import cloud.mmda.core.data.sql.SqlDateTime;
import cloud.mmda.core.enums.MetaForeignKeyAction;
import cloud.mmda.core.metadata.MetaForeignKey;
import cloud.mmda.core.metadata.MetaIndex;
import cloud.mmda.core.metadata.MetaIndexCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * MySql元数据提供者
 * @author roshion
 *
 */
@Component
public class MySqlMetadataProvider extends SqlMetadataProvider {
	private final static char Q = '`';//关键字引号

	private static final MySqlDateTime sqlDateTime = new MySqlDateTime();
	@Autowired
	public MySqlMetadataProvider(DataSource ds, String defaultDbName, String metadataDbName, RedisConnectionFactory factory){
		super(ds,defaultDbName,metadataDbName, factory);
	}


	@Override
	public final String quoteName(String name){
		return Q+name+Q;
	}
	@Override
	public final String getFrom(String objName){
		if(objName.indexOf(".")!=-1){
			String[] obj = objName.split("\\.");
			return quoteName(obj[0]) + "." + quoteName(obj[1]);
		}
		return super.getFrom(objName);
	}

	@Override
	public final String isNullFuncName(){
		return "IFNULL";
	}
	/**
	 * 参数占位符
	 * @return
	 */
	@Override
	public final char getParamPlaceholder(){
		return '?';
	}

	@Override
	public final SqlDateTime getSqlDateTime(){ return sqlDateTime;}

	@Override
	public final String call(String func, int argsNum){
		return "call " + func+"(" + BaseUtil.repeat("?",argsNum,",") + ")";
	}

	@Override
	public final boolean supportNamedPartition(){
		return true;
	}
	@Override
	public final boolean supportOrderByField(){
		return true;
	}
	@Override
	public String toRowNumSql(Sort sort, String partition){
		//8.0才支持ROW_NUMBER()=> return super.toRowNumSql(partition, orderBy);
		//需要select语句中加上：,(SELECT @ROW_NUMBER:=0) AS t
		return "(@ROW_NUMBER:=@ROW_NUMBER + 1) AS rowNum";
	}

	@Override
	public String toPagerSql(Paginator paginator){
		return String.format(" ORDER BY %1$s LIMIT %2$d,%3$d",
				paginator.getOrderBy(),
				(paginator.getPageNo()-1)* paginator.getPageSize(),
				paginator.getPageSize()
		);
	}

	//region 外键
	private static final Map<String, Table<String, String,MetaForeignKey>> foreignKeyMap = new ConcurrentHashMap<>();
	private record MySqlForeignKeyRecord(String constraintName,String dbSchema,String objName,String colName,
										 int colPosition, String refDbSchema, String refObjName, String refColName,
										 String onUpdateAction, String onDeleteAction){

	}
	private static class MySqlForeignKeyRowMapper implements RowMapper<MySqlForeignKeyRecord>{
		@Override
		public MySqlForeignKeyRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			var constraintName = rs.getString(1);
			var dbSchema = rs.getString(2);
			var objName = rs.getString(3);
			var colName = rs.getString(4);
			var colPosition = rs.getInt(5);
			var refDbSchema = rs.getString(6);
			var refObjName = rs.getString(7);
			var refColName = rs.getString(8);
			var onUpdateAction = rs.getString(9);
			var onDeleteAction = rs.getString(10);
			return new MySqlForeignKeyRecord(constraintName,dbSchema,objName,colName,
					colPosition,refDbSchema,refObjName,refColName,onUpdateAction,onDeleteAction);
		}
	}
	private Table<String,String,MetaForeignKey> loadForeignKeys(final String dbSchema){
		Table<String,String,MetaForeignKey> schemaFkTable = TreeBasedTable.create();
		var sql = """
				SELECT u.`CONSTRAINT_NAME` AS constraintName,
					u.TABLE_SCHEMA AS dbSchema, u.`TABLE_NAME` AS objName, u.`COLUMN_NAME` AS colName,
					u.ORDINAL_POSITION AS colPosition,
					u.REFERENCED_TABLE_SCHEMA AS refDbSchema, u.REFERENCED_TABLE_NAME AS refObjName, u.REFERENCED_COLUMN_NAME AS refColName,
					r.UPDATE_RULE AS onUpdateAction, r.DELETE_RULE AS onDeleteAction
				FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE u
					INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS c ON c.`CONSTRAINT_SCHEMA`=u.`CONSTRAINT_SCHEMA` AND c.`CONSTRAINT_NAME`=u.`CONSTRAINT_NAME` AND c.TABLE_SCHEMA = u.TABLE_SCHEMA AND c.`TABLE_NAME`=u.`TABLE_NAME`
					INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS r ON r.`CONSTRAINT_SCHEMA`=c.`CONSTRAINT_SCHEMA` AND r.`CONSTRAINT_NAME`=c.`CONSTRAINT_NAME`
				WHERE c.`CONSTRAINT_SCHEMA`=? AND c.CONSTRAINT_TYPE='FOREIGN KEY';
				""";
		var data = jdbcTemplate.query(sql,new MySqlForeignKeyRowMapper(),dbSchema);
		var groups = data.stream().collect(Collectors.groupingBy(it -> it.constraintName));
		for(var records : groups.values()){
			var r = records.get(0);
			var fk = new MetaForeignKey();
			fk.setDbSchema(dbSchema);
			fk.setObjName(r.objName);
			fk.setConstraintName(r.constraintName);
			fk.setRefDbSchema(r.refDbSchema);
			fk.setRefObjName(r.refObjName);
			fk.setOnUpdateAction(MetaForeignKeyAction.parse(r.onUpdateAction));
			fk.setOnDeleteAction(MetaForeignKeyAction.parse(r.onDeleteAction));
			var colRefs = new LinkedHashMap<String, String>();
			for(var col : records){
				colRefs.put(col.colName, col.refColName);
			}
			fk.setColRefs(colRefs);
			schemaFkTable.put(r.objName,r.constraintName,fk);
		}
		return schemaFkTable;
	}
	@Override
	public Collection<MetaForeignKey> getForeignKeys(final String dbSchema){
		return foreignKeyMap.computeIfAbsent(dbSchema, k -> loadForeignKeys(dbSchema)).values();
	}
	@Override
	public Collection<MetaForeignKey> getForeignKeys(final MetaObject metaObject){
		var dbSchema = metaObject.getDbSchema();
		var schemaFkTable  = foreignKeyMap.computeIfAbsent(dbSchema,k->loadForeignKeys(dbSchema));

		if(metaObject.getForeignKeys() == null){
			var objFks = schemaFkTable.row(metaObject.getObjName());
			for (var objFk : objFks.values()){
				objFk.setDbSchema(dbSchema);
				objFk.setObjName(metaObject.getObjName());
			}
			metaObject.setForeignKeys(objFks.values());
		}
		return metaObject.getForeignKeys();
	}
	//endregion of 外键

	//region 索引

	//dbSchema -> objName, indexName, MetaIndex
	private static final Map<String, Table<String,String,MetaIndex>> indexMap = new ConcurrentHashMap<>();
	private record MySqlIndexRecord(String objName, String indexName, String indexType, boolean nonUnique, int colIdx, String colName) {

	}
	private class MySqlIndexRowMapper implements RowMapper<MySqlIndexRecord> {
		@Override
		public MySqlIndexRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			var objName = rs.getString(1);
			var indexName = rs.getString(2);
			var indexType = rs.getString(3);
			var colIdx = rs.getInt(4);
			var colName = rs.getString(5);
			var nonUnique = rs.getLong(6) > 0;
			return new MySqlIndexRecord(objName,indexName,indexType,nonUnique,colIdx,colName);
		}
	}

	private Table<String,String,MetaIndex> loadIndexes(final String dbSchema){
		Table<String,String,MetaIndex> schemaIndexTable = TreeBasedTable.create();
		var sql = """
				SELECT `TABLE_NAME` AS objName, `INDEX_NAME` AS indexName, `INDEX_TYPE` AS indexType, 
					`SEQ_IN_INDEX` AS colIdx, `COLUMN_NAME` AS colName, `NON_UNIQUE` AS nonUnique
				FROM INFORMATION_SCHEMA.`STATISTICS` 
				WHERE `TABLE_SCHEMA`=? AND `INDEX_NAME`!='PRIMARY';
				""";
		var data = jdbcTemplate.query(sql, new MySqlIndexRowMapper(), dbSchema);
		var groups = data.stream().collect(Collectors.groupingBy(it -> it.indexName));
		for(var records : groups.values()){
			var metaIndex = new MetaIndex();
			metaIndex.setDbSchema(dbSchema);
			var r0 = records.get(0);
			metaIndex.setObjName(r0.objName);
			metaIndex.setIndexName(r0.indexName);
			metaIndex.setIndexDef(r0.indexType);
			metaIndex.setUnique(!r0.nonUnique);
			var indexCols = records.stream()
					.map(r -> new MetaIndexCol(r.colName, Sort.Order.ASC,false))
					.toList();
			metaIndex.setCols(indexCols);
			schemaIndexTable.put(r0.objName,r0.indexName,metaIndex);
		}
		return schemaIndexTable;
	}
	@Override
	public Collection<MetaIndex> getIndexes(MetaObject metaObject) {
		var dbSchema = metaObject.getDbSchema();
		var schemaIndexTable  = indexMap.computeIfAbsent(dbSchema,k->loadIndexes(dbSchema));

		if(metaObject.getIndexes() == null){
			var objIndexes = schemaIndexTable.row(metaObject.getObjName().toLowerCase());
			for(var index : objIndexes.values()){
				index.setDbSchema(metaObject.getDbSchema());
				index.setObjName(metaObject.getObjName());
			}
			metaObject.setIndexes(objIndexes.values());
		}
		return metaObject.getIndexes();
	}
	//endregion of 索引

}
