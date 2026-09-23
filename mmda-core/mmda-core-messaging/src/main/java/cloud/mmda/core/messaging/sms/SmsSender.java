package cloud.mmda.core.messaging.sms;

import cloud.mmda.core.messaging.Sender;

/**
 * 定义了单条和多条短信发送接口，你可以根据不同的短信平台实现。
 *
 * @see SmsMessage
 * @author Roy Luo
 * @since 2024.07
 */
public abstract class SmsSender implements Sender<SmsMessage> {
}
