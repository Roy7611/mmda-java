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
 * 模块操作类型
 * 
 * 0;USER_TASK;用户任务|1;SERVICE_TASK;服务任务|2;DICISION;自动判断
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum ModuleActionType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	USER_TASK(0,"用户任务"),
	SERVICE_TASK(1,"服务任务"),
	DICISION(2,"自动判断");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleActionType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ModuleActionType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte USER_TASK_VAL = 0;//用户任务
	public static final byte SERVICE_TASK_VAL = 1;//服务任务
	public static final byte DICISION_VAL = 2;//自动判断
	
	public static Map<Byte,ModuleActionType> enumMap = new LinkedHashMap<Byte,ModuleActionType>(){{
		put(USER_TASK_VAL,USER_TASK);
		put(SERVICE_TASK_VAL,SERVICE_TASK);
		put(DICISION_VAL,DICISION);
	}};
	//endregion of ~GENERATED PARTS END

}
