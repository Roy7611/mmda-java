/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import lombok.Getter;
import java.util.LinkedHashMap;
import java.util.Map;
import cloud.mmda.core.enums.EnumValue;
/**
 * 模块类型
 * 
 * 0;SYSTEM;子系统|1;MODULE;模块组|2;FEATURE;功能项
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum ModuleType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	SYSTEM(0,"子系统"),
	MODULE(1,"模块组"),
	FEATURE(2,"功能项");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ModuleType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte SYSTEM_VAL = 0;//子系统
	public static final byte MODULE_VAL = 1;//模块组
	public static final byte FEATURE_VAL = 2;//功能项
	
	public static Map<Byte,ModuleType> enumMap = new LinkedHashMap<Byte,ModuleType>(){{
		put(SYSTEM_VAL,SYSTEM);
		put(MODULE_VAL,MODULE);
		put(FEATURE_VAL,FEATURE);
	}};
	//endregion of ~GENERATED PARTS END

}
