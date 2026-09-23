/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

/**
 * 标签
 * 
 * @remarks 标签。支持所有实体对象打标签，实体中设置tags字段，逗号隔开多个标签。
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
public class Tag extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 标签ID
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long tagID;
	/**
	 * 标签名
	 */
	@NotBlank
	@Size(min=1,max=50)
	@Getter @Setter
	private String tagName;
	/**
	 * 标签对象，引用metaobject.objName
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String tagFor;
	/**
	 * 创建时间
	 */
	@NotNull
	@Getter @Setter
	private Timestamp createdAt;
	/**
	 * 最后使用
	 */
	@NotNull
	@Getter @Setter
	private Timestamp lastUsed;
	/**
	 * 使用计数
	 */
	@NotNull
	@Getter @Setter
	private long usedCount;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return tagID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		tagID = partitionID;
	}
	//endregion of partition

	//region key & sn
	/**
	 * 作为主键
	 */
	public final Long getId(){
		return tagID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

//	public void setDefaultUniqueKey() {
//		setTagName(generateNo("T", tagID));
//	}
	//endregion of key & sn

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _tagID = "tagID";
		public static final String _tagName = "tagName";
		public static final String _tagFor = "tagFor";
		public static final String _createdAt = "createdAt";
		public static final String _lastUsed = "lastUsed";
		public static final String _usedCount = "usedCount";
	}
	//endregion of ~GENERATED PARTS END

}
