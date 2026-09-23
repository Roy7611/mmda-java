parser grammar SqlQueryParser;

options { tokenVocab=SqlLexer; }

// 带通通用表表达式（CTE）的查询语句
sqlQueryWithCte
    : withCteClause? sqlQuery
    ;
    
sqlQuery
    : select
      from?
      where?
      groupBy?
      orderBy?
      limitOffset?
    ;
    
select
    : SELECT DISTINCT? selectExprList
    ;
from
    : FROM source+=tableSource (',' source+=tableSource)*
    ;
where
    : WHERE condition=criteria
    ;
groupBy
    : GROUP BY groupByExpr+=expression (',' groupByExpr+=expression)* (HAVING havingFilter=criteria)?
    ;
orderBy
    : ORDER BY orderByExpr+=sortExpression (',' orderByExpr+=sortExpression)*
    ;

limitOffset
    : LIMIT limitRows=expression OFFSET offsetStart=expression
    | LIMIT offsetStart=expression ',' limitRows=expression
    | OFFSET offsetStart=expression (ROW | ROWS) (FETCH (FIRST | NEXT) limitRows=expression (ROW | ROWS) ONLY)?
    ;

tableSource
    : tableSourceItemJoined
    | '(' tableSource ')'
    ;

tableSourceItemJoined
    : tableSourceItem joins+=joinClause*
    | '(' tableSourceItemJoined ')' joins+=joinClause*
    ;

tableSourceItem
    : schemaObject               asTableAlias?
    | '(' subQueryOrValuesClause ')'       (asTableAlias columnAliasList?)?
    | functionCall                (asTableAlias columnAliasList?)?
    | locId=LOCAL_ID             asTableAlias?
    | locIdCall=LOCAL_ID '.' locFuncCall=functionCall (asTableAlias columnAliasList?)?
    ;

// 联合语句 INNER JOIN table t1 ON t1.id=t.id
joinClause
    : (inner=INNER? | joinType=(LEFT | RIGHT | FULL) outer=OUTER?)?
             JOIN joinTo=tableSource ON joinOn=criteria
    | CROSS JOIN tableSource
    ;

// 搜索条件是多个条件的联合
criteria
    : NOT? (predicate | '(' criteria ')')
    | criteria AND criteria // AND takes precedence over OR
    | criteria OR criteria
    ;

// 条件
predicate
    : NOT? EXISTS '(' subquery ')'
    | expression comparisonOperator expression
    | expression comparisonOperator (ALL | SOME | ANY)? '(' subquery ')'
    | expression NOT? BETWEEN expression AND expression
    | expression NOT? IN '(' (subquery | expressionList) ')'
    | expression NOT? LIKE expression (ESCAPE expression)?
    | expression IS NOT? NULL_
    ;

subQueryOrValuesClause
    : subquery
    | '(' subquery (UNION ALL subquery)* ')'
    | valuesClause
    | '(' valuesClause ')'
    ;
valuesClause
    : VALUES '(' exprs+=expressionList ')' (',' '(' exprs+=expressionList ')')*
    ;

expressionList
    : expr+=expression (',' expr+=expression)*
    ;
selectExprList
    : selectExpr (',' selectExpr)*
    ;
selectExpr
    : selectAllStar
    | selectColumn
    | selectExpression
    ;

selectAllStar
    : (schemaObject '.')? '*'
    | (INSERTED | DELETED) '.' '*'
    ;

selectColumn
    : (fullColumnName | NULL_) asColumnAlias?
    ;

fullColumnName
    : (DELETED | INSERTED) '.' columnName=id
    | (schemaObject '.')? columnName=id
    | columnName=id
    ;
//selectUdtElem
//    : udt_column_name=id '.' non_static_attr=id udtMethodArgs asColumnAlias?
//    | udt_column_name=id DOUBLE_COLON static_attr=id udtMethodArgs? asColumnAlias?
//    ;

selectExpression
    : expressionAlias=columnAlias eq=EQUAL expressionAssignment=expression
    | expressionAs=expression asColumnAlias?
    ;
sortExpression
    : sortExpr=expression sortOrder=(ASC | DESC)?
    ;
schemaObject
    : (database=id '.' (schema=id)? '.' | schema=id '.')? objName=id
    ;

asTableAlias
    : AS? id
    ;
asColumnAlias
    : AS? columnAlias
    ;
columnAlias
    : id
    | STRING
    ;
columnAliasList
    : '(' alias+=columnAlias (',' alias+=columnAlias)* ')'
    ;

// 表达式
expression
    : primitiveExpression
    | functionCall
    | expression '.' (xmlValueCall | xmlQueryCall | xmlExistCall | xmlModifyCall)
    | expression COLLATE id
    | caseWhenExpression
    | columnExpression = fullColumnName
    | bracketExpression
    | unaryOpExpression
    | expression op=('*' | '/' | '%') expression
    | expression bitOperator expression
    | expression op=('+' | '-' | '||') expression
    | expression timeZone
    | overClause
    ;

primitiveExpression
    : DEFAULT | NULL_ | LOCAL_ID | constant
    ;

caseWhenExpression
    : CASE caseExpr=expression (WHEN expression THEN expression)+ (ELSE elseExpr=expression)? END
    | CASE (WHEN criteria THEN expression)+ (ELSE elseExpr=expression)? END
    ;

unaryOpExpression
    : op='~' expression
    | op=('+' | '-') expression
    ;

bracketExpression
    : '(' expression ')' | '(' subquery ')'
    ;

constantExpression
    : NULL_
    | TRUE
    | FALSE
    | constant
    | functionCall
    | '(' constantExpression ')'
    ;

subquery
    : sqlQuery
    ;

withCteClause
    : WITH cte+=commonTableExpression (',' cte+=commonTableExpression)*
    ;

commonTableExpression
    : expression_name=id ('(' columns=selectExprList ')')? AS '(' cteQuery=sqlQuery ')'
    ;
    
functionCall
    : rankingWindowedFunc
    | aggregateWindowedFunc
    | analyticWindowedFunc
    | builtInFunc
    | scalarFunc
    ;


builtInFunc
    // https://msdn.microsoft.com/en-us/library/ms173784.aspx
    : BINARY_CHECKSUM '(' '*' ')'                       #BinaryChecksumFunc
    // https://msdn.microsoft.com/en-us/library/hh231076.aspx
    // https://msdn.microsoft.com/en-us/library/ms187928.aspx
    | CAST '(' expression AS dataType ')'              #CastFunc
    | CONVERT '(' convert_dataType=dataType ','convert_expression=expression (',' style=expression)? ')'       #Convert
    // https://msdn.microsoft.com/en-us/library/ms189788.aspx
    | CHECKSUM '(' '*' ')'                              #ChecksumFunc
    // https://msdn.microsoft.com/en-us/library/ms190349.aspx
    | COALESCE '(' expressionList ')'                  #CoalesceFunc
    // https://msdn.microsoft.com/en-us/library/ms188751.aspx
    | CURRENT_TIMESTAMP                                 #CurrentTimestampFunc
    | CURRENT_DATE                                      #CurrentDateFunc
    // https://msdn.microsoft.com/en-us/library/ms176050.aspx
    | CURRENT_USER                                      #CurrentUserFunc
    // https://msdn.microsoft.com/en-us/library/ms186819.aspx
    | DATEADD '(' datepart=ID ',' number=expression ',' date=expression ')'  #DateAddFunc
    // https://msdn.microsoft.com/en-us/library/ms189794.aspx
    | DATEDIFF '(' datepart=ID ',' date_first=expression ',' date_second=expression ')' #DateDiffFunc
    // https://msdn.microsoft.com/en-us/library/ms174395.aspx
    | DATENAME '(' datepart=ID ',' date=expression ')'                #DateNameFunc
    // https://msdn.microsoft.com/en-us/library/ms174420.aspx
    | DATEPART '(' datepart=ID ',' date=expression ')'                #DatePartFunc
    // https://msdn.microsoft.com/en-us/library/ms189838.aspx
    | IDENTITY '(' dataType (',' seed=DEC_DIGITS)? (',' increment=DEC_DIGITS)? ')'      #IentityFunc
    // https://msdn.microsoft.com/en-us/library/ms177562.aspx
    | NULLIF '(' left=expression ',' right=expression ')'          #NullIfFunc
    // https://msdn.microsoft.com/en-us/library/ms179930.aspx
    | SYSTEM_USER                                       #SystemUserFunc
    | USER                                              #UserFunc
    // https://msdn.microsoft.com/en-us/library/ms184325.aspx
    | ISNULL '(' left=expression ',' right=expression ')'          #IsNullFunc
    // https://docs.microsoft.com/en-us/sql/t-sql/xml/xml-data-type-methods
    | xmlDataTypeFunc                                   #XmlDataFunc
    // https://docs.microsoft.com/en-us/sql/t-sql/functions/logical-functions-iif-transact-sql
    | IIF '(' cond=criteria ',' left=expression ',' right=expression ')'   #IIfFunc
    ;
scalarFunc
    : (schemaObject
    | RIGHT
    | LEFT
    | CHECKSUM) '(' expressionList? ')'
    ;
xmlDataTypeFunc
    : xmlValueFunc
    | xmlQueryFunc
    | xmlExistFunc
    | xmlModifyFunc
    ;

xmlValueFunc
    : (locId=LOCAL_ID | valueId=id | query=xmlQueryFunc | '(' subquery ')') '.' call=xmlValueCall
    ;

xmlValueCall
    :  VALUE '(' xquery=STRING ',' sqltype=STRING ')'
    ;

xmlQueryFunc
    : (locId=LOCAL_ID | valueId=id | table=schemaObject | '(' subquery ')' ) '.' call=xmlQueryCall
    ;

xmlQueryCall
    : QUERY '(' xquery=STRING ')'
    ;

xmlExistFunc
    : (locId=LOCAL_ID | valueId=id | '(' subquery ')') '.' call=xmlExistCall
    ;

xmlExistCall
    : EXIST '(' xquery=STRING ')'
    ;

xmlModifyFunc
    : (locId=LOCAL_ID | valueId=id | '(' subquery ')') '.' call=xmlModifyCall
    ;

xmlModifyCall
    : MODIFY '(' xmlDml=STRING ')'
    ;

timeZone
    : AT_KEYWORD TIME ZONE expression
    ;

rankingWindowedFunc
    : (RANK | DENSE_RANK | ROW_NUMBER) '(' ')' overClause
    ;

// https://msdn.microsoft.com/en-us/library/ms173454.aspx
aggregateWindowedFunc
    : agg_func=(AVG | MAX | MIN | SUM | STDEV | STDEVP | VAR)
      '(' allOrDistinctExpression ')' overClause?
    | cnt=COUNT '(' ('*' | allOrDistinctExpression) ')' overClause?
    | GROUPING '(' expression ')'
    | GROUPING_ID '(' expressionList ')'
    ;

// https://docs.microsoft.com/en-us/sql/t-sql/functions/analytic-functions-transact-sql
analyticWindowedFunc
    : (FIRST_VALUE | LAST_VALUE) '(' expression ')' overClause
    | (LAG | LEAD) '(' expression  (',' expression (',' expression)? )? ')' overClause
    | (CUME_DIST | PERCENT_RANK) '(' ')' OVER '(' (PARTITION BY expressionList)? orderBy ')'
    | (PERCENTILE_CONT | PERCENTILE_DISC) '(' expression ')' WITHIN GROUP '(' ORDER BY expression (ASC | DESC)? ')' OVER '(' (PARTITION BY expressionList)? ')'
    ;

allOrDistinctExpression
    : (ALL | DISTINCT)? expression
    ;

overClause
    : OVER '(' (PARTITION BY expressionList)? orderBy? ')'
    ;


timeUnit
    : YEAR
    | YEARS
    | QUARTER
    | QUARTERS
    | MONTH
    | MONTHS
    | WEEK
    | WEEKS
    | DAY
    | DAYS
    | HOUR
    | HOURS
    | MINUTE
    | MINUTES
    | SECOND
    | SECONDS
    | MICROSECOND
    | MICROSECONDS
    | MILLISECOND
    | MILLISECONDS
    ;
intervalUnit
    : timeUnit //以下仅用于MySql
    | YEAR_MONTH
    | DAY_HOUR
    | DAY_MINUTE
    | DAY_SECOND
    | DAY_MICROSECOND
    | HOUR_MINUTE
    | HOUR_SECOND
    | HOUR_MICROSECOND
    | MINUTE_SECOND
    | MINUTE_MICROSECOND
    | SECOND_MICROSECOND
    ;

//https://learn.microsoft.com/zh-cn/sql/odbc/reference/appendixes/interval-literals?view=sql-server-ver17
//https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/Interval-Expressions.html
//https://dev.mysql.com/doc/refman/8.4/en/expressions.html#temporal-intervals
//https://www.postgresql.org/docs/current/functions-datetime.html
intervalExpression
    : INTERVAL (intervalExpr=expression leadingUnit=DAY ('(' leadingPrec=DEC_DIGIT ')')? TO secondPrec=SECOND ('(' secondPrec=DEC_DIGIT ')')? //Oracle DAY TO SECOND
    | intervalExpr=expression leadingUnit=YEAR ('(' leadingPrec=DEC_DIGIT ')')? TO secondPrec=MONTH //ORACLE YEAR TO MONTH
    | intervalExpr=expression firstUnit=timeUnit ('(' leadingPrec=DEC_DIGIT ')')? (TO secondUnit=timeUnit ('(' secondPrec=DEC_DIGIT ')')?)? //SQL SERVER
    | '\''? intervalTemporal (intervalTemporal)* '\''? //INTERVAL expr unit
    )
    ;
intervalTemporal
    : intervalExpr=expression intervalUnit
    ;

//// 日期增加函数
//dateAddFunc
//    : DATEADD '(' datepart=ID ',' number=expression ',' date=expression ')' //Sql Server
//    | DATE_ADD '(' datepart=ID ',' intervalExpression ')'                   //MySql
//    // Oracle & Postgre 都是 date + interval 表达式
//    ;

// 数据类型
dataType
    : scaledType=(CHAR | VARCHAR | NCHAR | NVARCHAR | BINARY | VARBINARY) '(' DEC_DIGITS ')'
    | extType=id '(' precision=DEC_DIGITS ',' scale=DEC_DIGITS ')'
    | extType=id '(' scale=DEC_DIGITS ')'
    | unscaledType=id
    ;
constant
    : STRING // string, datetime or uuid
    | HEX_NUMBER
    | sign? DEC_DIGITS
    | sign? (REAL_NUMBER | DEC_NUMBER)  // float or decimal
    | sign? dollar='$' (DEC_DIGITS | DEC_NUMBER)       // money
    ;
sign
    : '+'   
    | '-'   
    ;
//关键字
keyword
    : SELECT
    | DISTINCT
    | AS
    | FROM
    | WHERE
    | GROUP
    | BY
    | HAVING
    | WHERE
    | ORDER
    | ASC
    | DESC
    | OVER
    | LIMIT
    | OFFSET
    | FIRST
    | LAST
    | NEXT
    | ROW
    | ROWS
    | ONLY
    //Data Types
    | BIT
    | BOOL

    | TINYINT
    | SMALLINT
    | INT
    | INTEGER
    | BIGINT
    | UNSIGNED
    | NUMBER
    | NUMERIC
    | DEC_DIGITS
    | MONEY
    | DOUBLE
    | FLOAT
    | REAL
    | PRECISION

    | CHAR
    | NCHAR
    | VARCHAR
    | NVARCHAR
    | UUID
    | CLOB
    | NCLOB
    | JSON
    | XML
    | TEXT
    | NTEXT

    | DATE
    | TIME
    | DATETIME
    | TIMESTAMP
    | INTERVAL
    | ZONE

    | VECTOR
    | BINARY
    | VARYING
    | BLOB
    | BFILE
    | GEOGRAPHY
    | GEOMETRY
    | ANY
    // Language
    | CASE
    | WHEN
    | IF
    | THEN
    | ELSE
    | BEGIN
    | END
    | IIF
    | NULL_
    | IS
    | NOT
    | AND
    | OR

    | YEAR
    | QUARTER
    | MONTH
    | WEEK
    | DAY
    | HOUR
    | MINUTE
    | SECOND
    | MICROSECOND
    | MILLISECOND
    | NANOSECOND
    // Built in Functions
    | SUM
    | COUNT
    | MAX
    | MIN
    | AVG
    | DATEADD
    | CURRENT_DATE
    | CURRENT_TIME
    | CURRENT_TIMESTAMP
    ;

id
    : ID
    | DOUBLE_QUOTE_ID
    | BACK_QUOTE_ID
    | SQUARE_BRACKET_ID
    | keyword
    ;

comparisonOperator
    : '=' | '>' | '<' | '<' '=' | '>' '=' | '<' '>' | '!' '=' | '!' '>' | '!' '<'
    ;
bitOperator
    : '>' '>' '>' | '>' '>' | '<' '<' | '&' | '|' | '^' 
    ;
assignmentOperator
    : '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '^=' | '|='
    ;