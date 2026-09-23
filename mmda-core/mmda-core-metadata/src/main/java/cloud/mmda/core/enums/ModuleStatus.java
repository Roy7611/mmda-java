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
 * 模块状态
 * 
 * 0;DEV;开发中|1;TESTING;测试中|2;RELEASED;已发布|-1;DEPRECATED;已停用
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum ModuleStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	DEV(0,"开发中"),
	TESTING(1,"测试中"),
	RELEASED(2,"已发布"),
	REMOVED(-1,"已下架");

	private final Short value;
	@Override
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ModuleStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public static final ModuleStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short DEV_VAL = 0;//开发中
	public static final short TESTING_VAL = 1;//测试中
	public static final short RELEASED_VAL = 2;//已发布
	public static final short REMOVED_VAL = -1;//已下架
	
	public static Map<Short,ModuleStatus> enumMap = new LinkedHashMap<Short,ModuleStatus>(){{
		put(DEV_VAL,DEV);
		put(TESTING_VAL,TESTING);
		put(RELEASED_VAL,RELEASED);
		put(REMOVED_VAL,REMOVED);
	}};
	//endregion of ~GENERATED PARTS END

}
