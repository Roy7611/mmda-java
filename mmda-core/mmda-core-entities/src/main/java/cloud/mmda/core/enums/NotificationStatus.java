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
 * 通知状态
 * 
 * 0;NEW;新通知|1;SENT;已送达|2;READ;已读|4;DONE;已办
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-07 10:30:04.0
 * 
 */
public enum NotificationStatus implements EnumValue<Byte> {
	//region ~GENERATED PARTS BEGIN
	NEW(0,"新通知"),
	SENT(1,"已送达"),
	READ(2,"已读"),
	DONE(4,"已办");

	private final Byte value;
	@Override
	public final Byte getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	NotificationStatus(int value, String text){
		this.value = (byte)value;
		this.text = text;
	}


	public static final NotificationStatus valueOf(byte value){
		return enumMap.get(value);
	}

	public static final byte NEW_VAL = 0;//新通知
	public static final byte SENT_VAL = 1;//已送达
	public static final byte READ_VAL = 2;//已读
	public static final byte DONE_VAL = 4;//已办
	
	public static Map<Byte,NotificationStatus> enumMap = new LinkedHashMap<Byte,NotificationStatus>(){{
		put(NEW_VAL,NEW);
		put(SENT_VAL,SENT);
		put(READ_VAL,READ);
		put(DONE_VAL,DONE);
	}};
	//endregion of ~GENERATED PARTS END

}
