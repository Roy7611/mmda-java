package cloud.mmda.core.sql.expressions;

import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.utils.BaseUtil;

import java.util.List;

/**
 * SQL条件表达式
 */
public class SqlCriteriaExp extends SqlExp {
    public SqlCriteriaExp(){
        super();
    }
    public SqlCriteriaExp(String colName){
        super(colName);
    }
    public SqlCriteriaExp(Object lhs, SqlOp op, Object rhs) {
        super(lhs, op, rhs);
    }
    public SqlCriteriaExp(SqlCriteriaExp exp){
        super(exp.getLhs(), exp.getOp(), exp.getRhs());
    }


    /**
     * 逻辑与、或连接另一个条件表达式
     * @param andOr 与或连接符
     * @param other 条件表达式
     * @return 条件表达式
     */
    public SqlCriteriaExp logicalJoin(SqlOp andOr, SqlCriteriaExp other){
        binaryOp(andOr, other);
        return this;
    }
    public SqlCriteriaExp and(SqlCriteriaExp other){
        binaryOp(SqlOp.AND, other);
        return this;
    }
    public SqlCriteriaExp or(SqlCriteriaExp other){
        binaryOp(SqlOp.OR, other);
        return this;
    }
    public SqlCriteriaExp isNull(){
        unaryOp(SqlOp.IS_NULL);
        return this;
    }
    public SqlCriteriaExp isNotNull(){
        unaryOp(SqlOp.IS_NOT_NULL);
        return this;
    }
    public SqlCriteriaExp eq(Object other){
        binaryOp(SqlOp.EQUAL, other);
        return this;
    }
    public <T> SqlCriteriaExp eq(LambdaGetter<T,?> other){
        binaryOp(SqlOp.EQUAL, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp neq(Object other){
        binaryOp(SqlOp.NOT_EQUAL, other);
        return this;
    }
    public <T> SqlCriteriaExp neq(LambdaGetter<T,?> other){
        binaryOp(SqlOp.NOT_EQUAL, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp lt(Object other){
        binaryOp(SqlOp.LESS_THAN, other);
        return this;
    }
    public <T> SqlCriteriaExp lt(LambdaGetter<T,?> other){
        binaryOp(SqlOp.LESS_THAN, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp le(Object other){
        binaryOp(SqlOp.LESS_OR_EQUAL, other);
        return this;
    }
    public <T> SqlCriteriaExp le(LambdaGetter<T,?> other){
        binaryOp(SqlOp.LESS_OR_EQUAL, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp gt(Object other){
        binaryOp(SqlOp.GREATER_THAN, other);
        return this;
    }
    public <T> SqlCriteriaExp gt(LambdaGetter<T,?> other){
        binaryOp(SqlOp.GREATER_THAN, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp ge(Object other){
        binaryOp(SqlOp.GREATER_OR_EQUAL, other);
        return this;
    }
    public <T> SqlCriteriaExp ge(LambdaGetter<T,?> other){
        binaryOp(SqlOp.GREATER_OR_EQUAL, LambdaUtil.getFieldName(other));
        return this;
    }
    public SqlCriteriaExp contains(String other){
        binaryOp(SqlOp.LIKE, "%"+other+"%");
        return this;
    }
    public SqlCriteriaExp startsWith(String other){
        binaryOp(SqlOp.LIKE, other+"%");
        return this;
    }
    public SqlCriteriaExp endsWith(String other){
        binaryOp(SqlOp.LIKE, "%"+other);
        return this;
    }

    public SqlCriteriaExp between(Object a, Object b){
        binaryOp(SqlOp.BETWEEN, List.of(a,b));
        return this;
    }

    public SqlCriteriaExp in(Object...other){
        binaryOp(SqlOp.IN, List.of(other));
        return this;
    }
    public SqlCriteriaExp notIn(Object...other){
        binaryOp(SqlOp.NOT_IN, List.of(other));
        return this;
    }

    public SqlCriteriaExp not(){
        unaryOp(SqlOp.NOT);
        return this;
    }


}
