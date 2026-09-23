/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cloud.mmda.core.metadata.ModuleFlow;

import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
/**
 * 模块操作流RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-21 00:28:30.0
 * 
 */
public class ModuleFlowRowMapper implements RowMapper<ModuleFlow> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ModuleFlow
	 * @throws SQLException
	 */
	@Override
	public ModuleFlow mapRow(ResultSet rs, int rowNum) throws SQLException{
		ModuleFlow t = new ModuleFlow();
		t.setRowNum(rowNum);
		t.setFlowCode(rs.getString("flowCode"));
		t.setActionCode(rs.getString("actionCode"));
		t.setNextActionCode(rs.getString("nextActionCode"));
		t.setModuleCode(rs.getString("moduleCode"));
		t.setImportance(Importance.valueOf(rs.getByte("importance")));
		t.setUrgency(Urgency.valueOf(rs.getByte("urgency")));
		int sopDuration = rs.getInt("sopDuration");
		if(rs.wasNull())
			t.setSopDuration(null);
		else
			t.setSopDuration(sopDuration);
		t.setNotice(rs.getString("notice"));
		t.setMultiplicity(rs.getInt("multiplicity"));
		t.setFallback(rs.getBoolean("fallback"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
