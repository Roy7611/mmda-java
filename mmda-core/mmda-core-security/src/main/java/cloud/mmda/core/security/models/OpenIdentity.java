package cloud.mmda.core.security.models;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Open identity with type and id property.
 *
 * @author Roy Luo
 * @since 4.0
 */
@Data
@AllArgsConstructor
@Table(name="openidentity",catalog = "mmda_base")
public class OpenIdentity {
    private String openIDType;
    private String openID;

    /**
     * Returns open id like {WeChat}123487363
     * @return
     */
    @Override
    public String toString() {
        return "{" + openIDType + "}" + openID;
    }
}
