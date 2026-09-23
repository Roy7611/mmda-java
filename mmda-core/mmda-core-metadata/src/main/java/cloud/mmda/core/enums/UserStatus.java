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
 * 用户状态
 * 
 * 0;NEW;新注册|1;ACTIVATED;已激活|-1;LOCKED;锁定|-2;DEACTIVATED;已注销
 * 
 * @author mmda code robot
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum UserStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	NEW(0,"新注册"),
	ACTIVATED(1,"已激活"),
	LOCKED(-1,"已锁定"),
	DEACTIVATED(-2,"已注销");

	private final Short value;
	@Override
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	UserStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public static final UserStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short NEW_VAL = 0;//新注册
	public static final short ACTIVATED_VAL = 1;//已激活
	public static final short LOCKED_VAL = -1;//锁定
	public static final short DEACTIVATED_VAL = -2;//已注销
	
	public static Map<Short,UserStatus> enumMap = new LinkedHashMap<Short,UserStatus>(){{
		put(NEW_VAL,NEW);
		put(ACTIVATED_VAL, ACTIVATED);
		put(LOCKED_VAL,LOCKED);
		put(DEACTIVATED_VAL, DEACTIVATED);
	}};
	//endregion of ~GENERATED PARTS END

}
