/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.MasterTenancyEntity;
import cloud.mmda.core.entities.TenancyEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 角色 Role
 *
 * @author Roy Luo
 * @since 4.0
 */
@Data
@Entity
@Table(name="role",catalog = "mmda_base")
public class Role extends MasterTenancyEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 角色ID
	 */
    @Id
	@Min(0)
    private long roleID;

	/**
	 * 角色编码
	 */
	@Size(max=30)
    private String roleCode;

	/**
	 * 角色名称
	 */
	@Size(max=30)
    private String roleName;

	/**
	 * 业务范畴，如SALES,CONSTRUCTION
	 */
	@Size(max=32)
    private String bizScope;
	/**
	 * 角色类型，如STAFF, SUPERVISOR, MANAGER
	 */
	@Size(max=32)
    private String roleType;

	/**
	 * 备注
	 */
	@Size(max=255)
    private String remark;

	/**
	 * 标签
	 */
	@Size(max=255)
    private String tags;
	/**
	 * 创建人
	 */
	@CreatedBy
	@NotBlank
	@Size(min=1,max=100)
	private String creator;
	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name="createdDate",columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime createdDate;
	/**
	 * 最后修改人
	 */
	@LastModifiedBy
	@Size(max=100)
	@Column(name="lastModifier")
	private String lastModifier;
	/**
	 * 最后修改时间
	 */
	@LastModifiedDate
	@Column(name="lastModified",columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime lastModified;

	/**
	 * 功能权限
	 */
	@Valid
	@OneToMany(/*mappedBy = "role",*/ fetch = FetchType.LAZY)
	private List<RoleModuleAuth> moduleAuths;
	public final boolean hasModuleAuths(){
		return hasAny(moduleAuths);
	}
	/**
	 * 数据权限
	 */
	@Valid
	@OneToMany(/*mappedBy = "role",*/ fetch = FetchType.LAZY)
	private List<RoleDataAuth> dataAuths;
	public final boolean hasDataAuths(){
		return hasAny(dataAuths);
	}
	/**
	 * UI权限
	 */
	@Valid
	@OneToMany(/*mappedBy = "role",*/ fetch = FetchType.LAZY)
	private List<RoleUiAuth> uiAuths;
	public final boolean hasUiAuths(){
		return hasAny(uiAuths);
	}

//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return roleID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		roleID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	public final Long getId(){
		return roleID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _roleID = "roleID";
		public static final String _roleCode = "roleCode";
		public static final String _roleName = "roleName";
		public static final String _bizScope = "bizScope";
		public static final String _roleType = "roleType";
		public static final String _remark = "remark";
		public static final String _tags = "tags";
		public static final String _creator = "creator";
		public static final String _createdDate = "createdDate";
		public static final String _lastModifier = "lastModifier";
		public static final String _lastModified = "lastModified";
		
		public static final String _moduleAuths = "moduleAuths";
		public static final String _dataAuths = "dataAuths";
		public static final String _uiAuths = "uiAuths";
	}
	//endregion of ~GENERATED PARTS END

}
