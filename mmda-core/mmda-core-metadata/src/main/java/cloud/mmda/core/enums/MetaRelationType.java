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
import cloud.mmda.core.enums.EnumValue;
/**
 * 元关系类型
 * 
 * 0;NONE;-|1;HAS_ONE;一对一|2;HAS_MANY;一对多|3;REF;引用|4;ENUM;枚举
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum MetaRelationType implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"-"),
	HAS_ONE(1,"一对一"),
	HAS_MANY(2,"一对多"),
	REF(3,"引用"),
	ENUM(4,"枚举");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	MetaRelationType(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}

	public boolean hasOneOrRef(){
		return this == REF || this == HAS_ONE;
	}
	public boolean hasOneOrMany(){
		return this == HAS_MANY || this == HAS_ONE;
	}

	public static final MetaRelationType valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//-
	public static final byte HAS_ONE_VAL = 1;//一对一
	public static final byte HAS_MANY_VAL = 2;//一对多
	public static final byte REF_VAL = 3;//引用
	public static final byte ENUM_VAL = 4;//枚举
	
	public static Map<Byte,MetaRelationType> enumMap = new LinkedHashMap<Byte,MetaRelationType>(){{
		put(NONE_VAL,NONE);
		put(HAS_ONE_VAL,HAS_ONE);
		put(HAS_MANY_VAL,HAS_MANY);
		put(REF_VAL,REF);
		put(ENUM_VAL,ENUM);
	}};
	//endregion of ~GENERATED PARTS END

	public static MetaRelationType parse(final String relationType){
		var rt = MetaRelationType.NONE;

		if("HAS_ONE".equals(relationType))
			rt = MetaRelationType.HAS_ONE;
		else if("REF".equals(relationType) || "REF_ONE".equals(relationType))
			rt = MetaRelationType.REF;
		else if("HAS_MANY".equals(relationType))
			rt = MetaRelationType.HAS_MANY;
		else if("ENUM".equals(relationType) || "ENUMS".equals(relationType))
			rt = MetaRelationType.ENUM;
		return rt;
	}
}
