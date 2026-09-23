/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.mappers;
import cloud.mmda.core.models.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 标签RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
public class TagRowMapper implements RowMapper<Tag> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return Tag
	 * @throws SQLException
	 */
	@Override
	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException{
		Tag t = new Tag();
		t.setRowNum(rowNum);
		t.setTagID(rs.getLong("tagID"));
		t.setTagName(rs.getString("tagName"));
		t.setTagFor(rs.getString("tagFor"));
		t.setCreatedAt(rs.getTimestamp("createdAt"));
		t.setLastUsed(rs.getTimestamp("lastUsed"));
		t.setUsedCount(rs.getLong("usedCount"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
