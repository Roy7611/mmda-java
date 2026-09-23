package cloud.mmda.core.sql;

import lombok.Getter;

/**
 * SQL 连接类型
 */
public enum SqlJoinType {
    INNER_JOIN(0," INNER JOIN "),
    LEFT_JOIN(1," LEFT JOIN "),
    RIGHT_JOIN(2," RIGHT JOIN "),
    FULL_JOIN(3, " FULL JOIN "),
    CROSS_JOIN(4, " CROSS JOIN "),;

    @Getter
    private final Byte value;
    @Getter
    private final String text;
    SqlJoinType(int value, String text) {
        this.value = (byte) value;
        this.text = text;
    }

    public static SqlJoinType parse(String joinType) {
        if(joinType.trim().toUpperCase().startsWith("LEFT")) {
            return LEFT_JOIN;
        }
        else if(joinType.trim().toUpperCase().startsWith("RIGHT")) {
            return RIGHT_JOIN;
        }
        else if(joinType.trim().toUpperCase().startsWith("FULL")) {
            return FULL_JOIN;
        }
        else if(joinType.trim().toUpperCase().startsWith("CROSS")) {
            return CROSS_JOIN;
        }
        else{
            return INNER_JOIN;
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
