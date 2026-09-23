package cloud.mmda.core.security.tokens;


import cloud.mmda.core.security.models.UserAccount;

public class JoseJwtTokenService implements JwtTokenService {
    @Override
    public String extractUserName(String token) {
        return "";
    }

    @Override
    public String generateToken(UserAccount userAccount) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token, UserAccount userAccount) {
        return false;
    }
}
