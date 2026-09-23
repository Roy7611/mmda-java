//package cloud.mmda.core.sql;
//
//import cloud.mmda.core.Tenancy;
//import cloud.mmda.core.enums.DataType;
//import cloud.mmda.core.lamda.LambdaGetter;
//import cloud.mmda.core.lamda.LambdaSetter;
//import cloud.mmda.core.lamda.LambdaUtil;
//import cloud.mmda.core.metadata.*;
//import cloud.mmda.core.sql.dialects.SqlDialect;
//import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
//import cloud.mmda.core.sql.expressions.SqlExp;
//import cloud.mmda.core.utils.BaseUtil;
//import lombok.Getter;
//import org.springframework.jdbc.core.PreparedStatementCreator;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.*;
//import java.util.function.Function;
//
//import static cloud.mmda.core.sql.SqlConsts.*;
//
///**
// * Sql 命令用来插入、更新和删除数据
// * <pre>
// *     {@code
// *          //插入
// *          var cmdInsert = SqlCommand.insert(User.class)
// *              .set(User::setUserName, "Bob")
// *              .set(User::setMobile, "13918832422")
// *              .set(...);//set 可以没有，表示所有可插入字段
// *          //单表删除
// *          var cmdDelete = SqlCommand.delete(User.class)
// *              .where(criteria(User::getUserID).eq(12L));
// *          //多表关联删除
// *          var cmdDeleteFrom = SqlCommand.delete(OrderItem.class)
// *              .from(Order.class)
// *              .where(criteria(Order::getStatus).lt(0));
// *          //单表更新
// *          var cmdUpdate = SqlCommand.update(User.class)
// *              .set(User::setUserName, "Bob")
// *              .set(User::setMobile, "13918832422")
// *              .set(...) //set 可以没有，表示所有可更新字段
// *              .where(criteria(User::getUserID).eq(12L));
// *          //多表关联更新
// *          var cmdUpdateFrom = SqlCommand.update(OrderItem.class)
// *              .from(Order.class, Partner.class)
// *              .set(OrderItem::setQuantity, exp(User::getQuantity).plus(1))
// *              .set(OrderItem::setUnit, "ton")
// *              .set(...)
// *              .where(criteria(OrderItem::getOrderID).eq(120L));
// *     }
// * </pre>
// */
//public class SqlCommand {
//    static final String T = "t"; //本身别名
//
//    @Getter
//    private String name;
//    private MetaObject target;
//    private String commandType; //INSERT, UPDATE, DELETE
//    @Getter
//    private List<MetaRelation> fromRelations;
//    private LinkedHashMap<MetaCol, Object> setters;
//    @Getter
//    private SqlExp whereExp;
//
//    @Getter
//    private String compiledSql;
//    private List<SqlArg> argList;
//    private Function<Integer,Long> getMinId, getMaxId;
//    private int argMinIdIndex;
//
//    public static <T> SqlCommand insert(MetaObject metaObject, String sql, List<SqlArg> argList){
//        var cmd = new SqlCommand(INSERT);
//        cmd.commandType = INSERT;
//        cmd.target = metaObject;
//        return cmd;
//    }
//
//    public boolean isCompiled(){
//        return compiledSql != null;
//    }
//
//    public PreparedStatementCreator statement(Object...args) {
//        if(!isCompiled()){
//            throw new IllegalStateException("Command must be compiled before calling statement");
//        }
//        if(args.length != argList.size()){
//            throw new IllegalArgumentException("The number of arguments does not match.");
//        }
//        return connection -> {
//            var ps = connection.prepareStatement(compiledSql);
//            int j = 0;
//            for (int i = 0; i < argList.size(); i++) {
//                var argProto = argList.get(i);
//                ps.setObject(i, args[i], argProto.dataType().jdbcType());
//            }
//            return ps;
//        };
//    }
//    public PreparedStatementCreator statement(int tenantId, Object...args) {
//        if(getMinId == null || getMaxId == null){
//            throw new IllegalStateException("Command is not tenancy");
//        }
//        var argArray = new LinkedList<>(Arrays.asList(args));
//        argArray.add(argMinIdIndex,getMaxId.apply(tenantId));
//        argArray.add(argMinIdIndex,getMinId.apply(tenantId));
//        return statement(argArray.toArray());
//    }
//
//    public SqlCommand(String name) {
//        this.name = name;
//        this.setters = new LinkedHashMap<>();
//    }
//    public SqlCommand(String name, MetaObject target) {
//        this.name = name;
//        this.target = target;
//        this.setters = new LinkedHashMap<>();
//    }
//
//    public <U> SqlCommand set(String colName, U value){
//        var col = target.getCol(colName);
//        return set(col,value);
//    }
//
//    public <T, U> SqlCommand set(LambdaSetter<T,U> field, U value){
//        var colName = LambdaUtil.getFieldName(field);
//        var col = target.getCol(colName);
//        return set(col,value);
//    }
//
//    public <U> SqlCommand set(MetaCol col, U value){
//        Objects.requireNonNull(col, "col must not be null");
//        if(col.isComputed() || col.isGenerated()) throw new IllegalArgumentException(col.getColName() + " can not be updated");
//        setters.put(col, value);
//        return this;
//    }
//
//    public SqlCommand set(LinkedHashMap<String,Object> values){
//        for(var entry : values.entrySet()){
//            set(entry.getKey(),entry.getValue());
//        }
//        return this;
//    }
//
//    public SqlCommand where(SqlCriteriaExp exp){
//        Objects.requireNonNull(exp, "exp must not be null");
//        this.whereExp = exp;
//        return this;
//    }
//
//    /**
//     * 编译出Sql语句和参数值和类型列表
//     * @param dialect
//     * @return
//     */
//    public SqlCommand compile(SqlDialect dialect){
//        if(isCompiled()) return this;
//        if(BaseUtil.hasText(target.getPartitionKey())){
//            getMinId = (tenantId) -> Tenancy.getMinEntityID(tenantId, target);
//            getMaxId = (tenantId) -> Tenancy.getMaxEntityID(tenantId, target);
//        }
//        compiledSql = switch (commandType){
//            case INSERT -> compileInsert(dialect);
//            case UPDATE -> compileUpdate(dialect);
//            case DELETE -> compileDelete(dialect);
//            default -> throw new IllegalStateException("Unexpected command type " + commandType);
//        };
//
//        return this;
//    }
//
//    private String compileInsert(SqlDialect dialect){
//        var sql =  dialect.insert(this.target,false);
//        argList = this.target.getInsertableCols().stream()
//                .map(col -> SqlArg.of(dialect, col))
//                .toList();
//        return sql;
//    }
//    private String compileUpdate(SqlDialect dialect){
//        argList = new ArrayList<>();
//        var sb = new StringBuilder(commandType).append(T);
//
//        // from
//        var fromClause = dialect.from(this.target, fromRelations);
//        sb.append(FROM).append(fromClause);
//
//        // setters
//        sb.append(SET);
//        var setterJoiner = new StringJoiner(COMMA);
//        for(var setter : setters.entrySet()){
//            argList.add(SqlArg.of(dialect, setter.getKey(), setter.getValue()));
//            var setterEquation = dialect.quoteCol(setter.getKey(), target)  + EQUALS + '?';
//            setterJoiner.add(setterEquation);
//        }
//        sb.append(setterJoiner.toString());
//
//        // where
//        String whereClause = EMPTY;
//        if(whereExp != null){
//            var expResult = whereExp.compile(dialect,target);
//            whereClause = expResult.expression();
//            if(BaseUtil.hasText(target.getPartitionKey())){
//                whereClause = wherePartition(dialect, argList) + AND + whereClause;
//            }
//            argList.addAll(expResult.argList());
//        }
//        else {
//            if(BaseUtil.hasText(target.getPartitionKey())){
//                whereClause = wherePartition(dialect, argList);
//            }
//        }
//        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);
//
//        return sb.toString();
//    }
//
//    private String compileDelete(SqlDialect dialect){
//        var sb = new StringBuilder(commandType);
//        argList = new ArrayList<>();
//        // from
//        var fromClause = dialect.from(this.target, fromRelations);
//        sb.append(T).append(FROM).append(fromClause);
//        // where
//        String whereClause = EMPTY;
//        if(whereExp != null){
//            var expResult = whereExp.compile(dialect,target);
//            whereClause = expResult.expression();
//            if(BaseUtil.hasText(target.getPartitionKey())){
//                whereClause = wherePartition(dialect, argList) + AND + whereClause;
//            }
//            argList.addAll(expResult.argList());
//        }
//        else {
//            if(BaseUtil.hasText(target.getPartitionKey())){
//                whereClause = wherePartition(dialect, argList);
//            }
//        }
//        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);
//
//        return sb.toString();
//    }
//    private String wherePartition(SqlDialect dialect, List<SqlArg> args){
//        var partitionIdType = dialect.getDbDataType(DataType.INT64);
//        argMinIdIndex = args.size();
//        args.add(new SqlArg(partitionIdType, "minId", getMinId));
//        args.add(new SqlArg(partitionIdType, "maxId", getMaxId));
//        return "(" + dialect.quoteObject(T, target.getPartitionKey())
//                + BETWEEN + "? AND ?)";
//    }
//
//
//    public static SqlCommand delete(String cmdName, MetaObject metaObject){
//        var cmd = new SqlCommand(cmdName);
//        cmd.commandType = DELETE;
//        cmd.target = metaObject;
//        return cmd;
//    }
//
//
//    public static <T> SqlCommand update(String cmdName, MetaObject metaObject){
//        var cmd = new SqlCommand(cmdName);
//        cmd.commandType = UPDATE;
//        cmd.target = metaObject;
//        return cmd;
//    }
//
//    //region Fluent API 接口
//    public interface CommandTarget{
//        CommandSetter to(MetaObject target, String...withRelations);
//        <R> CommandSetter to(Class<?> entityClass, LambdaGetter<R>...withRelatives);
//    }
//    public interface CommandSetter{
//        CommandSetter set(String colName, Object value);
//        <T> CommandSetter set(LambdaGetter colName, Object value);
//        CommandWhere where(SqlCriteriaExp exp);
//    }
//    public interface CommandWhere {
//
//    }
//    public interface CommandExecutable {
//        SqlCommand update();
//        SqlCommand delete();
//    }
//    //endregion of Fluent API
//
//    public static class Builder implements CommandTarget{
//        private final MetadataProvider metadataProvider;
//        private final SqlCommand command;
//        public Builder(final String name, final MetadataProvider metadataProvider){
//            this.metadataProvider = metadataProvider;
//            this.command = new SqlCommand(name);
//        }
//
//
//        @Override
//        public CommandSetter to(MetaObject target, String... withRelations) {
//            return null;
//        }
//
//        @Override
//        public <R> CommandSetter to(Class<?> entityClass, LambdaGetter<R>... withRelatives) {
//            return null;
//        }
//    }
//}
