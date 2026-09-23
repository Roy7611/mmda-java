package cloud.mmda.core.messaging;


/**
 * 定义了单条发送接口，你可以根据不同的平台实现。
 */
public interface Sender<T extends Message> {

    /**
     * 发送一条消息
     * @param t 要发送的短消息
     */
    boolean send(T t);
}
