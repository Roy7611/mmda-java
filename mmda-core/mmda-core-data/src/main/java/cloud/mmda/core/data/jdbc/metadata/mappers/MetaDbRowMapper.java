/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * Syc PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;

import cloud.mmda.core.metadata.MetaDb;
import org.springframework.jdbc.core.RowMapper;


/**
 * 元数据库RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-21 00:28:29.0
 * 
 */
public class MetaDbRowMapper implements RowMapper<MetaDb> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaDb
	 * @throws SQLException
	 */
	@Override
	public MetaDb mapRow(ResultSet rs, int rowNum) throws SQLException{
		MetaDb t = new MetaDb();
		t.setRowNum(rowNum);
		t.setDbSchema(rs.getString("dbSchema"));
		t.setDbName(rs.getString("dbName"));
		t.setDbOwner(rs.getString("dbOwner"));
		t.setNamespace(rs.getString("namespace"));
		t.setSystemCode(rs.getString("systemCode"));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setDefaultCharset(rs.getString("defaultCharset"));
		t.setDefaultCollation(rs.getString("defaultCollation"));
		t.setTablespace(rs.getString("tablespace"));
		t.setDescription(rs.getString("description"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
