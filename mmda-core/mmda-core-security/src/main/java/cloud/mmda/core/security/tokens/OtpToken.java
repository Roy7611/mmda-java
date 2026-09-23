package cloud.mmda.core.security.tokens;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.time.Instant;

/**
 * One-time password(OTP) is generated randomly by authentication server.
 * It can be sent by sms or email to a user device and will be expired within seconds.
 * Then users can provide this short password to verify its authentication in a login form.
 */
public class OtpToken {
    /**
     * Username could be a mobile phone number or an email address
     */
    @Getter
    private final String username;
    /**
     * Short code Randomly automatically generated as a dynamic password.
     */
    @Getter
    private final String password;
    /**
     * Timestamp when this token will be expired at.
     */
    @Getter
    private final long expiryTimestamp;

    /**
     * Construct a one-time password token
     * @param username mobile phone number or email address
     * @param password short verification code
     * @param timeout timeout in seconds
     */
    public OtpToken(final String username, final String password, int timeout) {
        this.username = username;
        this.password = password;
        this.expiryTimestamp = Instant.now().getEpochSecond() + timeout;
    }

    /**
     * Construct a one-time password token which will be expired in 300 seconds(5 min)
     * @param username mobile phone number or email address
     * @param password short verification code
     */
    public OtpToken(final String username, final String password){
        this(username, password, 300);
    }
    /**
     * Return expired or not of this token.
     * @return
     */
    @JsonIgnore
    public boolean isExpired() {
        return Instant.now().getEpochSecond() > expiryTimestamp;
    }

    /**
     * Return how many seconds left before expiration
     * @return
     */
    @JsonIgnore
    public long getTimeout(){
        return expiryTimestamp - Instant.now().getEpochSecond();
    }
}
