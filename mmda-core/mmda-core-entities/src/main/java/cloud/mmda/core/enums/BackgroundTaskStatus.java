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
 * 任务状态
 * 
 * 0;NEW;新任务|1;RUNNING;执行中|2;SUSPENDED;已暂停|8;SUCCEEDED;成功|-4;CANCELED;已取消|-8;FAILEDED;失败
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-07 10:30:04.0
 * 
 */
public enum BackgroundTaskStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	NEW(0,"新任务"),
	RUNNING(1,"执行中"),
	SUSPENDED(2,"已暂停"),
	SUCCESS(8,"成功"),
	CANCELED(-4,"已取消"),
	FAILED(-8,"失败");

	private final Short value;
	
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	BackgroundTaskStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public static final BackgroundTaskStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short NEW_VAL = 0;
	public static final short RUNNING_VAL = 1;
	public static final short SUSPENDED_VAL = 2;
	public static final short SUCCESS_VAL = 8;
	public static final short CANCELED_VAL = -4;
	public static final short FAILED_VAL = -8;

	
	public static Map<Short, BackgroundTaskStatus> enumMap = new LinkedHashMap<Short, BackgroundTaskStatus>(){{
		put(NEW_VAL,NEW);
		put(RUNNING_VAL,RUNNING);
		put(SUSPENDED_VAL,SUSPENDED);
		put(SUCCESS_VAL,SUCCESS);
		put(CANCELED_VAL,CANCELED);
		put(FAILED_VAL,FAILED);
	}};
	//endregion of ~GENERATED PARTS END

}
