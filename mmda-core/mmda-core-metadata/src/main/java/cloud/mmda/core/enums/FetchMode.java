/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import lombok.Getter;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import cloud.mmda.core.enums.EnumValue;
/**
 * 加载模式
 * 
 * 0;EAGER;急加载|1;LAZY;懒加载
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum FetchMode implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	EAGER(0,"急加载"),
	LAZY(1,"懒加载");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	FetchMode(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}

	public static final FetchMode valueOf(byte value){
		return value == 1 ? LAZY : EAGER;
	}

//	public static final byte EAGER_VAL = 0;//急加载
//	public static final byte LAZY_VAL = 1;//懒加载
//
//	public static Map<Byte,FetchMode> enumMap = new LinkedHashMap<Byte,FetchMode>(){{
//		put(EAGER_VAL,EAGER);
//		put(LAZY_VAL,LAZY);
//	}};
	//endregion of ~GENERATED PARTS END

}
