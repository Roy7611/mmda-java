/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import javax.sql.DataSource;

import cloud.mmda.core.data.jdbc.mappers.ChangeLogRowMapper;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.entities.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static cloud.mmda.core.models.ChangeLog.Meta.*;


/**
 * 修改日志Repository
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-09-15 09:11:47.0
 * 
 */
@Repository
public class ChangeLogRepository extends TenancyEntityRepository<ChangeLog,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public ChangeLogRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<ChangeLog> createRowMapper() {
		return new ChangeLogRowMapper();
	}

	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public ChangeLog create() {
		ChangeLog t = new ChangeLog();
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

	/**
	 * 插入修改日志
	 * @param tenantID 租户ID
	 * @param changes 数据更改，通常是新老数据差异比较{@link EntityRepository#differ(Entity, Entity)}后得出的Map
	 * @param refName 引用实体名称
	 * @param refKey 引用对象主键
	 * @param prevLogID 前一个日志标识
	 * @return 返回修改日志的logID
	 */
	public long insert(int tenantID, Object changes, final String refName, final String refKey, final Long prevLogID)
			throws DataAccessException {
		Assert.notNull(changes, "changes cannot be null");
		var t = super.create(tenantID);
		t.writeDifference(changes);
		t.setRefName(refName);
		t.setRefKey(refKey);
		t.setPrevLogID(prevLogID);
		int r = insert(t);
		return t.getLogID();
	}
	/**
	 * 插入修改日志
	 * @param tenantID 租户ID
	 * @param changes 数据更改，通常是新老数据差异比较{@link EntityRepository#differ(Entity, Entity)}后得出的Map
	 * @param refName 引用实体名称
	 * @param refKey 引用对象主键
	 * @param prevLogID 前一个日志标识
	 * @return 返回修改日志的logID
	 */
	public long insert(int tenantID, Object changes, final String refName, final String refKey, final Long prevLogID,final  String  tags)
			throws DataAccessException {
		Assert.notNull(changes, "changes cannot be null");
		var t = super.create(tenantID);
		t.writeDifference(changes);
		t.setRefName(refName);
		t.setRefKey(refKey);
		t.setPrevLogID(prevLogID);
		t.setTags(tags);
		int r = insert(t);
		return t.getLogID();
	}
	/**
	 * 更新关联数据（已删除）的所有日志可被清理
	 * @param refName 引用实体名称
	 * @param refKey 引用对象主键
	 * @return 影响的记录数
	 * @throws DataAccessException
	 */
	public int updateDeletedByRef(final String refName, final String refKey) throws DataAccessException {
		var condition = this.expressionBuilder()
				.exp(_refName).eq(refName)
				.and(_refKey).eq(refKey)
				.result();
		var attributes = this.expressionBuilder()
				.exp(_refDeleted).eq(true)
				.result();
		return updateAll(attributes, condition);
	}

	/**
	 * 更新logId这条日志被撤消
	 * @param logId 日志ID
	 * @return 影响的记录数
	 * @throws DataAccessException
	 */
	public int updateUndoneById(long logId) throws DataAccessException {
		var attributes = this.expressionBuilder()
				.exp(_undone).eq(true)
				.result();
		return update(logId, attributes);
	}
	public PagedList<ChangeLog> searchAllByRef(final String refName, final String refKey, String searchWord, Paginator paginator) {
		var condition = this.expressionBuilder()
				.exp(_refName).eq(refName)
				.and(_refKey).eq(refKey)
				.and(_tags).contains(searchWord)
				.result();
		return findAllBy(paginator, condition);
	}
	public PagedList<ChangeLog> findAllByRef(final String refName, final String refKey, Paginator paginator) {
		var condition = this.expressionBuilder()
				.exp(_refName).eq(refName)
				.and(_refKey).eq(refKey)
				.result();
		return findAllBy(paginator, condition);
	}
	public List<ChangeLog> findAllByRef(final String refName, final String refKey) {
		var condition = this.expressionBuilder()
				.exp(_refName).eq(refName)
				.and(_refKey).eq(refKey)
				.result();
		return findAllBy(condition,getDefaultSort());
	}

	@Override
	public Sort getDefaultSort() {
		return Sort.of(_logID, Sort.Order.DESC);
	}
}
