/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;

import cloud.mmda.core.models.CustomizedQuery;
import org.springframework.jdbc.core.RowMapper;


/**
 * 自定义查询RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:56.0
 * 
 */
public class CustomizedQueryRowMapper implements RowMapper<CustomizedQuery> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return CustomizedQuery
	 * @throws SQLException
	 */
	@Override
	public CustomizedQuery mapRow(ResultSet rs, int rowNum) throws SQLException{
		CustomizedQuery t = new CustomizedQuery();
		t.setRowNum(rowNum);
		t.setQueryID(rs.getLong("queryID"));
		t.setObjName(rs.getString("objName"));
		t.setQueryName(rs.getString("queryName"));
		t.setQueryExpression(rs.getString("queryExpression"));
		t.setCreatorOnly(rs.getBoolean("creatorOnly"));
		t.setPredefined(rs.getBoolean("predifined"));
		t.setRemark(rs.getString("remark"));
		long creatorID = rs.getLong("creatorID");
		if(rs.wasNull())
			t.setCreatorID(null);
		else
			t.setCreatorID(creatorID);
		long deptID = rs.getLong("deptID");
		if(rs.wasNull())
			t.setDeptID(null);
		else
			t.setDeptID(deptID);
		t.setCreateDate(rs.getTimestamp("createDate"));
		long lastModifierID = rs.getLong("lastModifierID");
		if(rs.wasNull())
			t.setLastModifierID(null);
		else
			t.setLastModifierID(lastModifierID);
		t.setLastModified(rs.getTimestamp("lastModified"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
