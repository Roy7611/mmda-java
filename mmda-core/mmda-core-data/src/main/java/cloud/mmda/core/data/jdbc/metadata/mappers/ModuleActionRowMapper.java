/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.FlowTokenRequirement;
import cloud.mmda.core.enums.MessageLevel;
import cloud.mmda.core.enums.ModuleActionPrompt;
import cloud.mmda.core.enums.ModuleActionType;
import cloud.mmda.core.metadata.ModuleAction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 模块操作RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.585
 * 
 */
public class ModuleActionRowMapper implements RowMapper<ModuleAction> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return ModuleAction
	 * @throws SQLException
	 */
	@Override
	public ModuleAction mapRow(ResultSet rs, int rowNum) throws SQLException{
		ModuleAction t = new ModuleAction();
		t.setRowNum(rowNum);
		t.setModuleCode(rs.getString("moduleCode"));
		t.setActionName(rs.getString("actionName"));
		t.setActionCode(rs.getString("actionCode"));
		t.setActionType(ModuleActionType.valueOf(rs.getByte("actionType")));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setDisplayIcon(rs.getString("displayIcon"));
		t.setDisplayHint(MessageLevel.valueOf(rs.getByte("displayHint")));
		t.setOwnerOnly(rs.getBoolean("ownerOnly"));
		t.setIncomingTokensRequired(FlowTokenRequirement.valueOf(rs.getByte("incomingTokensRequired")));
		t.setExecutableExpression(rs.getString("executableExpression"));
		t.setStatusTransition(rs.getString("statusTransition"));
		t.setPromptType(ModuleActionPrompt.valueOf(rs.getByte("promptType")));
		t.setDescription(rs.getString("description"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
