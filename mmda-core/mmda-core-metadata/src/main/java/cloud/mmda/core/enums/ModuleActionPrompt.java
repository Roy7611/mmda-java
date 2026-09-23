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
 * 模块操作提示
 * 
 * 0;NONE;-|1;CONFIRM;确认|2;FLOW_TO;流转
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-10 18:17:15.0
 * 
 */
public enum ModuleActionPrompt implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"-"),
	CONFIRM(1,"确认"),
	FLOW_TO(2,"流转");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleActionPrompt(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ModuleActionPrompt valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//-
	public static final byte CONFIRM_VAL = 1;//确认
	public static final byte FLOW_TO_VAL = 2;//流转
	
	public static Map<Byte,ModuleActionPrompt> enumMap = new LinkedHashMap<Byte,ModuleActionPrompt>(){{
		put(NONE_VAL,NONE);
		put(CONFIRM_VAL,CONFIRM);
		put(FLOW_TO_VAL,FLOW_TO);
	}};
	//endregion of ~GENERATED PARTS END

}
