package cloud.mmda.core.messaging.tokens;

public interface MessageTokenStore {
    /**
     * 存入令牌
     * @param token
     */
    void store(MessageToken token);

    /**
     * 移除令牌
     * @param mobile
     */
    boolean remove(String mobile);
    /**
     * 判断是否存在
     * @param mobile
     * @return
     */
    boolean exists(String mobile);
    /**
     * 判断令牌是否有效
     * @param mobile
     * @param token
     * @return
     */
    boolean isValid(String mobile,String token);

    /**
     * 限制同一手机号1分钟内重发
     * @param mobile
     * @return
     */
    boolean sendable(String mobile);
}
