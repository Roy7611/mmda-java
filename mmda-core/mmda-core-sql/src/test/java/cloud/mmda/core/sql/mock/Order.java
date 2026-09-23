package cloud.mmda.core.sql.mock;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private long orderID;
    private String orderNo;
    private String orderName;
    private BigDecimal orderAmount;
    private long partnerID;
}
