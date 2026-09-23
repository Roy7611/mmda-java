/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import cloud.mmda.core.data.conversion.IntegerEnumBitSetConverter;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.Getter;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 域合计设置
 * 
 * 0;NONE;无|1;COUNT;计数|2;SUM;求和|4;AVG;平均|8;MIN;最小值|16;MAX;最大值
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum FieldAggregation implements EnumBitValue{
	//region ~GENERATED PARTS BEGIN
	NONE(0,"无",-1),
	COUNT(1,"计数",0),
	SUM(2,"求和",1),
	AVG(4,"平均",2),
	MIN(8,"最小值",3),
	MAX(16,"最大值",4);

	private final Integer value;
	@Override
	public final Integer getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	@Getter
	private final int bit;

	FieldAggregation(int value, String text, int bit){
		this.value = value;
		this.text = text;
		this.bit = bit;
	}


	public final boolean hasFlag(FieldAggregation e){
		return (value & e.value) == e.value;
	}
	public static final FieldAggregation valueOf(int value){
		return enumMap.get(value);
	}

	public static final int NONE_VAL = 0;//无
	public static final int COUNT_VAL = 1;//计数
	public static final int SUM_VAL = 2;//求和
	public static final int AVG_VAL = 4;//平均
	public static final int MIN_VAL = 8;//最小值
	public static final int MAX_VAL = 16;//最大值
	
	public static Map<Integer,FieldAggregation> enumMap = new LinkedHashMap<Integer,FieldAggregation>(){{
		put(NONE_VAL,NONE);
		put(COUNT_VAL,COUNT);
		put(SUM_VAL,SUM);
		put(AVG_VAL,AVG);
		put(MIN_VAL,MIN);
		put(MAX_VAL,MAX);
	}};

	static final IntegerEnumBitSetConverter<FieldAggregation> _enumSetConverter
			= new IntegerEnumBitSetConverter<>(FieldAggregation.class);

	//region Json 序列化

	/**
	 * 从位元枚举集合转换为整数，用于Json序列化
	 */
	public static class SetToIntConverter extends StdConverter<EnumBitSet<FieldAggregation>,Integer> {
		@Override
		public Integer convert(EnumBitSet<FieldAggregation> value) {
			return _enumSetConverter.revert(value);
		}
	}
	/**
	 * 从整型数构建枚举集合，用于Json反序列化
	 */
	public static class IntToSetConverter extends StdConverter<Integer,EnumBitSet<FieldAggregation>> {
		@Override
		public EnumBitSet<FieldAggregation> convert(Integer value) {
			return _enumSetConverter.convert(value);
		}
	}
	//endregion of Json 序列化
	//endregion of ~GENERATED PARTS END

}
