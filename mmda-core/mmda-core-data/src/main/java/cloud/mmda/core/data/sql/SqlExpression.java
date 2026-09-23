package cloud.mmda.core.data.sql;


import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.DateRangeKind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Sql表达式和带值的参数
 * 
 * @author roshion
 *
 */
public class SqlExpression {
	public static final String EQUAL = "=";
//	public static final String BETWEEN = SqlOperator.BETWEEN.getOp();
//	public static final String LIKE = SqlOperator.LIKE.getOp();
//	public static final String IN = SqlOperator.IN.getOp();
	public static final String IS_NULL = SqlOperator.IS_NULL.getOp();

	public static final String AND = SqlOperator.AND.getOp();
	public static final String OR = SqlOperator.OR.getOp();

	// 生成表达式时右边类型
	public static final char NAMED_PARAMETER = ':';
	public static final char ALIAS_DOT_NAME = '.';
	public static final char PARSED_VALUE = '$';
	public static final String SEARCH_WORD = "_WORD_";

	// 右边值类型
	public static final int NONE = 0;
	public static final int CONSTANT = 1;
	public static final int VARIABLE = 2;
	public static final int EXPRESSION = 3;

	protected MetaCol col;
	protected Object value;
	protected SqlOperator operator;
	protected String additionalCond;//额外限制条件

	private List<String> argNames;
	private List<String> argSymbols;
	private List<Object> argValues;
	private List<Integer> argTypes;

	private SqlExpression() {
		this.operator = SqlOperator.EQUAL;
	}

	public SqlExpression(MetaCol col) {
		this(col,SqlOperator.EQUAL,null);
	}

	private SqlExpression(MetaCol col, Object val) {
		this(col,SqlOperator.EQUAL,val);
	}

	private SqlExpression(MetaCol col, SqlOperator op, Object val) {
		this.col = col;
		this.operator = op;
		this.value = val;
	}
	public static SqlExpression ofEqual(MetaCol col){
		return new SqlExpression(col);
	}
	public static SqlExpression ofEqual(MetaCol col,Object val){
		return new SqlExpression(col,val);
	}

	public static SqlExpression ofAllEqual(List<MetaCol> cols){
		SqlExpression sqlExpression = new SqlExpression(cols.get(0));
		for (int i = 1; i < cols.size(); i++) {
			MetaCol c = cols.get(i);
			sqlExpression = sqlExpression.and(new SqlExpression(c));
		}
		return sqlExpression;
	}
	public static SqlExpression ofAllEqual(List<MetaCol> cols, List<Object> args){
		SqlExpression sqlExpression = new SqlExpression(cols.get(0), args.get(0));
		for (int i = 1; i < cols.size(); i++) {
			MetaCol c = cols.get(i);
			Object v = args.get(i);
			sqlExpression = sqlExpression.and(new SqlExpression(c, v));
		}
		return sqlExpression;
	}

	public static SqlExpression searchAll(List<MetaCol> cols){
		SqlExpression sqlExpression = new SqlExpression(cols.get(0), SqlOperator.LIKE, SEARCH_WORD);

		for (int i = 1; i < cols.size(); i++) {
			MetaCol c = cols.get(i);
			sqlExpression = sqlExpression.or(new SqlExpression(c, SqlOperator.LIKE, SEARCH_WORD));
		}
		return sqlExpression;
	}

	public MetaCol getCol() {
		return col;
	}

	public void setCol(MetaCol col) {
		this.col = col;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isDateFilter(){
		return col.getDataType().hasDatePart()
				&& operator.isAssignment()
				&& (value instanceof DateRangeKind || Arrays.stream(DateRangeKind.values()).anyMatch(dateRangeKind -> dateRangeKind.name().equals(value)));
	}

	public SqlOperator getOperator() {
		return operator;
	}

	public final boolean isUnaryOperator(){
		return this.operator.getArgNum()==1;
	}

	public final boolean hasArgs(){
		return this.operator.getArgNum()<4 && this.operator.getArgNum()>1;
	}

	public String getAdditionalCond(){ return additionalCond; }
	public void setAdditionalCond(String additionalCond ){
		this.additionalCond = additionalCond;
	}
	public SqlExpression withAdditionalCondition(String additionalCond){
		this.additionalCond=additionalCond;
		return this;
	}

	class SqlInnerExpression {
		String op;
		SqlExpression expression;

		public SqlInnerExpression(String l, SqlExpression e) {
			op = l;
			expression = e;
		}
	}

	private List<SqlInnerExpression> innerExpressions;

	protected List<SqlInnerExpression> getInnerExpressions() {
		if (innerExpressions == null)
			innerExpressions = new LinkedList<SqlInnerExpression>();
		return innerExpressions;
	}

	public SqlExpression and(SqlExpression e) {
		getInnerExpressions().add(new SqlInnerExpression(AND, e));
		return this;
	}

	public SqlExpression or(SqlExpression e) {
		getInnerExpressions().add(new SqlInnerExpression(OR, e));
		return this;
	}
	
	public boolean isComplex(){
		return BaseUtil.hasAny(this.innerExpressions);
	}

	public List<String> getArgNames() {
		return argNames;
	}

	public List<Object> getArgValues() {
		return argValues;
	}

	public List<Integer> getArgTypes() {
		return argTypes;
	}

	public List<String> getArgSymbols() {
		return argSymbols;
	}


	private void collectExpressionArgs( SqlExpression sqle, Function<MetaCol, String> aliasDotNameFunc, char placeHolder, boolean withName) {
		MetaCol col = sqle.getCol();
		int argNum = sqle.getOperator().getArgNum();
		if(sqle.getOperator().hasTwoRightArg()){//SqlOperator.BETWEEN
			argNames.add(col.getColName());
			Object[] values = (Object[]) sqle.getValue();
			//参数1
			argSymbols.add(toArgSymbol(sqle.getValue(),aliasDotNameFunc,placeHolder,withName,col.getColName()+0));
			addArg(values[0],col.getDataType().getJdbcType());

			//参数2
			argSymbols.add(toArgSymbol(sqle.getValue(),aliasDotNameFunc,placeHolder,withName,col.getColName()+1));
			addArg(values[1],col.getDataType().getJdbcType());
		}
		else if(sqle.getOperator().hasOneRightArg()) {
			argNames.add(col.getColName());
			argSymbols.add(toArgSymbol(sqle.getValue(),aliasDotNameFunc,placeHolder,withName,col.getColName()));
			addArg(sqle.getValue(),col.getDataType().getJdbcType());
		}

		if (sqle.isComplex())
		{
			for (SqlInnerExpression ie : sqle.getInnerExpressions()) {
				collectExpressionArgs(ie.expression, aliasDotNameFunc, placeHolder, withName);
			}
		}
	}
	/**
	 * 构建参数值和类型列表 构建后可使用{@link #getArgValues}和{@link #getArgTypes}获取结果
	 */
	public void buildArgs(Function<MetaCol, String> aliasDotNameFunc, char placeHolder, boolean withName) {
		argNames = new ArrayList<String>();
		argSymbols = new ArrayList<String>();
		argValues = new ArrayList<Object>();
		argTypes = new ArrayList<Integer>();

		collectExpressionArgs(this, aliasDotNameFunc, placeHolder, withName);
	}

	/**
	 * 构建等式右边的计算表达式的右参数rhs 字符串形式
	 * @param v
	 * @param aliasDotNameFunc
	 * @param placeHolder
	 * @return 返回字符串，如 quantity, quantity + 3, (quantity + ?) * 2 或者常量 8
	 */
	private static String buildArgExpressionVar(Object v,Function<MetaCol, String> aliasDotNameFunc, char placeHolder){
		String varName;
		if(v instanceof SqlArgExpression){
			varName = buildArgExpresion((SqlArgExpression)v,aliasDotNameFunc,placeHolder);
		}
		else if(v instanceof MetaCol){
			varName = aliasDotNameFunc.apply((MetaCol) v);
		}
		else if(v instanceof SqlVariable){
			varName = String.valueOf(placeHolder);
		}
		else{
			varName = String.valueOf(v);
		}
		return varName;
	}

	/**
	 * 构建参数表达式，通常指=右边的算术表达式
	 * @param e
	 * @param aliasDotNameFunc
	 * @param placeHolder
	 * @return
	 */
	public static String buildArgExpresion(SqlArgExpression e,Function<MetaCol, String> aliasDotNameFunc, char placeHolder){
		String lhsName = buildArgExpressionVar(e.lhs,aliasDotNameFunc,placeHolder);
		if(e.op.getArgNum()<0){
			return e.op.getOp() + lhsName;
		}
		return lhsName + e.op.getOp() + (
				e.op.getArgNum()>1
						? buildArgExpressionVar(e.rhs,aliasDotNameFunc,placeHolder)
						: ""
		);
	}

	public static String buildExpression(SqlExpression e,Function<MetaCol, String> aliasDotNameFunc, char placeHolder){
		StringBuilder sb = new StringBuilder();
		String colName = aliasDotNameFunc.apply(e.col);
		sb.append(colName).append(e.operator.getOp());

		if(e.value instanceof SqlArgExpression){
			String valueString = buildArgExpresion((SqlArgExpression) e.value,aliasDotNameFunc,placeHolder);
			sb.append(valueString);
		}
		//使用SqlArgExpression
		else if(e.value instanceof MetaCol){
			MetaCol rightCol = (MetaCol) e.getValue();
			sb.append(aliasDotNameFunc.apply(rightCol));
		}
		else{
			int argNum = e.getOperator().getArgNum();
			if(e.getOperator().hasTwoRightArg()) //Between
				sb.append(placeHolder).append(AND).append(placeHolder);
			else if(e.getOperator().hasConstRightArg())//IN,Not IN,Like,Not Like
				sb.append(e.getValue());
			else if(e.getOperator().hasOneRightArg())
				sb.append(placeHolder);
		}

		if (e.isComplex()) {
			for (SqlInnerExpression sle : e.getInnerExpressions()) {
				String expr = buildExpression(sle.expression,aliasDotNameFunc, placeHolder);
				sb.append(sle.op).append(expr);
			}
		}
		// 加括弧(expr)
		sb.insert(0, '(').append(')');
		if(BaseUtil.hasText(e.additionalCond))
			return and(sb.toString(),e.additionalCond);
		return sb.toString();
	}
	/**
	 * 构建表达式
	 * 
	 * @param aliasDotNameFunc
	 *            取得字段全称的函数
	 * @param placeHolder
	 *            占位符 :命名参数，?匿名参数
	 * @return
	 */
	public String buildExpression(Function<MetaCol, String> aliasDotNameFunc, char placeHolder) {
		return buildExpression(this,aliasDotNameFunc,placeHolder);
	}
	

	public MetaCol getRightCol() {
		if (value == null || !MetaCol.class.isAssignableFrom(value.getClass())) {
			return null;
		}
		return (MetaCol) value;
	}

	private void collectRightCols(SqlExpression e, List<MetaCol> rightCols){
		MetaCol rightCol = e.getRightCol();
		if (rightCol != null) rightCols.add(rightCol);
		if (e.isComplex()) {
			for (SqlInnerExpression le : e.getInnerExpressions()) {
				collectRightCols( le.expression,rightCols);
			}
		}
	}
	public List<MetaCol> getRightCols() {
		List<MetaCol> rightCols = new ArrayList<MetaCol>();
		collectRightCols(this,rightCols);
		return rightCols;
	}

	public List<String> getRightObjNames() {
		List<MetaCol> rightCols = getRightCols();
		return rightCols.stream()
						.map(c -> c.getObjName())
						.distinct()
						.collect(Collectors.toList());
	}
	
	///////////////////////////////////////////////////////////////////
	//属性表达式哈希，用于缓存
	private String attrIds;

	private void addArg(Object arg, int dataType){
		if(arg instanceof MetaCol) return;
		if(arg instanceof SqlArgExpression){
			SqlVariable v = ((SqlArgExpression)arg).getVariable();
			if(v!=null){
				if(v.value instanceof EnumValue)
					argValues.add(((EnumValue<?>) v.value).getValue());
				else
					argValues.add(arg);
				if(argTypes!=null) argTypes.add(v.dataType);
			}
		}
		else{
			if(arg instanceof EnumValue)
				argValues.add(((EnumValue<?>) arg).getValue());
			else
				argValues.add(arg);
			if(argTypes!=null) argTypes.add(dataType);
		}
	}
	private String toArgSymbol(Object arg, Function<MetaCol, String> aliasDotNameFunc, char placeHolder, boolean withName, String name){
		if(arg instanceof SqlArgExpression) {
			String symbol = buildArgExpressionVar(arg,aliasDotNameFunc,placeHolder);
			if(withName) symbol = symbol.replace(String.valueOf(placeHolder),placeHolder + col.getColName());
			return symbol;
		}
		return withName ? placeHolder + name : String.valueOf(placeHolder);
	}
	/**
	 * 构建更新属性列表
	 */
	public void buildAttributes(){
		argValues = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder().append(col.getColIdx());
		addArg(value,col.getDataType().getJdbcType());
		for(int i=0; i<getInnerExpressions().size(); i++){
			SqlInnerExpression le = innerExpressions.get(i);
			addArg(le.expression.value,le.expression.col.getDataType().getJdbcType());
			sb.append(' ').append(le.expression.col.getColIdx());
		}
		attrIds = sb.toString();
	}

	/**
	 * 获取属性对应id，以空格隔开的列表
	 * @return 例如"1 2 3 5"
	 */
	public String getAttrIds() {
		return attrIds;
	}

	/**
	 * 获取属性值列表
	 * @return
	 */
	public List<Object> getAttrValues() {
		return argValues;
	}
	
	/**
	 * 判断是否平面型，不含嵌套表达式
	 * @return
	 */
	public boolean isFlat(){
		if(isComplex()) {
			for(SqlInnerExpression sle: innerExpressions){
				if(sle.expression.isComplex()) return false;
			}
		}
		return true;
	}
	
	//条件表达式哈希用于缓存
	private String condIds;
	
	public String getCondIds() {
		return condIds;
	}


	public void buildFlatConditions(){
		argValues = new ArrayList<Object>();
		argTypes = new ArrayList<>();
		StringBuilder sb = new StringBuilder().append(col.getColIdx()).append(operator);
		if(operator.hasTwoRightArg()){//SqlOperator.BETWEEN
			Object[] values = (Object[]) value;
			addArg(values[0],col.getDataType().getJdbcType());
			addArg(values[1],col.getDataType().getJdbcType());
		}
		else if(operator.hasOneRightArg())
			addArg(value,col.getDataType().getJdbcType());

		for(int i=0; i<getInnerExpressions().size(); i++){
			SqlInnerExpression le = innerExpressions.get(i);
			if(le.expression.operator.hasTwoRightArg()){
				Object[] values = (Object[]) le.expression.value;
				int dataType = le.expression.col.getDataType().getValue();
				addArg(values[0],dataType);
				addArg(values[1],dataType);
			}
			else if(le.expression.operator.hasOneRightArg())
				addArg(le.expression.value,le.expression.col.getDataType().getJdbcType());
			sb.append(le.expression.getCol().getColIdx()).append(le.expression.operator);
		}
		condIds = sb.toString();
	}

	public static final String and(final String condA, final String condB){
		if(!BaseUtil.hasText(condA)) return condB;
		if(!BaseUtil.hasText(condB)) return condA;
		return "("+condA+")"+AND+"("+condB+")";
	}
	public static final String or(final String condA, final String condB){
		if(!BaseUtil.hasText(condA)) return condB;
		if(!BaseUtil.hasText(condB)) return condA;
		return "("+condA+")"+OR+"("+condB+")";
	}
//	public static final SqlExpression and(final SqlExpression a, final SqlExpression b){
//		SqlExpression c =
//	}

	//region SqlArgExpression
	public static class SqlConst{
		private final Object value;
		private SqlConst(Object value){
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
	public static class SqlVariable{
		private final Object value;
		private final int dataType;
		private SqlVariable(Object value,int dataType){
			this.value = value;
			this.dataType = dataType;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
	public static class SqlFunc{
		private final String funcName;
		private final List<Object> parameters;
		private SqlFunc(String name,List<Object> parameters){
			this.funcName = name;
			this.parameters = parameters;
		}
	}

	public static class SqlArgExpression {
		private Object lhs;
		private SqlOperator op;
		private Object rhs;
		private int status = 0;//1 lhs, 2 operator, 3 rhs

		private SqlArgExpression(Object lhs, SqlOperator op, Object rhs){
			this.lhs = lhs;
			this.op = op;
			this.rhs = rhs;
			this.status = 3;
		}

		private SqlArgExpression(Object lhs){
			BaseUtil.requireNonNull(lhs,"至少指定表达式的第一个参数");
			this.lhs = lhs;
			this.op = SqlOperator.NONE;
			this.status = 1;
		}

		private final SqlArgExpression op(final SqlOperator op, final Object rhs){
			if(this.status == 3){
				return new SqlArgExpression(this,op,rhs);
			}
			this.op = op;
			this.rhs = rhs;
			this.status = 3;
			return this;
		}
		public final SqlArgExpression plus(final Object value){
			return op( SqlOperator.PLUS,value);
		}
		public final SqlArgExpression minus(final Object value){
			return op( SqlOperator.MINUS,value);
		}
		public final SqlArgExpression multiply(final Object value){
			return op( SqlOperator.MULTIPLY,value);
		}
		public final SqlArgExpression devide(final Object value){
			return op( SqlOperator.DIVIDE,value);
		}
		public final SqlArgExpression modulus(final Object value){
			return op( SqlOperator.MODULUS,value);
		}
		public final SqlArgExpression leftShift(final Object value){
			return op( SqlOperator.LEFT_SHIFT,value);
		}
		public final SqlArgExpression rightShift(final Object value){
			return op( SqlOperator.RIGHT_SHIFT,value);
		}
		public final SqlArgExpression rightShiftUnsigned(final Object value){
			return op( SqlOperator.RIGHT_SHIFT_UNSIGNED,value);
		}
		public final SqlArgExpression bitAnd(final Object value){
			return op( SqlOperator.BIT_AND,value);
		}
		public final SqlArgExpression bitOr(final Object value){
			return op( SqlOperator.BIT_OR,value);
		}
		public final SqlArgExpression bitXor(final Object value){
			return op( SqlOperator.BIT_XOR,value);
		}
		public final SqlArgExpression bitNot(){
			return op( SqlOperator.BIT_NOT,null);
		}

//		public Object toArgValue(){
//			//左参数
//			if(lhs instanceof SqlVariable) return ((SqlVariable) lhs).value;
//			if(rhs != null && rhs instanceof SqlVariable) return ((SqlVariable) rhs).value;
//			//如果是嵌套表达式
//			if(lhs instanceof SqlArgExpression) {
//				return ((SqlArgExpression) lhs).toArgValue();
//			}
//			//否则
//			return rhs;
//		}
		public SqlVariable getVariable(){
			//左参数
			if(lhs instanceof SqlVariable) return ((SqlVariable) lhs);
			if(rhs != null && rhs instanceof SqlVariable) return ((SqlVariable) rhs);
			//如果是嵌套表达式
			if(lhs instanceof SqlArgExpression) {
				return ((SqlArgExpression) lhs).getVariable();
			}
			//否则
			return null;
		}
	}

	public static final SqlVariable var(Object value, int dataType){
		return new SqlVariable(value,dataType);
	}
	public static final SqlVariable var(){
		return new SqlVariable(null, DataType.CHAR.getJdbcType());
	}
	public static final SqlArgExpression arg(Object lhs){
		return new SqlArgExpression(lhs);
	}

	//endregion



	//region SqlExpressionBuilder
	public static SqlExpressionBuilder builder(MetaObject metaObject){
		return new SqlExpressionBuilder(metaObject);
	}

	public static final class SqlExpressionBuilder {
		private MetaObject metaObj;
		private SqlExpression resultExp;
		private SqlExpression currExp;

		public SqlExpressionBuilder(MetaObject metaObj){
			this.metaObj = metaObj;
		}

		public SqlExpressionBuilder exp(String colName){
			MetaCol col = metaObj.getCol(colName);
			return exp(col);
		}
		public SqlExpressionBuilder exp(MetaCol col){
			currExp = new SqlExpression(col);
			resultExp=currExp;
			return this;
		}

		public SqlExpressionBuilder op(SqlOperator operator, Object value){
			currExp.operator= operator;
			currExp.setValue(value);
			return this;
		}
		//
		public SqlExpressionBuilder eq(Object value){
			return op(SqlOperator.EQUAL,value);
		}

		/**
		 * 等于，请使用{@link this#eq(Object)}避免与对象本身的equals混淆
		 * @param value
		 * @return
		 */
		public SqlExpressionBuilder equal(Object value){
			return op(SqlOperator.EQUAL,value);
		}

		/**
		 * 不等于
		 * <p>请使用{@link this#ne(Object)}</p>
		 * @param value 值
		 * @return
		 *
		 */
		public SqlExpressionBuilder notEqual(Object value){
			return op(SqlOperator.NOT_EQUAL,value);
		}
		public SqlExpressionBuilder ne(Object value){
			return op(SqlOperator.NOT_EQUAL,value);
		}

		public SqlExpressionBuilder greaterThan(Object value){
			return op(SqlOperator.GREATER_THAN,value);
		}
		public SqlExpressionBuilder gt(Object value){
			return op(SqlOperator.GREATER_THAN,value);
		}

		public SqlExpressionBuilder greaterOrEqual(Object value){
			return op(SqlOperator.GREATER_OR_EQUAL,value);
		}
		public SqlExpressionBuilder ge(Object value){
			return op(SqlOperator.GREATER_OR_EQUAL,value);
		}

		public SqlExpressionBuilder lessThan(Object value){
			return op(SqlOperator.LESS_THAN,value);
		}
		public SqlExpressionBuilder lt(Object value){
			return op(SqlOperator.LESS_THAN,value);
		}

		public SqlExpressionBuilder lessOrEqual(Object value){
			return op(SqlOperator.LESS_OR_EQUAL,value);
		}
		public SqlExpressionBuilder le(Object value){
			return op(SqlOperator.LESS_OR_EQUAL,value);
		}

		public SqlExpressionBuilder between(Object value1, Object value2){
			return op(SqlOperator.BETWEEN,new Object[]{value1, value2});
		}

		final String quoteValue(String value){
			return "'"+value+"'";
		}

		public SqlExpressionBuilder startsWith(String value){
			return op(SqlOperator.LIKE, quoteValue(value+"%"));
		}
		public SqlExpressionBuilder endsWith(String value){
			return op(SqlOperator.LIKE, quoteValue("%"+value));
		}
		public SqlExpressionBuilder contains(String value){
			return op(SqlOperator.LIKE, quoteValue("%"+value+"%"));
		}

		boolean isNeedToQuote(){
			return currExp.getCol().getDataType().isQuotable();
		}
		public SqlExpressionBuilder in(String values){
			if(values.indexOf("'") == -1 && isNeedToQuote()){
				values = Arrays.stream(values.split(","))
						.map(this::quoteValue)
						.collect(Collectors.joining(","));
			}
			return op(SqlOperator.IN, "("+values+")");
		}
		public SqlExpressionBuilder in(String...values){
			if(isNeedToQuote() && values[0].indexOf("'") == -1){
				String list = Arrays.stream(values)
						.map(this::quoteValue)
						.collect(Collectors.joining(","));
				return op(SqlOperator.IN, "("+list+")");
			}
			return op(SqlOperator.IN, "("+String.join(",",values)+")");
		}
		public SqlExpressionBuilder notIn(String values){
			if(values.indexOf("'") == -1 && isNeedToQuote()){
				values = Arrays.stream(values.split(","))
						.map(this::quoteValue)
						.collect(Collectors.joining(","));
			}
			return op(SqlOperator.NOT_IN, "("+values+")");
		}
		public SqlExpressionBuilder notIn(String...values){
			if(isNeedToQuote() && values[0].indexOf("'") == -1){
				String list = Arrays.stream(values)
						.map(this::quoteValue)
						.collect(Collectors.joining(","));
				return op(SqlOperator.NOT_IN, "("+list+")");
			}
			return op(SqlOperator.NOT_IN, "("+String.join(",",values)+")");
		}


		public SqlExpressionBuilder isNull(){
			return op(SqlOperator.IS_NULL, "");
		}
		public SqlExpressionBuilder isNotNull(){
			return op(SqlOperator.IS_NOT_NULL, "");
		}
		public SqlExpressionBuilder and(String colName){
			MetaCol col = metaObj.getCol(colName);
			return and(col);
		}
		public SqlExpressionBuilder and(MetaCol col){
			currExp = new SqlExpression(col);
			resultExp.and(currExp);
			return this;
		}
		public SqlExpressionBuilder or(String colName){
			MetaCol col = metaObj.getCol(colName);
			return or(col);
		}
		public SqlExpressionBuilder or(MetaCol col){
			currExp = new SqlExpression(col);
			resultExp.or(currExp);
			return this;
		}
		public SqlExpression result(){
			return resultExp;
		}
		public SqlExpression build(){
			return resultExp;
		}
	}
	//endregion of builder
}
