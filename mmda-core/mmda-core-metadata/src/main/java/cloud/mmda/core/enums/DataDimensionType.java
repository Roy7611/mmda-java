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
 * 维度类型
 * 
 * 0;UNKNOWN;未知|1;TIME;时间|2;MEASURE;度量|3;OTHER;其他|17;GEOGRAPHY;地理|18;ORGANIZATION;组织
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum DataDimensionType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	UNKNOWN(0,"未知"),
	TIME(1,"时间"),
	MEASURE(2,"度量"),
	OTHER(3,"其他"),
	GEOGRAPHY(17,"地理"),
	ORGANIZATION(18,"组织");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	DataDimensionType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final DataDimensionType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte UNKNOWN_VAL = 0;//未知
	public static final byte TIME_VAL = 1;//时间
	public static final byte MEASURE_VAL = 2;//度量
	public static final byte OTHER_VAL = 3;//其他
	public static final byte GEOGRAPHY_VAL = 17;//地理
	public static final byte ORGANIZATION_VAL = 18;//组织
	
	public static Map<Byte,DataDimensionType> enumMap = new LinkedHashMap<Byte,DataDimensionType>(){{
		put(UNKNOWN_VAL,UNKNOWN);
		put(TIME_VAL,TIME);
		put(MEASURE_VAL,MEASURE);
		put(OTHER_VAL,OTHER);
		put(GEOGRAPHY_VAL,GEOGRAPHY);
		put(ORGANIZATION_VAL,ORGANIZATION);
	}};
	//endregion of ~GENERATED PARTS END

}
