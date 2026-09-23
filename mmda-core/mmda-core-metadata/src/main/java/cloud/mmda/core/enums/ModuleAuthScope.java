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
 * 模块权限范围
 * 
 * 0;SELF;本人|1;GROUP;组|2;DEPARTMENT;部门|4;DIVISION;子公司|8;ALL;全局
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum ModuleAuthScope implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	SELF(0,"本人"),
	GROUP(1,"组"),
	DEPARTMENT(2,"部门"),
	DIVISION(4,"子公司"),
	ALL(8,"全局");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleAuthScope(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ModuleAuthScope valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte SELF_VAL = 0;//本人
	public static final byte GROUP_VAL = 1;//组
	public static final byte DEPARTMENT_VAL = 2;//部门
	public static final byte DIVISION_VAL = 4;//子公司
	public static final byte ALL_VAL = 8;//全局
	
	public static Map<Byte, ModuleAuthScope> enumMap = new LinkedHashMap<Byte, ModuleAuthScope>(){{
		put(SELF_VAL,SELF);
		put(GROUP_VAL,GROUP);
		put(DEPARTMENT_VAL,DEPARTMENT);
		put(DIVISION_VAL,DIVISION);
		put(ALL_VAL,ALL);
	}};
	//endregion of ~GENERATED PARTS END

}
