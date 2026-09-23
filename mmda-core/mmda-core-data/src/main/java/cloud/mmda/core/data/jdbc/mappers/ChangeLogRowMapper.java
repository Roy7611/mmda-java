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

import cloud.mmda.core.models.ChangeLog;
import org.springframework.jdbc.core.RowMapper;


/**
 * 修改日志RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-09-15 09:11:47.0
 * 
 */
public class ChangeLogRowMapper implements RowMapper<ChangeLog> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ChangeLog
	 * @throws SQLException
	 */
	@Override
	public ChangeLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChangeLog t = new ChangeLog();
		t.setRowNum(rowNum);
		t.setLogID(rs.getLong("logID"));
		t.setDifference(rs.getBytes("difference"));
		t.setRefName(rs.getString("refName"));
		t.setRefKey(rs.getString("refKey"));
		t.setRefDeleted(rs.getBoolean("refDeleted"));
		t.setUndone(rs.getBoolean("undone"));
		long prevLogID = rs.getLong("prevLogID");
		if(rs.wasNull()){
			t.setPrevLogID(null);
		}
		else {
			t.setPrevLogID(prevLogID);
		}
		t.setTags(rs.getString("tags"));
		//or using stream chunks
//		try(var inputStream = rs.getBinaryStream("difference");
//			var byteStream = new ByteArrayOutputStream()){
//			byte[] buffer = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = inputStream.read(buffer)) > 0) {
//				byteStream.write(buffer, 0, bytesRead);
//			}
//			t.setDifference(byteStream.toByteArray());
//		}
//		catch (IOException ex){
//
//		}
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
