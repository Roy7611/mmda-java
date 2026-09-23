/**
 *
 */
package cloud.mmda.core.security.tokens.jwt;
import cloud.mmda.core.security.UserAccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author roshion
 */
@Component
public class Converter {
	private static final Log logger = LogFactory.getLog(Converter.class);
	//private final static String ISSUER_URI = "https://www.mmda.cloud";
	//private final static String ISSUER_URI = "http://localhost:8001";
	private UserAccountService userAccountService;
	@Autowired
	public void setUserAccountService(final UserAccountService userAccountService){
		this.userAccountService = userAccountService;
	}
	private  JwtDecoder jwtDecoder;

	public Converter(JwtDecoder jwtDecoder) {
		this.jwtDecoder=jwtDecoder;
	}
	//@PostConstruct
	//private void init(){
	//	jwtDecoder= NimbusJwtDecoder.withIssuerLocation(ISSUER_URI).build();
	//}
	public Authentication extractAuthentication(String token){
		Jwt jwt = jwtDecoder.decode(token);
		try {
			Map<String, Object> claims = jwt.getClaims();
			String userName = String.valueOf(claims.get("sub"));
			UserDetails userDetails = userAccountService.loadUserByUsername(userName);
			return new Token(userDetails, "",userDetails.getAuthorities());
		} catch (Exception ignored) {
			logger.warn("parse failed");
			return null;
		}
	}
}
