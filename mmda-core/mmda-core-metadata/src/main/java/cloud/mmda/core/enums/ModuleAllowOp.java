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
import cloud.mmda.core.enums.EnumBitValue;
/**
 * 模块允许操作
 * 
 * 0;NONE;无|1;READ;读取|2;EDIT;编辑|4;CREATE;创建|8;DELETE;删除|15;CRUD;增删改|16;PRINT;打印|32;IMPORT;导入|64;EXPORT;导出|127;ALL;所有
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum ModuleAllowOp implements EnumBitValue{
	//region ~GENERATED PARTS BEGIN
	NONE(0,"无",-1),
	READ(1,"读取",0),
	EDIT(2,"编辑",1),
	CREATE(4,"创建",2),
	DELETE(8,"删除",3),
	CRUD(15,"增删改",3),
	PRINT(16,"打印",4),
	IMPORT(32,"导入",5),
	EXPORT(64,"导出",6),
	UPLOAD(128,"上传模板",7),
	ALL(255,"所有",8);

	private final Integer value;
	@Override
	public final Integer getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	@Getter
	private final int bit;

	ModuleAllowOp(int value, String text, int bit){
		this.value = value;
		this.text = text;
		this.bit = bit;
	}


	public final boolean hasFlag(ModuleAllowOp e){
		return (value & e.value) == e.value;
	}
	public static final ModuleAllowOp valueOf(int value){
		return enumMap.get(value);
	}

	public static final int NONE_VAL = 0;//无
	public static final int READ_VAL = 1;//读取
	public static final int EDIT_VAL = 2;//编辑
	public static final int CREATE_VAL = 4;//创建
	public static final int DELETE_VAL = 8;//删除
	public static final int CRUD_VAL = 15;//增删改
	public static final int PRINT_VAL = 16;//打印
	public static final int IMPORT_VAL = 32;//导入
	public static final int EXPORT_VAL = 64;//导出
	public static final int UPLOAD_VAL = 128;//上传模板
	public static final int ALL_VAL = 255;//所有
	
	public static Map<Integer,ModuleAllowOp> enumMap = new LinkedHashMap<Integer,ModuleAllowOp>(){{
		put(NONE_VAL,NONE);
		put(READ_VAL,READ);
		put(EDIT_VAL,EDIT);
		put(CREATE_VAL,CREATE);
		put(DELETE_VAL,DELETE);
		put(CRUD_VAL,CRUD);
		put(PRINT_VAL,PRINT);
		put(IMPORT_VAL,IMPORT);
		put(EXPORT_VAL,EXPORT);
		put(UPLOAD_VAL,UPLOAD);
		put(ALL_VAL,ALL);
	}};
	//endregion of ~GENERATED PARTS END

}
