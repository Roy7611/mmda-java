/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;
import cloud.mmda.core.enums.SkeweredMode;
import cloud.mmda.core.metadata.ModuleSkewer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 模块串烧RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2025-01-20 20:44:33.0
 * 
 */
public class ModuleSkewerRowMapper implements RowMapper<ModuleSkewer> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ModuleSkewer
	 * @throws SQLException
	 */
	@Override
	public ModuleSkewer mapRow(ResultSet rs, int rowNum) throws SQLException{
		ModuleSkewer t = new ModuleSkewer();
		t.setRowNum(rowNum);
		t.setModuleCode(rs.getString("moduleCode"));
		t.setSkeweredModuleCode(rs.getString("skeweredModuleCode"));
		t.setSkeweredMode(SkeweredMode.valueOf(rs.getByte("skeweredMode")));
		t.setSkeweredKey(rs.getString("skeweredKey"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
