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
import cloud.mmda.core.enums.EnumValue;
/**
 * 消息级别
 * 
 * 0;INFO;信息|1;SUCCESS;成功|2;WARNING;警告|4;DANGER;危险
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-07 10:30:04.0
 * 
 */
public enum MessageLevel implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	INFO(0,"信息"),
	SUCCESS(1,"成功"),
	WARNING(2,"警告"),
	DANGER(4,"危险");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	MessageLevel(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final MessageLevel valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte INFO_VAL = 0;//信息
	public static final byte SUCCESS_VAL = 1;//成功
	public static final byte WARNING_VAL = 2;//警告
	public static final byte DANGER_VAL = 4;//危险
	
	public static Map<Byte,MessageLevel> enumMap = new LinkedHashMap<Byte,MessageLevel>(){{
		put(INFO_VAL,INFO);
		put(SUCCESS_VAL,SUCCESS);
		put(WARNING_VAL,WARNING);
		put(DANGER_VAL,DANGER);
	}};
	//endregion of ~GENERATED PARTS END

}
