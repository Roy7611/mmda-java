/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 模块许可状态
 * 
 * 0;UNLICENSED;未经许可|1;TRIAL_LICENSED;试用许可|2;LICENSED;正式许可|4;LICENSE_EXPIRED;许可过期
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-04-13T20:42:03.032
 * 
 */
public enum ModuleLicenseStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	UNLICENSED(0,"未经许可"),
	TRIAL_LICENSED(1,"试用许可"),
	LICENSED(2,"正式许可"),
	LICENSE_EXPIRED(4,"许可过期");

	private final Short value;
	@Override
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleLicenseStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public final boolean hasFlag(ModuleLicenseStatus e){
		return (value & e.value) == e.value;
	}
	public static final ModuleLicenseStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short UNLICENSED_VAL = 0;//未经许可
	public static final short TRIAL_LICENSED_VAL = 1;//试用许可
	public static final short LICENSED_VAL = 2;//正式许可
	public static final short LICENSE_EXPIRED_VAL = 4;//许可过期
	
	public static Map<Short,ModuleLicenseStatus> enumMap = new LinkedHashMap<Short,ModuleLicenseStatus>(){{
		put(UNLICENSED_VAL,UNLICENSED);
		put(TRIAL_LICENSED_VAL,TRIAL_LICENSED);
		put(LICENSED_VAL,LICENSED);
		put(LICENSE_EXPIRED_VAL,LICENSE_EXPIRED);
	}};
	//endregion of ~GENERATED PARTS END

}
