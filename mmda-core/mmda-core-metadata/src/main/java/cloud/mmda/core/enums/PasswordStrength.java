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
 * 密码强度
 * 
 * 0;SIMPLE;简单|1;MEDIUM;中等|2;STRONG;强壮
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-03-06T05:29:35.159
 * 
 */
public enum PasswordStrength implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	SIMPLE(0,"简单"),
	MEDIUM(1,"中等"),
	STRONG(2,"强壮");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	PasswordStrength(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final PasswordStrength valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte SIMPLE_VAL = 0;//简单
	public static final byte MEDIUM_VAL = 1;//中等
	public static final byte STRONG_VAL = 2;//强壮
	
	public static Map<Byte,PasswordStrength> enumMap = new LinkedHashMap<Byte,PasswordStrength>(){{
		put(SIMPLE_VAL,SIMPLE);
		put(MEDIUM_VAL,MEDIUM);
		put(STRONG_VAL,STRONG);
	}};
	//endregion of ~GENERATED PARTS END

}
