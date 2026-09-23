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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * 元度量
 * 
 * @remarks 元度量。定义多维数据集中的一个度量。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-03-09 03:13:56.0
 * 
 */
public class MetaBiMeasure extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 数据集名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String cubeName;
	/**
	 * 度量名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String measureName;
	/**
	 * 标题
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String displayLabel;
	/**
	 * 文件夹
	 */
	@NotBlank
	@Size(min=1,max=50)
	@Getter @Setter
	private String displayFolder;
	/**
	 * 表达式
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String expression;
	/**
	 * 精度
	 */
	@NotNull
	@Getter @Setter
	private int numericPrecision;
	/**
	 * 小数
	 */
	@NotNull
	@Getter @Setter
	private short numericScale;
	/**
	 * 单位
	 */
	@Size(max=10)
	@Getter @Setter
	private String unit;
	/**
	 * 描述
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	/**
	 * 自定义属性
	 */
	@Size(max=2000)
	@Getter @Setter
	private String customJson;
	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable{
		/**
		 * 数据集名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String cubeName;
		/**
		 * 度量名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String measureName;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return cubeName + COMPOSITE_KEY_SEPARATOR + measureName;
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
		return new Key(cubeName, measureName);
	}
	//endregion of ~GENERATED PARTS END

}
