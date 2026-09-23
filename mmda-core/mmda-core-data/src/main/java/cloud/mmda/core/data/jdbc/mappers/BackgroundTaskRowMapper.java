/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;

import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.enums.BackgroundTaskStatus;
import org.springframework.jdbc.core.RowMapper;

/**
 * 后台任务RowMapper
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2025-12-25 11:16:07.0
 * 
 */
public class BackgroundTaskRowMapper implements RowMapper<BackgroundTask> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return BackgroundTask
	 * @throws SQLException
	 */
	@Override
	public BackgroundTask mapRow(ResultSet rs, int rowNum) throws SQLException{
		BackgroundTask t = new BackgroundTask();
		t.setRowNum(rowNum);
		t.setTaskID(rs.getLong("taskID"));
		t.setTaskNo(rs.getString("taskNo"));
		t.setTaskName(rs.getString("taskName"));
		t.setStatus(BackgroundTaskStatus.valueOf(rs.getByte("status")));
		t.setExpectedFinish(rs.getTimestamp("expectedFinish"));
		t.setStartedTime(rs.getTimestamp("startedTime"));
		t.setFinishedTime(rs.getTimestamp("finishedTime"));
		t.setTaskProgress(rs.getBigDecimal("taskProgress"));
		t.setTaskResult(rs.getString("taskResult"));
		t.setRemark(rs.getString("remark"));
		t.setCustomJson(rs.getString("customJson"));
		long deptID = rs.getLong("deptID");
		if(rs.wasNull())
			t.setDeptID(null);
		else
			t.setDeptID(deptID);
		long creatorID = rs.getLong("creatorID");
		if(rs.wasNull())
			t.setCreatorID(null);
		else
			t.setCreatorID(creatorID);
		t.setCreateDate(rs.getTimestamp("createDate"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
