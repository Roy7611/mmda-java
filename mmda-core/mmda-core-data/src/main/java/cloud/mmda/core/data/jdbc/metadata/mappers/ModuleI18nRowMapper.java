/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.metadata.ModuleI18n;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 模块国际化RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.654
 * 
 */
public class ModuleI18nRowMapper implements RowMapper<ModuleI18n> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ModuleI18n
	 * @throws SQLException
	 */
	@Override
	public ModuleI18n mapRow(ResultSet rs, int rowNum) throws SQLException{
		ModuleI18n t = new ModuleI18n();
		t.setRowNum(rowNum);
		t.setModuleCode(rs.getString("moduleCode"));
		t.setLocale(rs.getString("locale"));
		t.setModuleLabel(rs.getString("moduleLabel"));
		t.setShortLabel(rs.getString("shortLabel"));
		t.setDescription(rs.getString("description"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
