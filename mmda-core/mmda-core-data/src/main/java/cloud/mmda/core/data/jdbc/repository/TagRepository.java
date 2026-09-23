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

import cloud.mmda.core.data.jdbc.mappers.TagRowMapper;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import static cloud.mmda.core.models.Tag.Meta.*;

/**
 * 标签Repository
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
@Repository
public class TagRepository extends TenancyEntityRepository<Tag,Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
	@Autowired
	public TagRepository(DataSource ds) {
		super(ds);
	}

	/**
	 * 创建RowMapper
	 */
	@Override
	protected RowMapper<Tag> createRowMapper() {
		return new TagRowMapper();
	}

	/**
	 * 根据标签名查找
	 * @param tenantID 租户id
	 * @param tagName 标签名
	 * @return 标签
	 * @throws DataAccessException
	 */
	public Tag findByTagName(int tenantID, String tagName) throws DataAccessException{
		return super.findByUniqueKey(tenantID, tagName);
	}
	
	/**
	 * 使用默认值创建新实体
	 * 未存储至数据库，供客户端新建使用
	 */
	public Tag create() {
		Tag t = new Tag();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		t.setCreatedAt(now);
		t.setLastUsed(now);
		t.setCreated();
		return t;
	}

	//endregion of ~GENERATED PARTS END

	public boolean existsByTagNameFor(int tenantID, String tagName, String tagFor) {
		SqlExpression cond = expressionBuilder()
				.exp(_tagName).eq(tagName)
				.and(_tagFor).eq(tagFor)
				.result();
		return super.count(tenantID,cond) > 0;
	}
	public Tag findFirstByTagNameFor(int tenantID, String tagName, String tagFor) {
		SqlExpression cond = expressionBuilder()
				.exp(_tagName).eq(tagName)
				.and(_tagFor).eq(tagFor)
				.result();
		return super.findFirst(tenantID,cond);
	}
	public int updateUsedCountByTagNameFor(long tagID, String tagName, String tagFor) {
		SqlExpression attr = expressionBuilder()
				.exp(_usedCount).eq(_usedCount + 1)
				.result();
		return super.update(tagID,attr);
	}
}
