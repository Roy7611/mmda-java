/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.enums.ModuleAllowOpSet;
import cloud.mmda.core.metadata.Authority;
import cloud.mmda.core.metadata.Module;
import cloud.mmda.core.metadata.ModuleAction;
import cloud.mmda.core.metadata.ModuleAuth;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import cloud.mmda.core.entities.*;

import cloud.mmda.core.enums.ModuleAuthScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 角色功能权限
 * 
 * @remarks 角色功能权限
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
@Entity
@IdClass(RoleModuleAuth.Key.class)
@Table(name="rolemoduleauth",catalog = "mmda_base")
public class RoleModuleAuth extends TenancyEntity<RoleModuleAuth.Key> implements Authority {
	public final static RoleModuleAuth create(long roleId, Module module){
		RoleModuleAuth a = new RoleModuleAuth();
		a.roleID = roleId;
		a.setAuthScope(ModuleAuthScope.ALL);
		a.assembleModule(module);
		a.setCreated();
		return a;
	}
	//region ~GENERATED PARTS BEGIN
	/**
	 * 角色标识
	 */
	@Id
	@Min(0)
	@Getter @Setter
	private long roleID;
	/**
	 * 模块编码
	 */
	@Id
	@NotBlank
	@Size(min=1,max=15)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 读取
	 */
	@NotNull
	@Getter @Setter
	private boolean allowRead;
	/**
	 * 创建
	 */
	@NotNull
	@Getter @Setter
	private boolean allowCreate;
	/**
	 * 编辑
	 */
	@NotNull
	@Getter @Setter
	private boolean allowEdit;
	/**
	 * 删除
	 */
	@NotNull
	@Getter @Setter
	private boolean allowDelete;
	/**
	 * 打印
	 */
	@NotNull
	@Getter @Setter
	private boolean allowPrint;
	/**
	 * 导入
	 */
	@NotNull
	@Getter @Setter
	private boolean allowImport;
	/**
	 * 导出
	 */
	@NotNull
	@Getter @Setter
	private boolean allowExport;
	/**
	 * 上传模板
	 */
	@NotNull
	@Getter @Setter
	private boolean allowUpload;
	/**
	 * 权限范围：0;SELF;本人|1;GROUP;组|2;DEPARTMENT;部门|4;DIVISION;子公司|8;ALL;全局
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	@Transient
	private ModuleAuthScope authScope;

	@Column(name = "authScope")
	@Basic
	private byte authScopeValue = 0;

	/**
	 * 权限操作，多个逗号隔开
	 */
	@Size(max=255)
	@Getter @Setter
	private String authActions;
	/**
	 * 权限规则
	 */
	@Size(max=255)
	@Getter @Setter
	private String authRule;
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
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements TenancyKey{
		/**
		 * 角色标识
		 */
		@NotNull
		@Min(0)
		@Getter @Setter
		private long roleID;
		/**
		 * 模块编码
		 */
		@NotBlank
		@Size(min=1,max=15)
		@Getter @Setter
		private String moduleCode;
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
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return roleID + CompositeKey.KEY_DELIMITER + moduleCode;
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(CompositeKey.KEY_DELIMITER);
			return new Key(Long.parseLong(keys[0]),keys[1]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key getId(){
		return new Key(roleID, moduleCode);
	}

	@Override
	public Class<Key> getIdClass() {
		return Key.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _roleID = "roleID";
		public static final String _moduleCode = "moduleCode";
		public static final String _allowRead = "allowRead";
		public static final String _allowCreate = "allowCreate";
		public static final String _allowEdit = "allowEdit";
		public static final String _allowDelete = "allowDelete";
		public static final String _allowPrint = "allowPrint";
		public static final String _allowImport = "allowImport";
		public static final String _allowExport = "allowExport";
		public static final String _authScope = "authScope";
		public static final String _authActions = "authActions";
		public static final String _authRule = "authRule";
	}
	//endregion of ~GENERATED PARTS END

	@Transient
	@Getter @Setter
	private String moduleLabel;

	@Transient
	@Getter @Setter
	private ModuleAllowOpSet allowOp;
	/**
	 * 模块设置的操作
	 */
	@Getter
	@Transient
	private List<ModuleAction> moduleActions;
	/**
	 * 已授权的操作
	 */
	@Getter @Setter
	@Transient
	private Set<ModuleAction> authorizedActions;
	@Getter
	@Transient
	private List<RoleModuleAuth> subModuleAuths;
	public void addSubModuleAuth(RoleModuleAuth moduleAuth){
		if(subModuleAuths == null){
			subModuleAuths = new ArrayList<>();
		}
		subModuleAuths.add(moduleAuth);
	}
	/**
	 * 保存前将Set序列化为逗号隔开的字符串
	 */
	public void beforeSave(){
		if(!CollectionUtils.isEmpty(this.authorizedActions)){
			this.authActions = this.authorizedActions.stream()
					.map(action -> action.getActionName())
					.collect(Collectors.joining(","));
		}
		else{
			this.authActions = null;
		}
	}

	/**
	 * 加载后组装模块，将逗号隔开的action反序列化为集合
	 */
	public void assembleModule(Module module){
		if(module == null) return;

		if (this.moduleCode!=null && !this.moduleCode.equals(module.getModuleCode())) return;

		this.moduleCode = module.getModuleCode();
		this.moduleLabel = module.getModuleLabel();
		this.moduleActions = module.getActions();
		this.allowOp = module.getAllowOps();

		if(StringUtils.hasText(this.authActions) && module.hasActions()){
			this.authorizedActions = new HashSet<>();
			String[] actions = this.authActions.split(",");
			for( ModuleAction moduleAction : module.getActions()){
				for (String action : actions) {
					if(action.equals(moduleAction.getActionName())){
						this.authorizedActions.add(moduleAction);
						break;
					}
				}
			}
		}
		else{
			this.authorizedActions = null;
		}
	}

	public void addToModuleAuth(final ModuleAuth moduleAuth){
		if(moduleAuth == null) return;
		if (this.moduleCode!=null && !this.moduleCode.equals(moduleAuth.getModuleCode())) return;

		assembleModule(moduleAuth);
		moduleAuth.addAuthority(this);
	}

	@Override
	public boolean isDeletable() {
		return false;
	}

	@PostLoad
	public void fillTransient() {
		this.authScope = ModuleAuthScope.valueOf(authScopeValue);
	}

	@PrePersist
	public void fillPersistent() {
		this.authScopeValue = authScope.getValue();
	}
}
