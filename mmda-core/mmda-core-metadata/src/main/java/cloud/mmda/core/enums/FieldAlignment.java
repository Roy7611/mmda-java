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
 * 域对齐方式
 * 
 * 0;LEFT;左对齐|1;RIGHT;右对齐|2;CENTER;居中|3;JUSTIFY;两边|4;START;开始|5;END;结束
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum FieldAlignment implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	LEFT(0,"左对齐"),
	RIGHT(1,"右对齐"),
	CENTER(2,"居中"),
	JUSTIFY(3,"两边"),
	START(4,"开始"),
	END(5,"结束");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	FieldAlignment(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final FieldAlignment valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte LEFT_VAL = 0;//左对齐
	public static final byte RIGHT_VAL = 1;//右对齐
	public static final byte CENTER_VAL = 2;//居中
	public static final byte JUSTIFY_VAL = 3;//两边
	public static final byte START_VAL = 4;//开始
	public static final byte END_VAL = 5;//结束
	
	public static Map<Byte,FieldAlignment> enumMap = new LinkedHashMap<Byte,FieldAlignment>(){{
		put(LEFT_VAL,LEFT);
		put(RIGHT_VAL,RIGHT);
		put(CENTER_VAL,CENTER);
		put(JUSTIFY_VAL,JUSTIFY);
		put(START_VAL,START);
		put(END_VAL,END);
	}};
	//endregion of ~GENERATED PARTS END

}
