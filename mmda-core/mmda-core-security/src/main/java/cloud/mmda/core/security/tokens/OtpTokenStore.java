package cloud.mmda.core.security.tokens;

/**
 * The store interface of {@link OtpToken} (one-time password token)
 */
public interface OtpTokenStore {
    /**
     * Store a token
     *
     * @param token
     */
    void store(OtpToken token);

    /**
     * Retrieve a named token value
     * @param name the token name could be a mobile phone number or an email address
     * @return
     */
    String retrieve(String name);
    /**
     * Remove the token with name
     *
     * @param name the token name could be a mobile phone number or an email address
     */
    boolean remove(String name);

    /**
     * Determine if a named token exists in this store or not
     *
     * @param name the token name
     * @return
     */
    boolean exists(String name);

}
