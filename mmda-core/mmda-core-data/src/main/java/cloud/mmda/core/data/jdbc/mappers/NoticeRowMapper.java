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

import cloud.mmda.core.enums.*;
import org.springframework.jdbc.core.RowMapper;

import cloud.mmda.core.models.Notice;

/**
 * 通知RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-08-11 22:39:57.0
 * 
 */
public class NoticeRowMapper implements RowMapper<Notice> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return Notice
	 * @throws SQLException
	 */
	@Override
	public Notice mapRow(ResultSet rs, int rowNum) throws SQLException{
		Notice t = new Notice();
		t.setRowNum(rowNum);
		t.setNoticeID(rs.getLong("noticeID"));
		t.setNoticeTime(rs.getTimestamp("noticeTime"));
		t.setImportance(Importance.valueOf(rs.getByte("importance")));
		t.setEmergency(Urgency.valueOf(rs.getByte("emergency")));
		t.setNoticeContent(rs.getString("noticeContent"));
		t.setNoticeToUserID(rs.getLong("noticeToUserID"));
		t.setNoticeTo(rs.getString("noticeTo"));
		t.setNotifyingThru(MessageChannel.valueOf(rs.getByte("notifyingThru")));
		t.setStatus(NotificationStatus.valueOf(rs.getByte("status")));
		t.setCreateDate(rs.getTimestamp("createDate"));
//		boolean todo = rs.getBoolean("todo");
//		if(rs.wasNull())
//			t.setTodo(null);
//		else
//			t.setTodo(todo);
		t.setRefName(rs.getString("refName"));
		t.setRefNo(rs.getString("refNo"));
		long refID = rs.getLong("refID");
		if(rs.wasNull())
			t.setRefID(null);
		else
			t.setRefID(refID);
		short refItemID = rs.getShort("refItemID");
		if(rs.wasNull())
			t.setRefItemID(null);
		else
			t.setRefItemID(refItemID);
		long flowTrailID = rs.getLong("flowTrailID");
		if(rs.wasNull())
			t.setFlowTrailID(null);
		else
			t.setFlowTrailID(flowTrailID);
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
