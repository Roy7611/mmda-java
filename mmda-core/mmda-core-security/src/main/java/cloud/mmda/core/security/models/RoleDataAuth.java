/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;

import cloud.mmda.core.Tenancy;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import java.util.Objects;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import cloud.mmda.core.entities.*;

/**
 * 角色数据授权
 * 
 * @remarks 角色数据授权。
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
@Data
@Entity
@IdClass(RoleDataAuth.Key.class)
@Table(name="roledataauth",catalog = "mmda_base")
public class RoleDataAuth extends CompositeTenancyEntity<RoleDataAuth.Key> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 角色ID
	 */
	@Id
	@Min(0)
	private long roleID;
	/**
	 * 数据库
	 */
	@Id
	@NotBlank
	@Size(min=1,max=30)
	private String dbSchema;
	/**
	 * 对象
	 */
	@Id
	@NotBlank
	@Size(min=1,max=30)
	private String objName;
	/**
	 * 字段
	 */
	@Id
	@NotBlank
	@Size(min=1,max=30)
	private String colName;
	/**
	 * 只读
	 */
	@NotNull
	private boolean readOnly;
	/**
	 * 隐藏
	 */
	@NotNull
	private boolean hidden;
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
	public static class Key implements CompositeTenancyKey{
		/**
		 * 角色ID
		 */
		@NotNull
		@Min(0)
		@Getter @Setter
		private long roleID;
		/**
		 * 数据库
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String dbSchema;
		/**
		 * 对象
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String objName;
		/**
		 * 字段
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String colName;
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
			return join(roleID,dbSchema,objName,colName);
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(CompositeKey.KEY_DELIMITER);
			return new Key(Long.parseLong(keys[0]),keys[1],keys[2],keys[3]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key getId(){
		return new Key(roleID, dbSchema, objName, colName);
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
		public static final String _dbSchema = "dbSchema";
		public static final String _objName = "objName";
		public static final String _colName = "colName";
		public static final String _readOnly = "readOnly";
		public static final String _hidden = "hidden";
	}
	//endregion of ~GENERATED PARTS END

}
