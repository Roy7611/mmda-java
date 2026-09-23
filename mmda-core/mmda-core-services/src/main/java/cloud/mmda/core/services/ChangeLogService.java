/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.data.jdbc.repository.ChangeLogRepository;
import cloud.mmda.core.models.ChangeLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

/**
 * 修改日志Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-09-15 09:11:47.0
 * 
 */
@Service
public class ChangeLogService extends TenancyEntityService<ChangeLog,Long> {
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(ChangeLogService.class);
	private final ChangeLogRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public ChangeLogService(final ChangeLogRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save

	//endregion
	//endregion of ~GENERATED PARTS END

}
