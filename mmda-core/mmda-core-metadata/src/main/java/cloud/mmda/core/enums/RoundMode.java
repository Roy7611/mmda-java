/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * Syc PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import lombok.Getter;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 取整模式
 * 
 * 0;NONE;不取整|1;ONE;逢一进位|3;THREE;二舍三入|5;FIVE;四舍五入
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum RoundMode implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"不取整"),
	ONE(1,"逢一进位"),
	THREE(3,"二舍三入"),
	FIVE(5,"四舍五入");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	RoundMode(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final RoundMode valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//不取整
	public static final byte ONE_VAL = 1;//逢一进位
	public static final byte THREE_VAL = 3;//二舍三入
	public static final byte FIVE_VAL = 5;//四舍五入
	
	public static Map<Byte,RoundMode> enumMap = new LinkedHashMap<Byte,RoundMode>(){{
		put(NONE_VAL,NONE);
		put(ONE_VAL,ONE);
		put(THREE_VAL,THREE);
		put(FIVE_VAL,FIVE);
	}};
	//endregion of ~GENERATED PARTS END

}
