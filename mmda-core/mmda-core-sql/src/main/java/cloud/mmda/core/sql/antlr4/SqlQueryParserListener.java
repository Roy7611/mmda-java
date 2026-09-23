// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlQueryParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlQueryParser}.
 */
public interface SqlQueryParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#sqlQueryWithCte}.
	 * @param ctx the parse tree
	 */
	void enterSqlQueryWithCte(SqlQueryParser.SqlQueryWithCteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#sqlQueryWithCte}.
	 * @param ctx the parse tree
	 */
	void exitSqlQueryWithCte(SqlQueryParser.SqlQueryWithCteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#sqlQuery}.
	 * @param ctx the parse tree
	 */
	void enterSqlQuery(SqlQueryParser.SqlQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#sqlQuery}.
	 * @param ctx the parse tree
	 */
	void exitSqlQuery(SqlQueryParser.SqlQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(SqlQueryParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(SqlQueryParser.SelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#from}.
	 * @param ctx the parse tree
	 */
	void enterFrom(SqlQueryParser.FromContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#from}.
	 * @param ctx the parse tree
	 */
	void exitFrom(SqlQueryParser.FromContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#where}.
	 * @param ctx the parse tree
	 */
	void enterWhere(SqlQueryParser.WhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#where}.
	 * @param ctx the parse tree
	 */
	void exitWhere(SqlQueryParser.WhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void enterGroupBy(SqlQueryParser.GroupByContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void exitGroupBy(SqlQueryParser.GroupByContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void enterOrderBy(SqlQueryParser.OrderByContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void exitOrderBy(SqlQueryParser.OrderByContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#limitOffset}.
	 * @param ctx the parse tree
	 */
	void enterLimitOffset(SqlQueryParser.LimitOffsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#limitOffset}.
	 * @param ctx the parse tree
	 */
	void exitLimitOffset(SqlQueryParser.LimitOffsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSource(SqlQueryParser.TableSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSource(SqlQueryParser.TableSourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceItemJoined(SqlQueryParser.TableSourceItemJoinedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceItemJoined(SqlQueryParser.TableSourceItemJoinedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceItem(SqlQueryParser.TableSourceItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceItem(SqlQueryParser.TableSourceItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void enterJoinClause(SqlQueryParser.JoinClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void exitJoinClause(SqlQueryParser.JoinClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#criteria}.
	 * @param ctx the parse tree
	 */
	void enterCriteria(SqlQueryParser.CriteriaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#criteria}.
	 * @param ctx the parse tree
	 */
	void exitCriteria(SqlQueryParser.CriteriaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(SqlQueryParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(SqlQueryParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#subQueryOrValuesClause}.
	 * @param ctx the parse tree
	 */
	void enterSubQueryOrValuesClause(SqlQueryParser.SubQueryOrValuesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#subQueryOrValuesClause}.
	 * @param ctx the parse tree
	 */
	void exitSubQueryOrValuesClause(SqlQueryParser.SubQueryOrValuesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#valuesClause}.
	 * @param ctx the parse tree
	 */
	void enterValuesClause(SqlQueryParser.ValuesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#valuesClause}.
	 * @param ctx the parse tree
	 */
	void exitValuesClause(SqlQueryParser.ValuesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(SqlQueryParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(SqlQueryParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#selectExprList}.
	 * @param ctx the parse tree
	 */
	void enterSelectExprList(SqlQueryParser.SelectExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#selectExprList}.
	 * @param ctx the parse tree
	 */
	void exitSelectExprList(SqlQueryParser.SelectExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#selectExpr}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpr(SqlQueryParser.SelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#selectExpr}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpr(SqlQueryParser.SelectExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#selectAllStar}.
	 * @param ctx the parse tree
	 */
	void enterSelectAllStar(SqlQueryParser.SelectAllStarContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#selectAllStar}.
	 * @param ctx the parse tree
	 */
	void exitSelectAllStar(SqlQueryParser.SelectAllStarContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#selectColumn}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumn(SqlQueryParser.SelectColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#selectColumn}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumn(SqlQueryParser.SelectColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(SqlQueryParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(SqlQueryParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpression(SqlQueryParser.SelectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpression(SqlQueryParser.SelectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#sortExpression}.
	 * @param ctx the parse tree
	 */
	void enterSortExpression(SqlQueryParser.SortExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#sortExpression}.
	 * @param ctx the parse tree
	 */
	void exitSortExpression(SqlQueryParser.SortExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#schemaObject}.
	 * @param ctx the parse tree
	 */
	void enterSchemaObject(SqlQueryParser.SchemaObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#schemaObject}.
	 * @param ctx the parse tree
	 */
	void exitSchemaObject(SqlQueryParser.SchemaObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#asTableAlias}.
	 * @param ctx the parse tree
	 */
	void enterAsTableAlias(SqlQueryParser.AsTableAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#asTableAlias}.
	 * @param ctx the parse tree
	 */
	void exitAsTableAlias(SqlQueryParser.AsTableAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#asColumnAlias}.
	 * @param ctx the parse tree
	 */
	void enterAsColumnAlias(SqlQueryParser.AsColumnAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#asColumnAlias}.
	 * @param ctx the parse tree
	 */
	void exitAsColumnAlias(SqlQueryParser.AsColumnAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#columnAlias}.
	 * @param ctx the parse tree
	 */
	void enterColumnAlias(SqlQueryParser.ColumnAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#columnAlias}.
	 * @param ctx the parse tree
	 */
	void exitColumnAlias(SqlQueryParser.ColumnAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#columnAliasList}.
	 * @param ctx the parse tree
	 */
	void enterColumnAliasList(SqlQueryParser.ColumnAliasListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#columnAliasList}.
	 * @param ctx the parse tree
	 */
	void exitColumnAliasList(SqlQueryParser.ColumnAliasListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SqlQueryParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SqlQueryParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#primitiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveExpression(SqlQueryParser.PrimitiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#primitiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveExpression(SqlQueryParser.PrimitiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#caseWhenExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaseWhenExpression(SqlQueryParser.CaseWhenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#caseWhenExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaseWhenExpression(SqlQueryParser.CaseWhenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#unaryOpExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOpExpression(SqlQueryParser.UnaryOpExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#unaryOpExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOpExpression(SqlQueryParser.UnaryOpExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(SqlQueryParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(SqlQueryParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(SqlQueryParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(SqlQueryParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(SqlQueryParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(SqlQueryParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#withCteClause}.
	 * @param ctx the parse tree
	 */
	void enterWithCteClause(SqlQueryParser.WithCteClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#withCteClause}.
	 * @param ctx the parse tree
	 */
	void exitWithCteClause(SqlQueryParser.WithCteClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#commonTableExpression}.
	 * @param ctx the parse tree
	 */
	void enterCommonTableExpression(SqlQueryParser.CommonTableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#commonTableExpression}.
	 * @param ctx the parse tree
	 */
	void exitCommonTableExpression(SqlQueryParser.CommonTableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SqlQueryParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SqlQueryParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterBinaryChecksumFunc(SqlQueryParser.BinaryChecksumFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitBinaryChecksumFunc(SqlQueryParser.BinaryChecksumFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CastFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCastFunc(SqlQueryParser.CastFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CastFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCastFunc(SqlQueryParser.CastFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Convert}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterConvert(SqlQueryParser.ConvertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Convert}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitConvert(SqlQueryParser.ConvertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterChecksumFunc(SqlQueryParser.ChecksumFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChecksumFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitChecksumFunc(SqlQueryParser.ChecksumFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CoalesceFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCoalesceFunc(SqlQueryParser.CoalesceFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CoalesceFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCoalesceFunc(SqlQueryParser.CoalesceFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentTimestampFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestampFunc(SqlQueryParser.CurrentTimestampFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentTimestampFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestampFunc(SqlQueryParser.CurrentTimestampFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentDateFunc(SqlQueryParser.CurrentDateFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentDateFunc(SqlQueryParser.CurrentDateFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUserFunc(SqlQueryParser.CurrentUserFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUserFunc(SqlQueryParser.CurrentUserFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateAddFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateAddFunc(SqlQueryParser.DateAddFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateAddFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateAddFunc(SqlQueryParser.DateAddFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateDiffFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateDiffFunc(SqlQueryParser.DateDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateDiffFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateDiffFunc(SqlQueryParser.DateDiffFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateNameFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateNameFunc(SqlQueryParser.DateNameFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateNameFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateNameFunc(SqlQueryParser.DateNameFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DatePartFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDatePartFunc(SqlQueryParser.DatePartFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DatePartFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDatePartFunc(SqlQueryParser.DatePartFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IentityFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIentityFunc(SqlQueryParser.IentityFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IentityFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIentityFunc(SqlQueryParser.IentityFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterNullIfFunc(SqlQueryParser.NullIfFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitNullIfFunc(SqlQueryParser.NullIfFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SystemUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterSystemUserFunc(SqlQueryParser.SystemUserFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SystemUserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitSystemUserFunc(SqlQueryParser.SystemUserFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterUserFunc(SqlQueryParser.UserFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitUserFunc(SqlQueryParser.UserFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNullFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIsNullFunc(SqlQueryParser.IsNullFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNullFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIsNullFunc(SqlQueryParser.IsNullFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlDataFunc(SqlQueryParser.XmlDataFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlDataFunc(SqlQueryParser.XmlDataFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIIfFunc(SqlQueryParser.IIfFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IIfFunc}
	 * labeled alternative in {@link SqlQueryParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIIfFunc(SqlQueryParser.IIfFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#scalarFunc}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunc(SqlQueryParser.ScalarFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#scalarFunc}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunc(SqlQueryParser.ScalarFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlDataTypeFunc(SqlQueryParser.XmlDataTypeFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlDataTypeFunc(SqlQueryParser.XmlDataTypeFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlValueFunc(SqlQueryParser.XmlValueFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlValueFunc(SqlQueryParser.XmlValueFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlValueCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlValueCall(SqlQueryParser.XmlValueCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlValueCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlValueCall(SqlQueryParser.XmlValueCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlQueryFunc(SqlQueryParser.XmlQueryFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlQueryFunc(SqlQueryParser.XmlQueryFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlQueryCall(SqlQueryParser.XmlQueryCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlQueryCall(SqlQueryParser.XmlQueryCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlExistFunc(SqlQueryParser.XmlExistFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlExistFunc(SqlQueryParser.XmlExistFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlExistCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlExistCall(SqlQueryParser.XmlExistCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlExistCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlExistCall(SqlQueryParser.XmlExistCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlModifyFunc(SqlQueryParser.XmlModifyFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlModifyFunc(SqlQueryParser.XmlModifyFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlModifyCall(SqlQueryParser.XmlModifyCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlModifyCall(SqlQueryParser.XmlModifyCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#timeZone}.
	 * @param ctx the parse tree
	 */
	void enterTimeZone(SqlQueryParser.TimeZoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#timeZone}.
	 * @param ctx the parse tree
	 */
	void exitTimeZone(SqlQueryParser.TimeZoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterRankingWindowedFunc(SqlQueryParser.RankingWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitRankingWindowedFunc(SqlQueryParser.RankingWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterAggregateWindowedFunc(SqlQueryParser.AggregateWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitAggregateWindowedFunc(SqlQueryParser.AggregateWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterAnalyticWindowedFunc(SqlQueryParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitAnalyticWindowedFunc(SqlQueryParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#allOrDistinctExpression}.
	 * @param ctx the parse tree
	 */
	void enterAllOrDistinctExpression(SqlQueryParser.AllOrDistinctExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#allOrDistinctExpression}.
	 * @param ctx the parse tree
	 */
	void exitAllOrDistinctExpression(SqlQueryParser.AllOrDistinctExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(SqlQueryParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(SqlQueryParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#timeUnit}.
	 * @param ctx the parse tree
	 */
	void enterTimeUnit(SqlQueryParser.TimeUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#timeUnit}.
	 * @param ctx the parse tree
	 */
	void exitTimeUnit(SqlQueryParser.TimeUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void enterIntervalUnit(SqlQueryParser.IntervalUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void exitIntervalUnit(SqlQueryParser.IntervalUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#intervalExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpression(SqlQueryParser.IntervalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#intervalExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpression(SqlQueryParser.IntervalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#intervalTemporal}.
	 * @param ctx the parse tree
	 */
	void enterIntervalTemporal(SqlQueryParser.IntervalTemporalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#intervalTemporal}.
	 * @param ctx the parse tree
	 */
	void exitIntervalTemporal(SqlQueryParser.IntervalTemporalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(SqlQueryParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(SqlQueryParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(SqlQueryParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(SqlQueryParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterSign(SqlQueryParser.SignContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitSign(SqlQueryParser.SignContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(SqlQueryParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(SqlQueryParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(SqlQueryParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(SqlQueryParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SqlQueryParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SqlQueryParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitOperator(SqlQueryParser.BitOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitOperator(SqlQueryParser.BitOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlQueryParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(SqlQueryParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlQueryParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(SqlQueryParser.AssignmentOperatorContext ctx);
}