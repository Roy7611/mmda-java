/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * Syc PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.enums;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.EnumSet;
import java.util.stream.Collectors;
/**
 * 消息通道枚举集合
 * 
 * @author mmda code robot 
 * @version 4.0 
 * @since 2024-08-07 10:30:04.0
 * 
 * @see MessageChannel 0;SYSTEM;系统|1;MAIL;邮件|2;SMS;短信|4;PUSH;推送消息
 */
@Deprecated
public class MessageChannelSet extends EnumBitSet<MessageChannel> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 默认构造函数
	 */
	private MessageChannelSet(){
		super(MessageChannel.class);
	}
	/**
	 * 整形值构造函数
	 * @param value 整形值
	 */
	private MessageChannelSet(int value){
		super(MessageChannel.class, value);
	}
	/**
	 * 枚举集构造函数
	 * @param enumSet 枚举集合
	 */
	private MessageChannelSet(EnumSet<MessageChannel> enumSet){
		super(MessageChannel.class, enumSet);
	}

	
	//region static constructors
	/**
	 * 空枚举集
	 * @return 
	 */
	public static final MessageChannelSet noneOf() {
		return new MessageChannelSet();
	}
	/**
	 * 整数值枚举集
	 * @param value 
	 * @return 
	 */
	public static final MessageChannelSet valueOf(int value) {
		return new MessageChannelSet(value);
	}
	/**
	 * 枚举集
	 * @param enumSet 
	 * @return 
	 */
	public static final MessageChannelSet setOf(EnumSet<MessageChannel> enumSet) {
		return new MessageChannelSet(enumSet);
	}
	/**
	 * 多个枚举值的枚举集
	 * @param first 
	 * @param elements 
	 * @return 
	 */
	public static final MessageChannelSet of(MessageChannel first, MessageChannel...elements) {
		var enumSet = EnumSet.of(first,elements);
		for(MessageChannel e : elements){
			enumSet.add(e);
		}
		return new MessageChannelSet(enumSet);
	}
	/**
	 * 所有成员的枚举集
	 * @return 
	 */
	public static final MessageChannelSet allOf() {
		return new MessageChannelSet(EnumSet.allOf(MessageChannel.class));
	}
	/**
	 * 默认枚举集
	 * @return 
	 */
	public static final MessageChannelSet ofDefault() {
		var enumSet = EnumSet.of(MessageChannel.INTERNAL);
		return new MessageChannelSet(enumSet);
	}
	/**
	 * 所有位为1的枚举集
	 * @return 
	 */
	public static final MessageChannelSet allBitOf() {
		var enumSet = EnumSet.range(MessageChannel.MAIL,MessageChannel.DING);
		return new MessageChannelSet(enumSet);
	}
	//endregion
	
	//region (de)serializer
	/**
	 * 转换为整型数，用于Json序列化
	 */
	public static class ToIntConverter extends StdConverter<MessageChannelSet,Integer>{
		@Override
		public Integer convert(MessageChannelSet value) {
			return value.getValue();
		}
	}
	/**
	 * 从整型数构建枚举集合，用于Json反序列化
	 */
	public static class FromIntConverter extends StdConverter<Integer,MessageChannelSet>{
		@Override
		public MessageChannelSet convert(Integer value) {
			if(value == null) return MessageChannelSet.noneOf();
			return MessageChannelSet.valueOf(value);
		}
	}
	//endregion
	
	//region converters between EnumSet and int
	public static final EnumSet<MessageChannel> setOf(int value){
		var enumSet = EnumSet.noneOf(MessageChannel.class);
		if(value>0) {
			for(var e : EnumSet.allOf(MessageChannel.class)){
				if(EnumBitValue.hasBit(value, e.getBit())) enumSet.add(e);
			}
		}
		return enumSet;
	}

	//endregion
	//endregion of ~GENERATED PARTS END

}
