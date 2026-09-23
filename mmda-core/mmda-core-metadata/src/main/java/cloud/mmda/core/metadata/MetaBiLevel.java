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
 * 元维度层次级别
 * 
 * @remarks 级别。级别是层次结构中的一个特定层，代表成员的不同粒度或详细程度，如时间维度中的年、季度、月等级别。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-03-09 03:13:56.0
 * 
 */
public class MetaBiLevel extends MetaEntity {
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
	 * 层次结构名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String hierarchyName;
	/**
	 * 级别名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String levelName;
	/**
	 * 级别类型
	 */
	@NotNull
	@Getter @Setter
	private int levelType;
	/**
	 * 标题
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String displayLabel;
	/**
	 * 级别数
	 */
	@NotNull
	@Getter @Setter
	private int levelNumber;
	/**
	 * 成员数量
	 */
	@NotNull
	@Getter @Setter
	private long memberCount;
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
		 * 维度名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String dimensionName;
		/**
		 * 层次结构名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String hierarchyName;
		/**
		 * 级别名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String levelName;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return cubeName + COMPOSITE_KEY_SEPARATOR + dimensionName + COMPOSITE_KEY_SEPARATOR + hierarchyName + COMPOSITE_KEY_SEPARATOR + levelName;
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(COMPOSITE_KEY_SEPARATOR);
			return new Key(keys[0],keys[1],keys[2],keys[3]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(cubeName, dimensionName, hierarchyName, levelName);
	}
	//endregion of ~GENERATED PARTS END

}
