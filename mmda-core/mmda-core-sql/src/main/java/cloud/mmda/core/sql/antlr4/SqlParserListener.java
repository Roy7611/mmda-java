// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlParserParser}.
 */
public interface SqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatements(SqlParserParser.SqlStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatements(SqlParserParser.SqlStatementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Dml}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDml(SqlParserParser.DmlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Dml}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDml(SqlParserParser.DmlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Dql}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDql(SqlParserParser.DqlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Dql}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDql(SqlParserParser.DqlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Ddl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdl(SqlParserParser.DdlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Ddl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdl(SqlParserParser.DdlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Dcl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDcl(SqlParserParser.DclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Dcl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDcl(SqlParserParser.DclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyStat}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStat(SqlParserParser.EmptyStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyStat}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStat(SqlParserParser.EmptyStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(SqlParserParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(SqlParserParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdlStatement(SqlParserParser.DdlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdlStatement(SqlParserParser.DdlStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Block}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SqlParserParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Block}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SqlParserParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclareVar}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVar(SqlParserParser.DeclareVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclareVar}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVar(SqlParserParser.DeclareVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Execute}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecute(SqlParserParser.ExecuteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Execute}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecute(SqlParserParser.ExecuteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CursorStat}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorStat(SqlParserParser.CursorStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CursorStat}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorStat(SqlParserParser.CursorStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Security}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterSecurity(SqlParserParser.SecurityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Security}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitSecurity(SqlParserParser.SecurityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetAssign}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAssign(SqlParserParser.SetAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetAssign}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAssign(SqlParserParser.SetAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Transaction}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransaction(SqlParserParser.TransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Transaction}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransaction(SqlParserParser.TransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Use}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterUse(SqlParserParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Use}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitUse(SqlParserParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Break}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreak(SqlParserParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Break}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreak(SqlParserParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Continue}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinue(SqlParserParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Continue}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinue(SqlParserParser.ContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code If}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterIf(SqlParserParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code If}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitIf(SqlParserParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturn(SqlParserParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturn(SqlParserParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrow(SqlParserParser.ThrowContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrow(SqlParserParser.ThrowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatch(SqlParserParser.TryCatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatch(SqlParserParser.TryCatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code While}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhile(SqlParserParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code While}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhile(SqlParserParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(SqlParserParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(SqlParserParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(SqlParserParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(SqlParserParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(SqlParserParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(SqlParserParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(SqlParserParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(SqlParserParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(SqlParserParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(SqlParserParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStatement(SqlParserParser.ThrowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStatement(SqlParserParser.ThrowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#throwErrorNumber}.
	 * @param ctx the parse tree
	 */
	void enterThrowErrorNumber(SqlParserParser.ThrowErrorNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#throwErrorNumber}.
	 * @param ctx the parse tree
	 */
	void exitThrowErrorNumber(SqlParserParser.ThrowErrorNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#throwMessage}.
	 * @param ctx the parse tree
	 */
	void enterThrowMessage(SqlParserParser.ThrowMessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#throwMessage}.
	 * @param ctx the parse tree
	 */
	void exitThrowMessage(SqlParserParser.ThrowMessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#throwState}.
	 * @param ctx the parse tree
	 */
	void enterThrowState(SqlParserParser.ThrowStateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#throwState}.
	 * @param ctx the parse tree
	 */
	void exitThrowState(SqlParserParser.ThrowStateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchStatement(SqlParserParser.TryCatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchStatement(SqlParserParser.TryCatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(SqlParserParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(SqlParserParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(SqlParserParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(SqlParserParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#objectTypeForGrant}.
	 * @param ctx the parse tree
	 */
	void enterObjectTypeForGrant(SqlParserParser.ObjectTypeForGrantContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#objectTypeForGrant}.
	 * @param ctx the parse tree
	 */
	void exitObjectTypeForGrant(SqlParserParser.ObjectTypeForGrantContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(SqlParserParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(SqlParserParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropDeault}.
	 * @param ctx the parse tree
	 */
	void enterDropDeault(SqlParserParser.DropDeaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropDeault}.
	 * @param ctx the parse tree
	 */
	void exitDropDeault(SqlParserParser.DropDeaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropPartitionFunction(SqlParserParser.DropPartitionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropPartitionFunction(SqlParserParser.DropPartitionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void enterDropPartitionScheme(SqlParserParser.DropPartitionSchemeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void exitDropPartitionScheme(SqlParserParser.DropPartitionSchemeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropDbRole}.
	 * @param ctx the parse tree
	 */
	void enterDropDbRole(SqlParserParser.DropDbRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropDbRole}.
	 * @param ctx the parse tree
	 */
	void exitDropDbRole(SqlParserParser.DropDbRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createDbRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateDbRole(SqlParserParser.CreateDbRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createDbRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateDbRole(SqlParserParser.CreateDbRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropSchema}.
	 * @param ctx the parse tree
	 */
	void enterDropSchema(SqlParserParser.DropSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropSchema}.
	 * @param ctx the parse tree
	 */
	void exitDropSchema(SqlParserParser.DropSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(SqlParserParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(SqlParserParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#disableTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDisableTrigger(SqlParserParser.DisableTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#disableTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDisableTrigger(SqlParserParser.DisableTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#enableTrigger}.
	 * @param ctx the parse tree
	 */
	void enterEnableTrigger(SqlParserParser.EnableTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#enableTrigger}.
	 * @param ctx the parse tree
	 */
	void exitEnableTrigger(SqlParserParser.EnableTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(SqlParserParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(SqlParserParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterPartitionFunction(SqlParserParser.AlterPartitionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterPartitionFunction(SqlParserParser.AlterPartitionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void enterAlterPartitionScheme(SqlParserParser.AlterPartitionSchemeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void exitAlterPartitionScheme(SqlParserParser.AlterPartitionSchemeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterSchema}.
	 * @param ctx the parse tree
	 */
	void enterAlterSchema(SqlParserParser.AlterSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterSchema}.
	 * @param ctx the parse tree
	 */
	void exitAlterSchema(SqlParserParser.AlterSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createSchema}.
	 * @param ctx the parse tree
	 */
	void enterCreateSchema(SqlParserParser.CreateSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createSchema}.
	 * @param ctx the parse tree
	 */
	void exitCreateSchema(SqlParserParser.CreateSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreatePartitionFunction(SqlParserParser.CreatePartitionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createPartitionFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreatePartitionFunction(SqlParserParser.CreatePartitionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void enterCreatePartitionScheme(SqlParserParser.CreatePartitionSchemeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createPartitionScheme}.
	 * @param ctx the parse tree
	 */
	void exitCreatePartitionScheme(SqlParserParser.CreatePartitionSchemeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void enterMergeStatement(SqlParserParser.MergeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void exitMergeStatement(SqlParserParser.MergeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#whenMatches}.
	 * @param ctx the parse tree
	 */
	void enterWhenMatches(SqlParserParser.WhenMatchesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#whenMatches}.
	 * @param ctx the parse tree
	 */
	void exitWhenMatches(SqlParserParser.WhenMatchesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#mergeMatched}.
	 * @param ctx the parse tree
	 */
	void enterMergeMatched(SqlParserParser.MergeMatchedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#mergeMatched}.
	 * @param ctx the parse tree
	 */
	void exitMergeMatched(SqlParserParser.MergeMatchedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#mergeNotMatched}.
	 * @param ctx the parse tree
	 */
	void enterMergeNotMatched(SqlParserParser.MergeNotMatchedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#mergeNotMatched}.
	 * @param ctx the parse tree
	 */
	void exitMergeNotMatched(SqlParserParser.MergeNotMatchedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(SqlParserParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(SqlParserParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#deleteStatement_from}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement_from(SqlParserParser.DeleteStatement_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#deleteStatement_from}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement_from(SqlParserParser.DeleteStatement_fromContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(SqlParserParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(SqlParserParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#insertStatementValues}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatementValues(SqlParserParser.InsertStatementValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#insertStatementValues}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatementValues(SqlParserParser.InsertStatementValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#withCteSelectStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithCteSelectStatement(SqlParserParser.WithCteSelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#withCteSelectStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithCteSelectStatement(SqlParserParser.WithCteSelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(SqlParserParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(SqlParserParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(SqlParserParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(SqlParserParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#outputClause}.
	 * @param ctx the parse tree
	 */
	void enterOutputClause(SqlParserParser.OutputClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#outputClause}.
	 * @param ctx the parse tree
	 */
	void exitOutputClause(SqlParserParser.OutputClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#outputDmlListElem}.
	 * @param ctx the parse tree
	 */
	void enterOutputDmlListElem(SqlParserParser.OutputDmlListElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#outputDmlListElem}.
	 * @param ctx the parse tree
	 */
	void exitOutputDmlListElem(SqlParserParser.OutputDmlListElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(SqlParserParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(SqlParserParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(SqlParserParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(SqlParserParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterIndex}.
	 * @param ctx the parse tree
	 */
	void enterAlterIndex(SqlParserParser.AlterIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterIndex}.
	 * @param ctx the parse tree
	 */
	void exitAlterIndex(SqlParserParser.AlterIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#rebuildPartition}.
	 * @param ctx the parse tree
	 */
	void enterRebuildPartition(SqlParserParser.RebuildPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#rebuildPartition}.
	 * @param ctx the parse tree
	 */
	void exitRebuildPartition(SqlParserParser.RebuildPartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createOrAlterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateOrAlterProcedure(SqlParserParser.CreateOrAlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createOrAlterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateOrAlterProcedure(SqlParserParser.CreateOrAlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createOrAlterTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateOrAlterTrigger(SqlParserParser.CreateOrAlterTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createOrAlterTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateOrAlterTrigger(SqlParserParser.CreateOrAlterTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#triggerOperation}.
	 * @param ctx the parse tree
	 */
	void enterTriggerOperation(SqlParserParser.TriggerOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#triggerOperation}.
	 * @param ctx the parse tree
	 */
	void exitTriggerOperation(SqlParserParser.TriggerOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createOrAlterFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateOrAlterFunction(SqlParserParser.CreateOrAlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createOrAlterFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateOrAlterFunction(SqlParserParser.CreateOrAlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#funcBodyReturnsSelect}.
	 * @param ctx the parse tree
	 */
	void enterFuncBodyReturnsSelect(SqlParserParser.FuncBodyReturnsSelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#funcBodyReturnsSelect}.
	 * @param ctx the parse tree
	 */
	void exitFuncBodyReturnsSelect(SqlParserParser.FuncBodyReturnsSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#funcBodyReturnsTable}.
	 * @param ctx the parse tree
	 */
	void enterFuncBodyReturnsTable(SqlParserParser.FuncBodyReturnsTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#funcBodyReturnsTable}.
	 * @param ctx the parse tree
	 */
	void exitFuncBodyReturnsTable(SqlParserParser.FuncBodyReturnsTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#funcBodyReturnsScalar}.
	 * @param ctx the parse tree
	 */
	void enterFuncBodyReturnsScalar(SqlParserParser.FuncBodyReturnsScalarContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#funcBodyReturnsScalar}.
	 * @param ctx the parse tree
	 */
	void exitFuncBodyReturnsScalar(SqlParserParser.FuncBodyReturnsScalarContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#procedureParam}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParam(SqlParserParser.ProcedureParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#procedureParam}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParam(SqlParserParser.ProcedureParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#procedureOption}.
	 * @param ctx the parse tree
	 */
	void enterProcedureOption(SqlParserParser.ProcedureOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#procedureOption}.
	 * @param ctx the parse tree
	 */
	void exitProcedureOption(SqlParserParser.ProcedureOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#functionOption}.
	 * @param ctx the parse tree
	 */
	void enterFunctionOption(SqlParserParser.FunctionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#functionOption}.
	 * @param ctx the parse tree
	 */
	void exitFunctionOption(SqlParserParser.FunctionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(SqlParserParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(SqlParserParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableIndices}.
	 * @param ctx the parse tree
	 */
	void enterTableIndices(SqlParserParser.TableIndicesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableIndices}.
	 * @param ctx the parse tree
	 */
	void exitTableIndices(SqlParserParser.TableIndicesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableOptions}.
	 * @param ctx the parse tree
	 */
	void enterTableOptions(SqlParserParser.TableOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableOptions}.
	 * @param ctx the parse tree
	 */
	void exitTableOptions(SqlParserParser.TableOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createOrAlterView}.
	 * @param ctx the parse tree
	 */
	void enterCreateOrAlterView(SqlParserParser.CreateOrAlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createOrAlterView}.
	 * @param ctx the parse tree
	 */
	void exitCreateOrAlterView(SqlParserParser.CreateOrAlterViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(SqlParserParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(SqlParserParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterDatabase(SqlParserParser.AlterDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterDatabase(SqlParserParser.AlterDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#host}.
	 * @param ctx the parse tree
	 */
	void enterHost(SqlParserParser.HostContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#host}.
	 * @param ctx the parse tree
	 */
	void exitHost(SqlParserParser.HostContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(SqlParserParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(SqlParserParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropRelationalOrXmlOrSpatialIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropRelationalOrXmlOrSpatialIndex(SqlParserParser.DropRelationalOrXmlOrSpatialIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropRelationalOrXmlOrSpatialIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropRelationalOrXmlOrSpatialIndex(SqlParserParser.DropRelationalOrXmlOrSpatialIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropBackwardCompatibleIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropBackwardCompatibleIndex(SqlParserParser.DropBackwardCompatibleIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropBackwardCompatibleIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropBackwardCompatibleIndex(SqlParserParser.DropBackwardCompatibleIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(SqlParserParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(SqlParserParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(SqlParserParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(SqlParserParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(SqlParserParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(SqlParserParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#drop_statistics}.
	 * @param ctx the parse tree
	 */
	void enterDrop_statistics(SqlParserParser.Drop_statisticsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#drop_statistics}.
	 * @param ctx the parse tree
	 */
	void exitDrop_statistics(SqlParserParser.Drop_statisticsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(SqlParserParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(SqlParserParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(SqlParserParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(SqlParserParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#createType}.
	 * @param ctx the parse tree
	 */
	void enterCreateType(SqlParserParser.CreateTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#createType}.
	 * @param ctx the parse tree
	 */
	void exitCreateType(SqlParserParser.CreateTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dropType}.
	 * @param ctx the parse tree
	 */
	void enterDropType(SqlParserParser.DropTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dropType}.
	 * @param ctx the parse tree
	 */
	void exitDropType(SqlParserParser.DropTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#declareStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclareStatement(SqlParserParser.DeclareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#declareStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclareStatement(SqlParserParser.DeclareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorStatement(SqlParserParser.CursorStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorStatement(SqlParserParser.CursorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatement(SqlParserParser.ExecuteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatement(SqlParserParser.ExecuteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeBody}.
	 * @param ctx the parse tree
	 */
	void enterExecuteBody(SqlParserParser.ExecuteBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeBody}.
	 * @param ctx the parse tree
	 */
	void exitExecuteBody(SqlParserParser.ExecuteBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeStatementArg}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatementArg(SqlParserParser.ExecuteStatementArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeStatementArg}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatementArg(SqlParserParser.ExecuteStatementArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeStatementArgNamed}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatementArgNamed(SqlParserParser.ExecuteStatementArgNamedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeStatementArgNamed}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatementArgNamed(SqlParserParser.ExecuteStatementArgNamedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeStatementArgUnnamed}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatementArgUnnamed(SqlParserParser.ExecuteStatementArgUnnamedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeStatementArgUnnamed}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatementArgUnnamed(SqlParserParser.ExecuteStatementArgUnnamedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeParameter}.
	 * @param ctx the parse tree
	 */
	void enterExecuteParameter(SqlParserParser.ExecuteParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeParameter}.
	 * @param ctx the parse tree
	 */
	void exitExecuteParameter(SqlParserParser.ExecuteParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeVarString}.
	 * @param ctx the parse tree
	 */
	void enterExecuteVarString(SqlParserParser.ExecuteVarStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeVarString}.
	 * @param ctx the parse tree
	 */
	void exitExecuteVarString(SqlParserParser.ExecuteVarStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#securityStatement}.
	 * @param ctx the parse tree
	 */
	void enterSecurityStatement(SqlParserParser.SecurityStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#securityStatement}.
	 * @param ctx the parse tree
	 */
	void exitSecurityStatement(SqlParserParser.SecurityStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#principalId}.
	 * @param ctx the parse tree
	 */
	void enterPrincipalId(SqlParserParser.PrincipalIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#principalId}.
	 * @param ctx the parse tree
	 */
	void exitPrincipalId(SqlParserParser.PrincipalIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#grantPermission}.
	 * @param ctx the parse tree
	 */
	void enterGrantPermission(SqlParserParser.GrantPermissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#grantPermission}.
	 * @param ctx the parse tree
	 */
	void exitGrantPermission(SqlParserParser.GrantPermissionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetStatement(SqlParserParser.SetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetStatement(SqlParserParser.SetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransactionStatement(SqlParserParser.TransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransactionStatement(SqlParserParser.TransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void enterUseStatement(SqlParserParser.UseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void exitUseStatement(SqlParserParser.UseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#executeClause}.
	 * @param ctx the parse tree
	 */
	void enterExecuteClause(SqlParserParser.ExecuteClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#executeClause}.
	 * @param ctx the parse tree
	 */
	void exitExecuteClause(SqlParserParser.ExecuteClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#declareLocal}.
	 * @param ctx the parse tree
	 */
	void enterDeclareLocal(SqlParserParser.DeclareLocalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#declareLocal}.
	 * @param ctx the parse tree
	 */
	void exitDeclareLocal(SqlParserParser.DeclareLocalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableTypeDef}.
	 * @param ctx the parse tree
	 */
	void enterTableTypeDef(SqlParserParser.TableTypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableTypeDef}.
	 * @param ctx the parse tree
	 */
	void exitTableTypeDef(SqlParserParser.TableTypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableTypeIndices}.
	 * @param ctx the parse tree
	 */
	void enterTableTypeIndices(SqlParserParser.TableTypeIndicesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableTypeIndices}.
	 * @param ctx the parse tree
	 */
	void exitTableTypeIndices(SqlParserParser.TableTypeIndicesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnDefTableConstraints}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefTableConstraints(SqlParserParser.ColumnDefTableConstraintsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnDefTableConstraints}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefTableConstraints(SqlParserParser.ColumnDefTableConstraintsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnDefTableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefTableConstraint(SqlParserParser.ColumnDefTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnDefTableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefTableConstraint(SqlParserParser.ColumnDefTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void enterColumnDef(SqlParserParser.ColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void exitColumnDef(SqlParserParser.ColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#materializedColumnDef}.
	 * @param ctx the parse tree
	 */
	void enterMaterializedColumnDef(SqlParserParser.MaterializedColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#materializedColumnDef}.
	 * @param ctx the parse tree
	 */
	void exitMaterializedColumnDef(SqlParserParser.MaterializedColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterColumnConstraint(SqlParserParser.ColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitColumnConstraint(SqlParserParser.ColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterTableConstraint(SqlParserParser.TableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitTableConstraint(SqlParserParser.TableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#foreignKeyOnDelete}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyOnDelete(SqlParserParser.ForeignKeyOnDeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#foreignKeyOnDelete}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyOnDelete(SqlParserParser.ForeignKeyOnDeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#foreignKeyOnUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyOnUpdate(SqlParserParser.ForeignKeyOnUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#foreignKeyOnUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyOnUpdate(SqlParserParser.ForeignKeyOnUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#indexOptions}.
	 * @param ctx the parse tree
	 */
	void enterIndexOptions(SqlParserParser.IndexOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#indexOptions}.
	 * @param ctx the parse tree
	 */
	void exitIndexOptions(SqlParserParser.IndexOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(SqlParserParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(SqlParserParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCursor(SqlParserParser.DeclareCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCursor(SqlParserParser.DeclareCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#declareSetCursorCommon}.
	 * @param ctx the parse tree
	 */
	void enterDeclareSetCursorCommon(SqlParserParser.DeclareSetCursorCommonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#declareSetCursorCommon}.
	 * @param ctx the parse tree
	 */
	void exitDeclareSetCursorCommon(SqlParserParser.DeclareSetCursorCommonContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#declareSetCursorCommonPartial}.
	 * @param ctx the parse tree
	 */
	void enterDeclareSetCursorCommonPartial(SqlParserParser.DeclareSetCursorCommonPartialContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#declareSetCursorCommonPartial}.
	 * @param ctx the parse tree
	 */
	void exitDeclareSetCursorCommonPartial(SqlParserParser.DeclareSetCursorCommonPartialContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#fetchFromCursor}.
	 * @param ctx the parse tree
	 */
	void enterFetchFromCursor(SqlParserParser.FetchFromCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#fetchFromCursor}.
	 * @param ctx the parse tree
	 */
	void exitFetchFromCursor(SqlParserParser.FetchFromCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#setSpecialOption}.
	 * @param ctx the parse tree
	 */
	void enterSetSpecialOption(SqlParserParser.SetSpecialOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#setSpecialOption}.
	 * @param ctx the parse tree
	 */
	void exitSetSpecialOption(SqlParserParser.SetSpecialOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#specialOptionList}.
	 * @param ctx the parse tree
	 */
	void enterSpecialOptionList(SqlParserParser.SpecialOptionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#specialOptionList}.
	 * @param ctx the parse tree
	 */
	void exitSpecialOptionList(SqlParserParser.SpecialOptionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#constantLocalId}.
	 * @param ctx the parse tree
	 */
	void enterConstantLocalId(SqlParserParser.ConstantLocalIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#constantLocalId}.
	 * @param ctx the parse tree
	 */
	void exitConstantLocalId(SqlParserParser.ConstantLocalIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#timeZone}.
	 * @param ctx the parse tree
	 */
	void enterTimeZone(SqlParserParser.TimeZoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#timeZone}.
	 * @param ctx the parse tree
	 */
	void exitTimeZone(SqlParserParser.TimeZoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#primitiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveExpression(SqlParserParser.PrimitiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#primitiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveExpression(SqlParserParser.PrimitiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaseExpression(SqlParserParser.CaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaseExpression(SqlParserParser.CaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperatorExpression(SqlParserParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperatorExpression(SqlParserParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(SqlParserParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(SqlParserParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(SqlParserParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(SqlParserParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(SqlParserParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(SqlParserParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#withExpression}.
	 * @param ctx the parse tree
	 */
	void enterWithExpression(SqlParserParser.WithExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#withExpression}.
	 * @param ctx the parse tree
	 */
	void exitWithExpression(SqlParserParser.WithExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#commonTableExpression}.
	 * @param ctx the parse tree
	 */
	void enterCommonTableExpression(SqlParserParser.CommonTableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#commonTableExpression}.
	 * @param ctx the parse tree
	 */
	void exitCommonTableExpression(SqlParserParser.CommonTableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void enterUpdateAssignment(SqlParserParser.UpdateAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void exitUpdateAssignment(SqlParserParser.UpdateAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#updateAssignmentMerge}.
	 * @param ctx the parse tree
	 */
	void enterUpdateAssignmentMerge(SqlParserParser.UpdateAssignmentMergeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#updateAssignmentMerge}.
	 * @param ctx the parse tree
	 */
	void exitUpdateAssignmentMerge(SqlParserParser.UpdateAssignmentMergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#searchCondition}.
	 * @param ctx the parse tree
	 */
	void enterSearchCondition(SqlParserParser.SearchConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#searchCondition}.
	 * @param ctx the parse tree
	 */
	void exitSearchCondition(SqlParserParser.SearchConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(SqlParserParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(SqlParserParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectQuery}.
	 * @param ctx the parse tree
	 */
	void enterSelectQuery(SqlParserParser.SelectQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectQuery}.
	 * @param ctx the parse tree
	 */
	void exitSelectQuery(SqlParserParser.SelectQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#unionSelect}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelect(SqlParserParser.UnionSelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#unionSelect}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelect(SqlParserParser.UnionSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(SqlParserParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(SqlParserParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(SqlParserParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(SqlParserParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#offsetClause}.
	 * @param ctx the parse tree
	 */
	void enterOffsetClause(SqlParserParser.OffsetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#offsetClause}.
	 * @param ctx the parse tree
	 */
	void exitOffsetClause(SqlParserParser.OffsetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(SqlParserParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(SqlParserParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectList}.
	 * @param ctx the parse tree
	 */
	void enterSelectList(SqlParserParser.SelectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectList}.
	 * @param ctx the parse tree
	 */
	void exitSelectList(SqlParserParser.SelectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#udtMethodArgs}.
	 * @param ctx the parse tree
	 */
	void enterUdtMethodArgs(SqlParserParser.UdtMethodArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#udtMethodArgs}.
	 * @param ctx the parse tree
	 */
	void exitUdtMethodArgs(SqlParserParser.UdtMethodArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#allColumns}.
	 * @param ctx the parse tree
	 */
	void enterAllColumns(SqlParserParser.AllColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#allColumns}.
	 * @param ctx the parse tree
	 */
	void exitAllColumns(SqlParserParser.AllColumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectColumnElem}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnElem(SqlParserParser.SelectColumnElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectColumnElem}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnElem(SqlParserParser.SelectColumnElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectUdtElem}.
	 * @param ctx the parse tree
	 */
	void enterSelectUdtElem(SqlParserParser.SelectUdtElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectUdtElem}.
	 * @param ctx the parse tree
	 */
	void exitSelectUdtElem(SqlParserParser.SelectUdtElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#expressionElem}.
	 * @param ctx the parse tree
	 */
	void enterExpressionElem(SqlParserParser.ExpressionElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#expressionElem}.
	 * @param ctx the parse tree
	 */
	void exitExpressionElem(SqlParserParser.ExpressionElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#selectListElem}.
	 * @param ctx the parse tree
	 */
	void enterSelectListElem(SqlParserParser.SelectListElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#selectListElem}.
	 * @param ctx the parse tree
	 */
	void exitSelectListElem(SqlParserParser.SelectListElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void enterTableSources(SqlParserParser.TableSourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void exitTableSources(SqlParserParser.TableSourcesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSource(SqlParserParser.TableSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSource(SqlParserParser.TableSourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceItemJoined(SqlParserParser.TableSourceItemJoinedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceItemJoined(SqlParserParser.TableSourceItemJoinedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceItem(SqlParserParser.TableSourceItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceItem(SqlParserParser.TableSourceItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableSourceItemJoinPart}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceItemJoinPart(SqlParserParser.TableSourceItemJoinPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableSourceItemJoinPart}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceItemJoinPart(SqlParserParser.TableSourceItemJoinPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#joinOnExpression}.
	 * @param ctx the parse tree
	 */
	void enterJoinOnExpression(SqlParserParser.JoinOnExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#joinOnExpression}.
	 * @param ctx the parse tree
	 */
	void exitJoinOnExpression(SqlParserParser.JoinOnExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#crossJoinExpression}.
	 * @param ctx the parse tree
	 */
	void enterCrossJoinExpression(SqlParserParser.CrossJoinExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#crossJoinExpression}.
	 * @param ctx the parse tree
	 */
	void exitCrossJoinExpression(SqlParserParser.CrossJoinExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#derivedTable}.
	 * @param ctx the parse tree
	 */
	void enterDerivedTable(SqlParserParser.DerivedTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#derivedTable}.
	 * @param ctx the parse tree
	 */
	void exitDerivedTable(SqlParserParser.DerivedTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SqlParserParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SqlParserParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#partitionFunc}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunc(SqlParserParser.PartitionFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#partitionFunc}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunc(SqlParserParser.PartitionFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryChecksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterBinaryChecksum(SqlParserParser.BinaryChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryChecksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitBinaryChecksum(SqlParserParser.BinaryChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Cast}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCast(SqlParserParser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Cast}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCast(SqlParserParser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CONVERT}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCONVERT(SqlParserParser.CONVERTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CONVERT}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCONVERT(SqlParserParser.CONVERTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Checksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterChecksum(SqlParserParser.ChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Checksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitChecksum(SqlParserParser.ChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Coalesce}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCoalesce(SqlParserParser.CoalesceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Coalesce}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCoalesce(SqlParserParser.CoalesceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentTimestamp}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestamp(SqlParserParser.CurrentTimestampContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentTimestamp}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestamp(SqlParserParser.CurrentTimestampContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentDate}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentDate(SqlParserParser.CurrentDateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentDate}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentDate(SqlParserParser.CurrentDateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUser(SqlParserParser.CurrentUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUser(SqlParserParser.CurrentUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateAdd}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateAdd(SqlParserParser.DateAddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateAdd}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateAdd(SqlParserParser.DateAddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateDiff}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateDiff(SqlParserParser.DateDiffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateDiff}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateDiff(SqlParserParser.DateDiffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateName}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDateName(SqlParserParser.DateNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateName}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDateName(SqlParserParser.DateNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DatePart}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterDatePart(SqlParserParser.DatePartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DatePart}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitDatePart(SqlParserParser.DatePartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIDENTITY(SqlParserParser.IDENTITYContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIDENTITY(SqlParserParser.IDENTITYContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterNullIf(SqlParserParser.NullIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitNullIf(SqlParserParser.NullIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SystemUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterSystemUser(SqlParserParser.SystemUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SystemUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitSystemUser(SqlParserParser.SystemUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code User}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterUser(SqlParserParser.UserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code User}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitUser(SqlParserParser.UserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNull}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIsNull(SqlParserParser.IsNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNull}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIsNull(SqlParserParser.IsNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlDataFunc(SqlParserParser.XmlDataFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlDataFunc(SqlParserParser.XmlDataFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void enterIIf(SqlParserParser.IIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 */
	void exitIIf(SqlParserParser.IIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlDataTypeFunc(SqlParserParser.XmlDataTypeFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlDataTypeFunc(SqlParserParser.XmlDataTypeFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlValueFunc(SqlParserParser.XmlValueFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlValueFunc(SqlParserParser.XmlValueFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlValueCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlValueCall(SqlParserParser.XmlValueCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlValueCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlValueCall(SqlParserParser.XmlValueCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlQueryFunc(SqlParserParser.XmlQueryFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlQueryFunc(SqlParserParser.XmlQueryFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlQueryCall(SqlParserParser.XmlQueryCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlQueryCall(SqlParserParser.XmlQueryCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlExistFunc(SqlParserParser.XmlExistFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlExistFunc(SqlParserParser.XmlExistFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlExistCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlExistCall(SqlParserParser.XmlExistCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlExistCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlExistCall(SqlParserParser.XmlExistCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 */
	void enterXmlModifyFunc(SqlParserParser.XmlModifyFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 */
	void exitXmlModifyFunc(SqlParserParser.XmlModifyFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 */
	void enterXmlModifyCall(SqlParserParser.XmlModifyCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 */
	void exitXmlModifyCall(SqlParserParser.XmlModifyCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#switchSection}.
	 * @param ctx the parse tree
	 */
	void enterSwitchSection(SqlParserParser.SwitchSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#switchSection}.
	 * @param ctx the parse tree
	 */
	void exitSwitchSection(SqlParserParser.SwitchSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#switchSearchConditionSection}.
	 * @param ctx the parse tree
	 */
	void enterSwitchSearchConditionSection(SqlParserParser.SwitchSearchConditionSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#switchSearchConditionSection}.
	 * @param ctx the parse tree
	 */
	void exitSwitchSearchConditionSection(SqlParserParser.SwitchSearchConditionSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#asColumnAlias}.
	 * @param ctx the parse tree
	 */
	void enterAsColumnAlias(SqlParserParser.AsColumnAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#asColumnAlias}.
	 * @param ctx the parse tree
	 */
	void exitAsColumnAlias(SqlParserParser.AsColumnAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#asTableAlias}.
	 * @param ctx the parse tree
	 */
	void enterAsTableAlias(SqlParserParser.AsTableAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#asTableAlias}.
	 * @param ctx the parse tree
	 */
	void exitAsTableAlias(SqlParserParser.AsTableAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnAliasList}.
	 * @param ctx the parse tree
	 */
	void enterColumnAliasList(SqlParserParser.ColumnAliasListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnAliasList}.
	 * @param ctx the parse tree
	 */
	void exitColumnAliasList(SqlParserParser.ColumnAliasListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnAlias}.
	 * @param ctx the parse tree
	 */
	void enterColumnAlias(SqlParserParser.ColumnAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnAlias}.
	 * @param ctx the parse tree
	 */
	void exitColumnAlias(SqlParserParser.ColumnAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableValuesCtor}.
	 * @param ctx the parse tree
	 */
	void enterTableValuesCtor(SqlParserParser.TableValuesCtorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableValuesCtor}.
	 * @param ctx the parse tree
	 */
	void exitTableValuesCtor(SqlParserParser.TableValuesCtorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(SqlParserParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(SqlParserParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterRankingWindowedFunc(SqlParserParser.RankingWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitRankingWindowedFunc(SqlParserParser.RankingWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterAggregateWindowedFunc(SqlParserParser.AggregateWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitAggregateWindowedFunc(SqlParserParser.AggregateWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void enterAnalyticWindowedFunc(SqlParserParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 */
	void exitAnalyticWindowedFunc(SqlParserParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#allDistinctExpression}.
	 * @param ctx the parse tree
	 */
	void enterAllDistinctExpression(SqlParserParser.AllDistinctExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#allDistinctExpression}.
	 * @param ctx the parse tree
	 */
	void exitAllDistinctExpression(SqlParserParser.AllDistinctExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(SqlParserParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(SqlParserParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#fullTableName}.
	 * @param ctx the parse tree
	 */
	void enterFullTableName(SqlParserParser.FullTableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#fullTableName}.
	 * @param ctx the parse tree
	 */
	void exitFullTableName(SqlParserParser.FullTableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#schemaDotObj}.
	 * @param ctx the parse tree
	 */
	void enterSchemaDotObj(SqlParserParser.SchemaDotObjContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#schemaDotObj}.
	 * @param ctx the parse tree
	 */
	void exitSchemaDotObj(SqlParserParser.SchemaDotObjContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#schemaDotProc}.
	 * @param ctx the parse tree
	 */
	void enterSchemaDotProc(SqlParserParser.SchemaDotProcContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#schemaDotProc}.
	 * @param ctx the parse tree
	 */
	void exitSchemaDotProc(SqlParserParser.SchemaDotProcContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dbSchemaDotProc}.
	 * @param ctx the parse tree
	 */
	void enterDbSchemaDotProc(SqlParserParser.DbSchemaDotProcContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dbSchemaDotProc}.
	 * @param ctx the parse tree
	 */
	void exitDbSchemaDotProc(SqlParserParser.DbSchemaDotProcContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#ddlObject}.
	 * @param ctx the parse tree
	 */
	void enterDdlObject(SqlParserParser.DdlObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#ddlObject}.
	 * @param ctx the parse tree
	 */
	void exitDdlObject(SqlParserParser.DdlObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(SqlParserParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(SqlParserParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnListWithOrder}.
	 * @param ctx the parse tree
	 */
	void enterColumnListWithOrder(SqlParserParser.ColumnListWithOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnListWithOrder}.
	 * @param ctx the parse tree
	 */
	void exitColumnListWithOrder(SqlParserParser.ColumnListWithOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#insertColumnList}.
	 * @param ctx the parse tree
	 */
	void enterInsertColumnList(SqlParserParser.InsertColumnListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#insertColumnList}.
	 * @param ctx the parse tree
	 */
	void exitInsertColumnList(SqlParserParser.InsertColumnListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#insertColumnId}.
	 * @param ctx the parse tree
	 */
	void enterInsertColumnId(SqlParserParser.InsertColumnIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#insertColumnId}.
	 * @param ctx the parse tree
	 */
	void exitInsertColumnId(SqlParserParser.InsertColumnIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#columnNameList}.
	 * @param ctx the parse tree
	 */
	void enterColumnNameList(SqlParserParser.ColumnNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#columnNameList}.
	 * @param ctx the parse tree
	 */
	void exitColumnNameList(SqlParserParser.ColumnNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#cursorName}.
	 * @param ctx the parse tree
	 */
	void enterCursorName(SqlParserParser.CursorNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#cursorName}.
	 * @param ctx the parse tree
	 */
	void exitCursorName(SqlParserParser.CursorNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#onOff}.
	 * @param ctx the parse tree
	 */
	void enterOnOff(SqlParserParser.OnOffContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#onOff}.
	 * @param ctx the parse tree
	 */
	void exitOnOff(SqlParserParser.OnOffContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#nullOrNot}.
	 * @param ctx the parse tree
	 */
	void enterNullOrNot(SqlParserParser.NullOrNotContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#nullOrNot}.
	 * @param ctx the parse tree
	 */
	void exitNullOrNot(SqlParserParser.NullOrNotContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#nullOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterNullOrDefault(SqlParserParser.NullOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#nullOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitNullOrDefault(SqlParserParser.NullOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#scalarFuncName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFuncName(SqlParserParser.ScalarFuncNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#scalarFuncName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFuncName(SqlParserParser.ScalarFuncNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(SqlParserParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(SqlParserParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(SqlParserParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(SqlParserParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(SqlParserParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(SqlParserParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Positive}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterPositive(SqlParserParser.PositiveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Positive}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitPositive(SqlParserParser.PositiveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negative}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterNegative(SqlParserParser.NegativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negative}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitNegative(SqlParserParser.NegativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(SqlParserParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(SqlParserParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(SqlParserParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(SqlParserParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleId(SqlParserParser.SimpleIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleId(SqlParserParser.SimpleIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SqlParserParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SqlParserParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(SqlParserParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(SqlParserParser.AssignmentOperatorContext ctx);
}