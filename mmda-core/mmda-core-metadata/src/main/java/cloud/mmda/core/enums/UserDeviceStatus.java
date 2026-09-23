/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
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
 * 用户设备状态
 * 
 * 0;OFFLINE;离线|1;ONLINE;在线|-1;DEPRECATED;已弃用
 * 
 * @author mmda code robot
 * @version 4.0 
 * @since 2024-07-25 14:44:01.0
 * 
 */
public enum UserDeviceStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	OFFLINE(0,"离线"),
	ONLINE(1,"在线"),
	DEPRECATED(-1,"已弃用");

	private final Short value;
	@Override
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	UserDeviceStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public static final UserDeviceStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short OFFLINE_VAL = 0;//离线
	public static final short ONLINE_VAL = 1;//在线
	public static final short DEPRECATED_VAL = -1;//已弃用
	
	public static Map<Short,UserDeviceStatus> enumMap = new LinkedHashMap<Short,UserDeviceStatus>(){{
		put(OFFLINE_VAL,OFFLINE);
		put(ONLINE_VAL,ONLINE);
		put(DEPRECATED_VAL,DEPRECATED);
	}};
	//endregion of ~GENERATED PARTS END

}
