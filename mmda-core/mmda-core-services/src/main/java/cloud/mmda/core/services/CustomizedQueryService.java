/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.data.jdbc.repository.CustomizedQueryRepository;
import cloud.mmda.core.models.CustomizedQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

/**
 * 自定义查询Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:56.0
 * 
 */
@Service
public class CustomizedQueryService extends TenancyEntityService<CustomizedQuery,Long> {
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(CustomizedQueryService.class);
	private final CustomizedQueryRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public CustomizedQueryService(final CustomizedQueryRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END

}
