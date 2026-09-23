
grammar SqlParser;

options { tokenVocab=SqlLexer; }

// 多个 SQL 语句
sqlStatements
    : sqlStatement* EOF
    ;

// SQL 包含：数据定义语言(DDL)、数据查询语言（DQL）、数据操纵语言（DML）、数据控制语言（DCL）
sqlStatement
    : dmlStatement SEMI?                #Dml
    | withCteSelectStatement SEMI?      #Dql
    | ddlStatement SEMI?                #Ddl
    | dclStatement SEMI?                #Dcl
    | emptyStatement                    #EmptyStat
    ;


// 数据处理语言（Data Manipulation Language）
dmlStatement
    : mergeStatement                    
    | deleteStatement                   
    | insertStatement                   
    | updateStatement                   
    | truncateTable                     
    ;

// 数据定义语言（Data Definition Language）
ddlStatement
    : alterDatabase
    | alterIndex
    | alterPartitionFunction
    | alterPartitionScheme
    | alterSchema
    | alterTable
    | createDatabase
    | createIndex
    | createSchema
    | createTable
    | createType
    | createOrAlterFunction
    | createOrAlterProcedure
    | createOrAlterTrigger
    | createOrAlterView
    | createPartitionFunction
    | createPartitionScheme
    | createDbRole
    | dropDatabase
    | dropDbRole
    | dropDeault
    | dropFunction
    | dropIndex
    | dropPartitionFunction
    | dropPartitionScheme
    | dropProcedure
    | dropSchema
    | drop_statistics
    | dropTable
    | dropTrigger
    | dropType
    | dropUser
    | dropView
    | disableTrigger
    | enableTrigger
    ;

// 数据控制语言（DCL） 包括 Control-of-Flow Language
dclStatement
    : blockStatement                    #Block
    | declareStatement                  #DeclareVar
    | executeStatement                  #Execute
    | cursorStatement                   #CursorStat
    | securityStatement                 #Security
    | setStatement                      #SetAssign
    | transactionStatement              #Transaction
    | useStatement                      #Use
    | breakStatement                    #Break
    | continueStatement                 #Continue
    | ifStatement                       #If
    | returnStatement                   #Return
    | throwStatement                    #Throw
    | tryCatchStatement                 #TryCatch
    | whileStatement                    #While
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/begin-end-transact-sql
blockStatement
    : BEGIN ';'? sqlStatement* END ';'?
    ;

breakStatement
    : BREAK ';'?
    ;

continueStatement
    : CONTINUE ';'?
    ;

returnStatement
    : RETURN expression? ';'?
    ;

ifStatement
    : IF searchCondition sqlStatement (ELSE sqlStatement)? ';'?
    ;

throwStatement
    : THROW (throwErrorNumber ',' throwMessage ',' throwState)? ';'?
    ;

throwErrorNumber
    : DEC_DIGITS | LOCAL_ID
    ;

throwMessage
    : STRING | LOCAL_ID
    ;

throwState
    : DEC_DIGITS | LOCAL_ID
    ;

tryCatchStatement
    : BEGIN TRY ';'? try_clauses=sqlStatement+ END TRY ';'? BEGIN CATCH ';'? catchClauses=sqlStatement* END CATCH ';'?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/while-transact-sql
whileStatement
    : WHILE searchCondition (sqlStatement | BREAK ';'? | CONTINUE ';'?)
    ;


emptyStatement
    : ';'
    ;



// https://docs.microsoft.com/en-us/sql/t-sql/statements/grant-transact-sql?view=sql-server-ver15
// SELECT DISTINCT '| ' + CLASS_DESC
// FROM sys.dm_audit_actions
// ORDER BY 1
objectTypeForGrant
    : COLUMN ( ENCRYPTION ) KEY
    | DATABASE ( SPECIFICATION
               | ENCRYPTION KEY
               | SCOPED )
    | ENDPOINT
    | LOGIN
    | OBJECT
    | PARTITION ( FUNCTION | SCHEME)
    | ROLE
    | SCHEMA
    | SERVICE
    | SQL LOGIN
    | TRIGGER ( DATABASE | SERVER)
    | TYPE
    | USER
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-database-transact-sql
dropDatabase
    : DROP DATABASE ( IF EXISTS )? (COMMA? database_name_or_database_snapshot_name=id)+
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-default-transact-sql
dropDeault
    : DROP DEFAULT ( IF EXISTS )? (COMMA? (schema_name=id DOT)? default_name=id)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-partition-function-transact-sql
dropPartitionFunction
    : DROP PARTITION FUNCTION partition_function_name=id
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-partition-scheme-transact-sql
dropPartitionScheme
    : DROP PARTITION SCHEME partition_scheme_name=id
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-role-transact-sql
dropDbRole
    : DROP ROLE ( IF EXISTS )? role_name=id
    ;
createDbRole
    : CREATE ROLE role_name=id (AUTHORIZATION owner_name = id)?
    ;
// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-schema-transact-sql
dropSchema
    :  DROP SCHEMA ( IF EXISTS )? schema_name=id
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-user-transact-sql
dropUser
    : DROP USER ( IF EXISTS )? user_name=id
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/disable-trigger-transact-sql
disableTrigger
    : DISABLE TRIGGER ( ( COMMA? (schema_name=id DOT)? trigger_name=id )+ | ALL)         ON ((schema_id=id DOT)? object_name=id|DATABASE|ALL SERVER)
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/enable-trigger-transact-sql
enableTrigger
    : ENABLE TRIGGER ( ( COMMA? (schema_name=id DOT)? trigger_name=id )+ | ALL)         ON ( (schema_id=id DOT)? object_name=id|DATABASE|ALL SERVER)
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/truncate-table-transact-sql
truncateTable
    : TRUNCATE TABLE tableName
          ( WITH LR_BRACKET
              PARTITIONS LR_BRACKET
                                (COMMA? (DEC_DIGITS|DEC_DIGITS TO DEC_DIGITS) )+
                         RR_BRACKET

                 RR_BRACKET
          )?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-partition-function-transact-sql
alterPartitionFunction
    : ALTER PARTITION FUNCTION partition_function_name=id LR_BRACKET RR_BRACKET        (SPLIT|MERGE) RANGE LR_BRACKET DEC_DIGITS RR_BRACKET
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-partition-scheme-transact-sql
alterPartitionScheme
    : ALTER PARTITION SCHEME partition_scheme_name=id NEXT USED (file_group_name=id)?
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-schema-transact-sql
alterSchema
    : ALTER SCHEMA schema_name=id TRANSFER ((OBJECT|TYPE) DOUBLE_COLON )? id (DOT id)?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-schema-transact-sql
createSchema
    : CREATE SCHEMA
	(schema_name=id
        | AUTHORIZATION owner_name=id
        | schema_name=id AUTHORIZATION owner_name=id
        )
        (createTable
         |createOrAlterView
         | GRANT (SELECT|INSERT|DELETE|UPDATE) ON (SCHEMA DOUBLE_COLON)? object_name=id TO owner_name=id
         | REVOKE (SELECT|INSERT|DELETE|UPDATE) ON (SCHEMA DOUBLE_COLON)? object_name=id FROM owner_name=id
        )*
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-partition-function-transact-sql?view=sql-server-ver15
createPartitionFunction
    : CREATE PARTITION FUNCTION partition_function_name=id '(' input_parameter_type=dataType ')'
      AS RANGE ( LEFT | RIGHT )?
      FOR VALUES '(' boundary_values=expressionList ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-partition-scheme-transact-sql?view=sql-server-ver15
createPartitionScheme
    : CREATE PARTITION SCHEME partition_scheme_name=id
      AS PARTITION partition_function_name=id
      ALL? TO '(' file_group_names+=id (',' file_group_names+=id)* ')'
    ;


// DML

// https://docs.microsoft.com/en-us/sql/t-sql/statements/merge-transact-sql
// note that there's a limit on number of whenMatches but it has to be done runtime due to different ordering of statements allowed
mergeStatement
    : withExpression?
      MERGE (TOP '(' expression ')' PERCENT?)?
      INTO? ddlObject asTableAlias?
      USING tableSources
      ON searchCondition
      whenMatches+
      outputClause? ';'
    ;

whenMatches
    : (WHEN MATCHED (AND searchCondition)?
          THEN mergeMatched)+
    | (WHEN NOT MATCHED (BY TARGET)? (AND searchCondition)?
          THEN mergeNotMatched)
    | (WHEN NOT MATCHED BY SOURCE (AND searchCondition)?
          THEN mergeMatched)+
    ;

mergeMatched
    : UPDATE SET updateAssignmentMerge (',' updateAssignmentMerge)*
    | DELETE
    ;

mergeNotMatched
    : INSERT ('(' columnNameList ')')?
      (tableValuesCtor | DEFAULT VALUES)
    ;

// https://msdn.microsoft.com/en-us/library/ms189835.aspx
deleteStatement
    : withExpression?
      DELETE (TOP '(' expression ')' PERCENT? | TOP DEC_DIGITS)?
      FROM? deleteStatement_from
      outputClause?
      (FROM tableSources)?
      (WHERE (searchCondition | CURRENT OF (GLOBAL? cursorName | cursor_var=LOCAL_ID)))? ';'?
    ;

deleteStatement_from
    : ddlObject
    | id
    | table_var=LOCAL_ID
    ;

// https://msdn.microsoft.com/en-us/library/ms174335.aspx
insertStatement
    : withExpression?
      INSERT (TOP '(' expression ')' PERCENT?)?
      INTO? ddlObject
      ('(' insertColumnList ')')?
      outputClause?
      insertStatementValues ';'?
    ;

insertStatementValues
    : tableValuesCtor
    | derivedTable
    | executeStatement
    | DEFAULT VALUES
    ;

// 带通通用表表达式（CTE）的查询语句
withCteSelectStatement
    : withExpression? selectStatement
    ;

// 可能带排序（Order by）和窗口限制（Offset..）的查询语句
selectStatement
    : selectQuery (orderByClause offsetClause?)? ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms177523.aspx
updateStatement
    : withExpression?
      UPDATE ddlObject
      SET updateAssignment (',' updateAssignment)*
      outputClause?
      (FROM tableSources)?
      (WHERE (searchCondition | CURRENT OF (GLOBAL? cursorName | cursor_var=LOCAL_ID)))? ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms177564.aspx
outputClause
    : OUTPUT outputDmlListElem (',' outputDmlListElem)*
      (INTO (LOCAL_ID | tableName) ('(' columnNameList ')')? )?
    ;

outputDmlListElem
    : (expression | allColumns) asColumnAlias?
    ;

// DDL

// https://msdn.microsoft.com/en-ie/library/ms176061.aspx
createDatabase
    : CREATE DATABASE (database=id)
//    ( CONTAINMENT '=' ( NONE | PARTIAL ) )?
//    ( ON PRIMARY? database_file_spec ( ',' database_file_spec )* )?
//    ( LOG ON database_file_spec ( ',' database_file_spec )* )?
//    ( COLLATE collation_name = id )?
//    ( WITH  createDatabase_option ( ',' createDatabase_option )* )?
    ;

// https://msdn.microsoft.com/en-us/library/ms188783.aspx
createIndex
    : CREATE UNIQUE? INDEX id ON tableName '(' columnListWithOrder ')'
    (INCLUDE '(' columnNameList ')' )?
    (WHERE where=searchCondition)?
    (indexOptions)?
    (ON id)?
    ';'?
    ;

alterIndex
    : ALTER INDEX id ON tableName (DISABLE | rebuildPartition)
    ;

rebuildPartition
    : REBUILD (PARTITION '=' ALL)? indexOptions?
    ;

// https://msdn.microsoft.com/en-us/library/ms187926(v=sql.120).aspx
createOrAlterProcedure
    : ((CREATE (OR ALTER)?) | ALTER) proc=(PROC | PROCEDURE) procName=schemaDotProc (';' DEC_DIGITS)?
      ('('? procedureParam (',' procedureParam)* ')'?)?
      (WITH procedureOption (',' procedureOption)*)?
      (FOR REPLICATION)? AS sqlStatement*
    ;


// https://docs.microsoft.com/en-us/sql/t-sql/statements/create-trigger-transact-sql
createOrAlterTrigger
    : ((CREATE (OR ALTER)?) | ALTER) TRIGGER schemaDotObj
      ON tableName
      (FOR | AFTER | INSTEAD OF)
      triggerOperation (',' triggerOperation)*
      (NOT FOR REPLICATION)?
      AS sqlStatement+
    ;

triggerOperation
    : (INSERT | UPDATE | DELETE)
    ;


// https://msdn.microsoft.com/en-us/library/ms186755.aspx
createOrAlterFunction
    : ((CREATE (OR ALTER)?) | ALTER) FUNCTION funcName=schemaDotProc
        (('(' procedureParam (',' procedureParam)* ')') | '(' ')') //must have (), but can be empty
        (funcBodyReturnsSelect | funcBodyReturnsTable | funcBodyReturnsScalar) ';'?
    ;

funcBodyReturnsSelect
    : RETURNS TABLE
        (WITH functionOption (',' functionOption)*)?
        AS? (RETURN ('(' withCteSelectStatement ')' | withCteSelectStatement))
    ;

funcBodyReturnsTable
    : RETURNS LOCAL_ID tableTypeDef
        (WITH functionOption (',' functionOption)*)?
        AS? (
        BEGIN
           sqlStatement*
           RETURN ';'?
        END ';'?)
    ;

funcBodyReturnsScalar
    : RETURNS dataType
        (WITH functionOption (',' functionOption)*)?
        AS? (
        BEGIN
           sqlStatement*
           RETURN ret=expression ';'?
       END)
    ;

procedureParam
    : LOCAL_ID AS? (type_schema=id '.')? dataType VARYING? ('=' default_val=defaultValue)? (OUT | OUTPUT | READONLY)?
    ;

procedureOption
    : ENCRYPTION
    | RECOMPILE
    | executeClause
    ;

functionOption
    : ENCRYPTION
    | RETURNS NULL_ ON NULL_ INPUT
    | executeClause
    ;

// https://msdn.microsoft.com/en-us/library/ms174979.aspx
createTable
    : CREATE TABLE tableName '(' columnDefTableConstraints  (','? tableIndices)*  ','? ')' (LOCK simpleId)? tableOptions* (ON id | DEFAULT)?';'?
    ;

tableIndices
    : INDEX id  UNIQUE? '(' columnListWithOrder ')'
    indexOptions?
    (ON id)?
    ;

tableOptions
    : WITH ('(' indexOption (',' indexOption)* ')' | indexOption (',' indexOption)*)
    ;

// https://msdn.microsoft.com/en-us/library/ms187956.aspx
createOrAlterView
    : ((CREATE (OR ALTER)?) | ALTER) VIEW schemaDotObj ('(' columnNameList ')')?
      AS withCteSelectStatement ';'?
    ;


// https://msdn.microsoft.com/en-us/library/ms190273.aspx
alterTable
    : ALTER TABLE tableName (ADD columnDefTableConstraints
                             | ALTER COLUMN columnDef
                             | DROP COLUMN id (',' id)*
                             | DROP CONSTRAINT constraint=id
                             | ADD (CONSTRAINT constraint=id)?
                                ( FOREIGN KEY '(' fk=columnNameList ')' REFERENCES tableName ('(' pk=columnNameList')')? (foreignKeyOnDelete | foreignKeyOnUpdate)*
                                | CHECK '(' searchCondition ')' )
                             | CONSTRAINT constraint=id
                             | (ENABLE | DISABLE) TRIGGER id?
                             | REBUILD tableOptions
                             )
                             ';'?
    ;


// https://msdn.microsoft.com/en-us/library/ms174269.aspx
alterDatabase
    : ALTER DATABASE (database=id | CURRENT)
      (MODIFY NAME '=' new_name=id
      | COLLATE collation=id
      ) ';'?
    ;

host
    : id DOT host
    | (id DOT |id)
    ;

// https://msdn.microsoft.com/en-us/library/ms176118.aspx
dropIndex
    : DROP INDEX (IF EXISTS)?
    ( dropRelationalOrXmlOrSpatialIndex (',' dropRelationalOrXmlOrSpatialIndex)*
    | dropBackwardCompatibleIndex (',' dropBackwardCompatibleIndex)*
    )
    ';'?
    ;

dropRelationalOrXmlOrSpatialIndex
    : index_name=id ON fullTableName
    ;

dropBackwardCompatibleIndex
    : (owner_name=id '.')? table_or_view_name=id '.' index_name=id
    ;

// https://msdn.microsoft.com/en-us/library/ms174969.aspx
dropProcedure
    : DROP proc=(PROC | PROCEDURE) (IF EXISTS)? schemaDotProc (',' schemaDotProc)* ';'?
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/statements/drop-trigger-transact-sql
dropTrigger
    : DROP TRIGGER (IF EXISTS)? schemaDotObj (',' schemaDotObj)* ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms190290.aspx
dropFunction
    : DROP FUNCTION (IF EXISTS)? schemaDotProc (',' schemaDotProc)* ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms175075.aspx
drop_statistics
    : DROP STATISTICS (COMMA? (tableName '.')? name=id)+ ';'
    ;

// https://msdn.microsoft.com/en-us/library/ms173790.aspx
dropTable
    : DROP TABLE (IF EXISTS)? tableName ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms173492.aspx
dropView
    : DROP VIEW (IF EXISTS)? schemaDotObj (',' schemaDotObj)* ';'?
    ;

createType
    : CREATE TYPE name = schemaDotObj
      (FROM dataType defaultValue)?
      (AS TABLE LR_BRACKET columnDefTableConstraints RR_BRACKET)?
    ;

dropType:
    DROP TYPE ( IF EXISTS )? name = schemaDotObj
    ;


// Other statements.

// https://msdn.microsoft.com/en-us/library/ms188927.aspx
declareStatement
    : DECLARE LOCAL_ID AS? (tableTypeDef | tableName) ';'?
    | DECLARE loc+=declareLocal (',' loc+=declareLocal)* ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms181441(v=sql.120).aspx
cursorStatement
    // https://msdn.microsoft.com/en-us/library/ms175035(v=sql.120).aspx
    : CLOSE GLOBAL? cursorName ';'?
    // https://msdn.microsoft.com/en-us/library/ms188782(v=sql.120).aspx
    | DEALLOCATE GLOBAL? CURSOR? cursorName ';'?
    // https://msdn.microsoft.com/en-us/library/ms180169(v=sql.120).aspx
    | declareCursor
    // https://msdn.microsoft.com/en-us/library/ms180152(v=sql.120).aspx
    | fetchFromCursor
    // https://msdn.microsoft.com/en-us/library/ms190500(v=sql.120).aspx
    | OPEN GLOBAL? cursorName ';'?
    ;


// https://msdn.microsoft.com/en-us/library/ms188332.aspx
executeStatement
    : EXECUTE executeBody ';'?
    ;

//https://docs.microsoft.com/it-it/sql/t-sql/language-elements/execute-transact-sql?view=sql-server-ver15
executeBody
    : (return_status=LOCAL_ID '=')? (dbSchemaDotProc | executeVarString)  executeStatementArg?
    | '(' executeVarString (',' executeVarString)* ')' (AS? (LOGIN | USER) '=' STRING)? (AT_KEYWORD linkedServer=id)?
    ;

executeStatementArg
    :
    executeStatementArgUnnamed (',' executeStatementArg) *    //Unnamed params can continue unnamed
    |
    executeStatementArgNamed (',' executeStatementArgNamed)* //Named can only be continued by unnamed
    ;

executeStatementArgNamed
    : name=LOCAL_ID '=' value=executeParameter
    ;

executeStatementArgUnnamed
    : value=executeParameter
    ;

executeParameter
    : (constant | LOCAL_ID (OUTPUT | OUT)? | id | DEFAULT | NULL_)
    ;

executeVarString
    : LOCAL_ID (OUTPUT | OUT)? ('+' LOCAL_ID ('+' executeVarString)?)?
    | STRING ('+' LOCAL_ID ('+' executeVarString)?)?
    ;

// https://msdn.microsoft.com/en-us/library/ff848791.aspx
securityStatement
    // https://msdn.microsoft.com/en-us/library/ms188354.aspx
    : executeClause ';'?
    // https://msdn.microsoft.com/en-us/library/ms187965.aspx
    | GRANT (ALL PRIVILEGES? | grantPermission ('(' columnNameList ')')?) (ON (objectTypeForGrant '::')? on_id=tableName)? TO to_principal+=principalId (',' to_principal+=principalId)* (WITH GRANT OPTION)? (AS as_principal=principalId)? ';'?
    ;

principalId:
    | id
    | PUBLIC
    ;


// https://docs.microsoft.com/en-us/sql/relational-databases/system-functions/sys-fn-builtin-permissions-transact-sql?view=sql-server-ver15
// SELECT DISTINCT '| ' + permission_name
// FROM sys.fn_builtin_permissions (DEFAULT)
// ORDER BY 1
grantPermission
    : ALTER ( ANY ( COLUMN
                  | CONNECTION
                  | DATABASE
                  | ENDPOINT
                  | FULLTEXT CATALOG
                  | LINKED SERVER
                  | LOGIN
                  | MASK
                  | REMOTE SERVICE BINDING
                  | ROLE
                  | SCHEMA
                  | SERVICE
                  | USER
                  )
            | RESOURCES
            | SERVER STATE
            | TRACE
            )?
    | BACKUP ( DATABASE | LOG )
    | CONNECT ( ANY DATABASE | REPLICATION | SQL )?
    | CREATE ( AGGREGATE
             | ANY DATABASE
             | DATABASE (DDL EVENT)?
             | DEFAULT
             | ENDPOINT
             | FULLTEXT CATALOG
             | FUNCTION
             | PROCEDURE
             | REMOTE SERVICE BINDING
             | ROLE
             | SCHEMA
             | SERVER ROLE
             | SERVICE
             | TABLE
             | TYPE
             | VIEW
             | XML SCHEMA COLLECTION
             )
    | DELETE
    | INSERT
    | RECEIVE
    | REFERENCES
    | SEND
    | SHUTDOWN
    | UNMASK
    | UPDATE
    | VIEW ( ANY ( DATABASE | DEFINITION | COLUMN KEY DEFINITION )
           | DATABASE STATE
           | DEFINITION
           | SERVER STATE
           )
    ;

// https://msdn.microsoft.com/en-us/library/ms190356.aspx
// https://msdn.microsoft.com/en-us/library/ms189484.aspx
setStatement
    : SET LOCAL_ID ('.' member_name=id)? '=' expression ';'?
    | SET LOCAL_ID assignmentOperator expression ';'?
    | SET LOCAL_ID '='
      CURSOR declareSetCursorCommon (FOR (READ ONLY | UPDATE (OF columnNameList)?))? ';'?
    // https://msdn.microsoft.com/en-us/library/ms189837.aspx
    | setSpecialOption
    ;

// https://msdn.microsoft.com/en-us/library/ms174377.aspx
transactionStatement
    // https://msdn.microsoft.com/en-us/library/ms188386.aspx
    : BEGIN (TRAN | TRANSACTION) ((id | LOCAL_ID))? ';'?
    // https://msdn.microsoft.com/en-us/library/ms190295.aspx
    | COMMIT (TRAN | TRANSACTION) (id | LOCAL_ID)? ';'?
    // https://msdn.microsoft.com/en-us/library/ms178628.aspx
    | COMMIT id
    | ROLLBACK id
    // https://msdn.microsoft.com/en-us/library/ms181299.aspx
    | ROLLBACK (TRAN | TRANSACTION) (id | LOCAL_ID)? ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms188366.aspx
useStatement
    : USE database=id ';'?
    ;

executeClause
    : EXECUTE AS clause=(SELF | OWNER | STRING)
    ;

declareLocal
    : LOCAL_ID AS? dataType ('=' expression)?
    ;

tableTypeDef
    : TABLE '(' columnDefTableConstraints (','? tableTypeIndices)*  ')'
    ;

tableTypeIndices
    :  ((PRIMARY KEY | INDEX id) | UNIQUE) '(' columnListWithOrder ')'
    | CHECK '(' searchCondition ')'
    ;

columnDefTableConstraints
    : columnDefTableConstraint (','? columnDefTableConstraint)*
    ;

columnDefTableConstraint
    : columnDef
    | materializedColumnDef
    | tableConstraint
    ;

// https://msdn.microsoft.com/en-us/library/ms187742.aspx
columnDef
    : id (dataType | AS expression PERSISTED? ) (COLLATE id)? nullOrNot?
      ((CONSTRAINT constraint=id)? nullOrDefault nullOrDefault?
       | IDENTITY ('(' seed=DEC_DIGITS ',' increment=DEC_DIGITS ')')? (NOT FOR REPLICATION)?)?
      columnConstraint*
    ;

materializedColumnDef
    : id (COMPUTE | AS) expression (MATERIALIZED | NOT MATERIALIZED)?
    ;

// https://msdn.microsoft.com/en-us/library/ms186712.aspx
columnConstraint
    :(CONSTRAINT constraint=id)?
      ((PRIMARY KEY | UNIQUE) indexOptions?
      | CHECK (NOT FOR REPLICATION)? '(' searchCondition ')'
      | (FOREIGN KEY)? REFERENCES tableName '(' pk = columnNameList')' foreignKeyOnDelete? foreignKeyOnUpdate?
      | nullOrNot)
    ;

// https://msdn.microsoft.com/en-us/library/ms188066.aspx
tableConstraint
    : (CONSTRAINT constraint=id)?
       ((PRIMARY KEY | UNIQUE) '(' columnListWithOrder ')' indexOptions? (ON id)?
         | CHECK (NOT FOR REPLICATION)? '(' searchCondition ')'
         | DEFAULT '('?  ((STRING | PLUS | functionCall | DEC_DIGITS)+ | NEXT VALUE FOR tableName) ')'? FOR id
         | FOREIGN KEY '(' fk = columnNameList ')' REFERENCES tableName ('(' pk = columnNameList')')? foreignKeyOnDelete? foreignKeyOnUpdate?)
    ;

foreignKeyOnDelete
    : ON DELETE (NO ACTION | CASCADE | SET NULL_ | SET DEFAULT)
    ;

foreignKeyOnUpdate
    : ON UPDATE (NO ACTION | CASCADE | SET NULL_ | SET DEFAULT)
    ;

indexOptions
    : WITH '(' indexOption (',' indexOption)* ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms186869.aspx
// Id runtime checking. Id in (PAD_INDEX, FILLFACTOR, IGNORE_DUP_KEY, STATISTICS_NORECOMPUTE, ALLOW_ROW_LOCKS,
// ALLOW_PAGE_LOCKS, SORT_IN_TEMPDB, ONLINE, MAXDOP, DATA_COMPRESSION, ONLINE).
indexOption
    : (simpleId | keyword) '=' (simpleId | keyword | onOff | DEC_DIGITS)
    | INDEX | HEAP
    | INDEX '(' id (ASC | DESC)? (',' id (ASC | DESC)?)* ')'
    ;

// https://msdn.microsoft.com/en-us/library/ms180169.aspx
declareCursor
    : DECLARE cursorName
      (CURSOR (declareSetCursorCommon (FOR UPDATE (OF columnNameList)?)?)?
      | (SEMI_SENSITIVE | INSENSITIVE)? SCROLL? CURSOR FOR withCteSelectStatement (FOR (READ ONLY | UPDATE | (OF columnNameList)))?
      ) ';'?
    ;

declareSetCursorCommon
    : declareSetCursorCommonPartial*
      FOR withCteSelectStatement
    ;

declareSetCursorCommonPartial
    : (LOCAL | GLOBAL)
    | (FORWARD_ONLY | SCROLL)
    | (STATIC | KEYSET | DYNAMIC | FAST_FORWARD)
    | (READ_ONLY | OPTIMISTIC)
    ;

fetchFromCursor
    : FETCH ((NEXT | PRIOR | FIRST | LAST | (ABSOLUTE | RELATIVE) expression)? FROM)?
      GLOBAL? cursorName (INTO LOCAL_ID (',' LOCAL_ID)*)? ';'?
    ;

// https://msdn.microsoft.com/en-us/library/ms190356.aspx
// Runtime check.
setSpecialOption
    : SET id (id | constantLocalId | onOff) ';'?
    | SET STATISTICS (IO | TIME | XML) onOff ';'?
    // https://msdn.microsoft.com/en-us/library/ms173763.aspx
    | SET TRANSACTION ISOLATION LEVEL
      (READ UNCOMMITTED | READ COMMITTED | REPEATABLE READ | SNAPSHOT | SERIALIZABLE | DEC_DIGITS) ';'?
    // https://msdn.microsoft.com/en-us/library/ms188059.aspx
    | SET specialOptionList (',' specialOptionList)* onOff
    | SET xmlModifyFunc
    ;

specialOptionList
    : QUOTED_IDENTIFIER
    | XACT_ABORT
    ;

constantLocalId
    : constant
    | LOCAL_ID
    ;

// Expression.

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/expressions-transact-sql
// Operator precendence: https://docs.microsoft.com/en-us/sql/t-sql/language-elements/operator-precedence-transact-sql
expression
    : primitiveExpression
    | functionCall
    | expression '.' (xmlValueCall | xmlQueryCall | xmlExistCall | xmlModifyCall)
    | expression COLLATE id
    | caseExpression
    | fullColumnName
    | bracketExpression
    | unaryOperatorExpression
    | expression op=('*' | '/' | '%') expression
    | expression op=('+' | '-' | '&' | '^' | '|' | '||') expression
    | expression timeZone
    | overClause
    | DOLLAR_ACTION
    ;

timeZone
    : AT_KEYWORD TIME ZONE expression
    ;

primitiveExpression
    : DEFAULT | NULL_ | LOCAL_ID | constant
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/language-elements/case-transact-sql
caseExpression
    : CASE caseExpr=expression switchSection+ (ELSE elseExpr=expression)? END
    | CASE switchSearchConditionSection+ (ELSE elseExpr=expression)? END
    ;

unaryOperatorExpression
    : '~' expression
    | op=('+' | '-') expression
    ;

bracketExpression
    : '(' expression ')' | '(' subquery ')'
    ;

constantExpression
    : NULL_
    | constant
    // system functions: https://msdn.microsoft.com/en-us/library/ms187786.aspx
    | functionCall
    | LOCAL_ID         // TODO: remove.
    | '(' constantExpression ')'
    ;

subquery
    : selectStatement
    ;

// https://msdn.microsoft.com/en-us/library/ms175972.aspx
withExpression
    : WITH ctes+=commonTableExpression (',' ctes+=commonTableExpression)*
    ;

commonTableExpression
    : expression_name=id ('(' columns=columnNameList ')')? AS '(' cte_query=selectStatement ')'
    ;

updateAssignment
    : LOCAL_ID '=' fullColumnName ('=' | assignmentOperator) expression //Combined variable and column update
    | (fullColumnName | LOCAL_ID) ('=' | assignmentOperator) expression
    | udt_column_name=id '.' method_name=id '(' expressionList ')'
    //| fullColumnName '.' WRITE (expression, )
    ;

updateAssignmentMerge
    : (fullColumnName | LOCAL_ID) ('=' | assignmentOperator) expression
    | udt_column_name=id '.' method_name=id '(' expressionList ')'
    //| fullColumnName '.' WRITE (expression, )
    ;

// 搜索条件是多个条件的联合
searchCondition
    : NOT? (predicate | '(' searchCondition ')')
    | searchCondition AND searchCondition // AND takes precedence over OR
    | searchCondition OR searchCondition
    ;

// 条件
predicate
    : NOT? EXISTS '(' subquery ')'
    | expression comparisonOperator expression
    | expression comparisonOperator (ALL | SOME | ANY)? '(' subquery ')'
    | expression NOT? BETWEEN expression AND expression
    | expression NOT? IN '(' (subquery | expressionList) ')'
    | expression NOT? LIKE expression (ESCAPE expression)?
    | expression IS nullOrNot
    ;

// Changed union rule to unionSelect to avoid union construct with C++ target.  Issue reported by person who generates into C++.  This individual reports change causes generated code to work
// 选择查询包含联合、排序和窗口限制
selectQuery
    : (selectSpec | '(' selectQuery ')' (UNION ALL? selectQuery)? )
    |  selectSpec (orderByClause offsetClause?)? unions+=unionSelect* //if using top, order by can be on the "top" side of union :/
    ;

unionSelect
    : (UNION ALL? | EXCEPT | INTERSECT) (spec=selectSpec | ('(' op=selectQuery ')'))
    ;

// 单个SELECT语句，没有Order By语句
selectSpec
    : SELECT allOrDistinct=(ALL | DISTINCT)?
      columns=selectList
      // https://msdn.microsoft.com/en-us/library/ms188029.aspx
      (INTO into=tableName)?
      (FROM from=tableSources)?
      (WHERE where=searchCondition)?
      // https://msdn.microsoft.com/en-us/library/ms177673.aspx
      (GROUP BY groupByAll=ALL? groupBys+=expression (',' groupBys+=expression)*)?
      (HAVING having=searchCondition)?
    ;

// https://msdn.microsoft.com/en-us/library/ms188385.aspx
orderByClause
    : ORDER BY order_bys+=orderByExpression (',' order_bys+=orderByExpression)*
    ;

offsetClause
    : OFFSET offset_exp=expression offset_rows=(ROW | ROWS) (FETCH fetch_offset=(FIRST | NEXT) fetch_exp=expression fetch_rows=(ROW | ROWS) ONLY)?
    ;

orderByExpression
    : order_by=expression (ascending=ASC | descending=DESC)?
    ;

// https://msdn.microsoft.com/en-us/library/ms176104.aspx
selectList
    : selectItem+=selectListElem (',' selectItem+=selectListElem)*
    ;

udtMethodArgs
    : '(' argument+=executeVarString (',' argument+=executeVarString)* ')'
    ;

// https://docs.microsoft.com/ru-ru/sql/t-sql/queries/select-clause-transact-sql
allColumns
    : (tableName '.')? '*'
    | (INSERTED | DELETED) '.' '*'
    ;

selectColumnElem
    : (fullColumnName | '$' IDENTITY | NULL_) asColumnAlias?
    ;

selectUdtElem
    : udt_column_name=id '.' non_static_attr=id udtMethodArgs asColumnAlias?
    | udt_column_name=id DOUBLE_COLON static_attr=id udtMethodArgs? asColumnAlias?
    ;

expressionElem
    : leftAlias=columnAlias eq='=' leftAssignment=expression
    | expressionAs=expression asColumnAlias?
    ;

selectListElem
    : allColumns
    | selectColumnElem
    | selectUdtElem
    | LOCAL_ID (assignmentOperator | '=') ( expression | NEXT VALUE FOR tableName)
    | expressionElem
    | NEXT VALUE FOR tableName asColumnAlias?
    ;

tableSources
    : source+=tableSource (',' source+=tableSource)*
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/queries/from-transact-sql
tableSource
    : tableSourceItemJoined
    | '(' tableSource ')'
    ;

tableSourceItemJoined
    : tableSourceItem joins+=tableSourceItemJoinPart*
    | '(' tableSourceItemJoined ')' joins+=tableSourceItemJoinPart*
    ;

tableSourceItem
    : fullTableName             asTableAlias?
    | '(' derivedTable ')'       (asTableAlias columnAliasList?)?
    | functionCall                (asTableAlias columnAliasList?)?
    | loc_id=LOCAL_ID             asTableAlias?
    | loc_id_call=LOCAL_ID '.' loc_fcall=functionCall (asTableAlias columnAliasList?)?
    | DOUBLE_COLON oldstyle_fcall=functionCall       asTableAlias? // Build-in function (old syntax)
    ;

// https://msdn.microsoft.com/en-us/library/ms191472.aspx
tableSourceItemJoinPart
    // https://msdn.microsoft.com/en-us/library/ms173815(v=sql.120).aspx
    : joinOnExpression
    | crossJoinExpression
    ;
joinOnExpression
    : (inner=INNER? | join_type=(LEFT | RIGHT | FULL) outer=OUTER?) (join_hint=(LOOP | HASH | MERGE | REMOTE))?
       JOIN source=tableSource ON cond=searchCondition
    ;

crossJoinExpression
    : CROSS JOIN tableSource
    ;

derivedTable
    : subquery
    | '(' subquery (UNION ALL subquery)* ')'
    | tableValuesCtor
    | '(' tableValuesCtor ')'
    ;

functionCall
    : rankingWindowedFunc                        
    | aggregateWindowedFunc                      
    | analyticWindowedFunc                       
    | builtInFunc                               
    | scalarFuncName '(' expressionList? ')'     
    | partitionFunc                                
    ;

partitionFunc
    : (database=id '.')? DOLLAR_PARTITION '.' func_name=id '(' expression ')'
    ;

builtInFunc
    // https://msdn.microsoft.com/en-us/library/ms173784.aspx
    : BINARY_CHECKSUM '(' '*' ')'                       #BinaryChecksum
    // https://msdn.microsoft.com/en-us/library/hh231076.aspx
    // https://msdn.microsoft.com/en-us/library/ms187928.aspx
    | CAST '(' expression AS dataType ')'              #Cast
    | CONVERT '(' convert_dataType=dataType ','convert_expression=expression (',' style=expression)? ')'                              #CONVERT
    // https://msdn.microsoft.com/en-us/library/ms189788.aspx
    | CHECKSUM '(' '*' ')'                              #Checksum
    // https://msdn.microsoft.com/en-us/library/ms190349.aspx
    | COALESCE '(' expressionList ')'                  #Coalesce
    // https://msdn.microsoft.com/en-us/library/ms188751.aspx
    | CURRENT_TIMESTAMP                                 #CurrentTimestamp
    | CURRENT_DATE                                      #CurrentDate
    // https://msdn.microsoft.com/en-us/library/ms176050.aspx
    | CURRENT_USER                                      #CurrentUser
    // https://msdn.microsoft.com/en-us/library/ms186819.aspx
    | DATEADD '(' datepart=ID ',' number=expression ',' date=expression ')'  #DateAdd
    // https://msdn.microsoft.com/en-us/library/ms189794.aspx
    | DATEDIFF '(' datepart=ID ',' date_first=expression ',' date_second=expression ')' #DateDiff
    // https://msdn.microsoft.com/en-us/library/ms174395.aspx
    | DATENAME '(' datepart=ID ',' date=expression ')'                #DateName
    // https://msdn.microsoft.com/en-us/library/ms174420.aspx
    | DATEPART '(' datepart=ID ',' date=expression ')'                #DatePart
    // https://msdn.microsoft.com/en-us/library/ms189838.aspx
    | IDENTITY '(' dataType (',' seed=DEC_DIGITS)? (',' increment=DEC_DIGITS)? ')'                                                           #IDENTITY
    // https://msdn.microsoft.com/en-us/library/ms177562.aspx
    | NULLIF '(' left=expression ',' right=expression ')'          #NullIf
    // https://msdn.microsoft.com/en-us/library/ms179930.aspx
    | SYSTEM_USER                                       #SystemUser
    | USER                                              #User
    // https://msdn.microsoft.com/en-us/library/ms184325.aspx
    | ISNULL '(' left=expression ',' right=expression ')'          #IsNull
    // https://docs.microsoft.com/en-us/sql/t-sql/xml/xml-data-type-methods
    | xmlDataTypeFunc                                   #XmlDataFunc
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/logical-functions-iif-transact-sql
    | IIF '(' cond=searchCondition ',' left=expression ',' right=expression ')'   #IIf
    ;

xmlDataTypeFunc
    : xmlValueFunc      
    | xmlQueryFunc      
    | xmlExistFunc      
    | xmlModifyFunc     
    ;

xmlValueFunc
    : (loc_id=LOCAL_ID | value_id=id | query=xmlQueryFunc | '(' subquery ')') '.' call=xmlValueCall
    ;

xmlValueCall
    :  VALUE '(' xquery=STRING ',' sqltype=STRING ')'
    ;

xmlQueryFunc
    : (loc_id=LOCAL_ID | value_id=id | table=fullTableName | '(' subquery ')' ) '.' call=xmlQueryCall
    ;

xmlQueryCall
    : QUERY '(' xquery=STRING ')'
    ;

xmlExistFunc
    : (loc_id=LOCAL_ID | value_id=id | '(' subquery ')') '.' call=xmlExistCall
    ;

xmlExistCall
    : EXIST '(' xquery=STRING ')'
    ;

xmlModifyFunc
    : (loc_id=LOCAL_ID | value_id=id | '(' subquery ')') '.' call=xmlModifyCall
    ;

xmlModifyCall
    : MODIFY '(' xml_dml=STRING ')'
    ;


switchSection
    : WHEN expression THEN expression
    ;

switchSearchConditionSection
    : WHEN searchCondition THEN expression
    ;

asColumnAlias
    : AS? columnAlias
    ;

asTableAlias
    : AS? id
    ;

columnAliasList
    : '(' alias+=columnAlias (',' alias+=columnAlias)* ')'
    ;

columnAlias
    : id
    | STRING
    ;

tableValuesCtor
    : VALUES '(' exps+=expressionList ')' (',' '(' exps+=expressionList ')')*
    ;

expressionList
    : exp+=expression (',' exp+=expression)*
    ;

// https://msdn.microsoft.com/en-us/library/ms189798.aspx
rankingWindowedFunc
    : (RANK | DENSE_RANK | ROW_NUMBER) '(' ')' overClause
    ;

// https://msdn.microsoft.com/en-us/library/ms173454.aspx
aggregateWindowedFunc
    : agg_func=(AVG | MAX | MIN | SUM | STDEV | STDEVP | VAR)
      '(' allDistinctExpression ')' overClause?
    | cnt=COUNT '(' ('*' | allDistinctExpression) ')' overClause?
    | GROUPING '(' expression ')'
    | GROUPING_ID '(' expressionList ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/functions/analytic-functions-transact-sql
analyticWindowedFunc
    : (FIRST_VALUE | LAST_VALUE) '(' expression ')' overClause
    | (LAG | LEAD) '(' expression  (',' expression (',' expression)? )? ')' overClause
    | (CUME_DIST | PERCENT_RANK) '(' ')' OVER '(' (PARTITION BY expressionList)? orderByClause ')'
    | (PERCENTILE_CONT | PERCENTILE_DISC) '(' expression ')' WITHIN GROUP '(' ORDER BY expression (ASC | DESC)? ')' OVER '(' (PARTITION BY expressionList)? ')'
    ;

allDistinctExpression
    : (ALL | DISTINCT)? expression
    ;

// https://msdn.microsoft.com/en-us/library/ms189461.aspx
overClause
    : OVER '(' (PARTITION BY expressionList)? orderByClause? ')'
    ;


// Primitive.
fullTableName
    : (linkedServer=id '.' '.' schema=id   '.'
    |                       server=id    '.' database=id '.'  schema=id   '.'
    |                                         database=id '.' (schema=id)? '.'
    |                                                           schema=id    '.')? table=id
    ;

tableName
    : (database=id '.' (schema=id)? '.' | schema=id '.')? table=id
    | (database=id '.' (schema=id)? '.' | schema=id '.')?
    ;

schemaDotObj
    : (schema=id '.')? name=id
    ;

schemaDotProc
    : ((schema=id) '.')? procedure=id
    ;

dbSchemaDotProc
    : database=id? '.' schema=id? '.' procedure=id
    | schemaDotProc
    ;

ddlObject
    : fullTableName
    | LOCAL_ID
    ;

fullColumnName
    : (DELETED | INSERTED) '.' column_name=id
    | server=id? '.' schema=id? '.' tablename=id? '.' column_name=id
    | schema=id? '.' tablename=id? '.' column_name=id
    | tablename=id? '.' column_name=id
    | column_name=id
    ;

columnListWithOrder
    : id (ASC | DESC)? (',' id (ASC | DESC)?)*
    ;

//For some reason, sql server allows any number of prefixes:  Here, h is the column: a.b.c.d.e.f.g.h
insertColumnList
    : col+=insertColumnId (',' col+=insertColumnId)*
    ;

insertColumnId
    : (ignore+=id? '.' )* id
    ;

columnNameList
    : col+=id (',' col+=id)*
    ;

cursorName
    : id
    | LOCAL_ID
    ;

onOff
    : ON
    | OFF
    ;

nullOrNot
    : NOT? NULL_
    ;

nullOrDefault
    :(nullOrNot | DEFAULT constantExpression (COLLATE id)? (WITH VALUES)?)
    ;

scalarFuncName
    : dbSchemaDotProc
    | RIGHT
    | LEFT
    | BINARY_CHECKSUM
    | CHECKSUM
    ;


// 数据类型
dataType
    : scaled=(VARCHAR | NVARCHAR | BINARY_KEYWORD | VARBINARY_KEYWORD) '(' DEC_DIGITS ')'
    | ext_type=id '(' scale=DEC_DIGITS ',' prec=DEC_DIGITS ')'
    | ext_type=id '(' scale=DEC_DIGITS ')'
    | ext_type=id IDENTITY ('(' seed=DEC_DIGITS ',' inc=DEC_DIGITS ')')? //Only for SQL Server
    | double_prec=DOUBLE PRECISION?
    | unscaled_type=id
    ;

defaultValue
    : NULL_
    | DEFAULT
    | constant
    ;

constant
    : STRING // string, datetime or uuid
    | HEX_NUMBER
    | sign? DEC_DIGITS
    | sign? (REAL_NUMBER | DEC_NUMBER)  // float or decimal
    | sign? dollar='$' (DEC_DIGITS | DEC_NUMBER)       // money
    ;

sign
    : '+'   #Positive
    | '-'   #Negative
    ;

keyword
    : ABSOLUTE
    | ACTION
    | ACTIVE
    | ADD
    | AFTER
    | AGGREGATE
    | ALGORITHM
    | AT_KEYWORD
    | AVG
    | BIGINT
    | BINARY_BASE64
    | BINARY_CHECKSUM
    | BINDING
    | BLOB_STORAGE
    | CAST
    | CATALOG
    | CATCH
    | CHECKSUM
    | COLLECTION
    | COMMITTED
    | CONCAT
    | COUNT
    | DATA
    | DATEADD
    | DATEDIFF
    | DATENAME
    | DATEPART
    | DAYS
    | DEFAULT_DOUBLE_QUOTE
    | DEFAULT_LANGUAGE
    | DELETED
    | DENSE_RANK
    | DES
    | DESCRIPTION
    | DESX
    | DISABLE
    | DISK_DRIVE
    | DYNAMIC
    | EMPTY
    | ENABLE
    | ENCRYPTION
    | EXIST
    | EXPLICIT
    | FAST
    | FAST_FORWARD
    | FILTER
    | FIRST
    | FIRST_VALUE
    | FOLLOWING
    | FORCE
    | FORMAT
    | FORWARD_ONLY
    | FULLTEXT
    | GLOBAL
    | GROUPING
    | GROUPING_ID
    | HASH
    | INPUT
    | INSENSITIVE
    | INSERTED
    | INT
    | ISOLATION
    | JOB
    | JSON
    | KEYS
    | KEYSET
    | LAG
    | LAST
    | LAST_VALUE
    | LEAD
    | LEVEL
    | LIST
    | LOCAL
    | LOCK
    | LOGIN
    | LOOP
    | MATERIALIZED
    | MAX
    | MEDIUM
    | MIN
    | MINUTES
    | MODE
    | MODIFY
    | MOVE
    | NAME
    | NEXT
    | NO
    | NUMBER
    | OBJECT
    | OFFSET
    | ONLY
    | OPTIMISTIC
    | OPTIMIZE
    | OUT
    | OUTPUT
    | OWNER
    | PARTITION
    | PARTITIONS
    | PATH
    | POOL
    | PORT
    | PRECEDING
    | PRIOR
    | PRIORITY
    | PRIVATE
    | PRIVILEGES
    | PROCEDURE_NAME
    | PROPERTY
    | PROVIDER
    | PROVIDER_KEY_NAME
    | QUERY
    | QUOTED_IDENTIFIER
    | RANGE
    | RANK
    | RC2
    | RC4
    | RC4_128
    | READ_ONLY
    | READ_WRITE
    | READONLY
    | REBUILD
    | RECEIVE
    | RECOMPILE
    | RECOVERY
    | RELATIVE
    | REMOTE
    | REMOVE
    | REPEATABLE
    | REPLICA
    | RESOURCE
    | ROOT
    | ROW
    | ROW_NUMBER
    | ROWS
    | SCOPED
    | SCROLL
    | SECONDS
    | SECURITY
    | SELF
    | SEMI_SENSITIVE
    | SEND
    | SENT
    | SERIALIZABLE
    | SHARE
    | SIMPLE
    | SIZE
    | SMALLINT
    | SNAPSHOT
    | STANDBY
    | STATIC
    | STATUS
    | STDEV
    | STDEVP
    | SUBJECT
    | SUM
    | SUSPEND
    | SYSTEM
    | THROW
    | TIME
    | TIMEOUT
    | TIMER
    | TINYINT
    | TRY
    | SQL
    | TYPE
    | UNBOUNDED
    | UNCOMMITTED
    | UNKNOWN
    | UNLIMITED
    | USING
    | VALIDATION
    | VALUE
    | VAR
    | VIEWS
    | WORK
    | XML
    | BEFORE
    | BLOCK
    | BUFFER
    | CACHE
    | COMPRESSION
    | CONTEXT
    | CUME_DIST
    | DISTRIBUTION
    | ENABLED
    | ENDPOINT
    | ERROR
    | EVENT
    | GET
    | IIF
    | IO
    | INCLUDE
    | INCREMENT
    | INFINITE
    | INIT
    | INSTEAD
    | ISNULL
    | LANGUAGE
    | LIBRARY
    | LIFETIME
    | LINUX
    | LOG
    | MATCHED
    | NONE
    | PAGE
    | PERCENTILE_CONT
    | PERCENTILE_DISC
    | PERCENT_RANK
    | PERSISTED
    | REPLICATE
    | REQUIRED
    | RESET
    | RESTART
    | RESUME
    | RETURNS
    | ROLE
    | SAFETY
    | SAFE
    | SCHEDULER
    | SCHEME
    | SERVER
    | SERVICE
    | SKIP_KEYWORD
    | SOURCE
    | SPECIFICATION
    | SPLIT
    | STATE
    | STATS
    | START
    | STARTED
    | STOP
    | STOPPED
    | TARGET
    | TCP
    | TRANSFER
    | UNCHECKED
    | UNLOCK
    | UNSAFE
    | URL
    | USED
    | VISIBILITY
    | WINDOWS
    | WITHOUT
    | WITNESS
    | ZONE
    //Build-ins:
    | VARCHAR
    | NVARCHAR
    | BINARY_KEYWORD
    | VARBINARY_KEYWORD
    | PRECISION //For some reason this is possible to use as ID
    ;

// https://msdn.microsoft.com/en-us/library/ms175874.aspx
id
    : ID
    | DOUBLE_QUOTE_ID
    | BACK_QUOTE_ID
    | SQUARE_BRACKET_ID
    | keyword
    ;

simpleId
    : ID
    ;

// https://msdn.microsoft.com/en-us/library/ms188074.aspx
// Spaces are allowed for comparison operators.
comparisonOperator
    : '=' | '>' | '<' | '<' '=' | '>' '=' | '<' '>' | '!' '=' | '!' '>' | '!' '<'
    ;

assignmentOperator
    : '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '^=' | '|='
    ;
