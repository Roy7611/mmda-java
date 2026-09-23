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
 * 串烧模式
 * 
 * 0;NONE;无|1;SINGLE;单个|2;MULTIPLE;多个
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2025-01-20 20:44:33.0
 * 
 */
public enum SkeweredMode implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"无"),
	SINGLE(1,"单个"),
	MULTIPLE(2,"多个");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	SkeweredMode(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final SkeweredMode valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//无
	public static final byte SINGLE_VAL = 1;//单个
	public static final byte MULTIPLE_VAL = 2;//多个
	
	public static Map<Byte,SkeweredMode> enumMap = new LinkedHashMap<Byte,SkeweredMode>(){{
		put(NONE_VAL,NONE);
		put(SINGLE_VAL,SINGLE);
		put(MULTIPLE_VAL,MULTIPLE);
	}};
	//endregion of ~GENERATED PARTS END

}
