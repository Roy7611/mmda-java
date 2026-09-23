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
import java.util.List;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.ReportTemplateRowMapper;
import cloud.mmda.core.models.ReportTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import static cloud.mmda.core.models.ReportTemplate.Meta._objName;


/**
 * 报表模板Repository
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-26 23:51:57.0
 * 
 */
@Repository
public class ReportTemplateRepository extends TenancyEntityRepository<ReportTemplate,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public ReportTemplateRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<ReportTemplate> createRowMapper() {
		return new ReportTemplateRowMapper();
	}

	/**
	 * 根据模板名称查找
	 * @param tenantID 租户id
	 * @param templateName 模板名称
	 * @return 报表模板
	 * @throws DataAccessException
	 */
	public ReportTemplate findByTemplateName(int tenantID, String templateName) throws DataAccessException{
		return super.findByUniqueKey(tenantID, templateName);
	}
	
	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public ReportTemplate create() {
		ReportTemplate t = new ReportTemplate();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setUploadTime(now);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

	/**
	 * 查找租户tenantID的对象objName所有报表模板
	 * @param tenantID 租户标识
	 * @param objName 对象名称
	 * @return 所有可用的报表模板
	 * @throws DataAccessException
	 */
	public List<ReportTemplate> findAllByObjName(int tenantID, String objName) throws DataAccessException{
		var exp = this.expressionBuilder()
				.exp(_objName).equal(objName)
				.result();
		return super.findAllBy(tenantID, exp);
	}
}
