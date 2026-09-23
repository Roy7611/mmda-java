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
/**
 * 激活令牌
 * 
 * 0;NONE;不要求|1;ANY;任意一个|2;ALL;所有
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum FlowTokenRequirement implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"不要求"),
	ANY(1,"任意一个"),
	ALL(2,"所有");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	FlowTokenRequirement(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final FlowTokenRequirement valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//不要求
	public static final byte ANY_VAL = 1;//任意一个
	public static final byte ALL_VAL = 2;//所有
	
	public static Map<Byte,FlowTokenRequirement> enumMap = new LinkedHashMap<Byte,FlowTokenRequirement>(){{
		put(NONE_VAL,NONE);
		put(ANY_VAL,ANY);
		put(ALL_VAL,ALL);
	}};
	//endregion of ~GENERATED PARTS END

}
