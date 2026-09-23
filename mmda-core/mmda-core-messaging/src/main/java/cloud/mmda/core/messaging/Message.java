package cloud.mmda.core.messaging;

/**
 * 通用的消息接口，允许你设置关键的消息属性。
 */
public interface Message {
    /**
     * 设置接收方信息
     * @param to 如手机号，邮箱，微信OpenID等
     */
    void setTo(String to);

    /**
     * 设置消息文本
     * @param text 文本
     */
    void setText(String text);

}
