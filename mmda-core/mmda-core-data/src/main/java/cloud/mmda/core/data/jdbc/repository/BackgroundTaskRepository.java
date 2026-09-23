/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.BackgroundTaskRowMapper;
import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.enums.BackgroundTaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


/**
 * 后台任务Repository
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2025-12-25 11:16:07.0
 * 
 */
@Repository
public class BackgroundTaskRepository extends TenancyEntityRepository<BackgroundTask,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public BackgroundTaskRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<BackgroundTask> createRowMapper() {
		return new BackgroundTaskRowMapper();
	}

	/**
	 * 根据任务编号查找
	 * @param tenantID 租户id
	 * @param taskNo 任务编号
	 * @return 后台任务
	 * @throws DataAccessException
	 */
	public BackgroundTask findByTaskNo(int tenantID, String taskNo) throws DataAccessException{
		return super.findByUniqueKey(tenantID, taskNo);
	}
	
	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public BackgroundTask create() {
		BackgroundTask t = new BackgroundTask();
		t.setTaskNo("");
		t.setStatus(BackgroundTaskStatus.NEW);
		t.setTaskProgress(BigDecimal.ZERO);
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setCreateDate(now);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

}
