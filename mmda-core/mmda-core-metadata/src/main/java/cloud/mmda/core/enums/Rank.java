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

/**
 * 星级
 * 
 * 0;NONE;-|1;A;A级|2;B;B级|3;C;C级|4;D;D级
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum Rank implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NONE(0,"-"),
	A(1,"A级"),
	B(2,"B级"),
	C(3,"C级"),
	D(4,"D级");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	Rank(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final Rank valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NONE_VAL = 0;//-
	public static final byte A_VAL = 1;//A级
	public static final byte B_VAL = 2;//B级
	public static final byte C_VAL = 3;//C级
	public static final byte D_VAL = 4;//D级
	
	public static Map<Byte,Rank> enumMap = new LinkedHashMap<Byte,Rank>(){{
		put(NONE_VAL,NONE);
		put(A_VAL,A);
		put(B_VAL,B);
		put(C_VAL,C);
		put(D_VAL,D);
	}};
	//endregion of ~GENERATED PARTS END

}
