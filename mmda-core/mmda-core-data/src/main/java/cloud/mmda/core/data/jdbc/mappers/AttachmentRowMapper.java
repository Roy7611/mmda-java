/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.mappers;

import cloud.mmda.core.models.Attachment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 附件RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
public class AttachmentRowMapper implements RowMapper<Attachment> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return Attachment
	 * @throws SQLException
	 */
	@Override
	public Attachment mapRow(ResultSet rs, int rowNum) throws SQLException{
		Attachment t = new Attachment();
		t.setRowNum(rowNum);
		t.setObjName(rs.getString("objName"));
		t.setObjID(rs.getLong("objID"));
		t.setFileName(rs.getString("fileName"));
		t.setFileSize(rs.getLong("fileSize"));
		t.setUploader(rs.getString("uploader"));
		t.setUploadTime(rs.getTimestamp("uploadTime"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
