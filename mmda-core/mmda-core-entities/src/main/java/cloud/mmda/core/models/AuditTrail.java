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
 * 审计追踪
 * 
 * @remarks 审计追踪
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
public class AuditTrail extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 审计标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long trailID;
	/**
	 * 审计时间
	 */
	@NotNull
	@Getter @Setter
	private Timestamp auditTime;
	/**
	 * 用户标识：REF User(userID,userName)
	 */
	@Getter @Setter
	private Long userID;
	/**
	 * 模块编码
	 */
	@Size(max=15)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 操作
	 */
	@Size(max=255)
	@Getter @Setter
	private String operation;
	/**
	 * 访问设备
	 */
	@Size(max=64)
	@Getter @Setter
	private String deviceUUID;
	/**
	 * 访问IP
	 */
	@Size(max=15)
	@Getter @Setter
	private String ipAddress;
	/**
	 * 访问IPv6
	 */
	@Size(max=39)
	@Getter @Setter
	private String ipAddressV6;
	/**
	 * 经度
	 */
	@Getter @Setter
	private Float longitude;
	/**
	 * 纬度
	 */
	@Getter @Setter
	private Float latitude;
	/**
	 * 修改日志标识，引用ChangeLog.logID
	 */
	@Getter @Setter
	private Long changeLogID;
	/**
	 * 备注
	 */
	@Size(max=255)
	@Getter @Setter
	private String remark;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return trailID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		trailID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	public final Long getId(){
		return trailID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _trailID = "trailID";
		public static final String _auditTime = "auditTime";
		public static final String _userID = "userID";
		public static final String _moduleCode = "moduleCode";
		public static final String _operation = "operation";
		public static final String _deviceUUID = "deviceUUID";
		public static final String _ipAddress = "ipAddress";
		public static final String _ipAddressV6 = "ipAddressV6";
		public static final String _longitude = "longitude";
		public static final String _latitude = "latitude";
		public static final String _changeLogID = "changeLogID";
		public static final String _remark = "remark";
		
	}
	//endregion of ~GENERATED PARTS END

}
