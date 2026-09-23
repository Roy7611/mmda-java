/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.AuditTrailRowMapper;
import cloud.mmda.core.models.AuditTrail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


/**
 * 审计追踪Repository
 * 
 * @author mmda code robot
 * @version 4.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
@Repository
public class AuditTrailRepository extends TenancyEntityRepository<AuditTrail,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public AuditTrailRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<AuditTrail> createRowMapper() {
		return new AuditTrailRowMapper();
	}

	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public AuditTrail create() {
		AuditTrail t = new AuditTrail();
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

}
