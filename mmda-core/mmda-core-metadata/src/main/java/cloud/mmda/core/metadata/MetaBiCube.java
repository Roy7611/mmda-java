/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.DataCubeType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * 元多维数据集
 * 
 * @remarks 元多维数据集。定义一个多维数据集，包含多个元维度（Dimension）、元度量（Measure）。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-03-09 03:13:55.0
 * 
 */
public class MetaBiCube extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 数据集名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String cubeName;
	/**
	 * 数据集类型：0;UNKNOWN;未知|1;CUBE;立方体|2;DIMENSION;维度
	 */
	@NotNull
	@Getter @Setter
	private DataCubeType cubeType;
	/**
	 * 标题
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String displayLabel;
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
	 * 创建时间
	 */
	@Getter @Setter
	private Timestamp createDate;
	/**
	 * 最后修改
	 */
	@Getter @Setter
	private Timestamp lastModified;
	/**
	 * 维度
	 */
	@Getter @Setter
	@Valid
	@NotEmpty
	private List<MetaBiDimension> dimensions;
	public final boolean hasDimensions(){
		return hasAny(dimensions);
	}
	/**
	 * 度量
	 */
	@Getter @Setter
	@Valid
	@NotEmpty
	private List<MetaBiMeasure> measures;
	public final boolean hasMeasures(){
		return hasAny(measures);
	}
	/**
	 * 作为主键
	 */
	public final String asKey(){
		return cubeName;
	}
	//endregion of ~GENERATED PARTS END

}
