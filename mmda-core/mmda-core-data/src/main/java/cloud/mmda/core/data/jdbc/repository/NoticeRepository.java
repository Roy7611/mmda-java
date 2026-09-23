/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * Syc PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.NoticeRowMapper;
import cloud.mmda.core.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cloud.mmda.core.models.Notice;

/**
 * 通知Repository
 * 
 * @author mmda code robot
 * @version 4.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
@Repository
public class NoticeRepository extends TenancyEntityRepository<Notice,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public NoticeRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<Notice> createRowMapper() {
		return new NoticeRowMapper();
	}

	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public Notice create() {
		Notice t = new Notice();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setNoticeTime(now);
		t.setCreateDate(now);
		t.setImportance(Importance.UNKNOWN);
		t.setEmergency(Urgency.NORMAL);
		t.setStatus(NotificationStatus.NEW);
		t.setNotifyingThru(MessageChannel.INTERNAL);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

	public int updateDoneByIds(List<Long> trailIDs){
		String sql="UPDATE Notice f SET f.`status`=?  WHERE f.`status`!=?  "+
				(String.format("AND f.flowTrailID IN (%s)  ",trailIDs.stream().map(String::valueOf).collect(Collectors.joining( ","))));
		return jdbcTemplate.update(sql,NotificationStatus.DONE_VAL,NotificationStatus.DONE_VAL);
	}
}
