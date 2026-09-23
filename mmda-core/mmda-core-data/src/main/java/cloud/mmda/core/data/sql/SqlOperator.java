package cloud.mmda.core.data.sql;

import static cloud.mmda.core.data.sql.SqlOperatorType.*;

/**
 * Sql操作符
 * <p>
 * eq 就是 equal等于
 * ne就是 not equal不等于
 * gt 就是 greater than大于
 * lt 就是 less than小于
 * ge 就是 greater than or equal 大于等于
 * le 就是 less than or equal 小于等于
 * in 就是 in 包含（数组）
 * isNull 就是 等于null
 * between 就是 在2个条件之间(包括边界值)
 * like就是 模糊查询
 * </p>
 */
public enum SqlOperator {
    NONE("", UNKNOWN,0),
    //算术
    PLUS("+",ARITHMETIC,2),
    MINUS("-",ARITHMETIC, 2),
    MULTIPLY("*",ARITHMETIC, 2),
    DIVIDE("/",ARITHMETIC, 2),
    MODULUS("%",ARITHMETIC, 2),
    //移位
    LEFT_SHIFT("<<",SHIFT,2),
    RIGHT_SHIFT(">>",SHIFT,2),
    RIGHT_SHIFT_UNSIGNED(">>>",SHIFT,2),
    //比较
    IS_NULL(" IS NULL",COMPARISON,1),
    IS_NOT_NULL(" IS NOT NULL",COMPARISON,1),
    LESS_THAN("<",COMPARISON,2),
    LESS_OR_EQUAL("<=",COMPARISON,2),
    GREATER_THAN(">",COMPARISON,2),
    GREATER_OR_EQUAL(">=",COMPARISON,2),
    NOT_EQUAL("!=",COMPARISON,2),
    BETWEEN(" BETWEEN ",COMPARISON,3),
    LIKE(" LIKE ",COMPARISON,4),
    IN(" IN ",COMPARISON,4),
    NOT_IN(" NOT IN ",COMPARISON,4),
    //位运算
    BIT_AND("&",BITWISE,2),
    BIT_OR("|",BITWISE,2),
    BIT_XOR("^",BITWISE,2),
    BIT_NOT("~",BITWISE,-1),

    //逻辑
    AND(" AND ",LOGICAL, 2),
    OR(" OR ",LOGICAL, 2),
    //赋值
    EQUAL("=",ASSIGNMENT|COMPARISON|LOGICAL,2)
    ;

    private final String op;
    private int type;
    private int argNum;

    SqlOperator(String op, int type,int argNum){
        this.op = op;
        this.type = type;
        this.argNum = argNum;
    }

    public final String getOp(){
        return op;
    }
    public final int getType(){
        return type;
    }
    public final boolean isArithmetic(){
        return (type & ARITHMETIC)>0;
    }
    public final boolean isShift(){
        return (type & SHIFT)>0;
    }

    /**
     * 是否可以作为右参数，此时的操作符是一个计算表达式的组成部分
     * @return
     */
    public final boolean canBeRightArgument(){
        return isArithmetic() || isShift() || isBitwise();
    }
    public final boolean isComparison(){
        return (type & COMPARISON)>0;
    }
    public final boolean isBitwise(){
        return (type & BITWISE)>0;
    }
    public final boolean isLogical(){
        return (type & LOGICAL)>0;
    }
    public final boolean isAssignment(){
        return (type & ASSIGNMENT)>0;
    }
    public final int getArgNum(){
        return argNum;
    }

    /**
     * 有一个右参数, = ?
     * @return
     */
    public final boolean hasOneRightArg(){
        return argNum == 2;
    }

    /**
     * 有两个右参数，比如 between ? and ?
     * @return
     */
    public final boolean hasTwoRightArg(){
        return argNum == 3;
    }

    /**
     * 右边是常数，比如 in, like, not in, not like
     * @return
     */
    public final boolean hasConstRightArg(){
        return argNum >=4;
    }
}
