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
 * 数据集类型
 * 
 * 0;UNKNOWN;未知|1;CUBE;立方体|2;DIMENSION;维度
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum DataCubeType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	UNKNOWN(0,"未知"),
	CUBE(1,"立方体"),
	DIMENSION(2,"维度");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	DataCubeType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final DataCubeType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte UNKNOWN_VAL = 0;//未知
	public static final byte CUBE_VAL = 1;//立方体
	public static final byte DIMENSION_VAL = 2;//维度
	
	public static Map<Byte,DataCubeType> enumMap = new LinkedHashMap<Byte,DataCubeType>(){{
		put(UNKNOWN_VAL,UNKNOWN);
		put(CUBE_VAL,CUBE);
		put(DIMENSION_VAL,DIMENSION);
	}};
	//endregion of ~GENERATED PARTS END

}
