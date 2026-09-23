/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.EnumSet;
/**
 * 域合计设置枚举集合
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 * @see FieldAggregation 0;NONE;无|1;COUNT;计数|2;SUM;求和|4;AVG;平均|8;MIN;最小值|16;MAX;最大值
 */
@Deprecated
public class FieldAggregationSet extends EnumBitSet<FieldAggregation> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 默认构造函数
	 */
	private FieldAggregationSet(){
		super(FieldAggregation.class);
	}
	/**
	 * 整形值构造函数
	 * @param value 整形值
	 */
	private FieldAggregationSet(int value){
		super(FieldAggregation.class, value);
	}
	/**
	 * 枚举集构造函数
	 * @param enumSet 枚举集合
	 */
	private FieldAggregationSet(EnumSet<FieldAggregation> enumSet){
		super(FieldAggregation.class, enumSet);
	}

	
	//region (de)serializer
	/**
	 * 转换为整型数，用于Json序列化
	 */
	public static class ToIntConverter extends StdConverter<FieldAggregationSet,Integer>{
		@Override
		public Integer convert(FieldAggregationSet value) {
			return value.getValue();
		}
	}
	/**
	 * 从整型数构建枚举集合，用于Json反序列化
	 */
	public static class FromIntConverter extends StdConverter<Integer,FieldAggregationSet>{
		@Override
		public FieldAggregationSet convert(Integer value) {
			if(value == null) return new FieldAggregationSet();
			return new FieldAggregationSet(value);
		}
	}
	//endregion

	//endregion of ~GENERATED PARTS END

}
