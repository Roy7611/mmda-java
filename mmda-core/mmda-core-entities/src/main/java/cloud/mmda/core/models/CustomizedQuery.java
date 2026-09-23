/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.Auditable;
import cloud.mmda.core.entities.TenancyEntity;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

/**
 * 自定义查询
 * 
 * @remarks 自定义查询
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:56.0
 * 
 */
public class CustomizedQuery extends TenancyEntity<Long> implements Auditable {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 查询标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long queryID;
	/**
	 * 对象名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String objName;
	/**
	 * 查询名称
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String queryName;
	/**
	 * 查询表达式，Json对象或者Sql条件表达式
	 */
	@NotBlank
	@Size(min=1,max=2000)
	@Getter @Setter
	private String queryExpression;
	/**
	 * 私有的
	 */
	@NotNull
	@Getter @Setter
	private boolean creatorOnly;
	/**
	 * 预定义的
	 */
	@NotNull
	@Getter @Setter
	private boolean predefined;
	/**
	 * 备注
	 */
	@Size(max=255)
	@Getter @Setter
	private String remark;
	/**
	 * 创建人：REF User(userID,userName)
	 */
	@Getter @Setter
	private Long creatorID;
	/**
	 * 创建部门：REF Department(deptID,deptName)
	 */
	@Getter @Setter
	private Long deptID;
	/**
	 * 创建日期
	 */
	@Getter @Setter
	private Timestamp createDate;
	/**
	 * 修改人：REF User(userID,userName)
	 */
	@Getter @Setter
	private Long lastModifierID;
	/**
	 * 最后修改
	 */
	@Getter @Setter
	private Timestamp lastModified;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return queryID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		queryID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	@Override
	public final Long getId(){
		return queryID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _queryID = "queryID";
		public static final String _objName = "objName";
		public static final String _queryName = "queryName";
		public static final String _queryExpression = "queryExpression";
		public static final String _creatorOnly = "creatorOnly";
		public static final String _predifined = "predifined";
		public static final String _remark = "remark";
		public static final String _creatorID = "creatorID";
		public static final String _deptID = "deptID";
		public static final String _createDate = "createDate";
		public static final String _lastModifierID = "lastModifierID";
		public static final String _lastModified = "lastModified";
		
	}
	/**
	 * 设置创建人
	 * @param creatorID 创建人标识
	 * @param deptID 部门标识
	 * @return 返回实体对象本身
	 */
	public CustomizedQuery createBy(long creatorID, long deptID){
		this.creatorID = creatorID;
		this.deptID = deptID;
		return this;
	}
	/**
	 * 设置修改人
	 * @param lastModifierID 最后修改人标识
	 * @return 实体对象本身
	 */
	public CustomizedQuery modifyBy(long lastModifierID){
		this.lastModifierID = lastModifierID;
		this.lastModified = Timestamp.valueOf(LocalDateTime.now());
		return this;
	}
	//endregion of ~GENERATED PARTS END

}
