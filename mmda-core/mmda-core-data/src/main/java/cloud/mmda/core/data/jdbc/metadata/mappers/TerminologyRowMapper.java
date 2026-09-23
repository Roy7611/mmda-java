/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.metadata.Terminology;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 术语RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.845
 * 
 */
public class TerminologyRowMapper implements RowMapper<Terminology> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return Terminology
	 * @throws SQLException
	 */
	@Override
	public Terminology mapRow(ResultSet rs, int rowNum) throws SQLException{
		Terminology t = new Terminology();
		t.setRowNum(rowNum);
		t.setAbbreviation(rs.getString("abbreviation"));
		t.setWords(rs.getString("words"));
		t.setDescription(rs.getString("description"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
