/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;

import cloud.mmda.core.Tenancy;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;

import java.util.Objects;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import cloud.mmda.core.entities.*;

/**
 * 用户开放标识
 * 
 * @remarks 用户开放标识
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
@Entity
@IdClass(UserOpenIdentity.Key.class)
@Table(name="useropenidentity",catalog = "mmda_base")
public class UserOpenIdentity extends CompositeTenancyEntity<UserOpenIdentity.Key> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 用户ID
	 */
	@Id
	@Min(0)
	@Getter @Setter
	private long userID;
	/**
	 * 开放标识类型
	 */
    @Id
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String openIDType;
	/**
	 * 开放标识
	 */
	@Size(max=255)
	@Getter @Setter
	private String openID;
	/**
	 * 头像
	 */
	@Size(max=255)
	@Getter @Setter
	private String avatar;
	/**
	 * 联合标识
	 */
	@Size(max=255)
	@Getter @Setter
	private String unionID;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return userID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		userID = partitionID;
	}
	//endregion of partition

	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements CompositeTenancyKey {
		/**
		 * 用户ID
		 */
		@NotNull
		@Min(0)
		@Getter @Setter
		private long userID;
		/**
		 * 开放标识类型
		 */
		@NotBlank
		@Size(min=1,max=255)
		@Getter @Setter
		private String openIDType;
		/**
		 * 获取租户分区ID
		 * 实现{@link Tenancy#getPartitionID()}接口
		 */
		@JsonIgnore
		@Override
		public final long getPartitionID(){
			return userID;
		}
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return userID + CompositeKey.KEY_DELIMITER + openIDType;
		}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key that = (Key) o;
            return userID == that.userID && that.openIDType.equals(openIDType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userID, openIDType);
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
	@Override
	public final Key getId(){
		return new Key(userID, openIDType);
	}

	@Override
	public Class<Key> getIdClass() {
		return Key.class;
	}

	//endregion of ~GENERATED PARTS END

    @Override
    public String toString() {
        return "{" + openIDType + "}" + openID;
    }

	/**
	 * 关联的用户
	 */
	@ManyToOne
	@JoinColumn(name = "userID", insertable = false, updatable = false)
	private User user;
}
