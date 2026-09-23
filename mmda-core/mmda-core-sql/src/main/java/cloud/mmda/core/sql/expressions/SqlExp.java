package cloud.mmda.core.sql.expressions;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaContext;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaView;
import cloud.mmda.core.sql.SqlArg;
import cloud.mmda.core.sql.SqlQuery;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.utils.BaseUtil;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * SQL 表达式
 * <p>
 * 复杂表达式使用静态函数{@link #builder(MetaObject)}，在构建语句中可使用{@link #exp(String)}构建右手边简单表达式
 */
public class SqlExp {
    public static Optional<String> PARAM_VALUE = Optional.of(PLACEHOLDER);
//    /**
//     * 表达式唯一名称，作为缓存键值
//     * @deprecated 不用了，通过{@link SqlCriteriaSupplier } 提供名称
//     */
//    @Deprecated
//    @Getter
//    protected String name;

    @Getter
    private Object lhs;
    @Getter
    private SqlOp op;
    @Getter
    private Object rhs;
    @Getter
    private boolean valid;

    private DataType dataType;

    @Getter
    private boolean compiled;
    /**
     * 编译结果为参数表达式，必须调用{@link #compile(SqlDialect, MetaContext, boolean)}后才有值，结果例如：
     * <pre>
     *     {@code
     *      {
     *          expression: "t.`amount` * ?",
     *          argList: [{DECIMAL,0.3}],
     *      }
     *     }
     * </pre>
     */
    @Getter
    private ParametricExpression parametricExpression;//

    private String compiledSql;//例如: t.`amount` * 0.3，使用toSql()访问

    protected SqlExp(){
        this.op = SqlOp.NONE;
    }
    protected SqlExp(final Object lhs){
        this.op = SqlOp.NONE;
        this.lhs = lhs;
    }
    protected SqlExp(final Object lhs, SqlOp op, final Object rhs) {
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
        if(lhs != null){
            this.valid = op.isUnary() && lhs != null || op.getArgNum()>1 && rhs != null;
        }
    }

    public SqlExp clone(SqlExp exp){
        this.lhs = exp.lhs;
        this.rhs = exp.rhs;
        this.op = exp.op;
        this.valid = exp.valid;
        this.compiledSql = exp.compiledSql;
        this.compiled = exp.compiled;
        this.parametricExpression = exp.parametricExpression;
        return this;
    }

    protected SqlExp start(final Object lhs){
        this.lhs = lhs;
        return this;
    }

    protected SqlExp unaryOp(final SqlOp op){
        if(!op.isUnary()) throw new IllegalArgumentException("op is not unary");
        if(lhs == null){
            throw new IllegalStateException("unary op not supported when lhs is null");
        }
        if(this.valid){
            this.lhs = new SqlExp(lhs, this.op, rhs);
            this.op = op;
            this.valid = true;
        }
        else{
            this.op = op;
            this.valid = true;
        }
        return this;
    }
    protected SqlExp binaryOp(SqlOp op, Object rhs) {
        //确保二元以上操作付，三元或者多元将右边参数整合成List
        if(op.isUnary()) throw new IllegalArgumentException("op is unary");

        if(lhs == null){
            this.lhs = rhs;
            this.valid = false;
        }
        else if(this.valid){
            this.lhs = new SqlExp(this.lhs, this.op, this.rhs);
            this.op = op;
            this.rhs = rhs;
            this.valid = true;
        }
        else{
            this.op = op;
            this.rhs = rhs;
            this.valid = true;
        }
        return this;
    }


    /**
     * 编译后的参数化表达式，包含编译后的SQL表达式和参数列表
     * @param expression 表达式，例如：t.`amount`*?+?
     * @param argList 参数列表，每个参数包含名称、类型属性
     */
    public record ParametricExpression(String expression, List<SqlArg> argList) {

    }

    /**
     * 结束表达式，抛弃未完部分，编译前自动调用
     * @return
     */
    protected SqlExp end(){
        if(!valid && lhs instanceof SqlExp leftExp){
            this.rhs = leftExp.rhs;
            this.op = leftExp.op;
            this.valid = leftExp.valid;
            this.lhs = leftExp.lhs;
        }
        return this;
    }
    public ParametricExpression compile(SqlDialect dialect, MetaContext context, boolean useContextAlias){
        if(!compiled) {
            var visitor = new Compiler(dialect,context,useContextAlias,true);
            visitor.visit(this.end());
            parametricExpression = new ParametricExpression(visitor.sb.toString(),visitor.argList);
        }
        return parametricExpression;
    }

    /**
     * 转化为Sql语句，不编译参数值和列表，例如用于排序语句的编译
     * @param dialect 方言
     * @param context 元对象上下文
     * @return 将字段加引号的Sql表达式
     */
    public String toSql(SqlDialect dialect, MetaContext context){
        if(!valid && !this.end().valid) return "";
        if(BaseUtil.hasText(compiledSql)) return compiledSql;

        var sb = new StringBuilder();
        if(op.isUnary()){
            if(op.isPreUnary()) sb.append('(').append(op.getText()).append(stringify(lhs,dialect,context)).append(')');
            else sb.append('(').append(stringify(lhs,dialect,context)).append(op.getText()).append(')');
        }
        else if(op.isBinary()){
            sb.append('(')
                    .append(stringify(lhs,dialect,context))
                    .append(op.getText())
                    .append(stringify(rhs,dialect,context))
                    .append(')');
        }
        else if(op.isTernary()){
            var list  = (List<?>)rhs;
            sb.append('(')
                    .append(stringify(lhs,dialect,context))
                    .append(op.getText())
                    .append(stringify(list.get(0),dialect,context))
                    .append(AND)
                    .append(stringify(list.get(1),dialect,context))
                    .append(')');
        }
        else{
            var list  = (List<?>)rhs;
            sb.append('(')
                    .append(stringify(lhs,dialect,context))
                    .append(op.getText()).append('(')
                    .append(list.stream().map(it -> stringify(it,dialect,context)).collect(Collectors.joining(COMMA)))
                    .append(')')
                    .append(')');
        }
        compiledSql = sb.toString();
        compiled = true;
        return compiledSql;
    }

    /**
     * 字符化字段或参数，编译Sql表达式使用
     * @param operand
     * @param dialect
     * @param context
     * @return
     */
    private String stringify(Object operand, SqlDialect dialect, MetaContext context){
        if(operand == null) return "NULL";
        if(operand instanceof MetaCol col) return dialect.quoteCol(col,context);
        if(operand instanceof SqlExp exp) return exp.toSql(dialect,context);
        if(operand instanceof String s){
            var col = context.findCol(s);
            if(col.isPresent()) return dialect.quoteCol(col.get(),context);
        }
        return operand.toString();
    }

    /**
     * 字符化参数，{@link #toString()}使用
     * @param parameter
     * @return
     */
    private String stringify(Object parameter){
        if(parameter == null) return "NULL";
        if(parameter instanceof MetaCol col) return col.getColName();
        return parameter.toString();
    }
    @Override
    public String toString() {
        if(lhs == null) return "";

        var sb = new StringBuilder();
        if(op.isUnary()){
            if(op.isPreUnary()) sb.append('(').append(op.getText()).append(stringify(lhs)).append(')');
            else sb.append('(').append(stringify(lhs)).append(op.getText()).append(')');
        }
        else{
            sb.append('(').append(stringify(lhs)).append(op.getText()).append(stringify(rhs)).append(')');
        }
        return sb.toString();
    }

    //region 静态构建
    /**
     * 创建一个表达式构建器，用于构建复杂的算术、条件表达式
     * @param context 元上下文可以是单个元对象{@link MetaObject}、元视图{@link MetaView}或者临时构建的上下文
     * @return SQL表达式构建器
     */
    public static Builder create(final MetaContext context) {
        Objects.requireNonNull(context, "context must not be null");
        return new Builder(context);
    }

    /**
     * 创建一个字段开始的算术表达式
     * <p>
     * 编译后{@link #compile(SqlDialect, MetaContext, boolean)}使得表达式中字段名被自动识别为元列字段
     *
     * @param colName 字段名
     * @return 未编译的SQL算术表达式
     */
    public static SqlArithmeticExp exp(String colName) {
        Objects.requireNonNull(colName, "colName must not be null");
        return new SqlArithmeticExp(colName);
    }
    public static <T> SqlArithmeticExp exp(LambdaGetter<T,?> getter) {
        Objects.requireNonNull(getter, "getter must not be null");
        return new SqlArithmeticExp(LambdaUtil.getFieldName(getter));
    }
    public static SqlExists exists(SqlQuery subQuery) {
        Objects.requireNonNull(subQuery, "subQuery must not be null");
        return new SqlExists(subQuery, false);
    }
    public static SqlExists notExists(SqlQuery subQuery) {
        Objects.requireNonNull(subQuery, "subQuery must not be null");
        return new SqlExists(subQuery, true);
    }
    /**
     * 创建一个字段开始的条件表达式
     * <p>
     * 编译后{@link #compile(SqlDialect, MetaContext, boolean)}使得表达式中字段名被自动识别为元列字段。
     * @see #col(LambdaGetter) 也可使用Lambda表达式初始化一个SQL条件表达式
     * @param colName 字段名
     * @return 未编译的SQL条件表达式
     */
    public static SqlCriteriaExp col(String colName) {
        Objects.requireNonNull(colName, "colName must not be null");
        return new SqlCriteriaExp(colName);
    }

    /**
     * 使用Lambda表达式初始化一个Sql条件表达式，例如：
     * <pre>
     *     {@code
     *      var result = SqlExp.criteria(User::getUserName).eq("bob")
     *          .compile(mySqlDialect, metaObj);//编译
     *      var sql = result.expression();//t.`userName` = ?
     *      var parameterJdbcTypes = result.argDataTypes();//[VARCHAR]
     *      var parameterValues = result.argList();//["bob"]
     *     }
     * </pre>
     * @param getter 实体的属性getter
     * @return 未编译的Sql条件表达式
     * @param <T> 实体类型
     */
    public static <T> SqlCriteriaExp col(LambdaGetter<T,?> getter) {
        Objects.requireNonNull(getter, "getter must not be null");
        return new SqlCriteriaExp(LambdaUtil.getFieldName(getter));
    }
    /**
     * 构建 WHERE 条件表达式
     * @param cols 字段列表
     * @param compareOp 比较运算符
     * @param andOr 逻辑运算符，只能 AND | OR
     * @return SQL 条件表达式
     */
    public static SqlCriteriaExp where(final List<MetaCol> cols, SqlOp compareOp, SqlOp andOr){
        if(cols == null || cols.isEmpty()) throw new IllegalArgumentException("cols must not be null or empty");
        if(andOr != SqlOp.OR && andOr != SqlOp.AND) throw new IllegalArgumentException("Operator andOr must be AND or OR");
        return cols.stream()
                .map(c -> new SqlCriteriaExp(c, compareOp, "?"))
                .reduce(new SqlCriteriaExp(), (result, exp) -> result.logicalJoin(andOr, exp));
    }
    /**
     * 创建条件表达式，由字段名-值字典指定的等式通过逻辑与组成
     * @param equations 字段名称和值的字典映射表
     * @param andOr 连接操作符号，必须是{@link SqlOp#AND}或者{@link SqlOp#OR}
     * @return 未编译的SQL条件表达式
     */
    public static SqlCriteriaExp allEquals(Map<Object,Object> equations, SqlOp andOr) {
        if(equations == null || equations.isEmpty()) throw new IllegalArgumentException("equations must not be null or empty");
        if(andOr != SqlOp.OR && andOr != SqlOp.AND) throw new IllegalArgumentException("operator must be AND or OR");

        return equations.entrySet().stream()
                .map(e -> new SqlCriteriaExp(e.getKey(), SqlOp.EQUAL,e.getValue()))
                .reduce(new SqlCriteriaExp(), (result, exp) -> result.logicalJoin(andOr, exp));
    }
    /**
     * 构建字典集合为查询等式，AND连接
     * @param equations 等式左右键值集合
     * @return 未编译的条件表达式 a = 1 AND b = 3 AND ...
     */
    public static SqlCriteriaExp allEqualsAnd(Map<Object,Object> equations) {
        return allEquals(equations, SqlOp.AND);
    }
    /**
     * 构建字典集合为查询等式，OR连接
     * @param equations 等式左右键值集合
     * @return 未编译的条件表达式 a = 1 OR b = 3 OR ...
     */
    public static SqlCriteriaExp allEqualsOr(Map<Object,Object> equations) {
        return allEquals(equations, SqlOp.OR);
    }

    public static SqlCriteriaExp allEqualsAnd(List<MetaCol> cols){
        var map = new LinkedHashMap<>();
        for(MetaCol col : cols){
            map.put(col, PARAM_VALUE);
        }
        return allEquals(map, SqlOp.AND);
    }

    //endregion of 静态构建

    //region Builder模式实现
    /**
     * SQL算术表达式构建接口
     */
    public interface ISqlArithmetic{
        ISqlArithmetic plus(String other);
        ISqlArithmetic plus(Integer other);
        ISqlArithmetic plus(Long other);
        ISqlArithmetic plus(BigDecimal other);
        ISqlArithmetic plus(Double other);

        ISqlArithmetic sub(String other);
        ISqlArithmetic sub(Integer other);
        ISqlArithmetic sub(BigDecimal other);
        ISqlArithmetic sub(Double other);

        ISqlArithmetic mul(String other);
        ISqlArithmetic mul(Integer other);
        ISqlArithmetic mul(Long other);
        ISqlArithmetic mul(BigDecimal other);
        ISqlArithmetic mul(Double other);

        ISqlArithmetic div(String other);
        ISqlArithmetic div(Integer other);
        ISqlArithmetic div(Long other);
        ISqlArithmetic div(BigDecimal other);
        ISqlArithmetic div(Double other);

        ISqlArithmetic mod(String other);
        ISqlArithmetic mod(Integer other);

        ISqlArithmetic leftShift(Integer other);
        ISqlArithmetic rightShift(Integer other, boolean unsigned);
        ISqlArithmetic bitAnd(Integer other);
        ISqlArithmetic bitOr(Integer other);
        ISqlArithmetic bitXor(Integer other);
        ISqlArithmetic bitNot();

        //以下比较接口调用后变为条件语句
        ISqlCriteria eq(Object other);
        ISqlCriteria ne(Object other);
        ISqlCriteria gt(Object other);
        ISqlCriteria ge(Object other);
        ISqlCriteria lt(Object other);
        ISqlCriteria le(Object other);

        ISqlCriteria isNull();
        ISqlCriteria isNotNull();

        ISqlCriteria contains(String other);
        ISqlCriteria startsWith(String other);
        ISqlCriteria endsWith(String other);

        ISqlCriteria between(Object a, Object b);

        ISqlCriteria in(Object...other);
        ISqlCriteria notIn(Object...other);

        ISqlCriteria not();
        //结束表达式
        SqlExp end();
    }

    /**
     * SQL条件表达式构建接口
     */
    public interface ISqlCriteria{
        /**
         * 并且
         * @param other 其他字段或者表达式
         * @return 左边算术表达式
         */
        ISqlArithmetic and(@NonNull Object other);

        /**
         * 或者
         * @param other 其他字段或者表达式
         * @return 左边算术表达式
         */
        ISqlArithmetic or(@NonNull Object other);
        ISqlCriteria andExists(@NonNull SqlQuery subQuery);
        ISqlCriteria andNotExists(@NonNull SqlQuery subQuery);
        ISqlCriteria orExists(SqlQuery subQuery);
        ISqlCriteria orNotExists(SqlQuery subQuery);
        ISqlCriteria not();
        SqlExp end();
    }

    /**
     * SQL表达式构建器提供Fluent API方式构建一个{@link SqlExp}
     */
    public static class Builder implements ISqlCriteria, ISqlArithmetic {
        private final MetaContext context;
        private SqlExp result;
        private SqlOp andOr;

        private SqlExp current;

        public Builder(@NonNull final MetaContext context) {
            this.context = context;
            this.current = new SqlExp(context);

        }

        /**
         * 开始表达式
         * @param name 第一个字段名
         * @return
         */
        public ISqlArithmetic start(final String name){
            if(current.valid){
                this.result = current;
                this.current = new SqlExp(context);
            }
            else{
                this.current.start(name);
            }
            return this;
        }

        /**
         * 结束表达式构建，编译前需调用以防止表达式未完成
         * @return
         */
        public SqlExp end(){
            if(current.valid){
                return new SqlExp(result, andOr, current);
            }
            return result;
        }

        @Override
        public ISqlArithmetic and(@NonNull final Object other) {
            //push current to result
            if(result == null || !result.valid) this.result = current;
            else this.result = new SqlExp(result, andOr, current);

            //start a new current
            this.andOr = SqlOp.AND;
            this.current = new SqlExp(other);
            return this;
        }

        @Override
        public ISqlArithmetic or(@NonNull final Object other) {
            //push current to result
            if(result == null || !result.valid) this.result = current;
            else this.result = new SqlExp(result, andOr, current);

            //start a new current
            this.andOr = SqlOp.OR;
            this.current = new SqlExp(other);
            return this;
        }

        @Override
        public ISqlCriteria andExists(@NonNull final SqlQuery subQuery) {
            this.current.binaryOp(SqlOp.AND, new SqlExists(subQuery,false));
            return this;
        }

        @Override
        public ISqlCriteria andNotExists(@NonNull final SqlQuery subQuery) {
            this.current.binaryOp(SqlOp.AND, new SqlExists(subQuery,true));
            return this;
        }

        @Override
        public ISqlCriteria orExists(@NonNull final SqlQuery subQuery) {
            this.current.binaryOp(SqlOp.OR, new SqlExists(subQuery,false));
            return this;
        }

        @Override
        public ISqlCriteria orNotExists(@NonNull final SqlQuery subQuery) {
            this.current.binaryOp(SqlOp.OR, new SqlExists(subQuery,true));
            return this;
        }

        @Override
        public ISqlCriteria not() {
            //push current to result
            if(result == null || !result.valid) this.result = current;
            else this.result = new SqlExp(result, andOr, current);

            //start a new current
            this.andOr = SqlOp.NOT;
            this.current = new SqlExp(context);
            return this;
        }

        @Override
        public ISqlArithmetic plus(String other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic plus(Integer other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic plus(Long other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic plus(BigDecimal other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic plus(Double other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic sub(String other) {
            this.current.binaryOp(SqlOp.PLUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic sub(Integer other) {
            this.current.binaryOp(SqlOp.MINUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic sub(BigDecimal other) {
            this.current.binaryOp(SqlOp.MINUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic sub(Double other) {
            this.current.binaryOp(SqlOp.MINUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic mul(String other) {
            this.current.binaryOp(SqlOp.MULTIPLY, other);
            return this;
        }

        @Override
        public ISqlArithmetic mul(Integer other) {
            this.current.binaryOp(SqlOp.MULTIPLY, other);
            return this;
        }
        @Override
        public ISqlArithmetic mul(Long other) {
            this.current.binaryOp(SqlOp.MULTIPLY, other);
            return this;
        }
        @Override
        public ISqlArithmetic mul(BigDecimal other) {
            this.current.binaryOp(SqlOp.MULTIPLY, other);
            return this;
        }
        @Override
        public ISqlArithmetic mul(Double other) {
            this.current.binaryOp(SqlOp.MULTIPLY, other);
            return this;
        }

        @Override
        public ISqlArithmetic div(String other) {
            this.current.binaryOp(SqlOp.DIVIDE, other);
            return this;
        }

        @Override
        public ISqlArithmetic div(Integer other) {
            this.current.binaryOp(SqlOp.DIVIDE, other);
            return this;
        }

        @Override
        public ISqlArithmetic div(Long other) {
            this.current.binaryOp(SqlOp.DIVIDE, other);
            return this;
        }

        @Override
        public ISqlArithmetic div(BigDecimal other) {
            this.current.binaryOp(SqlOp.DIVIDE, other);
            return this;
        }

        @Override
        public ISqlArithmetic div(Double other) {
            this.current.binaryOp(SqlOp.DIVIDE, other);
            return this;
        }

        @Override
        public ISqlArithmetic mod(String other) {
            this.current.binaryOp(SqlOp.MODULUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic mod(Integer other) {
            this.current.binaryOp(SqlOp.MODULUS, other);
            return this;
        }

        @Override
        public ISqlArithmetic leftShift(Integer other) {
            this.current.binaryOp(SqlOp.LEFT_SHIFT, other);
            return this;
        }

        @Override
        public ISqlArithmetic rightShift(Integer other, boolean unsigned) {
            if(unsigned) this.current.binaryOp(SqlOp.RIGHT_SHIFT_UNSIGNED, other);
            else this.current.binaryOp(SqlOp.RIGHT_SHIFT, other);
            return this;
        }

        @Override
        public ISqlArithmetic bitAnd(Integer other) {
            this.current.binaryOp(SqlOp.BIT_AND, other);
            return this;
        }

        @Override
        public ISqlArithmetic bitOr(Integer other) {
            this.current.binaryOp(SqlOp.BIT_OR, other);
            return this;
        }

        @Override
        public ISqlArithmetic bitXor(Integer other) {
            this.current.binaryOp(SqlOp.BIT_XOR, other);
            return this;
        }

        @Override
        public ISqlArithmetic bitNot() {
            this.current.unaryOp(SqlOp.BIT_NOT);
            return this;
        }


        @Override
        public ISqlCriteria eq(Object other) {
            this.current.binaryOp(SqlOp.EQUAL, other);
            return this;
        }

        @Override
        public ISqlCriteria ne(Object other) {
            this.current.binaryOp(SqlOp.NOT_EQUAL, other);
            return this;
        }

        @Override
        public ISqlCriteria gt(Object other) {
            this.current.binaryOp(SqlOp.GREATER_THAN, other);
            return this;
        }

        @Override
        public ISqlCriteria ge(Object other) {
            this.current.binaryOp(SqlOp.GREATER_OR_EQUAL, other);
            return this;
        }

        @Override
        public ISqlCriteria lt(Object other) {
            this.current.binaryOp(SqlOp.LESS_THAN, other);
            return this;
        }

        @Override
        public ISqlCriteria le(Object other) {
            this.current.binaryOp(SqlOp.LESS_OR_EQUAL, other);
            return this;
        }

        @Override
        public ISqlCriteria isNull() {
            this.current.unaryOp(SqlOp.IS_NULL);
            return this;
        }

        @Override
        public ISqlCriteria isNotNull() {
            this.current.unaryOp(SqlOp.IS_NOT_NULL);
            return this;
        }

        @Override
        public ISqlCriteria contains(String other) {
            this.current.binaryOp(SqlOp.LIKE, "%"+other+"%");
            return this;
        }

        @Override
        public ISqlCriteria startsWith(String other) {
            this.current.binaryOp(SqlOp.LIKE, other+"%");
            return this;
        }

        @Override
        public ISqlCriteria endsWith(String other) {
            this.current.binaryOp(SqlOp.LIKE, "%"+other);
            return this;
        }

        @Override
        public ISqlCriteria between(Object a, Object b) {
            this.current.binaryOp(SqlOp.BETWEEN, List.of(a,b));
            return this;
        }

        @Override
        public ISqlCriteria in(Object... other) {
            this.current.binaryOp(SqlOp.IN, List.of(other));
            return this;
        }

        @Override
        public ISqlCriteria notIn(Object... other) {
            this.current.binaryOp(SqlOp.NOT_IN, List.of(other));
            return this;
        }
    }

    //endregion of Builder

    //region Visitor模式编译

    /**
     * Sql表达式编译器实现{@link SqlExpVisitor}接口，可将表达式{@link SqlExp}编译为{@link ParametricExpression}，
     * 其中包含SQL表达式字符串和参数值、类型列表，用于进一步构建查询{@link cloud.mmda.core.sql.SqlQuery}
     */
    public class Compiler implements SqlExpVisitor {
        private final SqlDialect dialect;
        private final MetaContext context;
        private final boolean parametric;
        private DataType currentDataType;
        private int currentArgIndex;
        private boolean useContextAlias;
        private final StringBuilder sb = new StringBuilder();
        private final List<SqlArg> argList = new ArrayList<>();

        public Compiler(SqlDialect dialect, MetaContext context, boolean useContextAlias,boolean parametric) {
            this.dialect = dialect;
            this.context = context;
            this.useContextAlias = useContextAlias;
            this.parametric = parametric;
        }
        public Compiler(SqlDialect dialect, MetaContext context, boolean useContextAlias) {
            this(dialect, context, useContextAlias,false);
        }
        @Override
        public void visit(SqlExp expr) {
            var op = expr.getOp();
            if(expr.lhs instanceof String s){
                var col = context.findCol(s);
                if(col.isPresent()) {
                    expr.lhs = col.get();
                    currentDataType = expr.dataType = col.get().getDataType();
                }
            }
            if(expr.rhs instanceof String s){
                var col = context.findCol(s);
                if(col.isPresent()) {
                    expr.rhs = col.get();
                    if(expr.dataType == null) {
                        currentDataType = expr.dataType = col.get().getDataType();
                    }
                }
            }
            if(expr.lhs instanceof SqlExp e && expr.dataType == null){
                if(e.dataType != null) expr.dataType = e.dataType;
            }

            sb.append('(');
            if(op.isUnary()) visitUnary(expr);
            else if(op.isBinary()) visitBinary(expr);
            else if(op.isTernary()) visitTernary(expr);
            else visitMulti(expr);
            sb.append(')');
        }
        private void visitUnary(SqlExp expr){
            var op = expr.op;
            if(op.isPreUnary()) {
                sb.append(op.getText());
                visitOperand(expr.lhs, expr);
            }
            else {
                visitOperand(expr.lhs, expr);
                sb.append(op.getText());
            }
        }
        private void visitBinary(SqlExp expr){
            visitOperand(expr.lhs, expr);
            sb.append(expr.op.getText());
            visitOperand(expr.rhs, expr);
        }
        private void visitTernary(SqlExp expr){
            visitOperand(expr.lhs,expr);
            sb.append(expr.op.getText());
            var list  = (List<?>)expr.rhs;
            visitOperand(list.get(0),expr);
            sb.append(AND);
            visitOperand(list.get(1),expr);
        }
        private void visitMulti(SqlExp expr){
            var list  = (List<?>)expr.rhs;
            visitOperand(expr.lhs,expr);
            sb.append(expr.op.getText())
                    .append(PARENTHESES_START)
                    .append(BaseUtil.repeat("?", list.size(), COMMA))
                    .append(PARENTHESES_END);
            list.stream().forEach(it -> addArg(expr.dataType, it));
        }
        private void visitOperand(Object operand, SqlExp expr) {
            if(operand == null) return;

            if(operand instanceof MetaCol col) {
                sb.append(useContextAlias ? dialect.quoteCol(col, context) : dialect.quote(col));
                if(expr.dataType == null){
                    currentDataType = expr.dataType = col.getDataType();
                }
                return;
            }
            else if(operand instanceof SqlExp e){
                if(e.dataType != null && expr.dataType == null) {
                    currentDataType = expr.dataType = e.dataType;
                }
                visit(e);
                return;
            }
            if(expr.dataType == null) expr.dataType = currentDataType;

            addArg(expr.dataType, operand);

            if(this.parametric) sb.append('?');
            else sb.append(operand);
        }

        private void addArg(DataType dataType, Object value){
            var argName = "P" + (currentArgIndex++);
            argList.add(new SqlArg(argName, dataType, value));
        }
    }
    //endregion
}
