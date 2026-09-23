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
 * 重要性
 * 
 * 0;UNKNOWN;-|1;IMPORTANT;重要|2;VERY_IMPORTANT;非常重要
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-18T02:23:32.285
 * 
 */
public enum Importance implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	UNKNOWN(0,"-"),
	IMPORTANT(1,"重要"),
	VERY_IMPORTANT(2,"非常重要");

	private final Byte value;
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	Importance(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final Importance valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte UNKNOWN_VAL = 0;//-
	public static final byte IMPORTANT_VAL = 1;//重要
	public static final byte VERY_IMPORTANT_VAL = 2;//非常重要
	
	public static Map<Byte,Importance> enumMap = new LinkedHashMap<Byte,Importance>(){{
		put(UNKNOWN_VAL,UNKNOWN);
		put(IMPORTANT_VAL,IMPORTANT);
		put(VERY_IMPORTANT_VAL,VERY_IMPORTANT);
	}};
	//endregion of ~GENERATED PARTS END

}
