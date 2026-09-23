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
import cloud.mmda.core.enums.EnumBitValue;
/**
 * 消息通道
 * 
 * 0;SYSTEM;系统|1;MAIL;邮件|2;SMS;短信|4;PUSH;推送消息
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-07 10:30:04.0
 * 
 */
public enum MessageChannel implements EnumBitValue{
	//region ~GENERATED PARTS BEGIN
	INTERNAL(0,"内部",-1),
	MAIL(1,"邮件",0),
	SMS(2,"短信",1),
	PHONE_CALL(4,"电话呼叫",2),
	PUSH(8,"推送",3),
	WECHAT(16,"微信",4),
	DING(32,"钉钉",5);

	private final Integer value;
	@Override
	public final Integer getValue(){
		return this.value;
	}


	@Getter
	private final String text;

	@Getter
	private final int bit;

	MessageChannel(int value, String text, int bit){
		this.value = value;
		this.text = text;
		this.bit = bit;
	}


	public final boolean hasFlag(MessageChannel e){
		return (value & e.value) == e.value;
	}
	public static final MessageChannel valueOf(int value){
		return enumMap.get(value);
	}

	public static final int INTERNAL_VAL = 0;//内部
	public static final int MAIL_VAL = 1;//邮件
	public static final int SMS_VAL = 2;//短信
	public static final int PHONE_CALL_VAL = 4;//电话呼叫
	public static final int PUSH_VAL = 8;//推送
	public static final int WECHAT_VAL = 16;//微信
	public static final int DING_VAL = 32;//钉钉
	
	public static Map<Integer,MessageChannel> enumMap = new LinkedHashMap<Integer,MessageChannel>(){{
		put(INTERNAL_VAL,INTERNAL);
		put(MAIL_VAL,MAIL);
		put(SMS_VAL,SMS);
		put(PHONE_CALL_VAL,PHONE_CALL);
		put(PUSH_VAL,PUSH);
		put(WECHAT_VAL,WECHAT);
		put(DING_VAL,DING);
	}};
	//endregion of ~GENERATED PARTS END

}
