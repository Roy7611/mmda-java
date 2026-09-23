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
 * 日历类型
 * 
 * 0;GREGORIAN;公历|1;LUNAR;阴历
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2023-05-31T13:05:11.044
 * 
 */
public enum CalendarType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	GREGORIAN(0,"公历"),
	LUNAR(1,"阴历");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	CalendarType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}

	public static final CalendarType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte GREGORIAN_VAL = 0;//公历
	public static final byte LUNAR_VAL = 1;//阴历
	
	public static Map<Byte,CalendarType> enumMap = new LinkedHashMap<Byte,CalendarType>(){{
		put(GREGORIAN_VAL,GREGORIAN);
		put(LUNAR_VAL,LUNAR);
	}};
	//endregion of ~GENERATED PARTS END

}
