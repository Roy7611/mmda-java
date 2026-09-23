/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.data.jdbc.repository.AuditTrailRepository;
import cloud.mmda.core.models.AuditTrail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

/**
 * 审计追踪Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
@Service
public class AuditTrailService extends TenancyEntityService<AuditTrail,Long>{
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(AuditTrailService.class);
	private final AuditTrailRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public AuditTrailService(final AuditTrailRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END

}
