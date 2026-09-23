/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.data.jdbc.repository.ReportTemplateRepository;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.models.ReportTemplate;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.ValidationError;
import cloud.mmda.core.utils.BaseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static cloud.mmda.core.models.ReportTemplate.Meta.*;

/**
 * 报表模板Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:57.0
 * 
 */
@Service
public class ReportTemplateService extends TenancyEntityService<ReportTemplate,Long> {
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(ReportTemplateService.class);
	private final ReportTemplateRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public ReportTemplateService(final ReportTemplateRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END

	public ReportTemplate getById(long templateId) {
		SqlExpression expression = SqlExpression.builder(getMetaObject())
				.exp(_templateID).eq(templateId)
				.and(_uploadTime).isNotNull()
				.result();
		return  super.getAny(expression, getDefaultCachePolicy(Tenancy.parseTenantID(templateId)));
	}
	/**
	 * 获取对象 objName 的所有报表模板
	 * @param objName 对象名称
	 * @param p 缓存策略
	 * @return 可用的报表模板
	 */
	public List<ReportTemplate> getAllReportTemplatesOf(String objName, CachePolicy p){
		var cacheProvider = getCacheProvider();
		var listCacheKey = cacheProvider.getListCacheKey(p.getTenantID(), objName);
		var result = getCacheProvider().getList(listCacheKey);
		if(BaseUtil.hasAny(result)) return result;

		result = repository.findAllByObjName(p.getTenantID(), objName);
		if(BaseUtil.hasAny(result)) {
			cacheProvider.putList(listCacheKey, result);
		}
		return result;
	}

	private void removeCachedList(int tenantID, String objName){
		var cacheProvider = getCacheProvider();
		var listCacheKey = cacheProvider.getListCacheKey(tenantID, objName);
		cacheProvider.removeList(listCacheKey);
	}
	@Override
	protected Consumer<ReportTemplate> afterInserted() {
		return t -> removeCachedList(t.getTenantID(), t.getObjName());
	}

	@Override
	protected Consumer<ReportTemplate> afterDeleted() {
		return t -> removeCachedList(t.getTenantID(), t.getObjName());
	}

	@Override
	protected Consumer<ReportTemplate> afterUpdated() {
		return t -> removeCachedList(t.getTenantID(), t.getObjName());
	}

	public Long getKeyByTemplateNameAndObjName( String templateName,String objNme,CachePolicy p){
		try {
			SqlExpression condition =sqlExpressionBuilder()
					.exp(_templateName).equal(templateName)
					.and(_objName).equal(objNme)
					.result();
			return repository.findScalar(p.getTenantID(),_templateID, condition,Long.class);
		}catch (Exception ex){
			return repository.newPartitionID(p.getTenantID());
		}

	}

	@Override
	public int save(ReportTemplate t, CachePolicy p, boolean checkExists) {
		if (BaseUtil.isNullOrZero(t.getTemplateID()) && checkExists){
			Long templateId = reportTemplateService.getKeyByTemplateNameAndObjName( t.getTemplateName(), t.getObjName(), p);
			t.setTemplateID(templateId);
		}
		return super.save(t, p, checkExists);
	}

	@Override
	protected List<ValidationError> validate(ReportTemplate reportTemplate) {
//		return super.validate(reportTemplate);
		return new ArrayList<>();
	}

	@Override
	protected void beforeValidate(ReportTemplate t) {
		if (BaseUtil.isNullOrZero(t.getTemplateID())) t.setTemplateID(repository.newPartitionID(t.getTenantID()));
	}
}
