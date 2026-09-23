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

import cloud.mmda.core.models.ReportTemplate;
import org.springframework.jdbc.core.RowMapper;


/**
 * 报表模板RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:57.0
 * 
 */
public class ReportTemplateRowMapper implements RowMapper<ReportTemplate> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ReportTemplate
	 * @throws SQLException
	 */
	@Override
	public ReportTemplate mapRow(ResultSet rs, int rowNum) throws SQLException{
		ReportTemplate t = new ReportTemplate();
		t.setRowNum(rowNum);
		t.setTemplateID(rs.getLong("templateID"));
		t.setObjName(rs.getString("objName"));
		t.setTemplateName(rs.getString("templateName"));
		t.setTemplateFile(rs.getString("templateFile"));
		t.setUploader(rs.getString("uploader"));
		t.setUploadTime(rs.getTimestamp("uploadTime"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
