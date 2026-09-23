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
 * 流程令牌状态
 * 
 * 0;NEW;未办理|1;DONE;已办理|-1;CANCELLED;已取消|-2;TERMINATED;已终止
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum FlowTokenStatus implements EnumValue<Integer> {
	//region ~GENERATED PARTS BEGIN
	NEW(0,"未办理"),
	DONE(1,"已办理"),
	CANCELLED(-1,"已取消"),
	TERMINATED(-2,"已终止");

	private final Integer value;
	@Override
	public final Integer getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	FlowTokenStatus(int value, String text){
		this.value = value;
		this.text = text;
	}


	public static final FlowTokenStatus valueOf(int value){
		return enumMap.get(value);
	}

	public static final int NEW_VAL = 0;//未办理
	public static final int DONE_VAL = 1;//已办理
	public static final int CANCELLED_VAL = -1;//已取消
	public static final int TERMINATED_VAL = -2;//已终止
	
	public static Map<Integer,FlowTokenStatus> enumMap = new LinkedHashMap<Integer,FlowTokenStatus>(){{
		put(NEW_VAL,NEW);
		put(DONE_VAL,DONE);
		put(CANCELLED_VAL,CANCELLED);
		put(TERMINATED_VAL,TERMINATED);
	}};
	//endregion of ~GENERATED PARTS END

}
