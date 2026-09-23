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
 * 显示形状
 * 
 * 0;LIST;列表|1;TREE;树形|2;HIERARCHY;层次|
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-12-23 21:48:11.0
 * 
 */
public enum DisplayShape implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	LIST(0,"列表"),
	TREE(1,"树形"),
	HIERARCHY(2,"层次"),
	PHOTO(3,"图片"),//ky 拓展	子表的显示形状 为九宫格
    BPMN(4,"流程图");//ky 拓展	BPMN

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	DisplayShape(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final DisplayShape valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte LIST_VAL = 0;//列表
	public static final byte TREE_VAL = 1;//树形
	public static final byte HIERARCHY_VAL = 2;//层次
	public static final byte PHOTO_VAL = 3;//图片
	public static final byte BPMN_VAL = 4;//流程图

	public static Map<Byte,DisplayShape> enumMap = new LinkedHashMap<Byte,DisplayShape>(){{
		put(LIST_VAL,LIST);
		put(TREE_VAL,TREE);
		put(HIERARCHY_VAL, HIERARCHY);
		put(PHOTO_VAL, PHOTO);
		put(BPMN_VAL, BPMN);
	}};
	//endregion of ~GENERATED PARTS END

}
