package cloud.mmda.core.sql.expressions;

/**
 * SQL 操作符类型常数
 */
public final class SqlOpType {
    //SQL 操作符类型
    public static final int UNKNOWN = 0;
    public static final int ARITHMETIC = 1;//算术
    public static final int SHIFT = 2;//移位
    public static final int COMPARISON = 4;//比较
    public static final int BITWISE = 8;//位
    public static final int LOGICAL= 16;//逻辑
    public static final int ASSIGNMENT = 32;//赋值

    private SqlOpType(){}
}
