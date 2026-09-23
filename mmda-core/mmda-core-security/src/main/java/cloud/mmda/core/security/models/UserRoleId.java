package cloud.mmda.core.security.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class of many-to-many relationship between user and role
 *
 * @author Roy Luo
 * @since 4.0
 */
@Data
@Embeddable
public class UserRoleId implements Serializable {
    private long userID;

    private long roleID;

    public UserRoleId(long userID, long roleID) {
        this.userID = userID;
        this.roleID = roleID;
    }

    public UserRoleId() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return userID == that.userID && roleID == that.roleID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, roleID);
    }
}
