/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.DataDimensionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 元维度
 * 
 * @remarks 元维度。定义一个多维数据集（Cube）中的一个维度，维度可能包含多个层次结构（Hierarchies）。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-03-09 03:13:55.0
 * 
 */
public class MetaBiDimension extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 数据集名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String cubeName;
	/**
	 * 维度名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String dimensionName;
	/**
	 * 标题
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String displayLabel;
	/**
	 * 维度类型：0;UNKNOWN;未知|1;TIME;时间|2;MEASURE;度量|3;OTHER;其他|17;GEOGRAPHY;地理|18;ORGANIZATION;组织
	 */
	@NotNull
	@Getter @Setter
	private DataDimensionType dimensionType;
	/**
	 * 可写
	 */
	@NotNull
	@Getter @Setter
	private boolean writeEnabled;
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
	 * 层次结构
	 */
	@Getter @Setter
	@Valid
	private List<MetaBiHierarchy> hierarchies;
	public final boolean hasHierarchies(){
		return hasAny(hierarchies);
	}
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
		 * 维度名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String dimensionName;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return cubeName + COMPOSITE_KEY_SEPARATOR + dimensionName;
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
		return new Key(cubeName, dimensionName);
	}
	//endregion of ~GENERATED PARTS END

}
