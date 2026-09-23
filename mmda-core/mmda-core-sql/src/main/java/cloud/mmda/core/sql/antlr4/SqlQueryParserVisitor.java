// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlQueryParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlQueryParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#sqlQueryWithCte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlQueryWithCte(SqlQueryParser.SqlQueryWithCteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#sqlQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlQuery(SqlQueryParser.SqlQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(SqlQueryParser.SelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom(SqlQueryParser.FromContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(SqlQueryParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#groupBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupBy(SqlQueryParser.GroupByContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#orderBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderBy(SqlQueryParser.OrderByContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#limitOffset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitOffset(SqlQueryParser.LimitOffsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#tableSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSource(SqlQueryParser.TableSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceItemJoined(SqlQueryParser.TableSourceItemJoinedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceItem(SqlQueryParser.TableSourceItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#joinClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinClause(SqlQueryParser.JoinClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#criteria}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCriteria(SqlQueryParser.CriteriaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(SqlQueryParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#subQueryOrValuesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubQueryOrValuesClause(SqlQueryParser.SubQueryOrValuesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#valuesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesClause(SqlQueryParser.ValuesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SqlQueryParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#selectExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExprList(SqlQueryParser.SelectExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#selectExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpr(SqlQueryParser.SelectExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#selectAllStar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectAllStar(SqlQueryParser.SelectAllStarContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#selectColumn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectColumn(SqlQueryParser.SelectColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#fullColumnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnName(SqlQueryParser.FullColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#selectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpression(SqlQueryParser.SelectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#sortExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortExpression(SqlQueryParser.SortExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#schemaObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaObject(SqlQueryParser.SchemaObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#asTableAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsTableAlias(SqlQueryParser.AsTableAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#asColumnAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsColumnAlias(SqlQueryParser.AsColumnAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#columnAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAlias(SqlQueryParser.ColumnAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#columnAliasList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAliasList(SqlQueryParser.ColumnAliasListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SqlQueryParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#primitiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveExpression(SqlQueryParser.PrimitiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#caseWhenExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseWhenExpression(SqlQueryParser.CaseWhenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#unaryOpExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpression(SqlQueryParser.UnaryOpExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#bracketExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(SqlQueryParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(SqlQueryParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(SqlQueryParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#withCteClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithCteClause(SqlQueryParser.WithCteClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#commonTableExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonTableExpression(SqlQueryParser.CommonTableExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(SqlQueryParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryChecksumFunc(SqlQueryParser.BinaryChecksumFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CastFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastFunc(SqlQueryParser.CastFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Convert}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert(SqlQueryParser.ConvertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChecksumFunc(SqlQueryParser.ChecksumFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CoalesceFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesceFunc(SqlQueryParser.CoalesceFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentTimestampFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentTimestampFunc(SqlQueryParser.CurrentTimestampFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentDateFunc(SqlQueryParser.CurrentDateFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentUserFunc(SqlQueryParser.CurrentUserFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateAddFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateAddFunc(SqlQueryParser.DateAddFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateDiffFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateDiffFunc(SqlQueryParser.DateDiffFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateNameFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateNameFunc(SqlQueryParser.DateNameFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DatePartFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatePartFunc(SqlQueryParser.DatePartFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IentityFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIentityFunc(SqlQueryParser.IentityFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullIfFunc(SqlQueryParser.NullIfFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SystemUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemUserFunc(SqlQueryParser.SystemUserFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserFunc(SqlQueryParser.UserFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsNullFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullFunc(SqlQueryParser.IsNullFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlDataFunc(SqlQueryParser.XmlDataFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIIfFunc(SqlQueryParser.IIfFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#scalarFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFunc(SqlQueryParser.ScalarFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlDataTypeFunc(SqlQueryParser.XmlDataTypeFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlValueFunc(SqlQueryParser.XmlValueFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlValueCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlValueCall(SqlQueryParser.XmlValueCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlQueryFunc(SqlQueryParser.XmlQueryFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlQueryCall(SqlQueryParser.XmlQueryCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlExistFunc(SqlQueryParser.XmlExistFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlExistCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlExistCall(SqlQueryParser.XmlExistCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlModifyFunc(SqlQueryParser.XmlModifyFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlModifyCall(SqlQueryParser.XmlModifyCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#timeZone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeZone(SqlQueryParser.TimeZoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRankingWindowedFunc(SqlQueryParser.RankingWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateWindowedFunc(SqlQueryParser.AggregateWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyticWindowedFunc(SqlQueryParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#allOrDistinctExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllOrDistinctExpression(SqlQueryParser.AllOrDistinctExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#overClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverClause(SqlQueryParser.OverClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#timeUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeUnit(SqlQueryParser.TimeUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#intervalUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalUnit(SqlQueryParser.IntervalUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#intervalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalExpression(SqlQueryParser.IntervalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#intervalTemporal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalTemporal(SqlQueryParser.IntervalTemporalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(SqlQueryParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(SqlQueryParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSign(SqlQueryParser.SignContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(SqlQueryParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SqlQueryParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(SqlQueryParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#bitOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOperator(SqlQueryParser.BitOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlQueryParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(SqlQueryParser.AssignmentOperatorContext ctx);
}