/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;
import java.io.Serializable;

import jakarta.validation.constraints.*;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 模块租户国际化
 * 
 * @remarks 租户启用的模块。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-21 00:28:30.0
 * 
 */
public class ModuleI18nt extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 租户ID
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private int tenantID;
	/**
	 * 模块编码
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 语言区域
	 */
	@NotBlank
	@Size(min=1,max=10)
	@Getter @Setter
	private String locale;
	/**
	 * 模块标签
	 */
	@Size(max=60)
	@Getter @Setter
	private String moduleLabel;
	/**
	 * 短标签
	 */
	@Size(max=30)
	@Getter @Setter
	private String shortLabel;
	/**
	 * 必须有创建参数
	 */
	@NotNull
	@Getter @Setter
	private boolean requiredCreateParam;
	/**
	 * 许可证编码
	 */
	@Size(max=255)
	@Getter @Setter
	private String licCode;
	/**
	 * 许可证状态
	 */
	@NotNull
	@Getter @Setter
	private short licStatus;
	/**
	 * 描述
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable{
		/**
		 * 租户ID
		 */
		@NotNull
		@Min(0)
		@Getter @Setter
		private int tenantID;
		/**
		 * 模块编码
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String moduleCode;
		/**
		 * 语言区域
		 */
		@NotBlank
		@Size(min=1,max=10)
		@Getter @Setter
		private String locale;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return tenantID + COMPOSITE_KEY_SEPARATOR + moduleCode + COMPOSITE_KEY_SEPARATOR + locale;
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(COMPOSITE_KEY_SEPARATOR);
			return new Key(Short.parseShort(keys[0]),keys[1],keys[2]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(tenantID, moduleCode, locale);
	}
	//endregion of ~GENERATED PARTS END

}
