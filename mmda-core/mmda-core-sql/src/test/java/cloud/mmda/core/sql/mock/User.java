package cloud.mmda.core.sql.mock;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private String mobile;
    private String email;
    private Integer status;
    private boolean active;
}
