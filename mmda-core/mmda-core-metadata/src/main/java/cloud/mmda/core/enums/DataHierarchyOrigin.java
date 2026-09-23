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
 * 层次结构源
 * 
 * 0;USER;用户|1;PARENT_CHILD;父子|2;ATTRIBUTE;属性
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum DataHierarchyOrigin implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	USER(0,"用户"),
	PARENT_CHILD(1,"父子"),
	ATTRIBUTE(2,"属性");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	DataHierarchyOrigin(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final DataHierarchyOrigin valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte USER_VAL = 0;//用户
	public static final byte PARENT_CHILD_VAL = 1;//父子
	public static final byte ATTRIBUTE_VAL = 2;//属性
	
	public static Map<Byte,DataHierarchyOrigin> enumMap = new LinkedHashMap<Byte,DataHierarchyOrigin>(){{
		put(USER_VAL,USER);
		put(PARENT_CHILD_VAL,PARENT_CHILD);
		put(ATTRIBUTE_VAL,ATTRIBUTE);
	}};
	//endregion of ~GENERATED PARTS END

}
