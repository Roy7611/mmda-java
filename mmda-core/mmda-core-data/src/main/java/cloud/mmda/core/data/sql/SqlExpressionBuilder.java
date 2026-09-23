package cloud.mmda.core.data.sql;//package cloud.mmda.data.jdbc.repository;
//
//import cloud.mmda.core.metadata.MetaCol;
//import cloud.mmda.core.metadata.MetaDataType;
//import cloud.mmda.core.metadata.MetaObject;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class SqlExpressionBuilder {
//	private MetaObject metaObj;
//	private SqlExpression resultExp;
//	private SqlExpression currExp;
//
//	public SqlExpressionBuilder(MetaObject metaObj){
//		this.metaObj = metaObj;
//	}
//
//	public SqlExpressionBuilder exp(String colName){
//		MetaCol col = metaObj.getCol(colName);
//		currExp = new SqlExpression(col);
//		resultExp=currExp;
//		return this;
//	}
//
//
//	public SqlExpressionBuilder op(String op, Object value){
//		currExp.setOperator(op);
//		currExp.setValue(value);
//		return this;
//	}
//
//	public SqlExpressionBuilder equal(Object value){
//		return op(SqlExpression.EQUAL,value);
//	}
//	public SqlExpressionBuilder notEqual(Object value){
//		return op(SqlExpression.NOT_EQUAL,value);
//	}
//	public SqlExpressionBuilder greaterThan(Object value){
//		return op(SqlExpression.GREATER_THAN,value);
//	}
//	public SqlExpressionBuilder greaterOrEqual(Object value){
//		return op(SqlExpression.GREATER_OR_EQUAL,value);
//	}
//	public SqlExpressionBuilder lessThan(Object value){
//		return op(SqlExpression.LESS_THAN,value);
//	}
//	public SqlExpressionBuilder lessOrEqual(Object value){
//		return op(SqlExpression.LESS_OR_EQUAL,value);
//	}
//	public SqlExpressionBuilder between(Object value1, Object value2){
//		return op(SqlExpression.BETWEEN,new Object[]{value1, value2});
//	}
//
//	final String quoteValue(String value){
//		return "'"+value+"'";
//	}
//
//	public SqlExpressionBuilder startsWith(String value){
//		return op(SqlExpression.LIKE, quoteValue(value+"%"));
//	}
//	public SqlExpressionBuilder endsWith(String value){
//		return op(SqlExpression.LIKE, quoteValue("%"+value));
//	}
//	public SqlExpressionBuilder contains(String value){
//		return op(SqlExpression.LIKE, quoteValue("%"+value+"%"));
//	}
//
//	boolean isNeedToQuote(){
//		return MetaDataType.isQuotable(currExp.getCol().getDataType());
//	}
//	public SqlExpressionBuilder in(String values){
//		if(values.indexOf("'") == -1 && isNeedToQuote()){
//			values = Arrays.stream(values.split(","))
//					.map(this::quoteValue)
//					.collect(Collectors.joining(","));
//		}
//		return op(SqlExpression.IN, "("+values+")");
//	}
//	public SqlExpressionBuilder in(String...values){
//		if(isNeedToQuote() && values[0].indexOf("'") == -1){
//			String list = Arrays.stream(values)
//					.map(this::quoteValue)
//					.collect(Collectors.joining(","));
//			return op(SqlExpression.IN, "("+list+")");
//		}
//		return op(SqlExpression.IN, "("+String.join(",",values)+")");
//	}
//	public SqlExpressionBuilder notIn(String values){
//		if(values.indexOf("'") == -1 && isNeedToQuote()){
//			values = Arrays.stream(values.split(","))
//					.map(this::quoteValue)
//					.collect(Collectors.joining(","));
//		}
//		return op(SqlExpression.NOT_IN, "("+values+")");
//	}
//	public SqlExpressionBuilder notIn(String...values){
//		if(isNeedToQuote() && values[0].indexOf("'") == -1){
//			String list = Arrays.stream(values)
//					.map(this::quoteValue)
//					.collect(Collectors.joining(","));
//			return op(SqlExpression.NOT_IN, "("+list+")");
//		}
//		return op(SqlExpression.NOT_IN, "("+String.join(",",values)+")");
//	}
//
//
//	public SqlExpressionBuilder isNull(){
//		return op(SqlExpression.IS_NULL, "");
//	}
//	public SqlExpressionBuilder isNotNull(){
//		return op(SqlExpression.IS_NOT_NULL, "");
//	}
//	public SqlExpressionBuilder and(String colName){
//		MetaCol col = metaObj.getCol(colName);
//		currExp = new SqlExpression(col);
//		resultExp.and(currExp);
//		return this;
//	}
//	public SqlExpressionBuilder or(String colName){
//		MetaCol col = metaObj.getCol(colName);
//		currExp = new SqlExpression(col);
//		resultExp.or(currExp);
//		return this;
//	}
//
//	public SqlExpression result(){
//		return resultExp;
//	}
//}
