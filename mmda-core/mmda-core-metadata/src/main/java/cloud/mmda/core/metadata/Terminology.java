/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 术语
 * 
 * 定义领域专业用语，符合用户习惯。
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2021-05-27T01:43:09.465
 * 
 */
public class Terminology extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 缩写
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String abbreviation;
	/**
	 * 全称
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String words;
	/**
	 * 中文解释
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	/**
	 * 作为主键
	 */
	public final String asKey(){
		return abbreviation;
	}
	//endregion of ~GENERATED PARTS END

}
