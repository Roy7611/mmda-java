/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import java.sql.*;
import java.time.LocalDateTime;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.CustomizedQueryRowMapper;
import cloud.mmda.core.models.CustomizedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 自定义查询Repository
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-26 23:51:56.0
 * 
 */
@Repository
public class CustomizedQueryRepository extends TenancyEntityRepository<CustomizedQuery,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public CustomizedQueryRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<CustomizedQuery> createRowMapper() {
		return new CustomizedQueryRowMapper();
	}

	/**
	 * 根据查询名称查找
	 * @param tenantID 租户id
	 * @param queryName 查询名称
	 * @return 自定义查询
	 * @throws DataAccessException
	 */
	public CustomizedQuery findByQueryName(int tenantID, String queryName) throws DataAccessException{
		return super.findByUniqueKey(tenantID, queryName);
	}
	
	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public CustomizedQuery create() {
		CustomizedQuery t = new CustomizedQuery();
		t.setCreatorOnly(true);
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setCreateDate(now);
		t.setLastModified(now);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

}
