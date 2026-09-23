/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.data.jdbc.repository.FlowTrailRepository;
import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.ModuleAction;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.utils.BaseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cloud.mmda.core.models.FlowTrail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 流程追踪Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
@Service
public class FlowTrailService extends TenancyEntityService<FlowTrail,Long>{
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(FlowTrailService.class);
	private final FlowTrailRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public FlowTrailService(final FlowTrailRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END

	/**
	 * 获取名称`objName`和标识`objId`的对象所有的流程追踪记录
	 * @param objName 对象名称
	 * @param objId 对象ID
	 * @return
	 */
	public List<FlowTrail> getAllById(String objName, long objId) {
		return getList(() -> repository.findAllBy(objName,objId));
	}
	public List<FlowTrail> getAllByObj(String objName, long objId) {
		return getList(() -> repository.findAllBy(objName,objId));
	}
	public Optional<FlowTrail> getLastByObj(String objName, long objId) {
		return repository.findLastBy(objName,objId);
	}
	/**
	 * 构建流程追踪记录
	 * @param user
	 * @param actionName
	 * @param fromStatus
	 * @param toStatus
	 * @param objName
	 * @param objId
	 * @param notification
	 * @param ownerID
	 * @param importance
	 * @param urgency
	 * @return
	 * @param <S>
	 */
	public <S extends Enum<S>> FlowTrail buildFlowTrail(final UserAccount user,
											 final String actionName, EnumSet<S> fromStatus, EnumSet<S> toStatus,
											 final String objName, long objId,
											 final String notification, Long ownerID,
											 Importance importance, Urgency urgency) {
		var tenantId = Tenancy.parseTenantID(objId);
		var t = this.repository.create(tenantId);
		t.setObjName(objName);
		t.setObjID(objId);

		t.setActorID(user.getUserID());
		t.setAsTransition(actionName+"("+fromStatus+"=>"+toStatus+")");
		t.setNotification(notification);
		t.setOwnerID(ownerID);

		t.setImportance(importance);
		t.setUrgency(urgency);
		return t;
	}

	/**
	 * 构建流程追踪记录
	 * @param user
	 * @param metaObject
	 * @param objId
	 * @param actionParam
	 * @param fromStatus
	 * @param toStatus
	 * @return
	 * @param <S>
	 */
	public <S extends Enum<S>> FlowTrail buildFlowTrail(final UserAccount user,
											final MetaObject metaObject, long objId,
											final DomainActionParam actionParam,
											Enum<S> fromStatus, Enum<S> toStatus, Long changeLogID) {
		var tenantId = Tenancy.parseTenantID(objId);
		var t = this.repository.create(tenantId);
		t.setObjName(metaObject.getObjName());
		t.setObjID(objId);

		t.setActorID(user.getUserID());
		t.setAsTransition(actionParam.getActionName()+"("+fromStatus+"=>"+toStatus+")");
		t.setNotification(actionParam.getNotification());
		t.setOwnerID(actionParam.getOwnerID());

		t.setImportance(actionParam.getImportance());
		t.setUrgency(actionParam.getUrgency());

		t.setChangeLogID(changeLogID);
		return t;
	}

	@Override
	public FlowTrail assembleRefProperties(FlowTrail t) {
		Objects.requireNonNull(t);
		//不要重复组装
		if(t.isAssembled(Entity.ASSEMBLE_REF)) return t;

		return super.assembleRefProperties(assembleRefActionProperties(t));
	}

	private static final String ACTION_COLNAME="actionName";
	private FlowTrail assembleRefActionProperties(FlowTrail t){
		try {
			String refValue=t.getActionName();
			String dbSchema=getMetaObject().getDbSchema();
			String locale=getSupportLangFromLocale(LocaleContextHolder.getLocale());
			String cacheKeyOfRef=getTenancyCacheProvider().joinCacheKey(String.valueOf(t.getTenantID()),locale,"REF_"+t.getObjName()+"_Action");
			if(getTenancyCacheProvider().existsRefMap(cacheKeyOfRef)){
				String refActionText = getTenancyCacheProvider().getRefText(cacheKeyOfRef,refValue);
				if(refActionText == null) {
					Map<String,String> refActionMap = loadRefActionMap(cacheKeyOfRef,dbSchema,t.getObjName(),locale);
					refActionText=refActionMap.getOrDefault(refValue,"-");
					getTenancyCacheProvider().setRefEnumText(cacheKeyOfRef,refValue,refActionText);
				}
				t.setRefProperty(ACTION_COLNAME,refActionText);
			}else{
				Map<String,String> refActionMap = loadRefActionMap(cacheKeyOfRef,dbSchema,t.getObjName(),locale);
				t.setRefProperty(ACTION_COLNAME, refActionMap.getOrDefault(refValue,"-"));
			}

		}catch (Exception ex){
			String msg = String.format("组装%1$s.%2$s引用属性值异常。", tClassName,ACTION_COLNAME);
			logger.error(msg, ex);
		}
		return t;
	}

	private Map<String, String> loadRefActionMap(final String refEnumMapCacheKey,String dbSchema,String objName,String locale){
		try {
			var metaObject=super.getMetaObject(dbSchema,objName);
			var module=super.getMetadataProvider().getModule(metaObject,locale);
			if (!BaseUtil.hasAny(module.getActions())){
				return Collections.unmodifiableMap(EMPTY_REF_ENUM_MAP) ;
			}
			Map<String, String> refActionMap = module.getActions().stream().collect(Collectors.toMap(ModuleAction::getActionName, ModuleAction::getDisplayLabel));
			getTenancyCacheProvider().putRefMap(refEnumMapCacheKey, refActionMap);
			return refActionMap;
		}catch (Exception e){
			logger.error(e.getLocalizedMessage(),e);
			return Collections.unmodifiableMap(EMPTY_REF_ENUM_MAP) ;
		}
	}
	public int updateDoneByIds(List<Long> flowTailIds) {
//		var attributes = sqlExpressionBuilder()
//				.exp(_status).equal(FlowTokenStatus.DONE)
//				.and(_consumedTime).equal(Timestamp.valueOf(LocalDateTime.now()))
//				.result();
//		var condition=sqlExpressionBuilder()
//				.exp(_status).equal(FlowTokenStatus.NEW)
//				.and(_trailID).in(flowTailIds)
//				.result();
//		return repository.updateAll(attributes,condition);
		return  repository.updateDoneByIds(flowTailIds);
	}
	public List<Long> getNewKeysBy(String objName, long objId) {
		return  repository.findNewKeysBy(objName,objId);
	}
}
