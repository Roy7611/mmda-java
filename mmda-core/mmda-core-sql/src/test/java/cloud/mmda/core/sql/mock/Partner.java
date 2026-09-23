package cloud.mmda.core.sql.mock;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Partner {
    private long partnerID;
    private String partnerNo;
    private String partnerName;
    private int orderCount;
    private long creatorId;
    private Timestamp createdAt;
    private User creator;
}
