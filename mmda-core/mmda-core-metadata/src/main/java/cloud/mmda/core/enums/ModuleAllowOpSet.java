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
import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumBitValue;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.EnumSet;
import java.util.stream.Collectors;
/**
 * 模块允许操作枚举集合
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 * @see ModuleAllowOp 0;NONE;无|1;READ;读取|2;EDIT;编辑|4;CREATE;创建|8;DELETE;删除|15;CRUD;增删改|16;PRINT;打印|32;IMPORT;导入|64;EXPORT;导出|127;ALL;所有
 */
@Deprecated
public class ModuleAllowOpSet extends EnumBitSet<ModuleAllowOp> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 默认构造函数
	 */
	private ModuleAllowOpSet(){
		super(ModuleAllowOp.class);
	}
	/**
	 * 整形值构造函数
	 * @param value 整形值
	 */
	private ModuleAllowOpSet(int value){
		super(ModuleAllowOp.class, value);
	}
	/**
	 * 枚举集构造函数
	 * @param enumSet 枚举集合
	 */
	private ModuleAllowOpSet(EnumSet<ModuleAllowOp> enumSet){
		super(ModuleAllowOp.class, enumSet);
	}

	//region static constructors
	/**
	 * 空枚举集
	 * @return 
	 */
	public static final ModuleAllowOpSet noneOf() {
		return new ModuleAllowOpSet();
	}
	/**
	 * 整数值枚举集
	 * @param value 
	 * @return 
	 */
	public static final ModuleAllowOpSet valueOf(int value) {
		return new ModuleAllowOpSet(value);
	}
	/**
	 * 枚举集
	 * @param enumSet 
	 * @return 
	 */
	public static final ModuleAllowOpSet setOf(EnumSet<ModuleAllowOp> enumSet) {
		return new ModuleAllowOpSet(enumSet);
	}
	/**
	 * 多个枚举值的枚举集
	 * @param first 
	 * @param elements 
	 * @return 
	 */
	public static final ModuleAllowOpSet of(ModuleAllowOp first, ModuleAllowOp...elements) {
		var enumSet = EnumSet.of(first,elements);
		for(ModuleAllowOp e : elements){
			enumSet.add(e);
		}
		return new ModuleAllowOpSet(enumSet);
	}
	/**
	 * 所有成员的枚举集
	 * @return 
	 */
	public static final ModuleAllowOpSet allOf() {
		return new ModuleAllowOpSet(EnumSet.allOf(ModuleAllowOp.class));
	}
	/**
	 * 默认枚举集
	 * @return 
	 */
	public static final ModuleAllowOpSet ofDefault() {
		var enumSet = EnumSet.of(ModuleAllowOp.NONE);
		return new ModuleAllowOpSet(enumSet);
	}
	/**
	 * 所有位为1的枚举集
	 * @return 
	 */
	public static final ModuleAllowOpSet allBitOf() {
		var enumSet = EnumSet.range(ModuleAllowOp.READ,ModuleAllowOp.ALL);
		return new ModuleAllowOpSet(enumSet);
	}
	//endregion
	
	//region (de)serializer
	/**
	 * 转换为整型数，用于Json序列化
	 */
	public static class ToIntConverter extends StdConverter<ModuleAllowOpSet,Integer>{
		@Override
		public Integer convert(ModuleAllowOpSet value) {
			return value.getValue();
		}
	}
	/**
	 * 从整型数构建枚举集合，用于Json反序列化
	 */
	public static class FromIntConverter extends StdConverter<Integer,ModuleAllowOpSet>{
		@Override
		public ModuleAllowOpSet convert(Integer value) {
			if(value == null) return ModuleAllowOpSet.noneOf();
			return ModuleAllowOpSet.valueOf(value);
		}
	}
	//endregion
	
	//region converters between EnumSet and int
	public static final EnumSet<ModuleAllowOp> setOf(int value){
		var enumSet = EnumSet.noneOf(ModuleAllowOp.class);
		if(value>0) {
			for(var e : EnumSet.allOf(ModuleAllowOp.class)){
				if(EnumBitValue.hasBit(value, e.getBit())) enumSet.add(e);
			}
		}
		return enumSet;
	}

	//endregion
	//endregion of ~GENERATED PARTS END

}
