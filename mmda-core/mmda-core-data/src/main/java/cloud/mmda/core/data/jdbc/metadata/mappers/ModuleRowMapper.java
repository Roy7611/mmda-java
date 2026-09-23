/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.ModuleAllowOpSet;
import cloud.mmda.core.enums.ModuleStatus;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.metadata.Module;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 模块RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.528
 * 
 */
public class ModuleRowMapper implements RowMapper<Module> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return Module
	 * @throws SQLException
	 */
	@Override
	public Module mapRow(ResultSet rs, int rowNum) throws SQLException{
		Module t = new Module();
		t.setRowNum(rowNum);
		t.setModuleCode(rs.getString("moduleCode"));
		t.setModuleLabel(rs.getString("moduleLabel"));
		t.setShortLabel(rs.getString("shortLabel"));
		t.setModuleType(ModuleType.valueOf(rs.getByte("moduleType")));
		t.setModuleIcon(rs.getString("moduleIcon"));
		t.setDbSchema(rs.getString("dbSchema"));
		t.setObjName(rs.getString("objName"));
		t.setAllowOps(ModuleAllowOpSet.valueOf(rs.getInt("allowOps")));
		t.setModuleUrl(rs.getString("moduleUrl"));
		t.setRequiredCreateParam(rs.getBoolean("requiredCreateParam"));
		t.setDefaultFilter(rs.getString("defaultFilter"));
		t.setDefaultGroupBy(rs.getString("defaultGroupBy"));
		t.setDefaultSort(rs.getString("defaultSort"));
		t.setStatus(ModuleStatus.valueOf(rs.getShort("status")));
		t.setDescription(rs.getString("description"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
