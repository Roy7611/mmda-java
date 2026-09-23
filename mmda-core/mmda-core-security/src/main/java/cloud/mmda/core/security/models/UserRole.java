package cloud.mmda.core.security.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Relationship between user and role.
 * Since UserRole table includes columns besides id so that we use embedded mode for composite key.
 *
 * @author Roy Luo
 * @since 4.0
 *
 * @see UserRoleId
 * @see
 * <p>
 *     <a href="https://www.baeldung.com/jpa-many-to-many">Many-To-Many Relationship in JPA</a>
 * </p>
 */
@Data
@Entity
@Table(name="userrole",catalog = "mmda_base")
//@IdClass(UserRoleId.class)
public class UserRole {
//    @Id
//    private long userID;

//    @Id
//    private long roleID;

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name="userID")
    private User user;

    @ManyToOne
    @MapsId("roleID")
    @JoinColumn(name="roleID")
    private Role role;


    private Long deptID;

    private boolean parttime;

}
