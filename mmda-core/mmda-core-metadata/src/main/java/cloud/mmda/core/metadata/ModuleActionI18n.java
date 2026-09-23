/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * 模块操作国际化
 * 
 * 模块功能操作国际化
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.613
 * 
 */
public class ModuleActionI18n extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 模块编码
	 */
	@NotBlank
	@Size(min=1,max=15)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 操作名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String actionName;
	/**
	 * 语言区域
	 */
	@NotBlank
	@Size(min=1,max=10)
	@Getter @Setter
	private String locale;
	/**
	 * 操作标签
	 */
	@Size(max=30)
	@Getter @Setter
	private String displayLabel;
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
		 * 模块编码
		 */
		@NotBlank
		@Size(min=1,max=15)
		@Getter @Setter
		private String moduleCode;
		/**
		 * 操作名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String actionName;
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
			return moduleCode + COMPOSITE_KEY_SEPARATOR + actionName + COMPOSITE_KEY_SEPARATOR + locale;
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(COMPOSITE_KEY_SEPARATOR);
			return new Key(keys[0],keys[1],keys[2]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(moduleCode, actionName, locale);
	}
	//endregion of ~GENERATED PARTS END

}
