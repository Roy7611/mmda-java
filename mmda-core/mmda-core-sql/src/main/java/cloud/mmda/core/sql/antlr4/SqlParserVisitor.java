// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#sqlStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlStatements(SqlParserParser.SqlStatementsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dml}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml(SqlParserParser.DmlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dql}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDql(SqlParserParser.DqlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ddl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdl(SqlParserParser.DdlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dcl}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDcl(SqlParserParser.DclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EmptyStat}
	 * labeled alternative in {@link SqlParserParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStat(SqlParserParser.EmptyStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDmlStatement(SqlParserParser.DmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#ddlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdlStatement(SqlParserParser.DdlStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Block}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SqlParserParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclareVar}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareVar(SqlParserParser.DeclareVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Execute}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute(SqlParserParser.ExecuteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CursorStat}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorStat(SqlParserParser.CursorStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Security}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity(SqlParserParser.SecurityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetAssign}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAssign(SqlParserParser.SetAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Transaction}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction(SqlParserParser.TransactionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Use}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse(SqlParserParser.UseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Break}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(SqlParserParser.BreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Continue}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(SqlParserParser.ContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code If}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(SqlParserParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(SqlParserParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrow(SqlParserParser.ThrowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatch(SqlParserParser.TryCatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code While}
	 * labeled alternative in {@link SqlParserParser#dclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(SqlParserParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(SqlParserParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(SqlParserParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(SqlParserParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(SqlParserParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(SqlParserParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#throwStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStatement(SqlParserParser.ThrowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#throwErrorNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowErrorNumber(SqlParserParser.ThrowErrorNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#throwMessage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowMessage(SqlParserParser.ThrowMessageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#throwState}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowState(SqlParserParser.ThrowStateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchStatement(SqlParserParser.TryCatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(SqlParserParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(SqlParserParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#objectTypeForGrant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectTypeForGrant(SqlParserParser.ObjectTypeForGrantContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(SqlParserParser.DropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropDeault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDeault(SqlParserParser.DropDeaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropPartitionFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropPartitionFunction(SqlParserParser.DropPartitionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropPartitionScheme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropPartitionScheme(SqlParserParser.DropPartitionSchemeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropDbRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDbRole(SqlParserParser.DropDbRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createDbRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDbRole(SqlParserParser.CreateDbRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropSchema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropSchema(SqlParserParser.DropSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropUser(SqlParserParser.DropUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#disableTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisableTrigger(SqlParserParser.DisableTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#enableTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnableTrigger(SqlParserParser.EnableTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#truncateTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncateTable(SqlParserParser.TruncateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterPartitionFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterPartitionFunction(SqlParserParser.AlterPartitionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterPartitionScheme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterPartitionScheme(SqlParserParser.AlterPartitionSchemeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterSchema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterSchema(SqlParserParser.AlterSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createSchema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSchema(SqlParserParser.CreateSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createPartitionFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePartitionFunction(SqlParserParser.CreatePartitionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createPartitionScheme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePartitionScheme(SqlParserParser.CreatePartitionSchemeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#mergeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeStatement(SqlParserParser.MergeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#whenMatches}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenMatches(SqlParserParser.WhenMatchesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#mergeMatched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeMatched(SqlParserParser.MergeMatchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#mergeNotMatched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeNotMatched(SqlParserParser.MergeNotMatchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#deleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement(SqlParserParser.DeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#deleteStatement_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement_from(SqlParserParser.DeleteStatement_fromContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#insertStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStatement(SqlParserParser.InsertStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#insertStatementValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStatementValues(SqlParserParser.InsertStatementValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#withCteSelectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithCteSelectStatement(SqlParserParser.WithCteSelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStatement(SqlParserParser.SelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#updateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateStatement(SqlParserParser.UpdateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#outputClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputClause(SqlParserParser.OutputClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#outputDmlListElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputDmlListElem(SqlParserParser.OutputDmlListElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(SqlParserParser.CreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndex(SqlParserParser.CreateIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterIndex(SqlParserParser.AlterIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#rebuildPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRebuildPartition(SqlParserParser.RebuildPartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createOrAlterProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrAlterProcedure(SqlParserParser.CreateOrAlterProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createOrAlterTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrAlterTrigger(SqlParserParser.CreateOrAlterTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#triggerOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerOperation(SqlParserParser.TriggerOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createOrAlterFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrAlterFunction(SqlParserParser.CreateOrAlterFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#funcBodyReturnsSelect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBodyReturnsSelect(SqlParserParser.FuncBodyReturnsSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#funcBodyReturnsTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBodyReturnsTable(SqlParserParser.FuncBodyReturnsTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#funcBodyReturnsScalar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBodyReturnsScalar(SqlParserParser.FuncBodyReturnsScalarContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#procedureParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureParam(SqlParserParser.ProcedureParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#procedureOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureOption(SqlParserParser.ProcedureOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#functionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionOption(SqlParserParser.FunctionOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTable(SqlParserParser.CreateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableIndices}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIndices(SqlParserParser.TableIndicesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOptions(SqlParserParser.TableOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createOrAlterView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrAlterView(SqlParserParser.CreateOrAlterViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(SqlParserParser.AlterTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#alterDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabase(SqlParserParser.AlterDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#host}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHost(SqlParserParser.HostContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndex(SqlParserParser.DropIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropRelationalOrXmlOrSpatialIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRelationalOrXmlOrSpatialIndex(SqlParserParser.DropRelationalOrXmlOrSpatialIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropBackwardCompatibleIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropBackwardCompatibleIndex(SqlParserParser.DropBackwardCompatibleIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropProcedure(SqlParserParser.DropProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTrigger(SqlParserParser.DropTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunction(SqlParserParser.DropFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#drop_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_statistics(SqlParserParser.Drop_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(SqlParserParser.DropTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(SqlParserParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#createType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateType(SqlParserParser.CreateTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dropType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropType(SqlParserParser.DropTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#declareStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareStatement(SqlParserParser.DeclareStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#cursorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorStatement(SqlParserParser.CursorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStatement(SqlParserParser.ExecuteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteBody(SqlParserParser.ExecuteBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeStatementArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStatementArg(SqlParserParser.ExecuteStatementArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeStatementArgNamed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStatementArgNamed(SqlParserParser.ExecuteStatementArgNamedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeStatementArgUnnamed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStatementArgUnnamed(SqlParserParser.ExecuteStatementArgUnnamedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteParameter(SqlParserParser.ExecuteParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeVarString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteVarString(SqlParserParser.ExecuteVarStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#securityStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurityStatement(SqlParserParser.SecurityStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#principalId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrincipalId(SqlParserParser.PrincipalIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#grantPermission}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantPermission(SqlParserParser.GrantPermissionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStatement(SqlParserParser.SetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#transactionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionStatement(SqlParserParser.TransactionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#useStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseStatement(SqlParserParser.UseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#executeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteClause(SqlParserParser.ExecuteClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#declareLocal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareLocal(SqlParserParser.DeclareLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableTypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableTypeDef(SqlParserParser.TableTypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableTypeIndices}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableTypeIndices(SqlParserParser.TableTypeIndicesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnDefTableConstraints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDefTableConstraints(SqlParserParser.ColumnDefTableConstraintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnDefTableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDefTableConstraint(SqlParserParser.ColumnDefTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDef(SqlParserParser.ColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#materializedColumnDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaterializedColumnDef(SqlParserParser.MaterializedColumnDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnConstraint(SqlParserParser.ColumnConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableConstraint(SqlParserParser.TableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#foreignKeyOnDelete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeignKeyOnDelete(SqlParserParser.ForeignKeyOnDeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#foreignKeyOnUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeignKeyOnUpdate(SqlParserParser.ForeignKeyOnUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#indexOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOptions(SqlParserParser.IndexOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#indexOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOption(SqlParserParser.IndexOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#declareCursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareCursor(SqlParserParser.DeclareCursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#declareSetCursorCommon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareSetCursorCommon(SqlParserParser.DeclareSetCursorCommonContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#declareSetCursorCommonPartial}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareSetCursorCommonPartial(SqlParserParser.DeclareSetCursorCommonPartialContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#fetchFromCursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetchFromCursor(SqlParserParser.FetchFromCursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#setSpecialOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSpecialOption(SqlParserParser.SetSpecialOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#specialOptionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialOptionList(SqlParserParser.SpecialOptionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#constantLocalId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantLocalId(SqlParserParser.ConstantLocalIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#timeZone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeZone(SqlParserParser.TimeZoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#primitiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveExpression(SqlParserParser.PrimitiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#caseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpression(SqlParserParser.CaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperatorExpression(SqlParserParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#bracketExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(SqlParserParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(SqlParserParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(SqlParserParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#withExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithExpression(SqlParserParser.WithExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#commonTableExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonTableExpression(SqlParserParser.CommonTableExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#updateAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateAssignment(SqlParserParser.UpdateAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#updateAssignmentMerge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateAssignmentMerge(SqlParserParser.UpdateAssignmentMergeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#searchCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchCondition(SqlParserParser.SearchConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(SqlParserParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectQuery(SqlParserParser.SelectQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#unionSelect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionSelect(SqlParserParser.UnionSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectSpec(SqlParserParser.SelectSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(SqlParserParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#offsetClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffsetClause(SqlParserParser.OffsetClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#orderByExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByExpression(SqlParserParser.OrderByExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectList(SqlParserParser.SelectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#udtMethodArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdtMethodArgs(SqlParserParser.UdtMethodArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#allColumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllColumns(SqlParserParser.AllColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectColumnElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectColumnElem(SqlParserParser.SelectColumnElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectUdtElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectUdtElem(SqlParserParser.SelectUdtElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#expressionElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionElem(SqlParserParser.ExpressionElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#selectListElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectListElem(SqlParserParser.SelectListElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableSources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSources(SqlParserParser.TableSourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSource(SqlParserParser.TableSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableSourceItemJoined}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceItemJoined(SqlParserParser.TableSourceItemJoinedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableSourceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceItem(SqlParserParser.TableSourceItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableSourceItemJoinPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableSourceItemJoinPart(SqlParserParser.TableSourceItemJoinPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#joinOnExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinOnExpression(SqlParserParser.JoinOnExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#crossJoinExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrossJoinExpression(SqlParserParser.CrossJoinExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#derivedTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivedTable(SqlParserParser.DerivedTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(SqlParserParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#partitionFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionFunc(SqlParserParser.PartitionFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryChecksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryChecksum(SqlParserParser.BinaryChecksumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Cast}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast(SqlParserParser.CastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CONVERT}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCONVERT(SqlParserParser.CONVERTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Checksum}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChecksum(SqlParserParser.ChecksumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Coalesce}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesce(SqlParserParser.CoalesceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentTimestamp}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentTimestamp(SqlParserParser.CurrentTimestampContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentDate}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentDate(SqlParserParser.CurrentDateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentUser(SqlParserParser.CurrentUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateAdd}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateAdd(SqlParserParser.DateAddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateDiff}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateDiff(SqlParserParser.DateDiffContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateName}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateName(SqlParserParser.DateNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DatePart}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatePart(SqlParserParser.DatePartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDENTITY(SqlParserParser.IDENTITYContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullIf(SqlParserParser.NullIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SystemUser}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemUser(SqlParserParser.SystemUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code User}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser(SqlParserParser.UserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsNull}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNull(SqlParserParser.IsNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XmlDataFunc}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlDataFunc(SqlParserParser.XmlDataFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IIf}
	 * labeled alternative in {@link SqlParserParser#builtInFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIIf(SqlParserParser.IIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlDataTypeFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlDataTypeFunc(SqlParserParser.XmlDataTypeFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlValueFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlValueFunc(SqlParserParser.XmlValueFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlValueCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlValueCall(SqlParserParser.XmlValueCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlQueryFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlQueryFunc(SqlParserParser.XmlQueryFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlQueryCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlQueryCall(SqlParserParser.XmlQueryCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlExistFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlExistFunc(SqlParserParser.XmlExistFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlExistCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlExistCall(SqlParserParser.XmlExistCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlModifyFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlModifyFunc(SqlParserParser.XmlModifyFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#xmlModifyCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlModifyCall(SqlParserParser.XmlModifyCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#switchSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchSection(SqlParserParser.SwitchSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#switchSearchConditionSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchSearchConditionSection(SqlParserParser.SwitchSearchConditionSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#asColumnAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsColumnAlias(SqlParserParser.AsColumnAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#asTableAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsTableAlias(SqlParserParser.AsTableAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnAliasList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAliasList(SqlParserParser.ColumnAliasListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAlias(SqlParserParser.ColumnAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableValuesCtor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableValuesCtor(SqlParserParser.TableValuesCtorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SqlParserParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#rankingWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRankingWindowedFunc(SqlParserParser.RankingWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#aggregateWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateWindowedFunc(SqlParserParser.AggregateWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#analyticWindowedFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyticWindowedFunc(SqlParserParser.AnalyticWindowedFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#allDistinctExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllDistinctExpression(SqlParserParser.AllDistinctExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#overClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverClause(SqlParserParser.OverClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#fullTableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullTableName(SqlParserParser.FullTableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#schemaDotObj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaDotObj(SqlParserParser.SchemaDotObjContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#schemaDotProc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaDotProc(SqlParserParser.SchemaDotProcContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dbSchemaDotProc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbSchemaDotProc(SqlParserParser.DbSchemaDotProcContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#ddlObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdlObject(SqlParserParser.DdlObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#fullColumnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnName(SqlParserParser.FullColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnListWithOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnListWithOrder(SqlParserParser.ColumnListWithOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#insertColumnList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertColumnList(SqlParserParser.InsertColumnListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#insertColumnId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertColumnId(SqlParserParser.InsertColumnIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#columnNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnNameList(SqlParserParser.ColumnNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#cursorName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorName(SqlParserParser.CursorNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#onOff}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnOff(SqlParserParser.OnOffContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#nullOrNot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullOrNot(SqlParserParser.NullOrNotContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#nullOrDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullOrDefault(SqlParserParser.NullOrDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#scalarFuncName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFuncName(SqlParserParser.ScalarFuncNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(SqlParserParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#defaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultValue(SqlParserParser.DefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(SqlParserParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Positive}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositive(SqlParserParser.PositiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negative}
	 * labeled alternative in {@link SqlParserParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegative(SqlParserParser.NegativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(SqlParserParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SqlParserParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#simpleId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleId(SqlParserParser.SimpleIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(SqlParserParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(SqlParserParser.AssignmentOperatorContext ctx);
}