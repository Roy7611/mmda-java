package cloud.mmda.core.sql.expressions;

import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;

import java.math.BigDecimal;

/**
 * SQL算术表达式
 */
public class SqlArithmeticExp extends SqlExp {

    public SqlArithmeticExp(String colName){
        super(colName);
    }
    public <T> SqlArithmeticExp(LambdaGetter<T,?> getter){
        super(LambdaUtil.getFieldName(getter));
    }

    public SqlArithmeticExp plus(String other) {
        binaryOp(SqlOp.PLUS, other);
        return this;
    }
    public <T> SqlArithmeticExp plus(LambdaGetter<T,?> getter) {
        binaryOp(SqlOp.PLUS, LambdaUtil.getFieldName(getter));
        return this;
    }

    public SqlArithmeticExp plus(Integer other) {
        binaryOp(SqlOp.PLUS, other);
        return this;
    }

    public SqlArithmeticExp plus(Long other) {
        binaryOp(SqlOp.PLUS, other);
        return this;
    }

    public SqlArithmeticExp plus(BigDecimal other) {
        binaryOp(SqlOp.PLUS, other);
        return this;
    }

    public SqlArithmeticExp plus(Double other) {
        binaryOp(SqlOp.PLUS, other);
        return this;
    }

    public <T> SqlArithmeticExp sub(LambdaGetter<T,?> getter) {
        binaryOp(SqlOp.MINUS, LambdaUtil.getFieldName(getter));
        return this;
    }
    public SqlArithmeticExp sub(String other) {
        binaryOp(SqlOp.MINUS, other);
        return this;
    }

    public SqlArithmeticExp sub(Integer other) {
        binaryOp(SqlOp.MINUS, other);
        return this;
    }

    public SqlArithmeticExp sub(BigDecimal other) {
        binaryOp(SqlOp.MINUS, other);
        return this;
    }

    public SqlArithmeticExp sub(Double other) {
        binaryOp(SqlOp.MINUS, other);
        return this;
    }

    public SqlArithmeticExp mul(String other) {
        binaryOp(SqlOp.MULTIPLY, other);
        return this;
    }
    public <T> SqlArithmeticExp mul(LambdaGetter<T,?> getter) {
        binaryOp(SqlOp.MULTIPLY, LambdaUtil.getFieldName(getter));
        return this;
    }

    public SqlArithmeticExp mul(Integer other) {
        binaryOp(SqlOp.MULTIPLY, other);
        return this;
    }

    public SqlArithmeticExp mul(Long other) {
        binaryOp(SqlOp.MULTIPLY, other);
        return this;
    }

    public SqlArithmeticExp mul(BigDecimal other) {
        binaryOp(SqlOp.MULTIPLY, other);
        return this;
    }

    public SqlArithmeticExp mul(Double other) {
        binaryOp(SqlOp.MULTIPLY, other);
        return this;
    }

    public SqlArithmeticExp div(String other) {
        binaryOp(SqlOp.DIVIDE, other);
        return this;
    }
    public <T> SqlArithmeticExp div(LambdaGetter<T,?> getter) {
        binaryOp(SqlOp.DIVIDE, LambdaUtil.getFieldName(getter));
        return this;
    }
    public SqlArithmeticExp div(Integer other) {
        binaryOp(SqlOp.DIVIDE, other);
        return this;
    }

    public SqlArithmeticExp div(Long other) {
        binaryOp(SqlOp.DIVIDE, other);
        return this;
    }

    public SqlArithmeticExp div(BigDecimal other) {
        binaryOp(SqlOp.DIVIDE, other);
        return this;
    }

    public SqlArithmeticExp div(Double other) {
        binaryOp(SqlOp.DIVIDE, other);
        return this;
    }

    public SqlArithmeticExp mod(String other) {
        binaryOp(SqlOp.MODULUS, other);
        return this;
    }

    public SqlArithmeticExp mod(Integer other) {
        binaryOp(SqlOp.MODULUS, other);
        return this;
    }

    public SqlArithmeticExp leftShift(Integer other) {
        binaryOp(SqlOp.LEFT_SHIFT, other);
        return this;
    }

    public SqlArithmeticExp rightShift(Integer other, boolean unsigned) {
        binaryOp(unsigned ? SqlOp.RIGHT_SHIFT_UNSIGNED : SqlOp.RIGHT_SHIFT, other);
        return this;
    }

    public SqlArithmeticExp bitAnd(Integer other) {
        binaryOp(SqlOp.BIT_AND, other);
        return this;
    }

    public SqlArithmeticExp bitOr(Integer other) {
        binaryOp(SqlOp.BIT_OR, other);
        return this;
    }

    public SqlArithmeticExp bitXor(Integer other) {
        binaryOp(SqlOp.BIT_XOR, other);
        return this;
    }

    public SqlArithmeticExp bitNot() {
        unaryOp(SqlOp.BIT_NOT);
        return this;
    }

}
