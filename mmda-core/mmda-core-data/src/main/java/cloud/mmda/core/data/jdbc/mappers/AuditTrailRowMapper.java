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

import cloud.mmda.core.models.AuditTrail;
import org.springframework.jdbc.core.RowMapper;


/**
 * 审计追踪RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
public class AuditTrailRowMapper implements RowMapper<AuditTrail> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return AuditTrail
	 * @throws SQLException
	 */
	@Override
	public AuditTrail mapRow(ResultSet rs, int rowNum) throws SQLException{
		AuditTrail t = new AuditTrail();
		t.setRowNum(rowNum);
		t.setTrailID(rs.getLong("trailID"));
		t.setAuditTime(rs.getTimestamp("auditTime"));
		long userID = rs.getLong("userID");
		if(rs.wasNull())
			t.setUserID(null);
		else
			t.setUserID(userID);
		t.setModuleCode(rs.getString("moduleCode"));
		t.setOperation(rs.getString("operation"));
		t.setDeviceUUID(rs.getString("deviceUUID"));
		t.setIpAddress(rs.getString("ipAddress"));
		t.setIpAddressV6(rs.getString("ipAddressV6"));
		float longitude = rs.getFloat("longitude");
		if(rs.wasNull())
			t.setLongitude(null);
		else
			t.setLongitude(longitude);
		float latitude = rs.getFloat("latitude");
		if(rs.wasNull())
			t.setLatitude(null);
		else
			t.setLatitude(latitude);
		long changeLogID = rs.getLong("changeLogID");
		if(rs.wasNull())
			t.setChangeLogID(null);
		else
			t.setChangeLogID(changeLogID);
		t.setRemark(rs.getString("remark"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
