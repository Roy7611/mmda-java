package cloud.mmda.core.security.tokens;


import cloud.mmda.core.security.models.UserAccount;

public interface JwtTokenService {
    String extractUserName(String token);

    String generateToken(UserAccount userAccount);

    boolean isTokenValid(String token, UserAccount userAccount);
}
