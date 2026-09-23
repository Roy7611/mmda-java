package cloud.mmda.core.sql.expressions;
import java.util.Arrays;
import java.util.List;

import static cloud.mmda.core.sql.expressions.SqlOpType.*;

/**
 * Sql 操作符，加上左右操作数(Operands)组成表达式{@link SqlExp}
 */
public enum SqlOp {
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
    LESS_THAN(" < ",COMPARISON,2),
    LESS_OR_EQUAL(" <= ",COMPARISON,2),
    GREATER_THAN(" > ",COMPARISON,2),
    GREATER_OR_EQUAL(" >= ",COMPARISON,2),
    NOT_EQUAL(" != ",COMPARISON,2),
    BETWEEN(" BETWEEN ",COMPARISON,3),
    LIKE(" LIKE ",COMPARISON,2),
    IN(" IN ",COMPARISON,4),
    NOT_IN(" NOT IN ",COMPARISON,4),

    EXISTS("EXISTS ",COMPARISON,-1),
    NOT_EXISTS("NOT EXISTS ",COMPARISON,-1),

    //位运算
    BIT_AND("&",BITWISE,2),
    BIT_OR("|",BITWISE,2),
    BIT_XOR("^",BITWISE,2),
    BIT_NOT("~",BITWISE,-1),

    //逻辑
    AND(" AND ",LOGICAL, 2),
    OR(" OR ",LOGICAL, 2),
    NOT("NOT ",COMPARISON|LOGICAL, -1),
    //赋值
    EQUAL(" = ",ASSIGNMENT|COMPARISON|LOGICAL,2)
    ;
    private final String text;
    private int type;
    private int argNum;

    SqlOp(String text, int type, int argNum){
        this.text = text;
        this.type = type;
        this.argNum = argNum;
    }

    public final String getText(){
        return text;
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

    /**
     * 是否比较等式和不等式操作符，包括 IN, LIKE, EXISTS
     * @return
     */
    public final boolean isComparison(){
        return (type & COMPARISON)>0;
    }

    /**
     * 是否位操作符
     * @return
     */
    public final boolean isBitwise(){
        return (type & BITWISE)>0;
    }

    /**
     * 是否 AND / OR / NOT 连接起来的逻辑表达式
     * @return
     */
    public final boolean isLogical(){
        return (type & LOGICAL)>0;
    }

    /**
     * 是否赋值等式
     * @return
     */
    public final boolean isAssignment(){
        return (type & ASSIGNMENT)>0;
    }

    /**
     * 参数数量，可进一步判断是否
     * {@linkplain #isUnary() 一元}、
     * {@link #isBinary() 二元}、
     * {@link #isTernary() 三元}还是
     * {@link #hasMultipleArgs() 多元} 操作符号
     * @return
     */
    public final int getArgNum(){
        return argNum;
    }

    /**
     * 判断是否一元操作符，一元操作符没有右参数
     * @return
     */
    public final boolean isUnary(){
        return argNum == 1 || argNum == -1;
    }
    public final boolean isPreUnary(){
        return argNum == -1;
    }
    /**
     * 判断是否二元操作符，二元操作符有且只有一个右手边参数(lhs = ?)
     * @return
     */
    public final boolean isBinary(){
        return argNum == 2;
    }

    /**
     * 判断是否三元操作符，三元操作符有两个右手边参数，比如（between ? and ?）
     * @return
     */
    public final boolean isTernary(){
        return argNum == 3;
    }

    /**
     * 判断是否多元操作符，多元操作符右边是列表集合参数，比如 in, like, not in, not like, exists
     * @return
     */
    public final boolean hasMultipleArgs(){
        return argNum >= 4;
    }

    public static final SqlOp parse(String text){
        var t = text.trim().toUpperCase();
        for(SqlOp op : values()){
            if(op.text.trim().equals(text)) return op;
        }
        throw new IllegalArgumentException("Unknown op: " + text);
    }
}
