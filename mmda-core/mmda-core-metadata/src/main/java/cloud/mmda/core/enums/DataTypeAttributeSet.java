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
import java.util.stream.Collectors;

/**
 * 数据类型扩展属性枚举集合
 * 
 * @author mmda code robot 
 * @version 4.0 
 *
 * @see DataTypeAttribute
 */
@Deprecated
public class DataTypeAttributeSet extends EnumBitSet<DataTypeAttribute> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 默认构造函数
	 */
	private DataTypeAttributeSet(){
		super(DataTypeAttribute.class);
	}
	/**
	 * 整形值构造函数
	 * @param value 整形值
	 */
	private DataTypeAttributeSet(int value){
		super(DataTypeAttribute.class, value);
	}
	/**
	 * 枚举集构造函数
	 * @param enumSet 枚举集合
	 */
	private DataTypeAttributeSet(EnumSet<DataTypeAttribute> enumSet){
		super(DataTypeAttribute.class, enumSet);
	}

	
	//region static constructors
	/**
	 * 空枚举集
	 * @return 
	 */
	public static final DataTypeAttributeSet noneOf() {
		return new DataTypeAttributeSet();
	}
	/**
	 * 整数值枚举集
	 * @param value 
	 * @return 
	 */
	public static final DataTypeAttributeSet valueOf(int value) {
		return new DataTypeAttributeSet(value);
	}
	/**
	 * 枚举集
	 * @param enumSet 
	 * @return 
	 */
	public static final DataTypeAttributeSet setOf(EnumSet<DataTypeAttribute> enumSet) {
		return new DataTypeAttributeSet(enumSet);
	}
	/**
	 * 多个枚举值的枚举集
	 * @param first
	 * @param elements
	 * @return 
	 */
	public static final DataTypeAttributeSet of(DataTypeAttribute first, DataTypeAttribute...elements) {
		var enumSet = EnumSet.of(first,elements);
		for(DataTypeAttribute e : elements){
			enumSet.add(e);
		}
		return new DataTypeAttributeSet(enumSet);
	}
	/**
	 * 所有成员的枚举集
	 * @return 
	 */
	public static final DataTypeAttributeSet allOf() {
		return new DataTypeAttributeSet(EnumSet.allOf(DataTypeAttribute.class));
	}
	/**
	 * 默认枚举集
	 * @return 
	 */
	public static final DataTypeAttributeSet ofDefault() {
		var enumSet = EnumSet.of(DataTypeAttribute.NONE);
		return new DataTypeAttributeSet(enumSet);
	}
	/**
	 * 所有位为1的枚举集
	 * @return 
	 */
	public static final DataTypeAttributeSet allBitOf() {
		var enumSet = EnumSet.range(DataTypeAttribute.MAX_LENGTH, DataTypeAttribute.BYTE_RANGE);
		return new DataTypeAttributeSet(enumSet);
	}
	//endregion
	
	//region (de)serializer
	/**
	 * 转换为整型数，用于Json序列化
	 */
	public static class ToIntConverter extends StdConverter<DataTypeAttributeSet,Integer>{
		@Override
		public Integer convert(DataTypeAttributeSet value) {
			return value.getValue();
		}
	}
	/**
	 * 从整型数构建枚举集合，用于Json反序列化
	 */
	public static class FromIntConverter extends StdConverter<Integer, DataTypeAttributeSet>{
		@Override
		public DataTypeAttributeSet convert(Integer value) {
			if(value == null) return DataTypeAttributeSet.noneOf();
			return DataTypeAttributeSet.valueOf(value);
		}
	}
	//endregion
	
	//region converters between EnumSet and int
	public static final EnumSet<DataTypeAttribute> setOf(int value){
		var enumSet = EnumSet.noneOf(DataTypeAttribute.class);
		if(value>0) {
			for(var e : EnumSet.allOf(DataTypeAttribute.class)){
				if(EnumBitValue.hasBit(value, e.getBit())) enumSet.add(e);
			}
		}
		return enumSet;
	}

	//endregion
	//endregion of ~GENERATED PARTS END

}
