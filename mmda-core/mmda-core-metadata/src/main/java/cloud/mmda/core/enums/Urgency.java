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
 * 紧急性
 * 
 * 0;NORMAL;普通|1;SENIOR;优先|2;URGENT;紧急
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-18T02:23:32.285
 * 
 */
public enum Urgency implements EnumValue<Byte>{
	//region ~GENERATED PARTS BEGIN
	NORMAL(0,"普通"),
	SENIOR(1,"优先"),
	URGENT(2,"紧急");

	private final Byte value;

	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	Urgency(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final Urgency valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NORMAL_VAL = 0;//普通
	public static final byte SENIOR_VAL = 1;//优先
	public static final byte URGENT_VAL = 2;//紧急
	
	public static Map<Byte,Urgency> enumMap = new LinkedHashMap<Byte,Urgency>(){{
		put(NORMAL_VAL,NORMAL);
		put(SENIOR_VAL,SENIOR);
		put(URGENT_VAL,URGENT);
	}};
	//endregion of ~GENERATED PARTS END

}
