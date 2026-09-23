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
 * 推送状态
 * 
 * 0;NEW;新|1;SENT;已发送|2;RECEIVED;已读|-1;CANCELED;已取消
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-09-06 01:24:09.0
 * 
 */
public enum PushStatus implements EnumValue<Short> {
	//region ~GENERATED PARTS BEGIN
	NEW(0,"新"),
	//SENT(1,"已送达"),	7.18 通知状态修改 新增 状态 已读 修改 状态 已送达 为 已发送
	SENT(1,"已发送"),
	RECEIVED(2,"已读"),
	CANCELED(-1,"已取消");

	private final Short value;
	@Override
	public final Short getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	PushStatus(int value, String text){
		this.value = (short)value;
		this.text = text;
	}


	public static final PushStatus valueOf(short value){
		return enumMap.get(value);
	}

	public static final short NEW_VAL = 0;//新
	//public static final short SENT_VAL = 1;//已送达
	public static final short SENT_VAL = 1;//已发送
	public static final short RECEIVED_VAL = 2;//已读
	public static final short CANCELED_VAL = -1;//已取消
	
	public static Map<Short,PushStatus> enumMap = new LinkedHashMap<Short,PushStatus>(){{
		put(NEW_VAL,NEW);
		//put(SENT_VAL,SENT);
		put(SENT_VAL,SENT);
		put(RECEIVED_VAL,RECEIVED);
		put(CANCELED_VAL,CANCELED);
	}};
	//endregion of ~GENERATED PARTS END

}
