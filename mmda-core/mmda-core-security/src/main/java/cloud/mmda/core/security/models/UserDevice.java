/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

import lombok.Data;

import cloud.mmda.core.enums.UserDeviceStatus;
/**
 * 用户终端设备
 * 
 * @remarks 用户终端设备
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-17 07:38:59.0
 * 
 */
@Data
@Entity
@Table(name="userdevice",catalog = "mmda_base")
public class UserDevice {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 设备唯一标识
	 */
	@Id
	@NotBlank
	@Size(min=1,max=64)
	private String deviceUUID;
	/**
	 * 设备名称
	 */
	@Size(max=255)
	private String deviceName;
	/**
	 * 用户ID
	 */
	@NotNull
	@Min(0)
	private long userID;
	/**
	 * 平台。例如 ios,android,windows, mac
	 */
	@NotBlank
	@Size(min=1,max=10)
	private String platform;
	/**
	 * 制造厂商
	 */
	@Size(max=30)
	private String manufacturer;
	/**
	 * 操作系统型号
	 */
	@Size(max=30)
	private String osModel;
	/**
	 * 操作系统版本
	 */
	@Size(max=10)
	private String osVersion;
	/**
	 * 语言区域，如zh-CN
	 */
	@Size(max=10)
	private String locale;
	/**
	 * App版本号
	 */
	@NotBlank
	@Size(min=1,max=10)
	private String appVersion;
	/**
	 * 状态：0;OFFLINE;离线|1;ONLINE;在线|-1;DEPRECATED;已弃用
	 */
	@NotNull
	private UserDeviceStatus status;
	/**
	 * 最近登录时间
	 */
	private Timestamp lastSignedIn;
	/**
	 * 最近登录IP
	 */
	@Size(max=39)
	private String lastSignedInIP;

	//endregion of ~GENERATED PARTS END
	/**
	 * 关联的用户
	 */
	@ManyToOne
	@JoinColumn(name = "userID", insertable = false, updatable = false)
	private User user;
}
