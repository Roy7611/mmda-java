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

import cloud.mmda.core.models.FlowTrail;
import org.springframework.jdbc.core.RowMapper;


import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import cloud.mmda.core.enums.FlowTokenStatus;
/**
 * 流程追踪RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
public class FlowTrailRowMapper implements RowMapper<FlowTrail> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return FlowTrail
	 * @throws SQLException
	 */
	@Override
	public FlowTrail mapRow(ResultSet rs, int rowNum) throws SQLException{
		FlowTrail t = new FlowTrail();
		t.setRowNum(rowNum);
		t.setTrailID(rs.getLong("trailID"));
		t.setObjName(rs.getString("objName"));
		t.setObjID(rs.getLong("objID"));
		t.setActTime(rs.getTimestamp("actTime"));
		t.setActorID(rs.getLong("actorID"));
		t.setAsTransition(rs.getString("asTransition"));
		long changeLogID = rs.getLong("changeLogID");
		if(rs.wasNull())
			t.setChangeLogID(null);
		else
			t.setChangeLogID(changeLogID);
		t.setImportance(Importance.valueOf(rs.getByte("importance")));
		t.setUrgency(Urgency.valueOf(rs.getByte("urgency")));
		t.setNotification(rs.getString("notification"));
		long ownerID = rs.getLong("ownerID");
		if(rs.wasNull())
			t.setOwnerID(null);
		else
			t.setOwnerID(ownerID);
		t.setStatus(FlowTokenStatus.valueOf(rs.getInt("status")));
		t.setConsumedTime(rs.getTimestamp("consumedTime"));
		int costTime = rs.getInt("costTime");
		if(rs.wasNull())
			t.setCostTime(null);
		else
			t.setCostTime(costTime);
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
