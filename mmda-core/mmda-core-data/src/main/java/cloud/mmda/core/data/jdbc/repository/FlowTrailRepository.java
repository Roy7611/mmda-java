/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * Syc PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.data.jdbc.repository;
import java.sql.*;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cloud.mmda.core.data.jdbc.mappers.FlowTrailRowMapper;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.models.FlowTrail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import cloud.mmda.core.enums.FlowTokenStatus;

/**
 * 流程追踪Repository
 * 
 * @author mmda code robot
 * @version 4.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
@Repository
public class FlowTrailRepository extends TenancyEntityRepository<FlowTrail,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public FlowTrailRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<FlowTrail> createRowMapper() {
		return new FlowTrailRowMapper();
	}

	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public FlowTrail create() {
		FlowTrail t = new FlowTrail();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setActTime(now);
		t.setImportance(Importance.UNKNOWN);
		t.setUrgency(Urgency.NORMAL);
		t.setStatus(FlowTokenStatus.NEW);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

	/**
	 * 查找一个实体的所有流程追踪记录
	 * @param objName 实体名称
	 * @param objId 实体标识
	 * @return
	 * @throws DataAccessException
	 */
	public List<FlowTrail> findAllBy(final String objName, final long objId) throws DataAccessException{
		SqlExpression exp = this.expressionBuilder()
				.exp(FlowTrail.Meta._objName).equal(objName)
				.and(FlowTrail.Meta._objID).equal(objId)
				.result();
		Sort sort = Sort.of(FlowTrail.Meta._actTime, Sort.Order.DESC);
		return this.findAllBy(exp,sort);
	}

	/**
	 * 查找一个实体的最后一条流程追踪记录
	 * @param objName 实体名称
	 * @param objId 实体标识
	 * @return
	 * @throws DataAccessException
	 */
	public Optional<FlowTrail> findLastBy(final String objName, final long objId) throws DataAccessException{
		SqlExpression exp = this.expressionBuilder()
				.exp(FlowTrail.Meta._objName).equal(objName)
				.and(FlowTrail.Meta._objID).equal(objId)
				.result();
		var pager = Paginator.limitOne(Sort.of(FlowTrail.Meta._changeLogID,Sort.Order.DESC));
		var result = findAllBy(pager, exp);
		return result.isEmpty()
				? Optional.empty()
				: Optional.of(result.getData().getFirst());
	}
	public List<Long> findNewKeysBy(final String objName, final long objId) throws DataAccessException{
		SqlExpression exp = this.expressionBuilder()
				.exp(FlowTrail.Meta._objName).equal(objName)
				.and(FlowTrail.Meta._objID).equal(objId)
				.and(FlowTrail.Meta._status).equal(FlowTokenStatus.NEW)
				.result();
		return this.findAllKeysBy(exp);
	}
	public int updateDoneByIds(List<Long> trailIDs){
		String sql="UPDATE flowtrail f SET f.`status`=? ,f.consumedTime=? WHERE f.`status`=?  "+
				(String.format("AND f.trailID IN (%s)  ",trailIDs.stream().map(String::valueOf).collect(Collectors.joining( ","))));
		return jdbcTemplate.update(sql,FlowTokenStatus.DONE_VAL,Timestamp.valueOf(LocalDateTime.now()),FlowTokenStatus.NEW_VAL);
	}
}
