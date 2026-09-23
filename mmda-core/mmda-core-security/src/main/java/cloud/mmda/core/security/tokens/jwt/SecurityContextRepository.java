/**
 * 
 */
package cloud.mmda.core.security.tokens.jwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SecurityContextRepository implements ServerSecurityContextRepository {
	private static final String TOKEN_SCHEME = "Bearer ";
	private static final Log logger = LogFactory.getLog(SecurityContextRepository.class);
	
	private Converter converter;
	
	public SecurityContextRepository(Converter converter){
		this.converter = converter;
	}
	
	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		// 获取Token
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_SCHEME)) {
            return Mono.empty();
        }
        
        try{
        	String token = authHeader.substring(TOKEN_SCHEME.length());
			Authentication authentication = converter.extractAuthentication(token);
            return Mono.justOrEmpty(new SecurityContextImpl(authentication));
        }
        catch(Exception e){
        	return Mono.error(e);
        }
	}

}
