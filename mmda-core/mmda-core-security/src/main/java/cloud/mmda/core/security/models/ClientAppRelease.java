/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.security.models;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

/**
 * 客户端应用发布
 * 
 * @remarks 客户端应用发布
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
@Data
@Entity
@IdClass(ClientAppRelease.Key.class)
@Table(name="clientapprelease",catalog = "mmda_base")
public class ClientAppRelease {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 应用标识
	 */
	@Id
	@NotBlank
	@Size(min=1,max=36)
	private String appId;
	/**
	 * 发布版本，比如1.0.0
	 */
	@Id
	@NotBlank
	@Size(min=1,max=30)
	private String releasedVersion;
	/**
	 * 发布时间
	 */
	@NotNull
	private Timestamp releasedAt;
	/**
	 * 获取链接
	 */
	@NotBlank
	@Size(min=1,max=255)
	private String getUri;
	/**
	 * 发布日志Uri，说明更新了哪些内容
	 */
	@NotBlank
	@Size(min=1,max=255)
	private String releaseNotesUri;
	/**
	 * 主键类
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable{
		/**
		 * 应用标识
		 */
		@NotBlank
		@Size(min=1,max=36)
		private String appId;
		/**
		 * 发布版本，比如1.0.0
		 */
		@NotBlank
		@Size(min=1,max=30)
		private String releasedVersion;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return appId + "," + releasedVersion;
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(",");
			return new Key(keys[0],keys[1]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(appId, releasedVersion);
	}

	//endregion of ~GENERATED PARTS END

}
