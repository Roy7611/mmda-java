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
 * 扩展类型
 * 
 * 0;NONE;无|1;INHERITS;继承|2;EXTENDS;扩展
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-10-17 10:33:52.0
 * 
 */
public enum ExtensionType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"无"),
	INHERITS(1,"继承"),
	EXTENDS(2,"扩展");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ExtensionType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ExtensionType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//无
	public static final byte INHERITS_VAL = 1;//继承
	public static final byte EXTENDS_VAL = 2;//扩展
	
	public static Map<Byte,ExtensionType> enumMap = new LinkedHashMap<Byte,ExtensionType>(){{
		put(NONE_VAL,NONE);
		put(INHERITS_VAL,INHERITS);
		put(EXTENDS_VAL,EXTENDS);
	}};
	//endregion of ~GENERATED PARTS END

}
