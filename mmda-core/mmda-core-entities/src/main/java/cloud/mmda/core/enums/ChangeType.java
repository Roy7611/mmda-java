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
 * 变更类型
 * 
 * 0;NONE;-|1;CHANGED;修改|2;ADDED;增项|4;REMOVED;减项
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-09-01 23:03:06.0
 * 
 */
public enum ChangeType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0," "),
	CHANGED(1,"修改"),
	ADDED(2,"增项"),
	REMOVED(4,"减项");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	ChangeType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final ChangeType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//-
	public static final byte CHANGED_VAL = 1;//修改
	public static final byte ADDED_VAL = 2;//增项
	public static final byte REMOVED_VAL = 4;//减项
	
	public static Map<Byte, ChangeType> enumMap = new LinkedHashMap<Byte, ChangeType>(){{
		put(NONE_VAL,NONE);
		put(CHANGED_VAL,CHANGED);
		put(ADDED_VAL,ADDED);
		put(REMOVED_VAL,REMOVED);
	}};
	//endregion of ~GENERATED PARTS END

}
