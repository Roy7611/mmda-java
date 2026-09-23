package cloud.mmda.core.security.models;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

/**
 * Defines common named roles in and out of an enterprise,
 * then provides the bean of hierarchy roles for authorization in our application.
 *
 * @author Roy Luo
 * @since 4.0
 */
public final class Roles {
    public static final String USER = "USER";
    public static final String STAFF = "STAFF";
    public static final String SUPERVISOR = "SUPERVISOR";
    public static final String MANAGER = "MANAGER";
    public static final String DIRECTOR = "DIRECTOR";

    public static final String ADMIN = "ADMIN";
    public static final String CARGO_OWNER = "CARGO_OWNER";
    public static final String WAREHOUSE_KEEPER = "WAREHOUSE_KEEPER";

    public static final String PARTNER = "PARTNER";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String SUPPLIER = "SUPPLIER";
    public static final String CARRIER = "CARRIER";

	private Roles(){}
	
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(STAFF).implies(USER)
                .role(SUPERVISOR).implies(STAFF)
                .role(MANAGER).implies(SUPERVISOR,STAFF)
                .role(DIRECTOR).implies(MANAGER, SUPERVISOR, STAFF)
                .role(ADMIN).implies(STAFF, USER)
                .role(CARGO_OWNER).implies(USER)
                .role(PARTNER).implies(USER)
                .role(CUSTOMER).implies(PARTNER, USER)
                .role(SUPPLIER).implies(PARTNER, USER)
                .role(CARRIER).implies(PARTNER, SUPPLIER, USER)
                .build();
    }
}
