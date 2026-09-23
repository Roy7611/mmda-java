/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;
import java.io.Serializable;

import cloud.mmda.core.enums.SkeweredMode;
import jakarta.validation.constraints.*;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 模块串烧
 * 
 * @remarks 模块串烧
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2025-01-20 20:44:33.0
 * 
 */
public class ModuleSkewer extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 模块编码，A.01.001
	 */
	@NotBlank
	@Size(min=1,max=15)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 串烧模块
	 */
	@NotBlank
	@Size(min=1,max=15)
	@Getter @Setter
	private String skeweredModuleCode;
	/**
	 * 串烧模式：0;NONE;无|1;SINGLE;单个|2;MULTIPLE;多个
	 */
	@NotNull
	@Getter @Setter
	private SkeweredMode skeweredMode;
	/**
	 * 串烧键
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String skeweredKey;
	//region key & sn
	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable{
		/**
		 * 模块编码，A.01.001
		 */
		@NotBlank
		@Size(min=1,max=15)
		@Getter @Setter
		private String moduleCode;
		/**
		 * 串烧模块
		 */
		@NotBlank
		@Size(min=1,max=15)
		@Getter @Setter
		private String skeweredModuleCode;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return moduleCode + COMPOSITE_KEY_SEPARATOR + skeweredModuleCode;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Key key)) return false;
			return moduleCode.equals(key.moduleCode) && skeweredModuleCode.equals(key.skeweredModuleCode);
		}
		@Override
		public int hashCode() {
			return Objects.hash(moduleCode, skeweredModuleCode);
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(COMPOSITE_KEY_SEPARATOR);
			return new Key(keys[0],keys[1]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(moduleCode, skeweredModuleCode);
	}
	//endregion of key & sn

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _moduleCode = "moduleCode";
		public static final String _skeweredModuleCode = "skeweredModuleCode";
		public static final String _skeweredMode = "skeweredMode";
		public static final String _skeweredKey = "skeweredKey";
	}
	//endregion of ~GENERATED PARTS END

}
