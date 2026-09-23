/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 元维度层次级别类型
 * 
 * @remarks 级别类型
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-03-09 03:13:56.0
 * 
 */
public class MetaBiLevelType extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private int id;
	/**
	 * 
	 */
	@NotBlank
	@Size(min=1,max=50)
	@Getter @Setter
	private String name;
	/**
	 * 
	 */
	@Size(max=255)
	@Getter @Setter
	private String text;
	/**
	 * 作为主键
	 */
	public final Integer asKey(){
		return id;
	}
	//endregion of ~GENERATED PARTS END

}
