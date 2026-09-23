// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlQueryParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SqlQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BOOL=1, TINYINT=2, SMALLINT=3, INT=4, INTEGER=5, BIGINT=6, UNSIGNED=7, 
		DECIMAL=8, NUMERIC=9, MONEY=10, FLOAT=11, REAL=12, DOUBLE=13, CHAR=14, 
		NCHAR=15, VARCHAR=16, NVARCHAR=17, UUID=18, BIT=19, CLOB=20, NCLOB=21, 
		TEXT=22, NTEXT=23, JSON=24, XML=25, DATE=26, TIME=27, DATETIME=28, TIMESTAMP=29, 
		INTERVAL=30, VECTOR=31, BIT_VECTOR=32, BINARY=33, VARBINARY=34, BLOB=35, 
		BFILE=36, GEOGRAPHY=37, GEOMETRY=38, ANY=39, ADD=40, ALL=41, ALTER=42, 
		AND=43, AS=44, ASC=45, BACKSLASH=46, BACKUP=47, BEGIN=48, BETWEEN=49, 
		BREAK=50, BULK=51, BY=52, CASCADE=53, CASE=54, CHECK=55, CLOSE=56, COALESCE=57, 
		COLLATE=58, COLUMN=59, COMMIT=60, COMPUTE=61, CONSTRAINT=62, CONTAINS=63, 
		CONTINUE=64, CONVERT=65, CREATE=66, CROSS=67, CURRENT=68, CURRENT_DATE=69, 
		CURRENT_TIME=70, CURRENT_TIMESTAMP=71, CURRENT_USER=72, CURSOR=73, DATABASE=74, 
		DEALLOCATE=75, DECLARE=76, DEFAULT=77, DELETE=78, DENY=79, DESC=80, DISK=81, 
		DISTINCT=82, DOUBLE_BACK_SLASH=83, DOUBLE_FORWARD_SLASH=84, DROP=85, DUMP=86, 
		ELSE=87, END=88, ESCAPE=89, EXCEPT=90, EXECUTE=91, EXISTS=92, EXIT=93, 
		FETCH=94, FILE=95, FOR=96, FOREIGN=97, FROM=98, FULL=99, FUNCTION=100, 
		GRANT=101, GROUP=102, HAVING=103, IDENTITY=104, IF=105, IN=106, INDEX=107, 
		INNER=108, INSERT=109, INTERSECT=110, INTO=111, IS=112, JOIN=113, KEY=114, 
		LEFT=115, LIKE=116, LOAD=117, MERGE=118, NATIONAL=119, NOT=120, NULL_=121, 
		NULLIF=122, NULLS=123, OF=124, OFF=125, OFFSET=126, OFFSETS=127, ON=128, 
		OPEN=129, OPTION=130, OR=131, ORDER=132, OUTER=133, OVER=134, PERCENT=135, 
		PRECISION=136, PRIMARY=137, PROC=138, PROCEDURE=139, PUBLIC=140, RAW=141, 
		READ=142, REFERENCES=143, REPLICATION=144, RESTORE=145, RESTRICT=146, 
		RETURN=147, REVERT=148, REVOKE=149, RIGHT=150, ROLLBACK=151, SCHEMA=152, 
		SELECT=153, SET=154, SHUTDOWN=155, SOME=156, STATISTICS=157, SYSTEM_USER=158, 
		TABLE=159, THEN=160, TO=161, TOP=162, TRAN=163, TRANSACTION=164, TRIGGER=165, 
		TRUE=166, FALSE=167, TRUNCATE=168, UNION=169, UNIQUE=170, UPDATE=171, 
		USE=172, USER=173, VALUES=174, VARYING=175, VIEW=176, WHEN=177, WHERE=178, 
		WHILE=179, WITH=180, WITHIN=181, DOLLAR_PARTITION=182, ABSOLUTE=183, AT_KEYWORD=184, 
		ACTION=185, ACTIVE=186, AFTER=187, AGGREGATE=188, ALGORITHM=189, AUTHORIZATION=190, 
		AVG=191, BINARY_BASE64=192, BINARY_CHECKSUM=193, BINDING=194, BLOB_STORAGE=195, 
		CAST=196, CATALOG=197, CATCH=198, CHANGE=199, CHECKSUM=200, COLLECTION=201, 
		COMMITTED=202, CONCAT=203, COUNT=204, CUME_DIST=205, DATA=206, DATEADD=207, 
		DATEDIFF=208, DATENAME=209, DATEPART=210, DAY=211, DAYS=212, DEFAULT_DOUBLE_QUOTE=213, 
		DEFAULT_LANGUAGE=214, DEFINITION=215, DELETED=216, DENSE_RANK=217, DES=218, 
		DESCRIPTION=219, DESX=220, DISABLE=221, DISK_DRIVE=222, DYNAMIC=223, EMPTY=224, 
		ENABLE=225, ENCRYPTION=226, EXIST=227, EXPLICIT=228, FAST=229, FAST_FORWARD=230, 
		FILTER=231, FIRST=232, FIRST_VALUE=233, FOLLOWING=234, FORCE=235, FORMAT=236, 
		FORWARD_ONLY=237, FULLTEXT=238, GLOBAL=239, GROUPING=240, GROUPING_ID=241, 
		HASH=242, HOUR=243, HOURS=244, INPUT=245, INSENSITIVE=246, INSERTED=247, 
		ISOLATION=248, JOB=249, KEYS=250, KEYSET=251, LAG=252, LAST=253, LAST_VALUE=254, 
		LEAD=255, LEVEL=256, LIST=257, LOCAL=258, LOCK=259, LOGIN=260, LOOP=261, 
		MATERIALIZED=262, MAX=263, MEDIUM=264, MIN=265, MINUTE=266, MINUTES=267, 
		MICROSECOND=268, MICROSECONDS=269, MILLISECOND=270, MILLISECONDS=271, 
		NANOSECOND=272, NANOSECONDS=273, YEAR_MONTH=274, DAY_HOUR=275, DAY_MINUTE=276, 
		DAY_SECOND=277, DAY_MICROSECOND=278, HOUR_MINUTE=279, HOUR_SECOND=280, 
		HOUR_MICROSECOND=281, MINUTE_SECOND=282, MINUTE_MICROSECOND=283, SECOND_MICROSECOND=284, 
		MODE=285, MODIFY=286, MONTH=287, MONTHS=288, MOVE=289, NAME=290, NEXT=291, 
		NO=292, NUMBER=293, OBJECT=294, ONLY=295, OPTIMISTIC=296, OPTIMIZE=297, 
		OUT=298, OUTPUT=299, OWNER=300, PARTITION=301, PARTITIONS=302, PATH=303, 
		PERCENT_RANK=304, PERCENTILE_CONT=305, PERCENTILE_DISC=306, POOL=307, 
		PORT=308, PRECEDING=309, PRIOR=310, PRIORITY=311, PRIVATE=312, PRIVILEGES=313, 
		PROCEDURE_NAME=314, PROPERTY=315, PROVIDER=316, PROVIDER_KEY_NAME=317, 
		QUARTER=318, QUARTERS=319, QUERY=320, QUOTED_IDENTIFIER=321, RANGE=322, 
		RANK=323, RC2=324, RC4=325, RC4_128=326, READ_ONLY=327, READ_WRITE=328, 
		READONLY=329, READWRITE=330, REBUILD=331, RECEIVE=332, RECOMPILE=333, 
		RECOVERY=334, RELATIVE=335, REMOTE=336, REMOVE=337, REPEATABLE=338, REPLICA=339, 
		RESOURCE=340, ROOT=341, ROW=342, ROW_NUMBER=343, ROWS=344, SCOPED=345, 
		SCROLL=346, SECOND=347, SECONDS=348, SECURITY=349, SELF=350, SEMI_SENSITIVE=351, 
		SEND=352, SENT=353, SERIALIZABLE=354, SHARE=355, SIMPLE=356, SIZE=357, 
		SNAPSHOT=358, STANDBY=359, STATIC=360, STATUS=361, STDEV=362, STDEVP=363, 
		SUBJECT=364, SUM=365, SUSPEND=366, SYSTEM=367, THROW=368, TIMEOUT=369, 
		TIMER=370, TRY=371, TYPE=372, UNBOUNDED=373, UNCOMMITTED=374, UNKNOWN=375, 
		UNLIMITED=376, UNMASK=377, USING=378, VALIDATION=379, VALUE=380, VAR=381, 
		VIEWS=382, WORK=383, YEAR=384, YEARS=385, ZONE=386, DOLLAR_ACTION=387, 
		BEFORE=388, BLOCK=389, BUFFER=390, CACHE=391, COMPRESSION=392, CONNECT=393, 
		CONNECTION=394, CONTEXT=395, DDL=396, DISTRIBUTION=397, ENABLED=398, ENDPOINT=399, 
		ERROR=400, EVENT=401, GET=402, HEAP=403, IIF=404, IO=405, INCLUDE=406, 
		INCREMENT=407, INFINITE=408, INIT=409, INSTEAD=410, ISNULL=411, LANGUAGE=412, 
		LIBRARY=413, LIFETIME=414, LINKED=415, LINUX=416, LOG=417, MASK=418, MATCHED=419, 
		NONE=420, PAGE=421, PERSISTED=422, REPLICATE=423, REQUIRED=424, RESET=425, 
		RESOURCES=426, RESTART=427, RESUME=428, RETURNS=429, ROLE=430, SAFETY=431, 
		SAFE=432, SCHEDULER=433, SCHEME=434, SCRIPT=435, SERVER=436, SERVICE=437, 
		SKIP_KEYWORD=438, SOURCE=439, SPECIFICATION=440, SPLIT=441, SQL=442, STATE=443, 
		STATS=444, START=445, STARTED=446, STOP=447, STOPPED=448, SWITCH=449, 
		TARGET=450, TCP=451, TRACE=452, TRANSFER=453, UNCHECKED=454, UNLOCK=455, 
		UNSAFE=456, URL=457, USED=458, VISIBILITY=459, WINDOWS=460, WITHOUT=461, 
		WITNESS=462, XACT_ABORT=463, SPACE=464, COMMENT=465, LINE_COMMENT=466, 
		BACK_QUOTE_ID=467, DOUBLE_QUOTE_ID=468, SINGLE_QUOTE=469, SQUARE_BRACKET_ID=470, 
		LOCAL_ID=471, DEC_DIGITS=472, ID=473, STRING=474, BIT_STR=475, HEX_NUMBER=476, 
		DEC_NUMBER=477, REAL_NUMBER=478, EQUAL=479, GREATER=480, LESS=481, EXCLAMATION=482, 
		PLUS_ASSIGN=483, MINUS_ASSIGN=484, MULT_ASSIGN=485, DIV_ASSIGN=486, MOD_ASSIGN=487, 
		AND_ASSIGN=488, XOR_ASSIGN=489, OR_ASSIGN=490, DOUBLE_BAR=491, DOT=492, 
		UNDERLINE=493, AT=494, SHARP=495, DOLLAR=496, LR_BRACKET=497, RR_BRACKET=498, 
		COMMA=499, SEMI=500, COLON=501, DOUBLE_COLON=502, STAR=503, DIVIDE=504, 
		MODULE=505, PLUS=506, MINUS=507, BIT_NOT=508, BIT_OR=509, BIT_AND=510, 
		BIT_XOR=511, LIMIT=512, WEEK=513, WEEKS=514, DEC_DIGIT=515;
	public static final int
		RULE_sqlQueryWithCte = 0, RULE_sqlQuery = 1, RULE_select = 2, RULE_from = 3, 
		RULE_where = 4, RULE_groupBy = 5, RULE_orderBy = 6, RULE_limitOffset = 7, 
		RULE_tableSource = 8, RULE_tableSourceItemJoined = 9, RULE_tableSourceItem = 10, 
		RULE_joinClause = 11, RULE_criteria = 12, RULE_predicate = 13, RULE_subQueryOrValuesClause = 14, 
		RULE_valuesClause = 15, RULE_expressionList = 16, RULE_selectExprList = 17, 
		RULE_selectExpr = 18, RULE_selectAllStar = 19, RULE_selectColumn = 20, 
		RULE_fullColumnName = 21, RULE_selectExpression = 22, RULE_sortExpression = 23, 
		RULE_schemaObject = 24, RULE_asTableAlias = 25, RULE_asColumnAlias = 26, 
		RULE_columnAlias = 27, RULE_columnAliasList = 28, RULE_expression = 29, 
		RULE_primitiveExpression = 30, RULE_caseWhenExpression = 31, RULE_unaryOpExpression = 32, 
		RULE_bracketExpression = 33, RULE_constantExpression = 34, RULE_subquery = 35, 
		RULE_withCteClause = 36, RULE_commonTableExpression = 37, RULE_functionCall = 38, 
		RULE_builtInFunc = 39, RULE_scalarFunc = 40, RULE_xmlDataTypeFunc = 41, 
		RULE_xmlValueFunc = 42, RULE_xmlValueCall = 43, RULE_xmlQueryFunc = 44, 
		RULE_xmlQueryCall = 45, RULE_xmlExistFunc = 46, RULE_xmlExistCall = 47, 
		RULE_xmlModifyFunc = 48, RULE_xmlModifyCall = 49, RULE_timeZone = 50, 
		RULE_rankingWindowedFunc = 51, RULE_aggregateWindowedFunc = 52, RULE_analyticWindowedFunc = 53, 
		RULE_allOrDistinctExpression = 54, RULE_overClause = 55, RULE_timeUnit = 56, 
		RULE_intervalUnit = 57, RULE_intervalExpression = 58, RULE_intervalTemporal = 59, 
		RULE_dataType = 60, RULE_constant = 61, RULE_sign = 62, RULE_keyword = 63, 
		RULE_id = 64, RULE_comparisonOperator = 65, RULE_bitOperator = 66, RULE_assignmentOperator = 67;
	private static String[] makeRuleNames() {
		return new String[] {
			"sqlQueryWithCte", "sqlQuery", "select", "from", "where", "groupBy", 
			"orderBy", "limitOffset", "tableSource", "tableSourceItemJoined", "tableSourceItem", 
			"joinClause", "criteria", "predicate", "subQueryOrValuesClause", "valuesClause", 
			"expressionList", "selectExprList", "selectExpr", "selectAllStar", "selectColumn", 
			"fullColumnName", "selectExpression", "sortExpression", "schemaObject", 
			"asTableAlias", "asColumnAlias", "columnAlias", "columnAliasList", "expression", 
			"primitiveExpression", "caseWhenExpression", "unaryOpExpression", "bracketExpression", 
			"constantExpression", "subquery", "withCteClause", "commonTableExpression", 
			"functionCall", "builtInFunc", "scalarFunc", "xmlDataTypeFunc", "xmlValueFunc", 
			"xmlValueCall", "xmlQueryFunc", "xmlQueryCall", "xmlExistFunc", "xmlExistCall", 
			"xmlModifyFunc", "xmlModifyCall", "timeZone", "rankingWindowedFunc", 
			"aggregateWindowedFunc", "analyticWindowedFunc", "allOrDistinctExpression", 
			"overClause", "timeUnit", "intervalUnit", "intervalExpression", "intervalTemporal", 
			"dataType", "constant", "sign", "keyword", "id", "comparisonOperator", 
			"bitOperator", "assignmentOperator"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'BOOL'", "'TINYINT'", "'SMALLINT'", "'INT'", "'INTEGER'", "'BIGINT'", 
			"'UNSIGNED'", "'DECIMAL'", "'NUMERIC'", "'MONEY'", "'FLOAT'", "'REAL'", 
			"'DOUBLE'", "'CHAR'", "'NCHAR'", "'VARCHAR'", "'NVARCHAR'", "'UUID'", 
			"'BIT'", "'CLOB'", "'NCLOB'", "'TEXT'", "'NTEXT'", "'JSON'", "'XML'", 
			"'DATE'", "'TIME'", "'DATETIME'", "'TIMESTAMP'", "'INTERVAL'", "'VECTOR'", 
			"'BIT_VECTOR'", "'BINARY'", "'VARBINARY'", "'BLOB'", "'BFILE'", "'GEOGRAPHY'", 
			"'GEOMETRY'", "'ANY'", "'ADD'", "'ALL'", "'ALTER'", "'AND'", "'AS'", 
			"'ASC'", "'\\'", "'BACKUP'", "'BEGIN'", "'BETWEEN'", "'BREAK'", "'BULK'", 
			"'BY'", "'CASCADE'", "'CASE'", "'CHECK'", "'CLOSE'", "'COALESCE'", "'COLLATE'", 
			"'COLUMN'", "'COMMIT'", "'COMPUTE'", "'CONSTRAINT'", "'CONTAINS'", "'CONTINUE'", 
			null, "'CREATE'", "'CROSS'", "'CURRENT'", "'CURRENT_DATE'", "'CURRENT_TIME'", 
			"'CURRENT_TIMESTAMP'", "'CURRENT_USER'", "'CURSOR'", "'DATABASE'", "'DEALLOCATE'", 
			"'DECLARE'", "'DEFAULT'", "'DELETE'", "'DENY'", "'DESC'", "'DISK'", "'DISTINCT'", 
			"'\\\\'", "'//'", "'DROP'", "'DUMP'", "'ELSE'", "'END'", "'ESCAPE'", 
			"'EXCEPT'", null, "'EXISTS'", "'EXIT'", "'FETCH'", "'FILE'", "'FOR'", 
			"'FOREIGN'", "'FROM'", "'FULL'", "'FUNCTION'", "'GRANT'", "'GROUP'", 
			"'HAVING'", "'IDENTITY'", "'IF'", "'IN'", "'INDEX'", "'INNER'", "'INSERT'", 
			"'INTERSECT'", "'INTO'", "'IS'", "'JOIN'", "'KEY'", "'LEFT'", "'LIKE'", 
			"'LOAD'", "'MERGE'", "'NATIONAL'", "'NOT'", "'NULL'", "'NULLIF'", "'NULLS'", 
			"'OF'", "'OFF'", "'OFFSET'", "'OFFSETS'", "'ON'", "'OPEN'", "'OPTION'", 
			"'OR'", "'ORDER'", "'OUTER'", "'OVER'", "'PERCENT'", "'PRECISION'", "'PRIMARY'", 
			"'PROC'", "'PROCEDURE'", "'PUBLIC'", "'RAW'", "'READ'", "'REFERENCES'", 
			"'REPLICATION'", "'RESTORE'", "'RESTRICT'", "'RETURN'", "'REVERT'", "'REVOKE'", 
			"'RIGHT'", "'ROLLBACK'", "'SCHEMA'", "'SELECT'", "'SET'", "'SHUTDOWN'", 
			"'SOME'", "'STATISTICS'", "'SYSTEM_USER'", "'TABLE'", "'THEN'", "'TO'", 
			"'TOP'", "'TRAN'", "'TRANSACTION'", "'TRIGGER'", "'TRUE'", "'FALSE'", 
			"'TRUNCATE'", "'UNION'", "'UNIQUE'", "'UPDATE'", "'USE'", "'USER'", "'VALUES'", 
			"'VARYING'", "'VIEW'", "'WHEN'", "'WHERE'", "'WHILE'", "'WITH'", "'WITHIN'", 
			"'$PARTITION'", "'ABSOLUTE'", "'AT'", "'ACTION'", "'ACTIVE'", "'AFTER'", 
			"'AGGREGATE'", "'ALGORITHM'", "'AUTHORIZATION'", "'AVG'", "'BINARY BASE64'", 
			"'BINARY_CHECKSUM'", "'BINDING'", "'BLOB_STORAGE'", "'CAST'", "'CATALOG'", 
			"'CATCH'", "'CHANGE'", "'CHECKSUM'", "'COLLECTION'", "'COMMITTED'", "'CONCAT'", 
			"'COUNT'", "'CUME_DIST'", "'DATA'", "'DATEADD'", "'DATEDIFF'", "'DATENAME'", 
			"'DATEPART'", "'DAY'", "'DAYS'", null, "'DEFAULT_LANGUAGE'", "'DEFINITION'", 
			"'DELETED'", "'DENSE_RANK'", "'DES'", "'DESCRIPTION'", "'DESX'", "'DISABLE'", 
			null, "'DYNAMIC'", "'EMPTY'", "'ENABLE'", "'ENCRYPTION'", "'EXIST'", 
			"'EXPLICIT'", "'FAST'", "'FAST_FORWARD'", "'FILTER'", "'FIRST'", "'FIRST_VALUE'", 
			"'FOLLOWING'", "'FORCE'", "'FORMAT'", "'FORWARD_ONLY'", "'FULLTEXT'", 
			"'GLOBAL'", "'GROUPING'", "'GROUPING_ID'", "'HASH'", "'HOUR'", "'HOURS'", 
			"'INPUT'", "'INSENSITIVE'", "'INSERTED'", "'ISOLATION'", "'JOB'", "'KEYS'", 
			"'KEYSET'", "'LAG'", "'LAST'", "'LAST_VALUE'", "'LEAD'", "'LEVEL'", "'LIST'", 
			"'LOCAL'", "'LOCK'", "'LOGIN'", "'LOOP'", "'MATERIALIZED'", "'MAX'", 
			"'MEDIUM'", "'MIN'", "'MINUTE'", "'MINUTES'", "'MICROSECOND'", "'MICROSECONDS'", 
			"'MILLISECOND'", "'MILLISECONDS'", "'NANOSECOND'", "'NANOSECONDS'", "'YEAR_MONTH'", 
			"'DAY_HOUR'", "'DAY_MINUTE'", "'DAY_SECOND'", "'DAY_MICROSECOND'", "'HOUR_MINUTE'", 
			"'HOUR_SECOND'", "'HOUR_MICROSECOND'", "'MINUTE_SECOND'", "'MINUTE_MICROSECOND'", 
			"'SECOND_MICROSECOND'", "'MODE'", "'MODIFY'", "'MONTH'", "'MONTHS'", 
			"'MOVE'", "'NAME'", "'NEXT'", "'NO'", "'NUMBER'", "'OBJECT'", "'ONLY'", 
			"'OPTIMISTIC'", "'OPTIMIZE'", "'OUT'", "'OUTPUT'", "'OWNER'", "'PARTITION'", 
			"'PARTITIONS'", "'PATH'", "'PERCENT_RANK'", "'PERCENTILE_CONT'", "'PERCENTILE_DISC'", 
			"'POOL'", "'PORT'", "'PRECEDING'", "'PRIOR'", "'PRIORITY'", "'PRIVATE'", 
			"'PRIVILEGES'", "'PROCEDURE_NAME'", "'PROPERTY'", "'PROVIDER'", "'PROVIDER_KEY_NAME'", 
			"'QUARTER'", "'QUARTERS'", "'QUERY'", "'QUOTED_IDENTIFIER'", "'RANGE'", 
			"'RANK'", "'RC2'", "'RC4'", "'RC4_128'", "'READ_ONLY'", "'READ_WRITE'", 
			"'READONLY'", "'READWRITE'", "'REBUILD'", "'RECEIVE'", "'RECOMPILE'", 
			"'RECOVERY'", "'RELATIVE'", "'REMOTE'", "'REMOVE'", "'REPEATABLE'", "'REPLICA'", 
			"'RESOURCE'", "'ROOT'", "'ROW'", "'ROW_NUMBER'", "'ROWS'", "'SCOPED'", 
			"'SCROLL'", "'SECOND'", "'SECONDS'", "'SECURITY'", "'SELF'", "'SEMI_SENSITIVE'", 
			"'SEND'", "'SENT'", "'SERIALIZABLE'", "'SHARE'", "'SIMPLE'", "'SIZE'", 
			"'SNAPSHOT'", "'STANDBY'", "'STATIC'", "'STATUS'", "'STDEV'", "'STDEVP'", 
			"'SUBJECT'", "'SUM'", "'SUSPEND'", "'SYSTEM'", "'THROW'", "'TIMEOUT'", 
			"'TIMER'", "'TRY'", "'TYPE'", "'UNBOUNDED'", "'UNCOMMITTED'", "'UNKNOWN'", 
			"'UNLIMITED'", "'UNMASK'", "'USING'", "'VALIDATION'", "'VALUE'", "'VAR'", 
			"'VIEWS'", "'WORK'", "'YEAR'", "'YEARS'", "'ZONE'", "'$ACTION'", "'BEFORE'", 
			"'BLOCK'", "'BUFFER'", "'CACHE'", "'COMPRESSION'", "'CONNECT'", "'CONNECTION'", 
			"'CONTEXT'", "'DDL'", "'DISTRIBUTION'", "'ENABLED'", "'ENDPOINT'", "'ERROR'", 
			"'EVENT'", "'GET'", "'HEAP'", "'IIF'", "'IO'", "'INCLUDE'", "'INCREMENT'", 
			"'INFINITE'", "'INIT'", "'INSTEAD'", "'ISNULL'", "'LANGUAGE'", "'LIBRARY'", 
			"'LIFETIME'", "'LINKED'", "'LINUX'", "'LOG'", "'MASK'", "'MATCHED'", 
			"'NONE'", "'PAGE'", "'PERSISTED'", "'REPLICATE'", "'REQUIRED'", "'RESET'", 
			"'RESOURCES'", "'RESTART'", "'RESUME'", "'RETURNS'", "'ROLE'", "'SAFETY'", 
			"'SAFE'", "'SCHEDULER'", "'SCHEME'", "'SCRIPT'", "'SERVER'", "'SERVICE'", 
			"'SKIP'", "'SOURCE'", "'SPECIFICATION'", "'SPLIT'", "'SQL'", "'STATE'", 
			"'STATS'", "'START'", "'STARTED'", "'STOP'", "'STOPPED'", "'SWITCH'", 
			"'TARGET'", "'TCP'", "'TRACE'", "'TRANSFER'", "'UNCHECKED'", "'UNLOCK'", 
			"'UNSAFE'", "'URL'", "'USED'", "'VISIBILITY'", "'WINDOWS'", "'WITHOUT'", 
			"'WITNESS'", "'XACT_ABORT'", null, null, null, null, null, "'''", null, 
			null, null, null, null, null, null, null, null, "'='", "'>'", "'<'", 
			"'!'", "'+='", "'-='", "'*='", "'/='", "'%='", "'&='", "'^='", "'|='", 
			"'||'", "'.'", "'_'", "'@'", "'#'", "'$'", "'('", "')'", "','", "';'", 
			"':'", "'::'", "'*'", "'/'", "'%'", "'+'", "'-'", "'~'", "'|'", "'&'", 
			"'^'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BOOL", "TINYINT", "SMALLINT", "INT", "INTEGER", "BIGINT", "UNSIGNED", 
			"DECIMAL", "NUMERIC", "MONEY", "FLOAT", "REAL", "DOUBLE", "CHAR", "NCHAR", 
			"VARCHAR", "NVARCHAR", "UUID", "BIT", "CLOB", "NCLOB", "TEXT", "NTEXT", 
			"JSON", "XML", "DATE", "TIME", "DATETIME", "TIMESTAMP", "INTERVAL", "VECTOR", 
			"BIT_VECTOR", "BINARY", "VARBINARY", "BLOB", "BFILE", "GEOGRAPHY", "GEOMETRY", 
			"ANY", "ADD", "ALL", "ALTER", "AND", "AS", "ASC", "BACKSLASH", "BACKUP", 
			"BEGIN", "BETWEEN", "BREAK", "BULK", "BY", "CASCADE", "CASE", "CHECK", 
			"CLOSE", "COALESCE", "COLLATE", "COLUMN", "COMMIT", "COMPUTE", "CONSTRAINT", 
			"CONTAINS", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", 
			"CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", 
			"DEALLOCATE", "DECLARE", "DEFAULT", "DELETE", "DENY", "DESC", "DISK", 
			"DISTINCT", "DOUBLE_BACK_SLASH", "DOUBLE_FORWARD_SLASH", "DROP", "DUMP", 
			"ELSE", "END", "ESCAPE", "EXCEPT", "EXECUTE", "EXISTS", "EXIT", "FETCH", 
			"FILE", "FOR", "FOREIGN", "FROM", "FULL", "FUNCTION", "GRANT", "GROUP", 
			"HAVING", "IDENTITY", "IF", "IN", "INDEX", "INNER", "INSERT", "INTERSECT", 
			"INTO", "IS", "JOIN", "KEY", "LEFT", "LIKE", "LOAD", "MERGE", "NATIONAL", 
			"NOT", "NULL_", "NULLIF", "NULLS", "OF", "OFF", "OFFSET", "OFFSETS", 
			"ON", "OPEN", "OPTION", "OR", "ORDER", "OUTER", "OVER", "PERCENT", "PRECISION", 
			"PRIMARY", "PROC", "PROCEDURE", "PUBLIC", "RAW", "READ", "REFERENCES", 
			"REPLICATION", "RESTORE", "RESTRICT", "RETURN", "REVERT", "REVOKE", "RIGHT", 
			"ROLLBACK", "SCHEMA", "SELECT", "SET", "SHUTDOWN", "SOME", "STATISTICS", 
			"SYSTEM_USER", "TABLE", "THEN", "TO", "TOP", "TRAN", "TRANSACTION", "TRIGGER", 
			"TRUE", "FALSE", "TRUNCATE", "UNION", "UNIQUE", "UPDATE", "USE", "USER", 
			"VALUES", "VARYING", "VIEW", "WHEN", "WHERE", "WHILE", "WITH", "WITHIN", 
			"DOLLAR_PARTITION", "ABSOLUTE", "AT_KEYWORD", "ACTION", "ACTIVE", "AFTER", 
			"AGGREGATE", "ALGORITHM", "AUTHORIZATION", "AVG", "BINARY_BASE64", "BINARY_CHECKSUM", 
			"BINDING", "BLOB_STORAGE", "CAST", "CATALOG", "CATCH", "CHANGE", "CHECKSUM", 
			"COLLECTION", "COMMITTED", "CONCAT", "COUNT", "CUME_DIST", "DATA", "DATEADD", 
			"DATEDIFF", "DATENAME", "DATEPART", "DAY", "DAYS", "DEFAULT_DOUBLE_QUOTE", 
			"DEFAULT_LANGUAGE", "DEFINITION", "DELETED", "DENSE_RANK", "DES", "DESCRIPTION", 
			"DESX", "DISABLE", "DISK_DRIVE", "DYNAMIC", "EMPTY", "ENABLE", "ENCRYPTION", 
			"EXIST", "EXPLICIT", "FAST", "FAST_FORWARD", "FILTER", "FIRST", "FIRST_VALUE", 
			"FOLLOWING", "FORCE", "FORMAT", "FORWARD_ONLY", "FULLTEXT", "GLOBAL", 
			"GROUPING", "GROUPING_ID", "HASH", "HOUR", "HOURS", "INPUT", "INSENSITIVE", 
			"INSERTED", "ISOLATION", "JOB", "KEYS", "KEYSET", "LAG", "LAST", "LAST_VALUE", 
			"LEAD", "LEVEL", "LIST", "LOCAL", "LOCK", "LOGIN", "LOOP", "MATERIALIZED", 
			"MAX", "MEDIUM", "MIN", "MINUTE", "MINUTES", "MICROSECOND", "MICROSECONDS", 
			"MILLISECOND", "MILLISECONDS", "NANOSECOND", "NANOSECONDS", "YEAR_MONTH", 
			"DAY_HOUR", "DAY_MINUTE", "DAY_SECOND", "DAY_MICROSECOND", "HOUR_MINUTE", 
			"HOUR_SECOND", "HOUR_MICROSECOND", "MINUTE_SECOND", "MINUTE_MICROSECOND", 
			"SECOND_MICROSECOND", "MODE", "MODIFY", "MONTH", "MONTHS", "MOVE", "NAME", 
			"NEXT", "NO", "NUMBER", "OBJECT", "ONLY", "OPTIMISTIC", "OPTIMIZE", "OUT", 
			"OUTPUT", "OWNER", "PARTITION", "PARTITIONS", "PATH", "PERCENT_RANK", 
			"PERCENTILE_CONT", "PERCENTILE_DISC", "POOL", "PORT", "PRECEDING", "PRIOR", 
			"PRIORITY", "PRIVATE", "PRIVILEGES", "PROCEDURE_NAME", "PROPERTY", "PROVIDER", 
			"PROVIDER_KEY_NAME", "QUARTER", "QUARTERS", "QUERY", "QUOTED_IDENTIFIER", 
			"RANGE", "RANK", "RC2", "RC4", "RC4_128", "READ_ONLY", "READ_WRITE", 
			"READONLY", "READWRITE", "REBUILD", "RECEIVE", "RECOMPILE", "RECOVERY", 
			"RELATIVE", "REMOTE", "REMOVE", "REPEATABLE", "REPLICA", "RESOURCE", 
			"ROOT", "ROW", "ROW_NUMBER", "ROWS", "SCOPED", "SCROLL", "SECOND", "SECONDS", 
			"SECURITY", "SELF", "SEMI_SENSITIVE", "SEND", "SENT", "SERIALIZABLE", 
			"SHARE", "SIMPLE", "SIZE", "SNAPSHOT", "STANDBY", "STATIC", "STATUS", 
			"STDEV", "STDEVP", "SUBJECT", "SUM", "SUSPEND", "SYSTEM", "THROW", "TIMEOUT", 
			"TIMER", "TRY", "TYPE", "UNBOUNDED", "UNCOMMITTED", "UNKNOWN", "UNLIMITED", 
			"UNMASK", "USING", "VALIDATION", "VALUE", "VAR", "VIEWS", "WORK", "YEAR", 
			"YEARS", "ZONE", "DOLLAR_ACTION", "BEFORE", "BLOCK", "BUFFER", "CACHE", 
			"COMPRESSION", "CONNECT", "CONNECTION", "CONTEXT", "DDL", "DISTRIBUTION", 
			"ENABLED", "ENDPOINT", "ERROR", "EVENT", "GET", "HEAP", "IIF", "IO", 
			"INCLUDE", "INCREMENT", "INFINITE", "INIT", "INSTEAD", "ISNULL", "LANGUAGE", 
			"LIBRARY", "LIFETIME", "LINKED", "LINUX", "LOG", "MASK", "MATCHED", "NONE", 
			"PAGE", "PERSISTED", "REPLICATE", "REQUIRED", "RESET", "RESOURCES", "RESTART", 
			"RESUME", "RETURNS", "ROLE", "SAFETY", "SAFE", "SCHEDULER", "SCHEME", 
			"SCRIPT", "SERVER", "SERVICE", "SKIP_KEYWORD", "SOURCE", "SPECIFICATION", 
			"SPLIT", "SQL", "STATE", "STATS", "START", "STARTED", "STOP", "STOPPED", 
			"SWITCH", "TARGET", "TCP", "TRACE", "TRANSFER", "UNCHECKED", "UNLOCK", 
			"UNSAFE", "URL", "USED", "VISIBILITY", "WINDOWS", "WITHOUT", "WITNESS", 
			"XACT_ABORT", "SPACE", "COMMENT", "LINE_COMMENT", "BACK_QUOTE_ID", "DOUBLE_QUOTE_ID", 
			"SINGLE_QUOTE", "SQUARE_BRACKET_ID", "LOCAL_ID", "DEC_DIGITS", "ID", 
			"STRING", "BIT_STR", "HEX_NUMBER", "DEC_NUMBER", "REAL_NUMBER", "EQUAL", 
			"GREATER", "LESS", "EXCLAMATION", "PLUS_ASSIGN", "MINUS_ASSIGN", "MULT_ASSIGN", 
			"DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "XOR_ASSIGN", "OR_ASSIGN", 
			"DOUBLE_BAR", "DOT", "UNDERLINE", "AT", "SHARP", "DOLLAR", "LR_BRACKET", 
			"RR_BRACKET", "COMMA", "SEMI", "COLON", "DOUBLE_COLON", "STAR", "DIVIDE", 
			"MODULE", "PLUS", "MINUS", "BIT_NOT", "BIT_OR", "BIT_AND", "BIT_XOR", 
			"LIMIT", "WEEK", "WEEKS", "DEC_DIGIT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SqlQueryParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SqlQueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlQueryWithCteContext extends ParserRuleContext {
		public SqlQueryContext sqlQuery() {
			return getRuleContext(SqlQueryContext.class,0);
		}
		public WithCteClauseContext withCteClause() {
			return getRuleContext(WithCteClauseContext.class,0);
		}
		public SqlQueryWithCteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlQueryWithCte; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSqlQueryWithCte(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSqlQueryWithCte(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSqlQueryWithCte(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlQueryWithCteContext sqlQueryWithCte() throws RecognitionException {
		SqlQueryWithCteContext _localctx = new SqlQueryWithCteContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_sqlQueryWithCte);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(136);
				withCteClause();
				}
			}

			setState(139);
			sqlQuery();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlQueryContext extends ParserRuleContext {
		public SelectContext select() {
			return getRuleContext(SelectContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public GroupByContext groupBy() {
			return getRuleContext(GroupByContext.class,0);
		}
		public OrderByContext orderBy() {
			return getRuleContext(OrderByContext.class,0);
		}
		public LimitOffsetContext limitOffset() {
			return getRuleContext(LimitOffsetContext.class,0);
		}
		public SqlQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSqlQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSqlQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSqlQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlQueryContext sqlQuery() throws RecognitionException {
		SqlQueryContext _localctx = new SqlQueryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sqlQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			select();
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(142);
				from();
				}
			}

			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(145);
				where();
				}
			}

			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GROUP) {
				{
				setState(148);
				groupBy();
				}
			}

			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(151);
				orderBy();
				}
			}

			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OFFSET || _la==LIMIT) {
				{
				setState(154);
				limitOffset();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(SqlQueryParser.SELECT, 0); }
		public SelectExprListContext selectExprList() {
			return getRuleContext(SelectExprListContext.class,0);
		}
		public TerminalNode DISTINCT() { return getToken(SqlQueryParser.DISTINCT, 0); }
		public SelectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectContext select() throws RecognitionException {
		SelectContext _localctx = new SelectContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_select);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(SELECT);
			setState(159);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(158);
				match(DISTINCT);
				}
				break;
			}
			setState(161);
			selectExprList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FromContext extends ParserRuleContext {
		public TableSourceContext tableSource;
		public List<TableSourceContext> source = new ArrayList<TableSourceContext>();
		public TerminalNode FROM() { return getToken(SqlQueryParser.FROM, 0); }
		public List<TableSourceContext> tableSource() {
			return getRuleContexts(TableSourceContext.class);
		}
		public TableSourceContext tableSource(int i) {
			return getRuleContext(TableSourceContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public FromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_from; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromContext from() throws RecognitionException {
		FromContext _localctx = new FromContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(FROM);
			setState(164);
			((FromContext)_localctx).tableSource = tableSource();
			((FromContext)_localctx).source.add(((FromContext)_localctx).tableSource);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(165);
				match(COMMA);
				setState(166);
				((FromContext)_localctx).tableSource = tableSource();
				((FromContext)_localctx).source.add(((FromContext)_localctx).tableSource);
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereContext extends ParserRuleContext {
		public CriteriaContext condition;
		public TerminalNode WHERE() { return getToken(SqlQueryParser.WHERE, 0); }
		public CriteriaContext criteria() {
			return getRuleContext(CriteriaContext.class,0);
		}
		public WhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereContext where() throws RecognitionException {
		WhereContext _localctx = new WhereContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_where);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(WHERE);
			setState(173);
			((WhereContext)_localctx).condition = criteria(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupByContext extends ParserRuleContext {
		public ExpressionContext expression;
		public List<ExpressionContext> groupByExpr = new ArrayList<ExpressionContext>();
		public CriteriaContext havingFilter;
		public TerminalNode GROUP() { return getToken(SqlQueryParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(SqlQueryParser.BY, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode HAVING() { return getToken(SqlQueryParser.HAVING, 0); }
		public CriteriaContext criteria() {
			return getRuleContext(CriteriaContext.class,0);
		}
		public GroupByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterGroupBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitGroupBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitGroupBy(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByContext groupBy() throws RecognitionException {
		GroupByContext _localctx = new GroupByContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_groupBy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			match(GROUP);
			setState(176);
			match(BY);
			setState(177);
			((GroupByContext)_localctx).expression = expression(0);
			((GroupByContext)_localctx).groupByExpr.add(((GroupByContext)_localctx).expression);
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(178);
				match(COMMA);
				setState(179);
				((GroupByContext)_localctx).expression = expression(0);
				((GroupByContext)_localctx).groupByExpr.add(((GroupByContext)_localctx).expression);
				}
				}
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(187);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HAVING) {
				{
				setState(185);
				match(HAVING);
				setState(186);
				((GroupByContext)_localctx).havingFilter = criteria(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderByContext extends ParserRuleContext {
		public SortExpressionContext sortExpression;
		public List<SortExpressionContext> orderByExpr = new ArrayList<SortExpressionContext>();
		public TerminalNode ORDER() { return getToken(SqlQueryParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(SqlQueryParser.BY, 0); }
		public List<SortExpressionContext> sortExpression() {
			return getRuleContexts(SortExpressionContext.class);
		}
		public SortExpressionContext sortExpression(int i) {
			return getRuleContext(SortExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public OrderByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterOrderBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitOrderBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitOrderBy(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByContext orderBy() throws RecognitionException {
		OrderByContext _localctx = new OrderByContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_orderBy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			match(ORDER);
			setState(190);
			match(BY);
			setState(191);
			((OrderByContext)_localctx).sortExpression = sortExpression();
			((OrderByContext)_localctx).orderByExpr.add(((OrderByContext)_localctx).sortExpression);
			setState(196);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(192);
				match(COMMA);
				setState(193);
				((OrderByContext)_localctx).sortExpression = sortExpression();
				((OrderByContext)_localctx).orderByExpr.add(((OrderByContext)_localctx).sortExpression);
				}
				}
				setState(198);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LimitOffsetContext extends ParserRuleContext {
		public ExpressionContext limitRows;
		public ExpressionContext offsetStart;
		public TerminalNode LIMIT() { return getToken(SqlQueryParser.LIMIT, 0); }
		public TerminalNode OFFSET() { return getToken(SqlQueryParser.OFFSET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public List<TerminalNode> ROW() { return getTokens(SqlQueryParser.ROW); }
		public TerminalNode ROW(int i) {
			return getToken(SqlQueryParser.ROW, i);
		}
		public List<TerminalNode> ROWS() { return getTokens(SqlQueryParser.ROWS); }
		public TerminalNode ROWS(int i) {
			return getToken(SqlQueryParser.ROWS, i);
		}
		public TerminalNode FETCH() { return getToken(SqlQueryParser.FETCH, 0); }
		public TerminalNode ONLY() { return getToken(SqlQueryParser.ONLY, 0); }
		public TerminalNode FIRST() { return getToken(SqlQueryParser.FIRST, 0); }
		public TerminalNode NEXT() { return getToken(SqlQueryParser.NEXT, 0); }
		public LimitOffsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitOffset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterLimitOffset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitLimitOffset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitLimitOffset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitOffsetContext limitOffset() throws RecognitionException {
		LimitOffsetContext _localctx = new LimitOffsetContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_limitOffset);
		int _la;
		try {
			setState(220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(199);
				match(LIMIT);
				setState(200);
				((LimitOffsetContext)_localctx).limitRows = expression(0);
				setState(201);
				match(OFFSET);
				setState(202);
				((LimitOffsetContext)_localctx).offsetStart = expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
				match(LIMIT);
				setState(205);
				((LimitOffsetContext)_localctx).offsetStart = expression(0);
				setState(206);
				match(COMMA);
				setState(207);
				((LimitOffsetContext)_localctx).limitRows = expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(209);
				match(OFFSET);
				setState(210);
				((LimitOffsetContext)_localctx).offsetStart = expression(0);
				setState(211);
				_la = _input.LA(1);
				if ( !(_la==ROW || _la==ROWS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(218);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FETCH) {
					{
					setState(212);
					match(FETCH);
					setState(213);
					_la = _input.LA(1);
					if ( !(_la==FIRST || _la==NEXT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(214);
					((LimitOffsetContext)_localctx).limitRows = expression(0);
					setState(215);
					_la = _input.LA(1);
					if ( !(_la==ROW || _la==ROWS) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(216);
					match(ONLY);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableSourceContext extends ParserRuleContext {
		public TableSourceItemJoinedContext tableSourceItemJoined() {
			return getRuleContext(TableSourceItemJoinedContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TableSourceContext tableSource() {
			return getRuleContext(TableSourceContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TableSourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableSource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterTableSource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitTableSource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitTableSource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableSourceContext tableSource() throws RecognitionException {
		TableSourceContext _localctx = new TableSourceContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_tableSource);
		try {
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(222);
				tableSourceItemJoined();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(223);
				match(LR_BRACKET);
				setState(224);
				tableSource();
				setState(225);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableSourceItemJoinedContext extends ParserRuleContext {
		public JoinClauseContext joinClause;
		public List<JoinClauseContext> joins = new ArrayList<JoinClauseContext>();
		public TableSourceItemContext tableSourceItem() {
			return getRuleContext(TableSourceItemContext.class,0);
		}
		public List<JoinClauseContext> joinClause() {
			return getRuleContexts(JoinClauseContext.class);
		}
		public JoinClauseContext joinClause(int i) {
			return getRuleContext(JoinClauseContext.class,i);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TableSourceItemJoinedContext tableSourceItemJoined() {
			return getRuleContext(TableSourceItemJoinedContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TableSourceItemJoinedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableSourceItemJoined; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterTableSourceItemJoined(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitTableSourceItemJoined(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitTableSourceItemJoined(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableSourceItemJoinedContext tableSourceItemJoined() throws RecognitionException {
		TableSourceItemJoinedContext _localctx = new TableSourceItemJoinedContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_tableSourceItemJoined);
		try {
			int _alt;
			setState(245);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(229);
				tableSourceItem();
				setState(233);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(230);
						((TableSourceItemJoinedContext)_localctx).joinClause = joinClause();
						((TableSourceItemJoinedContext)_localctx).joins.add(((TableSourceItemJoinedContext)_localctx).joinClause);
						}
						} 
					}
					setState(235);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(236);
				match(LR_BRACKET);
				setState(237);
				tableSourceItemJoined();
				setState(238);
				match(RR_BRACKET);
				setState(242);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(239);
						((TableSourceItemJoinedContext)_localctx).joinClause = joinClause();
						((TableSourceItemJoinedContext)_localctx).joins.add(((TableSourceItemJoinedContext)_localctx).joinClause);
						}
						} 
					}
					setState(244);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableSourceItemContext extends ParserRuleContext {
		public Token locId;
		public Token locIdCall;
		public FunctionCallContext locFuncCall;
		public SchemaObjectContext schemaObject() {
			return getRuleContext(SchemaObjectContext.class,0);
		}
		public AsTableAliasContext asTableAlias() {
			return getRuleContext(AsTableAliasContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubQueryOrValuesClauseContext subQueryOrValuesClause() {
			return getRuleContext(SubQueryOrValuesClauseContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public ColumnAliasListContext columnAliasList() {
			return getRuleContext(ColumnAliasListContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public TableSourceItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableSourceItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterTableSourceItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitTableSourceItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitTableSourceItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableSourceItemContext tableSourceItem() throws RecognitionException {
		TableSourceItemContext _localctx = new TableSourceItemContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_tableSourceItem);
		int _la;
		try {
			setState(280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				schemaObject();
				setState(249);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
				case 1:
					{
					setState(248);
					asTableAlias();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(251);
				match(LR_BRACKET);
				setState(252);
				subQueryOrValuesClause();
				setState(253);
				match(RR_BRACKET);
				setState(258);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(254);
					asTableAlias();
					setState(256);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LR_BRACKET) {
						{
						setState(255);
						columnAliasList();
						}
					}

					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(260);
				functionCall();
				setState(265);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(261);
					asTableAlias();
					setState(263);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LR_BRACKET) {
						{
						setState(262);
						columnAliasList();
						}
					}

					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(267);
				((TableSourceItemContext)_localctx).locId = match(LOCAL_ID);
				setState(269);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(268);
					asTableAlias();
					}
					break;
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(271);
				((TableSourceItemContext)_localctx).locIdCall = match(LOCAL_ID);
				setState(272);
				match(DOT);
				setState(273);
				((TableSourceItemContext)_localctx).locFuncCall = functionCall();
				setState(278);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(274);
					asTableAlias();
					setState(276);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LR_BRACKET) {
						{
						setState(275);
						columnAliasList();
						}
					}

					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JoinClauseContext extends ParserRuleContext {
		public Token inner;
		public Token joinType;
		public Token outer;
		public TableSourceContext joinTo;
		public CriteriaContext joinOn;
		public TerminalNode JOIN() { return getToken(SqlQueryParser.JOIN, 0); }
		public TerminalNode ON() { return getToken(SqlQueryParser.ON, 0); }
		public TableSourceContext tableSource() {
			return getRuleContext(TableSourceContext.class,0);
		}
		public CriteriaContext criteria() {
			return getRuleContext(CriteriaContext.class,0);
		}
		public TerminalNode LEFT() { return getToken(SqlQueryParser.LEFT, 0); }
		public TerminalNode RIGHT() { return getToken(SqlQueryParser.RIGHT, 0); }
		public TerminalNode FULL() { return getToken(SqlQueryParser.FULL, 0); }
		public TerminalNode INNER() { return getToken(SqlQueryParser.INNER, 0); }
		public TerminalNode OUTER() { return getToken(SqlQueryParser.OUTER, 0); }
		public TerminalNode CROSS() { return getToken(SqlQueryParser.CROSS, 0); }
		public JoinClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterJoinClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitJoinClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitJoinClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinClauseContext joinClause() throws RecognitionException {
		JoinClauseContext _localctx = new JoinClauseContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_joinClause);
		int _la;
		try {
			setState(299);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FULL:
			case INNER:
			case JOIN:
			case LEFT:
			case RIGHT:
				enterOuterAlt(_localctx, 1);
				{
				setState(289);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(283);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==INNER) {
						{
						setState(282);
						((JoinClauseContext)_localctx).inner = match(INNER);
						}
					}

					}
					break;
				case 2:
					{
					setState(285);
					((JoinClauseContext)_localctx).joinType = _input.LT(1);
					_la = _input.LA(1);
					if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & 2251799813750785L) != 0)) ) {
						((JoinClauseContext)_localctx).joinType = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(287);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(286);
						((JoinClauseContext)_localctx).outer = match(OUTER);
						}
					}

					}
					break;
				}
				setState(291);
				match(JOIN);
				setState(292);
				((JoinClauseContext)_localctx).joinTo = tableSource();
				setState(293);
				match(ON);
				setState(294);
				((JoinClauseContext)_localctx).joinOn = criteria(0);
				}
				break;
			case CROSS:
				enterOuterAlt(_localctx, 2);
				{
				setState(296);
				match(CROSS);
				setState(297);
				match(JOIN);
				setState(298);
				tableSource();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CriteriaContext extends ParserRuleContext {
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<CriteriaContext> criteria() {
			return getRuleContexts(CriteriaContext.class);
		}
		public CriteriaContext criteria(int i) {
			return getRuleContext(CriteriaContext.class,i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode NOT() { return getToken(SqlQueryParser.NOT, 0); }
		public TerminalNode AND() { return getToken(SqlQueryParser.AND, 0); }
		public TerminalNode OR() { return getToken(SqlQueryParser.OR, 0); }
		public CriteriaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_criteria; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCriteria(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCriteria(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCriteria(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CriteriaContext criteria() throws RecognitionException {
		return criteria(0);
	}

	private CriteriaContext criteria(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CriteriaContext _localctx = new CriteriaContext(_ctx, _parentState);
		CriteriaContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_criteria, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(302);
				match(NOT);
				}
				break;
			}
			setState(310);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(305);
				predicate();
				}
				break;
			case 2:
				{
				setState(306);
				match(LR_BRACKET);
				setState(307);
				criteria(0);
				setState(308);
				match(RR_BRACKET);
				}
				break;
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(320);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(318);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
					case 1:
						{
						_localctx = new CriteriaContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_criteria);
						setState(312);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(313);
						match(AND);
						setState(314);
						criteria(3);
						}
						break;
					case 2:
						{
						_localctx = new CriteriaContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_criteria);
						setState(315);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(316);
						match(OR);
						setState(317);
						criteria(2);
						}
						break;
					}
					} 
				}
				setState(322);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateContext extends ParserRuleContext {
		public TerminalNode EXISTS() { return getToken(SqlQueryParser.EXISTS, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode NOT() { return getToken(SqlQueryParser.NOT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}
		public TerminalNode ALL() { return getToken(SqlQueryParser.ALL, 0); }
		public TerminalNode SOME() { return getToken(SqlQueryParser.SOME, 0); }
		public TerminalNode ANY() { return getToken(SqlQueryParser.ANY, 0); }
		public TerminalNode BETWEEN() { return getToken(SqlQueryParser.BETWEEN, 0); }
		public TerminalNode AND() { return getToken(SqlQueryParser.AND, 0); }
		public TerminalNode IN() { return getToken(SqlQueryParser.IN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(SqlQueryParser.LIKE, 0); }
		public TerminalNode ESCAPE() { return getToken(SqlQueryParser.ESCAPE, 0); }
		public TerminalNode IS() { return getToken(SqlQueryParser.IS, 0); }
		public TerminalNode NULL_() { return getToken(SqlQueryParser.NULL_, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_predicate);
		int _la;
		try {
			setState(382);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(324);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(323);
					match(NOT);
					}
				}

				setState(326);
				match(EXISTS);
				setState(327);
				match(LR_BRACKET);
				setState(328);
				subquery();
				setState(329);
				match(RR_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331);
				expression(0);
				setState(332);
				comparisonOperator();
				setState(333);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(335);
				expression(0);
				setState(336);
				comparisonOperator();
				setState(338);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ANY || _la==ALL || _la==SOME) {
					{
					setState(337);
					_la = _input.LA(1);
					if ( !(_la==ANY || _la==ALL || _la==SOME) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(340);
				match(LR_BRACKET);
				setState(341);
				subquery();
				setState(342);
				match(RR_BRACKET);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(344);
				expression(0);
				setState(346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(345);
					match(NOT);
					}
				}

				setState(348);
				match(BETWEEN);
				setState(349);
				expression(0);
				setState(350);
				match(AND);
				setState(351);
				expression(0);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(353);
				expression(0);
				setState(355);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(354);
					match(NOT);
					}
				}

				setState(357);
				match(IN);
				setState(358);
				match(LR_BRACKET);
				setState(361);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(359);
					subquery();
					}
					break;
				case 2:
					{
					setState(360);
					expressionList();
					}
					break;
				}
				setState(363);
				match(RR_BRACKET);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(365);
				expression(0);
				setState(367);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(366);
					match(NOT);
					}
				}

				setState(369);
				match(LIKE);
				setState(370);
				expression(0);
				setState(373);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(371);
					match(ESCAPE);
					setState(372);
					expression(0);
					}
					break;
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(375);
				expression(0);
				setState(376);
				match(IS);
				setState(378);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(377);
					match(NOT);
					}
				}

				setState(380);
				match(NULL_);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubQueryOrValuesClauseContext extends ParserRuleContext {
		public List<SubqueryContext> subquery() {
			return getRuleContexts(SubqueryContext.class);
		}
		public SubqueryContext subquery(int i) {
			return getRuleContext(SubqueryContext.class,i);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<TerminalNode> UNION() { return getTokens(SqlQueryParser.UNION); }
		public TerminalNode UNION(int i) {
			return getToken(SqlQueryParser.UNION, i);
		}
		public List<TerminalNode> ALL() { return getTokens(SqlQueryParser.ALL); }
		public TerminalNode ALL(int i) {
			return getToken(SqlQueryParser.ALL, i);
		}
		public ValuesClauseContext valuesClause() {
			return getRuleContext(ValuesClauseContext.class,0);
		}
		public SubQueryOrValuesClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subQueryOrValuesClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSubQueryOrValuesClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSubQueryOrValuesClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSubQueryOrValuesClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubQueryOrValuesClauseContext subQueryOrValuesClause() throws RecognitionException {
		SubQueryOrValuesClauseContext _localctx = new SubQueryOrValuesClauseContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_subQueryOrValuesClause);
		int _la;
		try {
			setState(402);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(384);
				subquery();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(385);
				match(LR_BRACKET);
				setState(386);
				subquery();
				setState(392);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==UNION) {
					{
					{
					setState(387);
					match(UNION);
					setState(388);
					match(ALL);
					setState(389);
					subquery();
					}
					}
					setState(394);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(395);
				match(RR_BRACKET);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(397);
				valuesClause();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(398);
				match(LR_BRACKET);
				setState(399);
				valuesClause();
				setState(400);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValuesClauseContext extends ParserRuleContext {
		public ExpressionListContext expressionList;
		public List<ExpressionListContext> exprs = new ArrayList<ExpressionListContext>();
		public TerminalNode VALUES() { return getToken(SqlQueryParser.VALUES, 0); }
		public List<TerminalNode> LR_BRACKET() { return getTokens(SqlQueryParser.LR_BRACKET); }
		public TerminalNode LR_BRACKET(int i) {
			return getToken(SqlQueryParser.LR_BRACKET, i);
		}
		public List<TerminalNode> RR_BRACKET() { return getTokens(SqlQueryParser.RR_BRACKET); }
		public TerminalNode RR_BRACKET(int i) {
			return getToken(SqlQueryParser.RR_BRACKET, i);
		}
		public List<ExpressionListContext> expressionList() {
			return getRuleContexts(ExpressionListContext.class);
		}
		public ExpressionListContext expressionList(int i) {
			return getRuleContext(ExpressionListContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public ValuesClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuesClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterValuesClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitValuesClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitValuesClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesClauseContext valuesClause() throws RecognitionException {
		ValuesClauseContext _localctx = new ValuesClauseContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_valuesClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(VALUES);
			setState(405);
			match(LR_BRACKET);
			setState(406);
			((ValuesClauseContext)_localctx).expressionList = expressionList();
			((ValuesClauseContext)_localctx).exprs.add(((ValuesClauseContext)_localctx).expressionList);
			setState(407);
			match(RR_BRACKET);
			setState(415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(408);
				match(COMMA);
				setState(409);
				match(LR_BRACKET);
				setState(410);
				((ValuesClauseContext)_localctx).expressionList = expressionList();
				((ValuesClauseContext)_localctx).exprs.add(((ValuesClauseContext)_localctx).expressionList);
				setState(411);
				match(RR_BRACKET);
				}
				}
				setState(417);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionListContext extends ParserRuleContext {
		public ExpressionContext expression;
		public List<ExpressionContext> expr = new ArrayList<ExpressionContext>();
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			((ExpressionListContext)_localctx).expression = expression(0);
			((ExpressionListContext)_localctx).expr.add(((ExpressionListContext)_localctx).expression);
			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(419);
				match(COMMA);
				setState(420);
				((ExpressionListContext)_localctx).expression = expression(0);
				((ExpressionListContext)_localctx).expr.add(((ExpressionListContext)_localctx).expression);
				}
				}
				setState(425);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectExprListContext extends ParserRuleContext {
		public List<SelectExprContext> selectExpr() {
			return getRuleContexts(SelectExprContext.class);
		}
		public SelectExprContext selectExpr(int i) {
			return getRuleContext(SelectExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public SelectExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelectExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelectExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelectExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectExprListContext selectExprList() throws RecognitionException {
		SelectExprListContext _localctx = new SelectExprListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_selectExprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			selectExpr();
			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(427);
				match(COMMA);
				setState(428);
				selectExpr();
				}
				}
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectExprContext extends ParserRuleContext {
		public SelectAllStarContext selectAllStar() {
			return getRuleContext(SelectAllStarContext.class,0);
		}
		public SelectColumnContext selectColumn() {
			return getRuleContext(SelectColumnContext.class,0);
		}
		public SelectExpressionContext selectExpression() {
			return getRuleContext(SelectExpressionContext.class,0);
		}
		public SelectExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelectExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelectExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelectExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectExprContext selectExpr() throws RecognitionException {
		SelectExprContext _localctx = new SelectExprContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_selectExpr);
		try {
			setState(437);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(434);
				selectAllStar();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(435);
				selectColumn();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(436);
				selectExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectAllStarContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(SqlQueryParser.STAR, 0); }
		public SchemaObjectContext schemaObject() {
			return getRuleContext(SchemaObjectContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public TerminalNode INSERTED() { return getToken(SqlQueryParser.INSERTED, 0); }
		public TerminalNode DELETED() { return getToken(SqlQueryParser.DELETED, 0); }
		public SelectAllStarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectAllStar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelectAllStar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelectAllStar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelectAllStar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectAllStarContext selectAllStar() throws RecognitionException {
		SelectAllStarContext _localctx = new SelectAllStarContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_selectAllStar);
		int _la;
		try {
			setState(448);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case BACK_QUOTE_ID:
			case DOUBLE_QUOTE_ID:
			case SQUARE_BRACKET_ID:
			case DEC_DIGITS:
			case ID:
			case STAR:
			case LIMIT:
			case WEEK:
				enterOuterAlt(_localctx, 1);
				{
				setState(442);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22862123801509630L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -4460806539790505977L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 144143775445811205L) != 0) || ((((_la - 204)) & ~0x3f) == 0 && ((1L << (_la - 204)) & 7494553279922176137L) != 0) || ((((_la - 268)) & ~0x3f) == 0 && ((1L << (_la - 268)) & 1125900083527701L) != 0) || ((((_la - 342)) & ~0x3f) == 0 && ((1L << (_la - 342)) & 4611708008668332069L) != 0) || ((((_la - 467)) & ~0x3f) == 0 && ((1L << (_la - 467)) & 105553116266603L) != 0)) {
					{
					setState(439);
					schemaObject();
					setState(440);
					match(DOT);
					}
				}

				setState(444);
				match(STAR);
				}
				break;
			case DELETED:
			case INSERTED:
				enterOuterAlt(_localctx, 2);
				{
				setState(445);
				_la = _input.LA(1);
				if ( !(_la==DELETED || _la==INSERTED) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(446);
				match(DOT);
				setState(447);
				match(STAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectColumnContext extends ParserRuleContext {
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public TerminalNode NULL_() { return getToken(SqlQueryParser.NULL_, 0); }
		public AsColumnAliasContext asColumnAlias() {
			return getRuleContext(AsColumnAliasContext.class,0);
		}
		public SelectColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectColumn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelectColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelectColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelectColumn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectColumnContext selectColumn() throws RecognitionException {
		SelectColumnContext _localctx = new SelectColumnContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_selectColumn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(452);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(450);
				fullColumnName();
				}
				break;
			case 2:
				{
				setState(451);
				match(NULL_);
				}
				break;
			}
			setState(455);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(454);
				asColumnAlias();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FullColumnNameContext extends ParserRuleContext {
		public IdContext columnName;
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public TerminalNode DELETED() { return getToken(SqlQueryParser.DELETED, 0); }
		public TerminalNode INSERTED() { return getToken(SqlQueryParser.INSERTED, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public SchemaObjectContext schemaObject() {
			return getRuleContext(SchemaObjectContext.class,0);
		}
		public FullColumnNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullColumnName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterFullColumnName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitFullColumnName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitFullColumnName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FullColumnNameContext fullColumnName() throws RecognitionException {
		FullColumnNameContext _localctx = new FullColumnNameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_fullColumnName);
		int _la;
		try {
			setState(467);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(457);
				_la = _input.LA(1);
				if ( !(_la==DELETED || _la==INSERTED) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(458);
				match(DOT);
				setState(459);
				((FullColumnNameContext)_localctx).columnName = id();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(463);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
				case 1:
					{
					setState(460);
					schemaObject();
					setState(461);
					match(DOT);
					}
					break;
				}
				setState(465);
				((FullColumnNameContext)_localctx).columnName = id();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(466);
				((FullColumnNameContext)_localctx).columnName = id();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectExpressionContext extends ParserRuleContext {
		public ColumnAliasContext expressionAlias;
		public Token eq;
		public ExpressionContext expressionAssignment;
		public ExpressionContext expressionAs;
		public ColumnAliasContext columnAlias() {
			return getRuleContext(ColumnAliasContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(SqlQueryParser.EQUAL, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AsColumnAliasContext asColumnAlias() {
			return getRuleContext(AsColumnAliasContext.class,0);
		}
		public SelectExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSelectExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSelectExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSelectExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectExpressionContext selectExpression() throws RecognitionException {
		SelectExpressionContext _localctx = new SelectExpressionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_selectExpression);
		try {
			setState(477);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(469);
				((SelectExpressionContext)_localctx).expressionAlias = columnAlias();
				setState(470);
				((SelectExpressionContext)_localctx).eq = match(EQUAL);
				setState(471);
				((SelectExpressionContext)_localctx).expressionAssignment = expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(473);
				((SelectExpressionContext)_localctx).expressionAs = expression(0);
				setState(475);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
				case 1:
					{
					setState(474);
					asColumnAlias();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SortExpressionContext extends ParserRuleContext {
		public ExpressionContext sortExpr;
		public Token sortOrder;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ASC() { return getToken(SqlQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(SqlQueryParser.DESC, 0); }
		public SortExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSortExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSortExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSortExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortExpressionContext sortExpression() throws RecognitionException {
		SortExpressionContext _localctx = new SortExpressionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_sortExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(479);
			((SortExpressionContext)_localctx).sortExpr = expression(0);
			setState(481);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(480);
				((SortExpressionContext)_localctx).sortOrder = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ASC || _la==DESC) ) {
					((SortExpressionContext)_localctx).sortOrder = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaObjectContext extends ParserRuleContext {
		public IdContext database;
		public IdContext schema;
		public IdContext objName;
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(SqlQueryParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SqlQueryParser.DOT, i);
		}
		public SchemaObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaObject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSchemaObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSchemaObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSchemaObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaObjectContext schemaObject() throws RecognitionException {
		SchemaObjectContext _localctx = new SchemaObjectContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_schemaObject);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(483);
				((SchemaObjectContext)_localctx).database = id();
				setState(484);
				match(DOT);
				setState(486);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22862123801509630L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -4460806539790505977L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 144143775445811205L) != 0) || ((((_la - 204)) & ~0x3f) == 0 && ((1L << (_la - 204)) & 7494553279922176137L) != 0) || ((((_la - 268)) & ~0x3f) == 0 && ((1L << (_la - 268)) & 1125900083527701L) != 0) || ((((_la - 342)) & ~0x3f) == 0 && ((1L << (_la - 342)) & 4611708008668332069L) != 0) || ((((_la - 467)) & ~0x3f) == 0 && ((1L << (_la - 467)) & 105553116266603L) != 0)) {
					{
					setState(485);
					((SchemaObjectContext)_localctx).schema = id();
					}
				}

				setState(488);
				match(DOT);
				}
				break;
			case 2:
				{
				setState(490);
				((SchemaObjectContext)_localctx).schema = id();
				setState(491);
				match(DOT);
				}
				break;
			}
			setState(495);
			((SchemaObjectContext)_localctx).objName = id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsTableAliasContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode AS() { return getToken(SqlQueryParser.AS, 0); }
		public AsTableAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asTableAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAsTableAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAsTableAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAsTableAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsTableAliasContext asTableAlias() throws RecognitionException {
		AsTableAliasContext _localctx = new AsTableAliasContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_asTableAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(497);
				match(AS);
				}
				break;
			}
			setState(500);
			id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsColumnAliasContext extends ParserRuleContext {
		public ColumnAliasContext columnAlias() {
			return getRuleContext(ColumnAliasContext.class,0);
		}
		public TerminalNode AS() { return getToken(SqlQueryParser.AS, 0); }
		public AsColumnAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asColumnAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAsColumnAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAsColumnAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAsColumnAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsColumnAliasContext asColumnAlias() throws RecognitionException {
		AsColumnAliasContext _localctx = new AsColumnAliasContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_asColumnAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(502);
				match(AS);
				}
				break;
			}
			setState(505);
			columnAlias();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColumnAliasContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode STRING() { return getToken(SqlQueryParser.STRING, 0); }
		public ColumnAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterColumnAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitColumnAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitColumnAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnAliasContext columnAlias() throws RecognitionException {
		ColumnAliasContext _localctx = new ColumnAliasContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_columnAlias);
		try {
			setState(509);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case BACK_QUOTE_ID:
			case DOUBLE_QUOTE_ID:
			case SQUARE_BRACKET_ID:
			case DEC_DIGITS:
			case ID:
			case LIMIT:
			case WEEK:
				enterOuterAlt(_localctx, 1);
				{
				setState(507);
				id();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(508);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColumnAliasListContext extends ParserRuleContext {
		public ColumnAliasContext columnAlias;
		public List<ColumnAliasContext> alias = new ArrayList<ColumnAliasContext>();
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<ColumnAliasContext> columnAlias() {
			return getRuleContexts(ColumnAliasContext.class);
		}
		public ColumnAliasContext columnAlias(int i) {
			return getRuleContext(ColumnAliasContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public ColumnAliasListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnAliasList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterColumnAliasList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitColumnAliasList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitColumnAliasList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnAliasListContext columnAliasList() throws RecognitionException {
		ColumnAliasListContext _localctx = new ColumnAliasListContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_columnAliasList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			match(LR_BRACKET);
			setState(512);
			((ColumnAliasListContext)_localctx).columnAlias = columnAlias();
			((ColumnAliasListContext)_localctx).alias.add(((ColumnAliasListContext)_localctx).columnAlias);
			setState(517);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(513);
				match(COMMA);
				setState(514);
				((ColumnAliasListContext)_localctx).columnAlias = columnAlias();
				((ColumnAliasListContext)_localctx).alias.add(((ColumnAliasListContext)_localctx).columnAlias);
				}
				}
				setState(519);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(520);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public FullColumnNameContext columnExpression;
		public Token op;
		public PrimitiveExpressionContext primitiveExpression() {
			return getRuleContext(PrimitiveExpressionContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public CaseWhenExpressionContext caseWhenExpression() {
			return getRuleContext(CaseWhenExpressionContext.class,0);
		}
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public BracketExpressionContext bracketExpression() {
			return getRuleContext(BracketExpressionContext.class,0);
		}
		public UnaryOpExpressionContext unaryOpExpression() {
			return getRuleContext(UnaryOpExpressionContext.class,0);
		}
		public OverClauseContext overClause() {
			return getRuleContext(OverClauseContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode STAR() { return getToken(SqlQueryParser.STAR, 0); }
		public TerminalNode DIVIDE() { return getToken(SqlQueryParser.DIVIDE, 0); }
		public TerminalNode MODULE() { return getToken(SqlQueryParser.MODULE, 0); }
		public BitOperatorContext bitOperator() {
			return getRuleContext(BitOperatorContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(SqlQueryParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(SqlQueryParser.MINUS, 0); }
		public TerminalNode DOUBLE_BAR() { return getToken(SqlQueryParser.DOUBLE_BAR, 0); }
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public XmlValueCallContext xmlValueCall() {
			return getRuleContext(XmlValueCallContext.class,0);
		}
		public XmlQueryCallContext xmlQueryCall() {
			return getRuleContext(XmlQueryCallContext.class,0);
		}
		public XmlExistCallContext xmlExistCall() {
			return getRuleContext(XmlExistCallContext.class,0);
		}
		public XmlModifyCallContext xmlModifyCall() {
			return getRuleContext(XmlModifyCallContext.class,0);
		}
		public TerminalNode COLLATE() { return getToken(SqlQueryParser.COLLATE, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TimeZoneContext timeZone() {
			return getRuleContext(TimeZoneContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(530);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(523);
				primitiveExpression();
				}
				break;
			case 2:
				{
				setState(524);
				functionCall();
				}
				break;
			case 3:
				{
				setState(525);
				caseWhenExpression();
				}
				break;
			case 4:
				{
				setState(526);
				((ExpressionContext)_localctx).columnExpression = fullColumnName();
				}
				break;
			case 5:
				{
				setState(527);
				bracketExpression();
				}
				break;
			case 6:
				{
				setState(528);
				unaryOpExpression();
				}
				break;
			case 7:
				{
				setState(529);
				overClause();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(557);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(555);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(532);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(533);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 503)) & ~0x3f) == 0 && ((1L << (_la - 503)) & 7L) != 0)) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(534);
						expression(6);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(535);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(536);
						bitOperator();
						setState(537);
						expression(5);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(539);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(540);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 491)) & ~0x3f) == 0 && ((1L << (_la - 491)) & 98305L) != 0)) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(541);
						expression(4);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(542);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(543);
						match(DOT);
						setState(548);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case VALUE:
							{
							setState(544);
							xmlValueCall();
							}
							break;
						case QUERY:
							{
							setState(545);
							xmlQueryCall();
							}
							break;
						case EXIST:
							{
							setState(546);
							xmlExistCall();
							}
							break;
						case MODIFY:
							{
							setState(547);
							xmlModifyCall();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(550);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(551);
						match(COLLATE);
						setState(552);
						id();
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(553);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(554);
						timeZone();
						}
						break;
					}
					} 
				}
				setState(559);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveExpressionContext extends ParserRuleContext {
		public TerminalNode DEFAULT() { return getToken(SqlQueryParser.DEFAULT, 0); }
		public TerminalNode NULL_() { return getToken(SqlQueryParser.NULL_, 0); }
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public PrimitiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterPrimitiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitPrimitiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitPrimitiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveExpressionContext primitiveExpression() throws RecognitionException {
		PrimitiveExpressionContext _localctx = new PrimitiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_primitiveExpression);
		try {
			setState(564);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DEFAULT:
				enterOuterAlt(_localctx, 1);
				{
				setState(560);
				match(DEFAULT);
				}
				break;
			case NULL_:
				enterOuterAlt(_localctx, 2);
				{
				setState(561);
				match(NULL_);
				}
				break;
			case LOCAL_ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(562);
				match(LOCAL_ID);
				}
				break;
			case DEC_DIGITS:
			case STRING:
			case HEX_NUMBER:
			case DEC_NUMBER:
			case REAL_NUMBER:
			case DOLLAR:
			case PLUS:
			case MINUS:
				enterOuterAlt(_localctx, 4);
				{
				setState(563);
				constant();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaseWhenExpressionContext extends ParserRuleContext {
		public ExpressionContext caseExpr;
		public ExpressionContext elseExpr;
		public TerminalNode CASE() { return getToken(SqlQueryParser.CASE, 0); }
		public TerminalNode END() { return getToken(SqlQueryParser.END, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> WHEN() { return getTokens(SqlQueryParser.WHEN); }
		public TerminalNode WHEN(int i) {
			return getToken(SqlQueryParser.WHEN, i);
		}
		public List<TerminalNode> THEN() { return getTokens(SqlQueryParser.THEN); }
		public TerminalNode THEN(int i) {
			return getToken(SqlQueryParser.THEN, i);
		}
		public TerminalNode ELSE() { return getToken(SqlQueryParser.ELSE, 0); }
		public List<CriteriaContext> criteria() {
			return getRuleContexts(CriteriaContext.class);
		}
		public CriteriaContext criteria(int i) {
			return getRuleContext(CriteriaContext.class,i);
		}
		public CaseWhenExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseWhenExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCaseWhenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCaseWhenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCaseWhenExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseWhenExpressionContext caseWhenExpression() throws RecognitionException {
		CaseWhenExpressionContext _localctx = new CaseWhenExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_caseWhenExpression);
		int _la;
		try {
			setState(599);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(566);
				match(CASE);
				setState(567);
				((CaseWhenExpressionContext)_localctx).caseExpr = expression(0);
				setState(573); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(568);
					match(WHEN);
					setState(569);
					expression(0);
					setState(570);
					match(THEN);
					setState(571);
					expression(0);
					}
					}
					setState(575); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WHEN );
				setState(579);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(577);
					match(ELSE);
					setState(578);
					((CaseWhenExpressionContext)_localctx).elseExpr = expression(0);
					}
				}

				setState(581);
				match(END);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(583);
				match(CASE);
				setState(589); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(584);
					match(WHEN);
					setState(585);
					criteria(0);
					setState(586);
					match(THEN);
					setState(587);
					expression(0);
					}
					}
					setState(591); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WHEN );
				setState(595);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(593);
					match(ELSE);
					setState(594);
					((CaseWhenExpressionContext)_localctx).elseExpr = expression(0);
					}
				}

				setState(597);
				match(END);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryOpExpressionContext extends ParserRuleContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode BIT_NOT() { return getToken(SqlQueryParser.BIT_NOT, 0); }
		public TerminalNode PLUS() { return getToken(SqlQueryParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(SqlQueryParser.MINUS, 0); }
		public UnaryOpExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOpExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterUnaryOpExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitUnaryOpExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitUnaryOpExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryOpExpressionContext unaryOpExpression() throws RecognitionException {
		UnaryOpExpressionContext _localctx = new UnaryOpExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_unaryOpExpression);
		int _la;
		try {
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BIT_NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(601);
				((UnaryOpExpressionContext)_localctx).op = match(BIT_NOT);
				setState(602);
				expression(0);
				}
				break;
			case PLUS:
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(603);
				((UnaryOpExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
					((UnaryOpExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(604);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BracketExpressionContext extends ParserRuleContext {
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public BracketExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterBracketExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitBracketExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitBracketExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketExpressionContext bracketExpression() throws RecognitionException {
		BracketExpressionContext _localctx = new BracketExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_bracketExpression);
		try {
			setState(615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(607);
				match(LR_BRACKET);
				setState(608);
				expression(0);
				setState(609);
				match(RR_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(611);
				match(LR_BRACKET);
				setState(612);
				subquery();
				setState(613);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstantExpressionContext extends ParserRuleContext {
		public TerminalNode NULL_() { return getToken(SqlQueryParser.NULL_, 0); }
		public TerminalNode TRUE() { return getToken(SqlQueryParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(SqlQueryParser.FALSE, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterConstantExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitConstantExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitConstantExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantExpressionContext constantExpression() throws RecognitionException {
		ConstantExpressionContext _localctx = new ConstantExpressionContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_constantExpression);
		try {
			setState(626);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(617);
				match(NULL_);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(618);
				match(TRUE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(619);
				match(FALSE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(620);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(621);
				functionCall();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(622);
				match(LR_BRACKET);
				setState(623);
				constantExpression();
				setState(624);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubqueryContext extends ParserRuleContext {
		public SqlQueryContext sqlQuery() {
			return getRuleContext(SqlQueryContext.class,0);
		}
		public SubqueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subquery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSubquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSubquery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSubquery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubqueryContext subquery() throws RecognitionException {
		SubqueryContext _localctx = new SubqueryContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_subquery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			sqlQuery();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WithCteClauseContext extends ParserRuleContext {
		public CommonTableExpressionContext commonTableExpression;
		public List<CommonTableExpressionContext> cte = new ArrayList<CommonTableExpressionContext>();
		public TerminalNode WITH() { return getToken(SqlQueryParser.WITH, 0); }
		public List<CommonTableExpressionContext> commonTableExpression() {
			return getRuleContexts(CommonTableExpressionContext.class);
		}
		public CommonTableExpressionContext commonTableExpression(int i) {
			return getRuleContext(CommonTableExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public WithCteClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withCteClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterWithCteClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitWithCteClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitWithCteClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithCteClauseContext withCteClause() throws RecognitionException {
		WithCteClauseContext _localctx = new WithCteClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_withCteClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			match(WITH);
			setState(631);
			((WithCteClauseContext)_localctx).commonTableExpression = commonTableExpression();
			((WithCteClauseContext)_localctx).cte.add(((WithCteClauseContext)_localctx).commonTableExpression);
			setState(636);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(632);
				match(COMMA);
				setState(633);
				((WithCteClauseContext)_localctx).commonTableExpression = commonTableExpression();
				((WithCteClauseContext)_localctx).cte.add(((WithCteClauseContext)_localctx).commonTableExpression);
				}
				}
				setState(638);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommonTableExpressionContext extends ParserRuleContext {
		public IdContext expression_name;
		public SelectExprListContext columns;
		public SqlQueryContext cteQuery;
		public TerminalNode AS() { return getToken(SqlQueryParser.AS, 0); }
		public List<TerminalNode> LR_BRACKET() { return getTokens(SqlQueryParser.LR_BRACKET); }
		public TerminalNode LR_BRACKET(int i) {
			return getToken(SqlQueryParser.LR_BRACKET, i);
		}
		public List<TerminalNode> RR_BRACKET() { return getTokens(SqlQueryParser.RR_BRACKET); }
		public TerminalNode RR_BRACKET(int i) {
			return getToken(SqlQueryParser.RR_BRACKET, i);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public SqlQueryContext sqlQuery() {
			return getRuleContext(SqlQueryContext.class,0);
		}
		public SelectExprListContext selectExprList() {
			return getRuleContext(SelectExprListContext.class,0);
		}
		public CommonTableExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commonTableExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCommonTableExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCommonTableExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCommonTableExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommonTableExpressionContext commonTableExpression() throws RecognitionException {
		CommonTableExpressionContext _localctx = new CommonTableExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_commonTableExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			((CommonTableExpressionContext)_localctx).expression_name = id();
			setState(644);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LR_BRACKET) {
				{
				setState(640);
				match(LR_BRACKET);
				setState(641);
				((CommonTableExpressionContext)_localctx).columns = selectExprList();
				setState(642);
				match(RR_BRACKET);
				}
			}

			setState(646);
			match(AS);
			setState(647);
			match(LR_BRACKET);
			setState(648);
			((CommonTableExpressionContext)_localctx).cteQuery = sqlQuery();
			setState(649);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public RankingWindowedFuncContext rankingWindowedFunc() {
			return getRuleContext(RankingWindowedFuncContext.class,0);
		}
		public AggregateWindowedFuncContext aggregateWindowedFunc() {
			return getRuleContext(AggregateWindowedFuncContext.class,0);
		}
		public AnalyticWindowedFuncContext analyticWindowedFunc() {
			return getRuleContext(AnalyticWindowedFuncContext.class,0);
		}
		public BuiltInFuncContext builtInFunc() {
			return getRuleContext(BuiltInFuncContext.class,0);
		}
		public ScalarFuncContext scalarFunc() {
			return getRuleContext(ScalarFuncContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_functionCall);
		try {
			setState(656);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(651);
				rankingWindowedFunc();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(652);
				aggregateWindowedFunc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(653);
				analyticWindowedFunc();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(654);
				builtInFunc();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(655);
				scalarFunc();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BuiltInFuncContext extends ParserRuleContext {
		public BuiltInFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_builtInFunc; }
	 
		public BuiltInFuncContext() { }
		public void copyFrom(BuiltInFuncContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DatePartFuncContext extends BuiltInFuncContext {
		public Token datepart;
		public ExpressionContext date;
		public TerminalNode DATEPART() { return getToken(SqlQueryParser.DATEPART, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode ID() { return getToken(SqlQueryParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DatePartFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterDatePartFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitDatePartFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitDatePartFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConvertContext extends BuiltInFuncContext {
		public DataTypeContext convert_dataType;
		public ExpressionContext convert_expression;
		public ExpressionContext style;
		public TerminalNode CONVERT() { return getToken(SqlQueryParser.CONVERT, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ConvertContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterConvert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitConvert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitConvert(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ChecksumFuncContext extends BuiltInFuncContext {
		public TerminalNode CHECKSUM() { return getToken(SqlQueryParser.CHECKSUM, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode STAR() { return getToken(SqlQueryParser.STAR, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public ChecksumFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterChecksumFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitChecksumFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitChecksumFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CurrentTimestampFuncContext extends BuiltInFuncContext {
		public TerminalNode CURRENT_TIMESTAMP() { return getToken(SqlQueryParser.CURRENT_TIMESTAMP, 0); }
		public CurrentTimestampFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCurrentTimestampFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCurrentTimestampFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCurrentTimestampFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SystemUserFuncContext extends BuiltInFuncContext {
		public TerminalNode SYSTEM_USER() { return getToken(SqlQueryParser.SYSTEM_USER, 0); }
		public SystemUserFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSystemUserFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSystemUserFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSystemUserFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CastFuncContext extends BuiltInFuncContext {
		public TerminalNode CAST() { return getToken(SqlQueryParser.CAST, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode AS() { return getToken(SqlQueryParser.AS, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public CastFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCastFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCastFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCastFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CurrentDateFuncContext extends BuiltInFuncContext {
		public TerminalNode CURRENT_DATE() { return getToken(SqlQueryParser.CURRENT_DATE, 0); }
		public CurrentDateFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCurrentDateFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCurrentDateFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCurrentDateFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CurrentUserFuncContext extends BuiltInFuncContext {
		public TerminalNode CURRENT_USER() { return getToken(SqlQueryParser.CURRENT_USER, 0); }
		public CurrentUserFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCurrentUserFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCurrentUserFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCurrentUserFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CoalesceFuncContext extends BuiltInFuncContext {
		public TerminalNode COALESCE() { return getToken(SqlQueryParser.COALESCE, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public CoalesceFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterCoalesceFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitCoalesceFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitCoalesceFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IsNullFuncContext extends BuiltInFuncContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public TerminalNode ISNULL() { return getToken(SqlQueryParser.ISNULL, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IsNullFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIsNullFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIsNullFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIsNullFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DateNameFuncContext extends BuiltInFuncContext {
		public Token datepart;
		public ExpressionContext date;
		public TerminalNode DATENAME() { return getToken(SqlQueryParser.DATENAME, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode ID() { return getToken(SqlQueryParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DateNameFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterDateNameFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitDateNameFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitDateNameFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IentityFuncContext extends BuiltInFuncContext {
		public Token seed;
		public Token increment;
		public TerminalNode IDENTITY() { return getToken(SqlQueryParser.IDENTITY, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public List<TerminalNode> DEC_DIGITS() { return getTokens(SqlQueryParser.DEC_DIGITS); }
		public TerminalNode DEC_DIGITS(int i) {
			return getToken(SqlQueryParser.DEC_DIGITS, i);
		}
		public IentityFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIentityFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIentityFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIentityFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryChecksumFuncContext extends BuiltInFuncContext {
		public TerminalNode BINARY_CHECKSUM() { return getToken(SqlQueryParser.BINARY_CHECKSUM, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode STAR() { return getToken(SqlQueryParser.STAR, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public BinaryChecksumFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterBinaryChecksumFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitBinaryChecksumFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitBinaryChecksumFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DateDiffFuncContext extends BuiltInFuncContext {
		public Token datepart;
		public ExpressionContext date_first;
		public ExpressionContext date_second;
		public TerminalNode DATEDIFF() { return getToken(SqlQueryParser.DATEDIFF, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode ID() { return getToken(SqlQueryParser.ID, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DateDiffFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterDateDiffFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitDateDiffFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitDateDiffFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserFuncContext extends BuiltInFuncContext {
		public TerminalNode USER() { return getToken(SqlQueryParser.USER, 0); }
		public UserFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterUserFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitUserFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitUserFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XmlDataFuncContext extends BuiltInFuncContext {
		public XmlDataTypeFuncContext xmlDataTypeFunc() {
			return getRuleContext(XmlDataTypeFuncContext.class,0);
		}
		public XmlDataFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlDataFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlDataFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlDataFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IIfFuncContext extends BuiltInFuncContext {
		public CriteriaContext cond;
		public ExpressionContext left;
		public ExpressionContext right;
		public TerminalNode IIF() { return getToken(SqlQueryParser.IIF, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public CriteriaContext criteria() {
			return getRuleContext(CriteriaContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IIfFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIIfFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIIfFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIIfFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DateAddFuncContext extends BuiltInFuncContext {
		public Token datepart;
		public ExpressionContext number;
		public ExpressionContext date;
		public TerminalNode DATEADD() { return getToken(SqlQueryParser.DATEADD, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode ID() { return getToken(SqlQueryParser.ID, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DateAddFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterDateAddFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitDateAddFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitDateAddFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullIfFuncContext extends BuiltInFuncContext {
		public ExpressionContext left;
		public ExpressionContext right;
		public TerminalNode NULLIF() { return getToken(SqlQueryParser.NULLIF, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public NullIfFuncContext(BuiltInFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterNullIfFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitNullIfFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitNullIfFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BuiltInFuncContext builtInFunc() throws RecognitionException {
		BuiltInFuncContext _localctx = new BuiltInFuncContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_builtInFunc);
		int _la;
		try {
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				_localctx = new BinaryChecksumFuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(658);
				match(BINARY_CHECKSUM);
				setState(659);
				match(LR_BRACKET);
				setState(660);
				match(STAR);
				setState(661);
				match(RR_BRACKET);
				}
				break;
			case 2:
				_localctx = new CastFuncContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(662);
				match(CAST);
				setState(663);
				match(LR_BRACKET);
				setState(664);
				expression(0);
				setState(665);
				match(AS);
				setState(666);
				dataType();
				setState(667);
				match(RR_BRACKET);
				}
				break;
			case 3:
				_localctx = new ConvertContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(669);
				match(CONVERT);
				setState(670);
				match(LR_BRACKET);
				setState(671);
				((ConvertContext)_localctx).convert_dataType = dataType();
				setState(672);
				match(COMMA);
				setState(673);
				((ConvertContext)_localctx).convert_expression = expression(0);
				setState(676);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(674);
					match(COMMA);
					setState(675);
					((ConvertContext)_localctx).style = expression(0);
					}
				}

				setState(678);
				match(RR_BRACKET);
				}
				break;
			case 4:
				_localctx = new ChecksumFuncContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(680);
				match(CHECKSUM);
				setState(681);
				match(LR_BRACKET);
				setState(682);
				match(STAR);
				setState(683);
				match(RR_BRACKET);
				}
				break;
			case 5:
				_localctx = new CoalesceFuncContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(684);
				match(COALESCE);
				setState(685);
				match(LR_BRACKET);
				setState(686);
				expressionList();
				setState(687);
				match(RR_BRACKET);
				}
				break;
			case 6:
				_localctx = new CurrentTimestampFuncContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(689);
				match(CURRENT_TIMESTAMP);
				}
				break;
			case 7:
				_localctx = new CurrentDateFuncContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(690);
				match(CURRENT_DATE);
				}
				break;
			case 8:
				_localctx = new CurrentUserFuncContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(691);
				match(CURRENT_USER);
				}
				break;
			case 9:
				_localctx = new DateAddFuncContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(692);
				match(DATEADD);
				setState(693);
				match(LR_BRACKET);
				setState(694);
				((DateAddFuncContext)_localctx).datepart = match(ID);
				setState(695);
				match(COMMA);
				setState(696);
				((DateAddFuncContext)_localctx).number = expression(0);
				setState(697);
				match(COMMA);
				setState(698);
				((DateAddFuncContext)_localctx).date = expression(0);
				setState(699);
				match(RR_BRACKET);
				}
				break;
			case 10:
				_localctx = new DateDiffFuncContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(701);
				match(DATEDIFF);
				setState(702);
				match(LR_BRACKET);
				setState(703);
				((DateDiffFuncContext)_localctx).datepart = match(ID);
				setState(704);
				match(COMMA);
				setState(705);
				((DateDiffFuncContext)_localctx).date_first = expression(0);
				setState(706);
				match(COMMA);
				setState(707);
				((DateDiffFuncContext)_localctx).date_second = expression(0);
				setState(708);
				match(RR_BRACKET);
				}
				break;
			case 11:
				_localctx = new DateNameFuncContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(710);
				match(DATENAME);
				setState(711);
				match(LR_BRACKET);
				setState(712);
				((DateNameFuncContext)_localctx).datepart = match(ID);
				setState(713);
				match(COMMA);
				setState(714);
				((DateNameFuncContext)_localctx).date = expression(0);
				setState(715);
				match(RR_BRACKET);
				}
				break;
			case 12:
				_localctx = new DatePartFuncContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(717);
				match(DATEPART);
				setState(718);
				match(LR_BRACKET);
				setState(719);
				((DatePartFuncContext)_localctx).datepart = match(ID);
				setState(720);
				match(COMMA);
				setState(721);
				((DatePartFuncContext)_localctx).date = expression(0);
				setState(722);
				match(RR_BRACKET);
				}
				break;
			case 13:
				_localctx = new IentityFuncContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(724);
				match(IDENTITY);
				setState(725);
				match(LR_BRACKET);
				setState(726);
				dataType();
				setState(729);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(727);
					match(COMMA);
					setState(728);
					((IentityFuncContext)_localctx).seed = match(DEC_DIGITS);
					}
					break;
				}
				setState(733);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(731);
					match(COMMA);
					setState(732);
					((IentityFuncContext)_localctx).increment = match(DEC_DIGITS);
					}
				}

				setState(735);
				match(RR_BRACKET);
				}
				break;
			case 14:
				_localctx = new NullIfFuncContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(737);
				match(NULLIF);
				setState(738);
				match(LR_BRACKET);
				setState(739);
				((NullIfFuncContext)_localctx).left = expression(0);
				setState(740);
				match(COMMA);
				setState(741);
				((NullIfFuncContext)_localctx).right = expression(0);
				setState(742);
				match(RR_BRACKET);
				}
				break;
			case 15:
				_localctx = new SystemUserFuncContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(744);
				match(SYSTEM_USER);
				}
				break;
			case 16:
				_localctx = new UserFuncContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(745);
				match(USER);
				}
				break;
			case 17:
				_localctx = new IsNullFuncContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(746);
				match(ISNULL);
				setState(747);
				match(LR_BRACKET);
				setState(748);
				((IsNullFuncContext)_localctx).left = expression(0);
				setState(749);
				match(COMMA);
				setState(750);
				((IsNullFuncContext)_localctx).right = expression(0);
				setState(751);
				match(RR_BRACKET);
				}
				break;
			case 18:
				_localctx = new XmlDataFuncContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(753);
				xmlDataTypeFunc();
				}
				break;
			case 19:
				_localctx = new IIfFuncContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(754);
				match(IIF);
				setState(755);
				match(LR_BRACKET);
				setState(756);
				((IIfFuncContext)_localctx).cond = criteria(0);
				setState(757);
				match(COMMA);
				setState(758);
				((IIfFuncContext)_localctx).left = expression(0);
				setState(759);
				match(COMMA);
				setState(760);
				((IIfFuncContext)_localctx).right = expression(0);
				setState(761);
				match(RR_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScalarFuncContext extends ParserRuleContext {
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public SchemaObjectContext schemaObject() {
			return getRuleContext(SchemaObjectContext.class,0);
		}
		public TerminalNode RIGHT() { return getToken(SqlQueryParser.RIGHT, 0); }
		public TerminalNode LEFT() { return getToken(SqlQueryParser.LEFT, 0); }
		public TerminalNode CHECKSUM() { return getToken(SqlQueryParser.CHECKSUM, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ScalarFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scalarFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterScalarFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitScalarFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitScalarFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScalarFuncContext scalarFunc() throws RecognitionException {
		ScalarFuncContext _localctx = new ScalarFuncContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_scalarFunc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case BACK_QUOTE_ID:
			case DOUBLE_QUOTE_ID:
			case SQUARE_BRACKET_ID:
			case DEC_DIGITS:
			case ID:
			case LIMIT:
			case WEEK:
				{
				setState(765);
				schemaObject();
				}
				break;
			case RIGHT:
				{
				setState(766);
				match(RIGHT);
				}
				break;
			case LEFT:
				{
				setState(767);
				match(LEFT);
				}
				break;
			case CHECKSUM:
				{
				setState(768);
				match(CHECKSUM);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(771);
			match(LR_BRACKET);
			setState(773);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 166977311877365502L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 2559313295928627441L) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & 5764840620175130667L) != 0) || ((((_la - 196)) & ~0x3f) == 0 && ((1L << (_la - 196)) & 1083309430590733073L) != 0) || ((((_la - 263)) & ~0x3f) == 0 && ((1L << (_la - 263)) & 1188965700442522285L) != 0) || ((((_la - 342)) & ~0x3f) == 0 && ((1L << (_la - 342)) & 4611708558427291687L) != 0) || ((((_la - 411)) & ~0x3f) == 0 && ((1L << (_la - 411)) & -360287970189639679L) != 0) || ((((_la - 476)) & ~0x3f) == 0 && ((1L << (_la - 476)) & 213677768711L) != 0)) {
				{
				setState(772);
				expressionList();
				}
			}

			setState(775);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlDataTypeFuncContext extends ParserRuleContext {
		public XmlValueFuncContext xmlValueFunc() {
			return getRuleContext(XmlValueFuncContext.class,0);
		}
		public XmlQueryFuncContext xmlQueryFunc() {
			return getRuleContext(XmlQueryFuncContext.class,0);
		}
		public XmlExistFuncContext xmlExistFunc() {
			return getRuleContext(XmlExistFuncContext.class,0);
		}
		public XmlModifyFuncContext xmlModifyFunc() {
			return getRuleContext(XmlModifyFuncContext.class,0);
		}
		public XmlDataTypeFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlDataTypeFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlDataTypeFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlDataTypeFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlDataTypeFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlDataTypeFuncContext xmlDataTypeFunc() throws RecognitionException {
		XmlDataTypeFuncContext _localctx = new XmlDataTypeFuncContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_xmlDataTypeFunc);
		try {
			setState(781);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(777);
				xmlValueFunc();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(778);
				xmlQueryFunc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(779);
				xmlExistFunc();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(780);
				xmlModifyFunc();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlValueFuncContext extends ParserRuleContext {
		public Token locId;
		public IdContext valueId;
		public XmlQueryFuncContext query;
		public XmlValueCallContext call;
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public XmlValueCallContext xmlValueCall() {
			return getRuleContext(XmlValueCallContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public XmlQueryFuncContext xmlQueryFunc() {
			return getRuleContext(XmlQueryFuncContext.class,0);
		}
		public XmlValueFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlValueFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlValueFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlValueFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlValueFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlValueFuncContext xmlValueFunc() throws RecognitionException {
		XmlValueFuncContext _localctx = new XmlValueFuncContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_xmlValueFunc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(783);
				((XmlValueFuncContext)_localctx).locId = match(LOCAL_ID);
				}
				break;
			case 2:
				{
				setState(784);
				((XmlValueFuncContext)_localctx).valueId = id();
				}
				break;
			case 3:
				{
				setState(785);
				((XmlValueFuncContext)_localctx).query = xmlQueryFunc();
				}
				break;
			case 4:
				{
				setState(786);
				match(LR_BRACKET);
				setState(787);
				subquery();
				setState(788);
				match(RR_BRACKET);
				}
				break;
			}
			setState(792);
			match(DOT);
			setState(793);
			((XmlValueFuncContext)_localctx).call = xmlValueCall();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlValueCallContext extends ParserRuleContext {
		public Token xquery;
		public Token sqltype;
		public TerminalNode VALUE() { return getToken(SqlQueryParser.VALUE, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public List<TerminalNode> STRING() { return getTokens(SqlQueryParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(SqlQueryParser.STRING, i);
		}
		public XmlValueCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlValueCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlValueCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlValueCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlValueCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlValueCallContext xmlValueCall() throws RecognitionException {
		XmlValueCallContext _localctx = new XmlValueCallContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_xmlValueCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(795);
			match(VALUE);
			setState(796);
			match(LR_BRACKET);
			setState(797);
			((XmlValueCallContext)_localctx).xquery = match(STRING);
			setState(798);
			match(COMMA);
			setState(799);
			((XmlValueCallContext)_localctx).sqltype = match(STRING);
			setState(800);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlQueryFuncContext extends ParserRuleContext {
		public Token locId;
		public IdContext valueId;
		public SchemaObjectContext table;
		public XmlQueryCallContext call;
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public XmlQueryCallContext xmlQueryCall() {
			return getRuleContext(XmlQueryCallContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public SchemaObjectContext schemaObject() {
			return getRuleContext(SchemaObjectContext.class,0);
		}
		public XmlQueryFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlQueryFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlQueryFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlQueryFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlQueryFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlQueryFuncContext xmlQueryFunc() throws RecognitionException {
		XmlQueryFuncContext _localctx = new XmlQueryFuncContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_xmlQueryFunc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(809);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(802);
				((XmlQueryFuncContext)_localctx).locId = match(LOCAL_ID);
				}
				break;
			case 2:
				{
				setState(803);
				((XmlQueryFuncContext)_localctx).valueId = id();
				}
				break;
			case 3:
				{
				setState(804);
				((XmlQueryFuncContext)_localctx).table = schemaObject();
				}
				break;
			case 4:
				{
				setState(805);
				match(LR_BRACKET);
				setState(806);
				subquery();
				setState(807);
				match(RR_BRACKET);
				}
				break;
			}
			setState(811);
			match(DOT);
			setState(812);
			((XmlQueryFuncContext)_localctx).call = xmlQueryCall();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlQueryCallContext extends ParserRuleContext {
		public Token xquery;
		public TerminalNode QUERY() { return getToken(SqlQueryParser.QUERY, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(SqlQueryParser.STRING, 0); }
		public XmlQueryCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlQueryCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlQueryCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlQueryCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlQueryCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlQueryCallContext xmlQueryCall() throws RecognitionException {
		XmlQueryCallContext _localctx = new XmlQueryCallContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_xmlQueryCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(814);
			match(QUERY);
			setState(815);
			match(LR_BRACKET);
			setState(816);
			((XmlQueryCallContext)_localctx).xquery = match(STRING);
			setState(817);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlExistFuncContext extends ParserRuleContext {
		public Token locId;
		public IdContext valueId;
		public XmlExistCallContext call;
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public XmlExistCallContext xmlExistCall() {
			return getRuleContext(XmlExistCallContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public XmlExistFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlExistFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlExistFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlExistFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlExistFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlExistFuncContext xmlExistFunc() throws RecognitionException {
		XmlExistFuncContext _localctx = new XmlExistFuncContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_xmlExistFunc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOCAL_ID:
				{
				setState(819);
				((XmlExistFuncContext)_localctx).locId = match(LOCAL_ID);
				}
				break;
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case BACK_QUOTE_ID:
			case DOUBLE_QUOTE_ID:
			case SQUARE_BRACKET_ID:
			case DEC_DIGITS:
			case ID:
			case LIMIT:
			case WEEK:
				{
				setState(820);
				((XmlExistFuncContext)_localctx).valueId = id();
				}
				break;
			case LR_BRACKET:
				{
				setState(821);
				match(LR_BRACKET);
				setState(822);
				subquery();
				setState(823);
				match(RR_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(827);
			match(DOT);
			setState(828);
			((XmlExistFuncContext)_localctx).call = xmlExistCall();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlExistCallContext extends ParserRuleContext {
		public Token xquery;
		public TerminalNode EXIST() { return getToken(SqlQueryParser.EXIST, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(SqlQueryParser.STRING, 0); }
		public XmlExistCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlExistCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlExistCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlExistCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlExistCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlExistCallContext xmlExistCall() throws RecognitionException {
		XmlExistCallContext _localctx = new XmlExistCallContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_xmlExistCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
			match(EXIST);
			setState(831);
			match(LR_BRACKET);
			setState(832);
			((XmlExistCallContext)_localctx).xquery = match(STRING);
			setState(833);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlModifyFuncContext extends ParserRuleContext {
		public Token locId;
		public IdContext valueId;
		public XmlModifyCallContext call;
		public TerminalNode DOT() { return getToken(SqlQueryParser.DOT, 0); }
		public XmlModifyCallContext xmlModifyCall() {
			return getRuleContext(XmlModifyCallContext.class,0);
		}
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public SubqueryContext subquery() {
			return getRuleContext(SubqueryContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode LOCAL_ID() { return getToken(SqlQueryParser.LOCAL_ID, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public XmlModifyFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlModifyFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlModifyFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlModifyFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlModifyFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlModifyFuncContext xmlModifyFunc() throws RecognitionException {
		XmlModifyFuncContext _localctx = new XmlModifyFuncContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_xmlModifyFunc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(841);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOCAL_ID:
				{
				setState(835);
				((XmlModifyFuncContext)_localctx).locId = match(LOCAL_ID);
				}
				break;
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case BACK_QUOTE_ID:
			case DOUBLE_QUOTE_ID:
			case SQUARE_BRACKET_ID:
			case DEC_DIGITS:
			case ID:
			case LIMIT:
			case WEEK:
				{
				setState(836);
				((XmlModifyFuncContext)_localctx).valueId = id();
				}
				break;
			case LR_BRACKET:
				{
				setState(837);
				match(LR_BRACKET);
				setState(838);
				subquery();
				setState(839);
				match(RR_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(843);
			match(DOT);
			setState(844);
			((XmlModifyFuncContext)_localctx).call = xmlModifyCall();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XmlModifyCallContext extends ParserRuleContext {
		public Token xmlDml;
		public TerminalNode MODIFY() { return getToken(SqlQueryParser.MODIFY, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode STRING() { return getToken(SqlQueryParser.STRING, 0); }
		public XmlModifyCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlModifyCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterXmlModifyCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitXmlModifyCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitXmlModifyCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlModifyCallContext xmlModifyCall() throws RecognitionException {
		XmlModifyCallContext _localctx = new XmlModifyCallContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_xmlModifyCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(846);
			match(MODIFY);
			setState(847);
			match(LR_BRACKET);
			setState(848);
			((XmlModifyCallContext)_localctx).xmlDml = match(STRING);
			setState(849);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TimeZoneContext extends ParserRuleContext {
		public TerminalNode AT_KEYWORD() { return getToken(SqlQueryParser.AT_KEYWORD, 0); }
		public TerminalNode TIME() { return getToken(SqlQueryParser.TIME, 0); }
		public TerminalNode ZONE() { return getToken(SqlQueryParser.ZONE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TimeZoneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeZone; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterTimeZone(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitTimeZone(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitTimeZone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeZoneContext timeZone() throws RecognitionException {
		TimeZoneContext _localctx = new TimeZoneContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_timeZone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			match(AT_KEYWORD);
			setState(852);
			match(TIME);
			setState(853);
			match(ZONE);
			setState(854);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RankingWindowedFuncContext extends ParserRuleContext {
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public OverClauseContext overClause() {
			return getRuleContext(OverClauseContext.class,0);
		}
		public TerminalNode RANK() { return getToken(SqlQueryParser.RANK, 0); }
		public TerminalNode DENSE_RANK() { return getToken(SqlQueryParser.DENSE_RANK, 0); }
		public TerminalNode ROW_NUMBER() { return getToken(SqlQueryParser.ROW_NUMBER, 0); }
		public RankingWindowedFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rankingWindowedFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterRankingWindowedFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitRankingWindowedFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitRankingWindowedFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RankingWindowedFuncContext rankingWindowedFunc() throws RecognitionException {
		RankingWindowedFuncContext _localctx = new RankingWindowedFuncContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_rankingWindowedFunc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(856);
			_la = _input.LA(1);
			if ( !(_la==DENSE_RANK || _la==RANK || _la==ROW_NUMBER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(857);
			match(LR_BRACKET);
			setState(858);
			match(RR_BRACKET);
			setState(859);
			overClause();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AggregateWindowedFuncContext extends ParserRuleContext {
		public Token agg_func;
		public Token cnt;
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public AllOrDistinctExpressionContext allOrDistinctExpression() {
			return getRuleContext(AllOrDistinctExpressionContext.class,0);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode AVG() { return getToken(SqlQueryParser.AVG, 0); }
		public TerminalNode MAX() { return getToken(SqlQueryParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(SqlQueryParser.MIN, 0); }
		public TerminalNode SUM() { return getToken(SqlQueryParser.SUM, 0); }
		public TerminalNode STDEV() { return getToken(SqlQueryParser.STDEV, 0); }
		public TerminalNode STDEVP() { return getToken(SqlQueryParser.STDEVP, 0); }
		public TerminalNode VAR() { return getToken(SqlQueryParser.VAR, 0); }
		public OverClauseContext overClause() {
			return getRuleContext(OverClauseContext.class,0);
		}
		public TerminalNode COUNT() { return getToken(SqlQueryParser.COUNT, 0); }
		public TerminalNode STAR() { return getToken(SqlQueryParser.STAR, 0); }
		public TerminalNode GROUPING() { return getToken(SqlQueryParser.GROUPING, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode GROUPING_ID() { return getToken(SqlQueryParser.GROUPING_ID, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public AggregateWindowedFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregateWindowedFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAggregateWindowedFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAggregateWindowedFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAggregateWindowedFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AggregateWindowedFuncContext aggregateWindowedFunc() throws RecognitionException {
		AggregateWindowedFuncContext _localctx = new AggregateWindowedFuncContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_aggregateWindowedFunc);
		int _la;
		try {
			setState(888);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AVG:
			case MAX:
			case MIN:
			case STDEV:
			case STDEVP:
			case SUM:
			case VAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(861);
				((AggregateWindowedFuncContext)_localctx).agg_func = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==AVG || _la==MAX || _la==MIN || ((((_la - 362)) & ~0x3f) == 0 && ((1L << (_la - 362)) & 524299L) != 0)) ) {
					((AggregateWindowedFuncContext)_localctx).agg_func = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(862);
				match(LR_BRACKET);
				setState(863);
				allOrDistinctExpression();
				setState(864);
				match(RR_BRACKET);
				setState(866);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
				case 1:
					{
					setState(865);
					overClause();
					}
					break;
				}
				}
				break;
			case COUNT:
				enterOuterAlt(_localctx, 2);
				{
				setState(868);
				((AggregateWindowedFuncContext)_localctx).cnt = match(COUNT);
				setState(869);
				match(LR_BRACKET);
				setState(872);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case STAR:
					{
					setState(870);
					match(STAR);
					}
					break;
				case BOOL:
				case TINYINT:
				case SMALLINT:
				case INT:
				case INTEGER:
				case BIGINT:
				case UNSIGNED:
				case NUMERIC:
				case MONEY:
				case FLOAT:
				case REAL:
				case DOUBLE:
				case CHAR:
				case NCHAR:
				case VARCHAR:
				case NVARCHAR:
				case UUID:
				case BIT:
				case CLOB:
				case NCLOB:
				case TEXT:
				case NTEXT:
				case JSON:
				case XML:
				case DATE:
				case TIME:
				case DATETIME:
				case TIMESTAMP:
				case INTERVAL:
				case VECTOR:
				case BINARY:
				case BLOB:
				case BFILE:
				case GEOGRAPHY:
				case GEOMETRY:
				case ANY:
				case ALL:
				case AND:
				case AS:
				case ASC:
				case BEGIN:
				case BY:
				case CASE:
				case COALESCE:
				case CONVERT:
				case CURRENT_DATE:
				case CURRENT_TIME:
				case CURRENT_TIMESTAMP:
				case CURRENT_USER:
				case DEFAULT:
				case DESC:
				case DISTINCT:
				case ELSE:
				case END:
				case FROM:
				case GROUP:
				case HAVING:
				case IDENTITY:
				case IF:
				case IS:
				case LEFT:
				case NOT:
				case NULL_:
				case NULLIF:
				case OFFSET:
				case OR:
				case ORDER:
				case OVER:
				case PRECISION:
				case RIGHT:
				case SELECT:
				case SYSTEM_USER:
				case THEN:
				case USER:
				case VARYING:
				case WHEN:
				case WHERE:
				case AVG:
				case BINARY_CHECKSUM:
				case CAST:
				case CHECKSUM:
				case COUNT:
				case CUME_DIST:
				case DATEADD:
				case DATEDIFF:
				case DATENAME:
				case DATEPART:
				case DAY:
				case DELETED:
				case DENSE_RANK:
				case FIRST:
				case FIRST_VALUE:
				case GROUPING:
				case GROUPING_ID:
				case HOUR:
				case INSERTED:
				case LAG:
				case LAST:
				case LAST_VALUE:
				case LEAD:
				case MAX:
				case MIN:
				case MINUTE:
				case MICROSECOND:
				case MILLISECOND:
				case NANOSECOND:
				case MONTH:
				case NEXT:
				case NUMBER:
				case ONLY:
				case PERCENT_RANK:
				case PERCENTILE_CONT:
				case PERCENTILE_DISC:
				case QUARTER:
				case RANK:
				case ROW:
				case ROW_NUMBER:
				case ROWS:
				case SECOND:
				case STDEV:
				case STDEVP:
				case SUM:
				case VAR:
				case YEAR:
				case ZONE:
				case IIF:
				case ISNULL:
				case BACK_QUOTE_ID:
				case DOUBLE_QUOTE_ID:
				case SQUARE_BRACKET_ID:
				case LOCAL_ID:
				case DEC_DIGITS:
				case ID:
				case STRING:
				case HEX_NUMBER:
				case DEC_NUMBER:
				case REAL_NUMBER:
				case DOLLAR:
				case LR_BRACKET:
				case PLUS:
				case MINUS:
				case BIT_NOT:
				case LIMIT:
				case WEEK:
					{
					setState(871);
					allOrDistinctExpression();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(874);
				match(RR_BRACKET);
				setState(876);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
				case 1:
					{
					setState(875);
					overClause();
					}
					break;
				}
				}
				break;
			case GROUPING:
				enterOuterAlt(_localctx, 3);
				{
				setState(878);
				match(GROUPING);
				setState(879);
				match(LR_BRACKET);
				setState(880);
				expression(0);
				setState(881);
				match(RR_BRACKET);
				}
				break;
			case GROUPING_ID:
				enterOuterAlt(_localctx, 4);
				{
				setState(883);
				match(GROUPING_ID);
				setState(884);
				match(LR_BRACKET);
				setState(885);
				expressionList();
				setState(886);
				match(RR_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnalyticWindowedFuncContext extends ParserRuleContext {
		public List<TerminalNode> LR_BRACKET() { return getTokens(SqlQueryParser.LR_BRACKET); }
		public TerminalNode LR_BRACKET(int i) {
			return getToken(SqlQueryParser.LR_BRACKET, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> RR_BRACKET() { return getTokens(SqlQueryParser.RR_BRACKET); }
		public TerminalNode RR_BRACKET(int i) {
			return getToken(SqlQueryParser.RR_BRACKET, i);
		}
		public OverClauseContext overClause() {
			return getRuleContext(OverClauseContext.class,0);
		}
		public TerminalNode FIRST_VALUE() { return getToken(SqlQueryParser.FIRST_VALUE, 0); }
		public TerminalNode LAST_VALUE() { return getToken(SqlQueryParser.LAST_VALUE, 0); }
		public TerminalNode LAG() { return getToken(SqlQueryParser.LAG, 0); }
		public TerminalNode LEAD() { return getToken(SqlQueryParser.LEAD, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SqlQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlQueryParser.COMMA, i);
		}
		public TerminalNode OVER() { return getToken(SqlQueryParser.OVER, 0); }
		public OrderByContext orderBy() {
			return getRuleContext(OrderByContext.class,0);
		}
		public TerminalNode CUME_DIST() { return getToken(SqlQueryParser.CUME_DIST, 0); }
		public TerminalNode PERCENT_RANK() { return getToken(SqlQueryParser.PERCENT_RANK, 0); }
		public TerminalNode PARTITION() { return getToken(SqlQueryParser.PARTITION, 0); }
		public List<TerminalNode> BY() { return getTokens(SqlQueryParser.BY); }
		public TerminalNode BY(int i) {
			return getToken(SqlQueryParser.BY, i);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode WITHIN() { return getToken(SqlQueryParser.WITHIN, 0); }
		public TerminalNode GROUP() { return getToken(SqlQueryParser.GROUP, 0); }
		public TerminalNode ORDER() { return getToken(SqlQueryParser.ORDER, 0); }
		public TerminalNode PERCENTILE_CONT() { return getToken(SqlQueryParser.PERCENTILE_CONT, 0); }
		public TerminalNode PERCENTILE_DISC() { return getToken(SqlQueryParser.PERCENTILE_DISC, 0); }
		public TerminalNode ASC() { return getToken(SqlQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(SqlQueryParser.DESC, 0); }
		public AnalyticWindowedFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_analyticWindowedFunc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAnalyticWindowedFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAnalyticWindowedFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAnalyticWindowedFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnalyticWindowedFuncContext analyticWindowedFunc() throws RecognitionException {
		AnalyticWindowedFuncContext _localctx = new AnalyticWindowedFuncContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_analyticWindowedFunc);
		int _la;
		try {
			setState(946);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FIRST_VALUE:
			case LAST_VALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(890);
				_la = _input.LA(1);
				if ( !(_la==FIRST_VALUE || _la==LAST_VALUE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(891);
				match(LR_BRACKET);
				setState(892);
				expression(0);
				setState(893);
				match(RR_BRACKET);
				setState(894);
				overClause();
				}
				break;
			case LAG:
			case LEAD:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
				_la = _input.LA(1);
				if ( !(_la==LAG || _la==LEAD) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(897);
				match(LR_BRACKET);
				setState(898);
				expression(0);
				setState(905);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(899);
					match(COMMA);
					setState(900);
					expression(0);
					setState(903);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(901);
						match(COMMA);
						setState(902);
						expression(0);
						}
					}

					}
				}

				setState(907);
				match(RR_BRACKET);
				setState(908);
				overClause();
				}
				break;
			case CUME_DIST:
			case PERCENT_RANK:
				enterOuterAlt(_localctx, 3);
				{
				setState(910);
				_la = _input.LA(1);
				if ( !(_la==CUME_DIST || _la==PERCENT_RANK) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(911);
				match(LR_BRACKET);
				setState(912);
				match(RR_BRACKET);
				setState(913);
				match(OVER);
				setState(914);
				match(LR_BRACKET);
				setState(918);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PARTITION) {
					{
					setState(915);
					match(PARTITION);
					setState(916);
					match(BY);
					setState(917);
					expressionList();
					}
				}

				setState(920);
				orderBy();
				setState(921);
				match(RR_BRACKET);
				}
				break;
			case PERCENTILE_CONT:
			case PERCENTILE_DISC:
				enterOuterAlt(_localctx, 4);
				{
				setState(923);
				_la = _input.LA(1);
				if ( !(_la==PERCENTILE_CONT || _la==PERCENTILE_DISC) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(924);
				match(LR_BRACKET);
				setState(925);
				expression(0);
				setState(926);
				match(RR_BRACKET);
				setState(927);
				match(WITHIN);
				setState(928);
				match(GROUP);
				setState(929);
				match(LR_BRACKET);
				setState(930);
				match(ORDER);
				setState(931);
				match(BY);
				setState(932);
				expression(0);
				setState(934);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASC || _la==DESC) {
					{
					setState(933);
					_la = _input.LA(1);
					if ( !(_la==ASC || _la==DESC) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(936);
				match(RR_BRACKET);
				setState(937);
				match(OVER);
				setState(938);
				match(LR_BRACKET);
				setState(942);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PARTITION) {
					{
					setState(939);
					match(PARTITION);
					setState(940);
					match(BY);
					setState(941);
					expressionList();
					}
				}

				setState(944);
				match(RR_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AllOrDistinctExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ALL() { return getToken(SqlQueryParser.ALL, 0); }
		public TerminalNode DISTINCT() { return getToken(SqlQueryParser.DISTINCT, 0); }
		public AllOrDistinctExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_allOrDistinctExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAllOrDistinctExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAllOrDistinctExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAllOrDistinctExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AllOrDistinctExpressionContext allOrDistinctExpression() throws RecognitionException {
		AllOrDistinctExpressionContext _localctx = new AllOrDistinctExpressionContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_allOrDistinctExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(949);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				{
				setState(948);
				_la = _input.LA(1);
				if ( !(_la==ALL || _la==DISTINCT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(951);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OverClauseContext extends ParserRuleContext {
		public TerminalNode OVER() { return getToken(SqlQueryParser.OVER, 0); }
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode PARTITION() { return getToken(SqlQueryParser.PARTITION, 0); }
		public TerminalNode BY() { return getToken(SqlQueryParser.BY, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public OrderByContext orderBy() {
			return getRuleContext(OrderByContext.class,0);
		}
		public OverClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_overClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterOverClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitOverClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitOverClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OverClauseContext overClause() throws RecognitionException {
		OverClauseContext _localctx = new OverClauseContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_overClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(953);
			match(OVER);
			setState(954);
			match(LR_BRACKET);
			setState(958);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PARTITION) {
				{
				setState(955);
				match(PARTITION);
				setState(956);
				match(BY);
				setState(957);
				expressionList();
				}
			}

			setState(961);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(960);
				orderBy();
				}
			}

			setState(963);
			match(RR_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TimeUnitContext extends ParserRuleContext {
		public TerminalNode YEAR() { return getToken(SqlQueryParser.YEAR, 0); }
		public TerminalNode YEARS() { return getToken(SqlQueryParser.YEARS, 0); }
		public TerminalNode QUARTER() { return getToken(SqlQueryParser.QUARTER, 0); }
		public TerminalNode QUARTERS() { return getToken(SqlQueryParser.QUARTERS, 0); }
		public TerminalNode MONTH() { return getToken(SqlQueryParser.MONTH, 0); }
		public TerminalNode MONTHS() { return getToken(SqlQueryParser.MONTHS, 0); }
		public TerminalNode WEEK() { return getToken(SqlQueryParser.WEEK, 0); }
		public TerminalNode WEEKS() { return getToken(SqlQueryParser.WEEKS, 0); }
		public TerminalNode DAY() { return getToken(SqlQueryParser.DAY, 0); }
		public TerminalNode DAYS() { return getToken(SqlQueryParser.DAYS, 0); }
		public TerminalNode HOUR() { return getToken(SqlQueryParser.HOUR, 0); }
		public TerminalNode HOURS() { return getToken(SqlQueryParser.HOURS, 0); }
		public TerminalNode MINUTE() { return getToken(SqlQueryParser.MINUTE, 0); }
		public TerminalNode MINUTES() { return getToken(SqlQueryParser.MINUTES, 0); }
		public TerminalNode SECOND() { return getToken(SqlQueryParser.SECOND, 0); }
		public TerminalNode SECONDS() { return getToken(SqlQueryParser.SECONDS, 0); }
		public TerminalNode MICROSECOND() { return getToken(SqlQueryParser.MICROSECOND, 0); }
		public TerminalNode MICROSECONDS() { return getToken(SqlQueryParser.MICROSECONDS, 0); }
		public TerminalNode MILLISECOND() { return getToken(SqlQueryParser.MILLISECOND, 0); }
		public TerminalNode MILLISECONDS() { return getToken(SqlQueryParser.MILLISECONDS, 0); }
		public TimeUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterTimeUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitTimeUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitTimeUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeUnitContext timeUnit() throws RecognitionException {
		TimeUnitContext _localctx = new TimeUnitContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_timeUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(965);
			_la = _input.LA(1);
			if ( !(((((_la - 211)) & ~0x3f) == 0 && ((1L << (_la - 211)) & 2269814225079631875L) != 0) || ((((_la - 287)) & ~0x3f) == 0 && ((1L << (_la - 287)) & 3458764520262991875L) != 0) || _la==YEAR || _la==YEARS || _la==WEEK || _la==WEEKS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntervalUnitContext extends ParserRuleContext {
		public TimeUnitContext timeUnit() {
			return getRuleContext(TimeUnitContext.class,0);
		}
		public TerminalNode YEAR_MONTH() { return getToken(SqlQueryParser.YEAR_MONTH, 0); }
		public TerminalNode DAY_HOUR() { return getToken(SqlQueryParser.DAY_HOUR, 0); }
		public TerminalNode DAY_MINUTE() { return getToken(SqlQueryParser.DAY_MINUTE, 0); }
		public TerminalNode DAY_SECOND() { return getToken(SqlQueryParser.DAY_SECOND, 0); }
		public TerminalNode DAY_MICROSECOND() { return getToken(SqlQueryParser.DAY_MICROSECOND, 0); }
		public TerminalNode HOUR_MINUTE() { return getToken(SqlQueryParser.HOUR_MINUTE, 0); }
		public TerminalNode HOUR_SECOND() { return getToken(SqlQueryParser.HOUR_SECOND, 0); }
		public TerminalNode HOUR_MICROSECOND() { return getToken(SqlQueryParser.HOUR_MICROSECOND, 0); }
		public TerminalNode MINUTE_SECOND() { return getToken(SqlQueryParser.MINUTE_SECOND, 0); }
		public TerminalNode MINUTE_MICROSECOND() { return getToken(SqlQueryParser.MINUTE_MICROSECOND, 0); }
		public TerminalNode SECOND_MICROSECOND() { return getToken(SqlQueryParser.SECOND_MICROSECOND, 0); }
		public IntervalUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIntervalUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIntervalUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIntervalUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalUnitContext intervalUnit() throws RecognitionException {
		IntervalUnitContext _localctx = new IntervalUnitContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_intervalUnit);
		try {
			setState(979);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DAY:
			case DAYS:
			case HOUR:
			case HOURS:
			case MINUTE:
			case MINUTES:
			case MICROSECOND:
			case MICROSECONDS:
			case MILLISECOND:
			case MILLISECONDS:
			case MONTH:
			case MONTHS:
			case QUARTER:
			case QUARTERS:
			case SECOND:
			case SECONDS:
			case YEAR:
			case YEARS:
			case WEEK:
			case WEEKS:
				enterOuterAlt(_localctx, 1);
				{
				setState(967);
				timeUnit();
				}
				break;
			case YEAR_MONTH:
				enterOuterAlt(_localctx, 2);
				{
				setState(968);
				match(YEAR_MONTH);
				}
				break;
			case DAY_HOUR:
				enterOuterAlt(_localctx, 3);
				{
				setState(969);
				match(DAY_HOUR);
				}
				break;
			case DAY_MINUTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(970);
				match(DAY_MINUTE);
				}
				break;
			case DAY_SECOND:
				enterOuterAlt(_localctx, 5);
				{
				setState(971);
				match(DAY_SECOND);
				}
				break;
			case DAY_MICROSECOND:
				enterOuterAlt(_localctx, 6);
				{
				setState(972);
				match(DAY_MICROSECOND);
				}
				break;
			case HOUR_MINUTE:
				enterOuterAlt(_localctx, 7);
				{
				setState(973);
				match(HOUR_MINUTE);
				}
				break;
			case HOUR_SECOND:
				enterOuterAlt(_localctx, 8);
				{
				setState(974);
				match(HOUR_SECOND);
				}
				break;
			case HOUR_MICROSECOND:
				enterOuterAlt(_localctx, 9);
				{
				setState(975);
				match(HOUR_MICROSECOND);
				}
				break;
			case MINUTE_SECOND:
				enterOuterAlt(_localctx, 10);
				{
				setState(976);
				match(MINUTE_SECOND);
				}
				break;
			case MINUTE_MICROSECOND:
				enterOuterAlt(_localctx, 11);
				{
				setState(977);
				match(MINUTE_MICROSECOND);
				}
				break;
			case SECOND_MICROSECOND:
				enterOuterAlt(_localctx, 12);
				{
				setState(978);
				match(SECOND_MICROSECOND);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntervalExpressionContext extends ParserRuleContext {
		public ExpressionContext intervalExpr;
		public Token leadingUnit;
		public Token leadingPrec;
		public Token secondPrec;
		public TimeUnitContext firstUnit;
		public TimeUnitContext secondUnit;
		public TerminalNode INTERVAL() { return getToken(SqlQueryParser.INTERVAL, 0); }
		public TerminalNode TO() { return getToken(SqlQueryParser.TO, 0); }
		public List<IntervalTemporalContext> intervalTemporal() {
			return getRuleContexts(IntervalTemporalContext.class);
		}
		public IntervalTemporalContext intervalTemporal(int i) {
			return getRuleContext(IntervalTemporalContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DAY() { return getToken(SqlQueryParser.DAY, 0); }
		public TerminalNode SECOND() { return getToken(SqlQueryParser.SECOND, 0); }
		public TerminalNode YEAR() { return getToken(SqlQueryParser.YEAR, 0); }
		public TerminalNode MONTH() { return getToken(SqlQueryParser.MONTH, 0); }
		public List<TimeUnitContext> timeUnit() {
			return getRuleContexts(TimeUnitContext.class);
		}
		public TimeUnitContext timeUnit(int i) {
			return getRuleContext(TimeUnitContext.class,i);
		}
		public List<TerminalNode> LR_BRACKET() { return getTokens(SqlQueryParser.LR_BRACKET); }
		public TerminalNode LR_BRACKET(int i) {
			return getToken(SqlQueryParser.LR_BRACKET, i);
		}
		public List<TerminalNode> RR_BRACKET() { return getTokens(SqlQueryParser.RR_BRACKET); }
		public TerminalNode RR_BRACKET(int i) {
			return getToken(SqlQueryParser.RR_BRACKET, i);
		}
		public List<TerminalNode> SINGLE_QUOTE() { return getTokens(SqlQueryParser.SINGLE_QUOTE); }
		public TerminalNode SINGLE_QUOTE(int i) {
			return getToken(SqlQueryParser.SINGLE_QUOTE, i);
		}
		public List<TerminalNode> DEC_DIGIT() { return getTokens(SqlQueryParser.DEC_DIGIT); }
		public TerminalNode DEC_DIGIT(int i) {
			return getToken(SqlQueryParser.DEC_DIGIT, i);
		}
		public IntervalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIntervalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIntervalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIntervalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalExpressionContext intervalExpression() throws RecognitionException {
		IntervalExpressionContext _localctx = new IntervalExpressionContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_intervalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			match(INTERVAL);
			setState(1035);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(982);
				((IntervalExpressionContext)_localctx).intervalExpr = expression(0);
				setState(983);
				((IntervalExpressionContext)_localctx).leadingUnit = match(DAY);
				setState(987);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LR_BRACKET) {
					{
					setState(984);
					match(LR_BRACKET);
					setState(985);
					((IntervalExpressionContext)_localctx).leadingPrec = match(DEC_DIGIT);
					setState(986);
					match(RR_BRACKET);
					}
				}

				setState(989);
				match(TO);
				setState(990);
				((IntervalExpressionContext)_localctx).secondPrec = match(SECOND);
				setState(994);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LR_BRACKET) {
					{
					setState(991);
					match(LR_BRACKET);
					setState(992);
					((IntervalExpressionContext)_localctx).secondPrec = match(DEC_DIGIT);
					setState(993);
					match(RR_BRACKET);
					}
				}

				}
				break;
			case 2:
				{
				setState(996);
				((IntervalExpressionContext)_localctx).intervalExpr = expression(0);
				setState(997);
				((IntervalExpressionContext)_localctx).leadingUnit = match(YEAR);
				setState(1001);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LR_BRACKET) {
					{
					setState(998);
					match(LR_BRACKET);
					setState(999);
					((IntervalExpressionContext)_localctx).leadingPrec = match(DEC_DIGIT);
					setState(1000);
					match(RR_BRACKET);
					}
				}

				setState(1003);
				match(TO);
				setState(1004);
				((IntervalExpressionContext)_localctx).secondPrec = match(MONTH);
				}
				break;
			case 3:
				{
				setState(1006);
				((IntervalExpressionContext)_localctx).intervalExpr = expression(0);
				setState(1007);
				((IntervalExpressionContext)_localctx).firstUnit = timeUnit();
				setState(1011);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LR_BRACKET) {
					{
					setState(1008);
					match(LR_BRACKET);
					setState(1009);
					((IntervalExpressionContext)_localctx).leadingPrec = match(DEC_DIGIT);
					setState(1010);
					match(RR_BRACKET);
					}
				}

				setState(1020);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TO) {
					{
					setState(1013);
					match(TO);
					setState(1014);
					((IntervalExpressionContext)_localctx).secondUnit = timeUnit();
					setState(1018);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LR_BRACKET) {
						{
						setState(1015);
						match(LR_BRACKET);
						setState(1016);
						((IntervalExpressionContext)_localctx).secondPrec = match(DEC_DIGIT);
						setState(1017);
						match(RR_BRACKET);
						}
					}

					}
				}

				}
				break;
			case 4:
				{
				setState(1023);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SINGLE_QUOTE) {
					{
					setState(1022);
					match(SINGLE_QUOTE);
					}
				}

				setState(1025);
				intervalTemporal();
				setState(1029);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 166977311877365502L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 2559313295928627441L) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & 5764840620175130667L) != 0) || ((((_la - 196)) & ~0x3f) == 0 && ((1L << (_la - 196)) & 1083309430590733073L) != 0) || ((((_la - 263)) & ~0x3f) == 0 && ((1L << (_la - 263)) & 1188965700442522285L) != 0) || ((((_la - 342)) & ~0x3f) == 0 && ((1L << (_la - 342)) & 4611708558427291687L) != 0) || ((((_la - 411)) & ~0x3f) == 0 && ((1L << (_la - 411)) & -360287970189639679L) != 0) || ((((_la - 476)) & ~0x3f) == 0 && ((1L << (_la - 476)) & 213677768711L) != 0)) {
					{
					{
					setState(1026);
					intervalTemporal();
					}
					}
					setState(1031);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1033);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SINGLE_QUOTE) {
					{
					setState(1032);
					match(SINGLE_QUOTE);
					}
				}

				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntervalTemporalContext extends ParserRuleContext {
		public ExpressionContext intervalExpr;
		public IntervalUnitContext intervalUnit() {
			return getRuleContext(IntervalUnitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IntervalTemporalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalTemporal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterIntervalTemporal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitIntervalTemporal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitIntervalTemporal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalTemporalContext intervalTemporal() throws RecognitionException {
		IntervalTemporalContext _localctx = new IntervalTemporalContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_intervalTemporal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1037);
			((IntervalTemporalContext)_localctx).intervalExpr = expression(0);
			setState(1038);
			intervalUnit();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DataTypeContext extends ParserRuleContext {
		public Token scaledType;
		public IdContext extType;
		public Token precision;
		public Token scale;
		public IdContext unscaledType;
		public TerminalNode LR_BRACKET() { return getToken(SqlQueryParser.LR_BRACKET, 0); }
		public List<TerminalNode> DEC_DIGITS() { return getTokens(SqlQueryParser.DEC_DIGITS); }
		public TerminalNode DEC_DIGITS(int i) {
			return getToken(SqlQueryParser.DEC_DIGITS, i);
		}
		public TerminalNode RR_BRACKET() { return getToken(SqlQueryParser.RR_BRACKET, 0); }
		public TerminalNode CHAR() { return getToken(SqlQueryParser.CHAR, 0); }
		public TerminalNode VARCHAR() { return getToken(SqlQueryParser.VARCHAR, 0); }
		public TerminalNode NCHAR() { return getToken(SqlQueryParser.NCHAR, 0); }
		public TerminalNode NVARCHAR() { return getToken(SqlQueryParser.NVARCHAR, 0); }
		public TerminalNode BINARY() { return getToken(SqlQueryParser.BINARY, 0); }
		public TerminalNode VARBINARY() { return getToken(SqlQueryParser.VARBINARY, 0); }
		public TerminalNode COMMA() { return getToken(SqlQueryParser.COMMA, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_dataType);
		int _la;
		try {
			setState(1057);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1040);
				((DataTypeContext)_localctx).scaledType = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 25770049536L) != 0)) ) {
					((DataTypeContext)_localctx).scaledType = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1041);
				match(LR_BRACKET);
				setState(1042);
				match(DEC_DIGITS);
				setState(1043);
				match(RR_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1044);
				((DataTypeContext)_localctx).extType = id();
				setState(1045);
				match(LR_BRACKET);
				setState(1046);
				((DataTypeContext)_localctx).precision = match(DEC_DIGITS);
				setState(1047);
				match(COMMA);
				setState(1048);
				((DataTypeContext)_localctx).scale = match(DEC_DIGITS);
				setState(1049);
				match(RR_BRACKET);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1051);
				((DataTypeContext)_localctx).extType = id();
				setState(1052);
				match(LR_BRACKET);
				setState(1053);
				((DataTypeContext)_localctx).scale = match(DEC_DIGITS);
				setState(1054);
				match(RR_BRACKET);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1056);
				((DataTypeContext)_localctx).unscaledType = id();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstantContext extends ParserRuleContext {
		public Token dollar;
		public TerminalNode STRING() { return getToken(SqlQueryParser.STRING, 0); }
		public TerminalNode HEX_NUMBER() { return getToken(SqlQueryParser.HEX_NUMBER, 0); }
		public TerminalNode DEC_DIGITS() { return getToken(SqlQueryParser.DEC_DIGITS, 0); }
		public SignContext sign() {
			return getRuleContext(SignContext.class,0);
		}
		public TerminalNode REAL_NUMBER() { return getToken(SqlQueryParser.REAL_NUMBER, 0); }
		public TerminalNode DEC_NUMBER() { return getToken(SqlQueryParser.DEC_NUMBER, 0); }
		public TerminalNode DOLLAR() { return getToken(SqlQueryParser.DOLLAR, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_constant);
		int _la;
		try {
			setState(1074);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1059);
				match(STRING);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1060);
				match(HEX_NUMBER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1062);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(1061);
					sign();
					}
				}

				setState(1064);
				match(DEC_DIGITS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1066);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(1065);
					sign();
					}
				}

				setState(1068);
				_la = _input.LA(1);
				if ( !(_la==DEC_NUMBER || _la==REAL_NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1070);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(1069);
					sign();
					}
				}

				setState(1072);
				((ConstantContext)_localctx).dollar = match(DOLLAR);
				setState(1073);
				_la = _input.LA(1);
				if ( !(_la==DEC_DIGITS || _la==DEC_NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SignContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(SqlQueryParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(SqlQueryParser.MINUS, 0); }
		public SignContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterSign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitSign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitSign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignContext sign() throws RecognitionException {
		SignContext _localctx = new SignContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_sign);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1076);
			_la = _input.LA(1);
			if ( !(_la==PLUS || _la==MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(SqlQueryParser.SELECT, 0); }
		public TerminalNode DISTINCT() { return getToken(SqlQueryParser.DISTINCT, 0); }
		public TerminalNode AS() { return getToken(SqlQueryParser.AS, 0); }
		public TerminalNode FROM() { return getToken(SqlQueryParser.FROM, 0); }
		public TerminalNode WHERE() { return getToken(SqlQueryParser.WHERE, 0); }
		public TerminalNode GROUP() { return getToken(SqlQueryParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(SqlQueryParser.BY, 0); }
		public TerminalNode HAVING() { return getToken(SqlQueryParser.HAVING, 0); }
		public TerminalNode ORDER() { return getToken(SqlQueryParser.ORDER, 0); }
		public TerminalNode ASC() { return getToken(SqlQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(SqlQueryParser.DESC, 0); }
		public TerminalNode OVER() { return getToken(SqlQueryParser.OVER, 0); }
		public TerminalNode LIMIT() { return getToken(SqlQueryParser.LIMIT, 0); }
		public TerminalNode OFFSET() { return getToken(SqlQueryParser.OFFSET, 0); }
		public TerminalNode FIRST() { return getToken(SqlQueryParser.FIRST, 0); }
		public TerminalNode LAST() { return getToken(SqlQueryParser.LAST, 0); }
		public TerminalNode NEXT() { return getToken(SqlQueryParser.NEXT, 0); }
		public TerminalNode ROW() { return getToken(SqlQueryParser.ROW, 0); }
		public TerminalNode ROWS() { return getToken(SqlQueryParser.ROWS, 0); }
		public TerminalNode ONLY() { return getToken(SqlQueryParser.ONLY, 0); }
		public TerminalNode BIT() { return getToken(SqlQueryParser.BIT, 0); }
		public TerminalNode BOOL() { return getToken(SqlQueryParser.BOOL, 0); }
		public TerminalNode TINYINT() { return getToken(SqlQueryParser.TINYINT, 0); }
		public TerminalNode SMALLINT() { return getToken(SqlQueryParser.SMALLINT, 0); }
		public TerminalNode INT() { return getToken(SqlQueryParser.INT, 0); }
		public TerminalNode INTEGER() { return getToken(SqlQueryParser.INTEGER, 0); }
		public TerminalNode BIGINT() { return getToken(SqlQueryParser.BIGINT, 0); }
		public TerminalNode UNSIGNED() { return getToken(SqlQueryParser.UNSIGNED, 0); }
		public TerminalNode NUMBER() { return getToken(SqlQueryParser.NUMBER, 0); }
		public TerminalNode NUMERIC() { return getToken(SqlQueryParser.NUMERIC, 0); }
		public TerminalNode DEC_DIGITS() { return getToken(SqlQueryParser.DEC_DIGITS, 0); }
		public TerminalNode MONEY() { return getToken(SqlQueryParser.MONEY, 0); }
		public TerminalNode DOUBLE() { return getToken(SqlQueryParser.DOUBLE, 0); }
		public TerminalNode FLOAT() { return getToken(SqlQueryParser.FLOAT, 0); }
		public TerminalNode REAL() { return getToken(SqlQueryParser.REAL, 0); }
		public TerminalNode PRECISION() { return getToken(SqlQueryParser.PRECISION, 0); }
		public TerminalNode CHAR() { return getToken(SqlQueryParser.CHAR, 0); }
		public TerminalNode NCHAR() { return getToken(SqlQueryParser.NCHAR, 0); }
		public TerminalNode VARCHAR() { return getToken(SqlQueryParser.VARCHAR, 0); }
		public TerminalNode NVARCHAR() { return getToken(SqlQueryParser.NVARCHAR, 0); }
		public TerminalNode UUID() { return getToken(SqlQueryParser.UUID, 0); }
		public TerminalNode CLOB() { return getToken(SqlQueryParser.CLOB, 0); }
		public TerminalNode NCLOB() { return getToken(SqlQueryParser.NCLOB, 0); }
		public TerminalNode JSON() { return getToken(SqlQueryParser.JSON, 0); }
		public TerminalNode XML() { return getToken(SqlQueryParser.XML, 0); }
		public TerminalNode TEXT() { return getToken(SqlQueryParser.TEXT, 0); }
		public TerminalNode NTEXT() { return getToken(SqlQueryParser.NTEXT, 0); }
		public TerminalNode DATE() { return getToken(SqlQueryParser.DATE, 0); }
		public TerminalNode TIME() { return getToken(SqlQueryParser.TIME, 0); }
		public TerminalNode DATETIME() { return getToken(SqlQueryParser.DATETIME, 0); }
		public TerminalNode TIMESTAMP() { return getToken(SqlQueryParser.TIMESTAMP, 0); }
		public TerminalNode INTERVAL() { return getToken(SqlQueryParser.INTERVAL, 0); }
		public TerminalNode ZONE() { return getToken(SqlQueryParser.ZONE, 0); }
		public TerminalNode VECTOR() { return getToken(SqlQueryParser.VECTOR, 0); }
		public TerminalNode BINARY() { return getToken(SqlQueryParser.BINARY, 0); }
		public TerminalNode VARYING() { return getToken(SqlQueryParser.VARYING, 0); }
		public TerminalNode BLOB() { return getToken(SqlQueryParser.BLOB, 0); }
		public TerminalNode BFILE() { return getToken(SqlQueryParser.BFILE, 0); }
		public TerminalNode GEOGRAPHY() { return getToken(SqlQueryParser.GEOGRAPHY, 0); }
		public TerminalNode GEOMETRY() { return getToken(SqlQueryParser.GEOMETRY, 0); }
		public TerminalNode ANY() { return getToken(SqlQueryParser.ANY, 0); }
		public TerminalNode CASE() { return getToken(SqlQueryParser.CASE, 0); }
		public TerminalNode WHEN() { return getToken(SqlQueryParser.WHEN, 0); }
		public TerminalNode IF() { return getToken(SqlQueryParser.IF, 0); }
		public TerminalNode THEN() { return getToken(SqlQueryParser.THEN, 0); }
		public TerminalNode ELSE() { return getToken(SqlQueryParser.ELSE, 0); }
		public TerminalNode BEGIN() { return getToken(SqlQueryParser.BEGIN, 0); }
		public TerminalNode END() { return getToken(SqlQueryParser.END, 0); }
		public TerminalNode IIF() { return getToken(SqlQueryParser.IIF, 0); }
		public TerminalNode NULL_() { return getToken(SqlQueryParser.NULL_, 0); }
		public TerminalNode IS() { return getToken(SqlQueryParser.IS, 0); }
		public TerminalNode NOT() { return getToken(SqlQueryParser.NOT, 0); }
		public TerminalNode AND() { return getToken(SqlQueryParser.AND, 0); }
		public TerminalNode OR() { return getToken(SqlQueryParser.OR, 0); }
		public TerminalNode YEAR() { return getToken(SqlQueryParser.YEAR, 0); }
		public TerminalNode QUARTER() { return getToken(SqlQueryParser.QUARTER, 0); }
		public TerminalNode MONTH() { return getToken(SqlQueryParser.MONTH, 0); }
		public TerminalNode WEEK() { return getToken(SqlQueryParser.WEEK, 0); }
		public TerminalNode DAY() { return getToken(SqlQueryParser.DAY, 0); }
		public TerminalNode HOUR() { return getToken(SqlQueryParser.HOUR, 0); }
		public TerminalNode MINUTE() { return getToken(SqlQueryParser.MINUTE, 0); }
		public TerminalNode SECOND() { return getToken(SqlQueryParser.SECOND, 0); }
		public TerminalNode MICROSECOND() { return getToken(SqlQueryParser.MICROSECOND, 0); }
		public TerminalNode MILLISECOND() { return getToken(SqlQueryParser.MILLISECOND, 0); }
		public TerminalNode NANOSECOND() { return getToken(SqlQueryParser.NANOSECOND, 0); }
		public TerminalNode SUM() { return getToken(SqlQueryParser.SUM, 0); }
		public TerminalNode COUNT() { return getToken(SqlQueryParser.COUNT, 0); }
		public TerminalNode MAX() { return getToken(SqlQueryParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(SqlQueryParser.MIN, 0); }
		public TerminalNode AVG() { return getToken(SqlQueryParser.AVG, 0); }
		public TerminalNode DATEADD() { return getToken(SqlQueryParser.DATEADD, 0); }
		public TerminalNode CURRENT_DATE() { return getToken(SqlQueryParser.CURRENT_DATE, 0); }
		public TerminalNode CURRENT_TIME() { return getToken(SqlQueryParser.CURRENT_TIME, 0); }
		public TerminalNode CURRENT_TIMESTAMP() { return getToken(SqlQueryParser.CURRENT_TIMESTAMP, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1078);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 22862123801509630L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -4460806539790505977L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 144143775445811205L) != 0) || ((((_la - 204)) & ~0x3f) == 0 && ((1L << (_la - 204)) & 7494553279922176137L) != 0) || ((((_la - 268)) & ~0x3f) == 0 && ((1L << (_la - 268)) & 1125900083527701L) != 0) || ((((_la - 342)) & ~0x3f) == 0 && ((1L << (_la - 342)) & 4611708008668332069L) != 0) || ((((_la - 472)) & ~0x3f) == 0 && ((1L << (_la - 472)) & 3298534883329L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SqlQueryParser.ID, 0); }
		public TerminalNode DOUBLE_QUOTE_ID() { return getToken(SqlQueryParser.DOUBLE_QUOTE_ID, 0); }
		public TerminalNode BACK_QUOTE_ID() { return getToken(SqlQueryParser.BACK_QUOTE_ID, 0); }
		public TerminalNode SQUARE_BRACKET_ID() { return getToken(SqlQueryParser.SQUARE_BRACKET_ID, 0); }
		public KeywordContext keyword() {
			return getRuleContext(KeywordContext.class,0);
		}
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_id);
		try {
			setState(1085);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(1080);
				match(ID);
				}
				break;
			case DOUBLE_QUOTE_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(1081);
				match(DOUBLE_QUOTE_ID);
				}
				break;
			case BACK_QUOTE_ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(1082);
				match(BACK_QUOTE_ID);
				}
				break;
			case SQUARE_BRACKET_ID:
				enterOuterAlt(_localctx, 4);
				{
				setState(1083);
				match(SQUARE_BRACKET_ID);
				}
				break;
			case BOOL:
			case TINYINT:
			case SMALLINT:
			case INT:
			case INTEGER:
			case BIGINT:
			case UNSIGNED:
			case NUMERIC:
			case MONEY:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case NVARCHAR:
			case UUID:
			case BIT:
			case CLOB:
			case NCLOB:
			case TEXT:
			case NTEXT:
			case JSON:
			case XML:
			case DATE:
			case TIME:
			case DATETIME:
			case TIMESTAMP:
			case INTERVAL:
			case VECTOR:
			case BINARY:
			case BLOB:
			case BFILE:
			case GEOGRAPHY:
			case GEOMETRY:
			case ANY:
			case AND:
			case AS:
			case ASC:
			case BEGIN:
			case BY:
			case CASE:
			case CURRENT_DATE:
			case CURRENT_TIME:
			case CURRENT_TIMESTAMP:
			case DESC:
			case DISTINCT:
			case ELSE:
			case END:
			case FROM:
			case GROUP:
			case HAVING:
			case IF:
			case IS:
			case NOT:
			case NULL_:
			case OFFSET:
			case OR:
			case ORDER:
			case OVER:
			case PRECISION:
			case SELECT:
			case THEN:
			case VARYING:
			case WHEN:
			case WHERE:
			case AVG:
			case COUNT:
			case DATEADD:
			case DAY:
			case FIRST:
			case HOUR:
			case LAST:
			case MAX:
			case MIN:
			case MINUTE:
			case MICROSECOND:
			case MILLISECOND:
			case NANOSECOND:
			case MONTH:
			case NEXT:
			case NUMBER:
			case ONLY:
			case QUARTER:
			case ROW:
			case ROWS:
			case SECOND:
			case SUM:
			case YEAR:
			case ZONE:
			case IIF:
			case DEC_DIGITS:
			case LIMIT:
			case WEEK:
				enterOuterAlt(_localctx, 5);
				{
				setState(1084);
				keyword();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonOperatorContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(SqlQueryParser.EQUAL, 0); }
		public TerminalNode GREATER() { return getToken(SqlQueryParser.GREATER, 0); }
		public TerminalNode LESS() { return getToken(SqlQueryParser.LESS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(SqlQueryParser.EXCLAMATION, 0); }
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterComparisonOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitComparisonOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitComparisonOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_comparisonOperator);
		try {
			setState(1102);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1087);
				match(EQUAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1088);
				match(GREATER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1089);
				match(LESS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1090);
				match(LESS);
				setState(1091);
				match(EQUAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1092);
				match(GREATER);
				setState(1093);
				match(EQUAL);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1094);
				match(LESS);
				setState(1095);
				match(GREATER);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1096);
				match(EXCLAMATION);
				setState(1097);
				match(EQUAL);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1098);
				match(EXCLAMATION);
				setState(1099);
				match(GREATER);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1100);
				match(EXCLAMATION);
				setState(1101);
				match(LESS);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BitOperatorContext extends ParserRuleContext {
		public List<TerminalNode> GREATER() { return getTokens(SqlQueryParser.GREATER); }
		public TerminalNode GREATER(int i) {
			return getToken(SqlQueryParser.GREATER, i);
		}
		public List<TerminalNode> LESS() { return getTokens(SqlQueryParser.LESS); }
		public TerminalNode LESS(int i) {
			return getToken(SqlQueryParser.LESS, i);
		}
		public TerminalNode BIT_AND() { return getToken(SqlQueryParser.BIT_AND, 0); }
		public TerminalNode BIT_OR() { return getToken(SqlQueryParser.BIT_OR, 0); }
		public TerminalNode BIT_XOR() { return getToken(SqlQueryParser.BIT_XOR, 0); }
		public BitOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bitOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterBitOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitBitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitBitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BitOperatorContext bitOperator() throws RecognitionException {
		BitOperatorContext _localctx = new BitOperatorContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_bitOperator);
		try {
			setState(1114);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1104);
				match(GREATER);
				setState(1105);
				match(GREATER);
				setState(1106);
				match(GREATER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1107);
				match(GREATER);
				setState(1108);
				match(GREATER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1109);
				match(LESS);
				setState(1110);
				match(LESS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1111);
				match(BIT_AND);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1112);
				match(BIT_OR);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1113);
				match(BIT_XOR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentOperatorContext extends ParserRuleContext {
		public TerminalNode PLUS_ASSIGN() { return getToken(SqlQueryParser.PLUS_ASSIGN, 0); }
		public TerminalNode MINUS_ASSIGN() { return getToken(SqlQueryParser.MINUS_ASSIGN, 0); }
		public TerminalNode MULT_ASSIGN() { return getToken(SqlQueryParser.MULT_ASSIGN, 0); }
		public TerminalNode DIV_ASSIGN() { return getToken(SqlQueryParser.DIV_ASSIGN, 0); }
		public TerminalNode MOD_ASSIGN() { return getToken(SqlQueryParser.MOD_ASSIGN, 0); }
		public TerminalNode AND_ASSIGN() { return getToken(SqlQueryParser.AND_ASSIGN, 0); }
		public TerminalNode XOR_ASSIGN() { return getToken(SqlQueryParser.XOR_ASSIGN, 0); }
		public TerminalNode OR_ASSIGN() { return getToken(SqlQueryParser.OR_ASSIGN, 0); }
		public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).enterAssignmentOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlQueryParserListener ) ((SqlQueryParserListener)listener).exitAssignmentOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlQueryParserVisitor ) return ((SqlQueryParserVisitor<? extends T>)visitor).visitAssignmentOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentOperatorContext assignmentOperator() throws RecognitionException {
		AssignmentOperatorContext _localctx = new AssignmentOperatorContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_assignmentOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1116);
			_la = _input.LA(1);
			if ( !(((((_la - 483)) & ~0x3f) == 0 && ((1L << (_la - 483)) & 255L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return criteria_sempred((CriteriaContext)_localctx, predIndex);
		case 29:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean criteria_sempred(CriteriaContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0203\u045f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0001\u0000\u0003\u0000\u008a"+
		"\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001\u0090"+
		"\b\u0001\u0001\u0001\u0003\u0001\u0093\b\u0001\u0001\u0001\u0003\u0001"+
		"\u0096\b\u0001\u0001\u0001\u0003\u0001\u0099\b\u0001\u0001\u0001\u0003"+
		"\u0001\u009c\b\u0001\u0001\u0002\u0001\u0002\u0003\u0002\u00a0\b\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u0003\u00a8\b\u0003\n\u0003\f\u0003\u00ab\t\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0005\u0005\u00b5\b\u0005\n\u0005\f\u0005\u00b8\t\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u00bc\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0005\u0006\u00c3\b\u0006\n\u0006\f\u0006\u00c6"+
		"\t\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007\u00db\b\u0007\u0003\u0007\u00dd\b\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u00e4\b\b\u0001\t\u0001"+
		"\t\u0005\t\u00e8\b\t\n\t\f\t\u00eb\t\t\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0005\t\u00f1\b\t\n\t\f\t\u00f4\t\t\u0003\t\u00f6\b\t\u0001\n\u0001\n"+
		"\u0003\n\u00fa\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0101"+
		"\b\n\u0003\n\u0103\b\n\u0001\n\u0001\n\u0001\n\u0003\n\u0108\b\n\u0003"+
		"\n\u010a\b\n\u0001\n\u0001\n\u0003\n\u010e\b\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u0115\b\n\u0003\n\u0117\b\n\u0003\n\u0119\b\n"+
		"\u0001\u000b\u0003\u000b\u011c\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u0120\b\u000b\u0003\u000b\u0122\b\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u012c\b\u000b\u0001\f\u0001\f\u0003\f\u0130\b\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0003\f\u0137\b\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0005\f\u013f\b\f\n\f\f\f\u0142\t\f\u0001\r\u0003\r"+
		"\u0145\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0153\b\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0003\r\u015b\b\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0003\r\u0164\b\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u016a\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0170\b\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0176\b\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u017b\b\r\u0001\r\u0001\r\u0003\r\u017f\b\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u0187"+
		"\b\u000e\n\u000e\f\u000e\u018a\t\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0193\b\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u019e\b\u000f\n\u000f"+
		"\f\u000f\u01a1\t\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u01a6\b\u0010\n\u0010\f\u0010\u01a9\t\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0005\u0011\u01ae\b\u0011\n\u0011\f\u0011\u01b1\t\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u01b6\b\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u01bb\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u01c1\b\u0013\u0001\u0014\u0001\u0014\u0003\u0014"+
		"\u01c5\b\u0014\u0001\u0014\u0003\u0014\u01c8\b\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u01d0"+
		"\b\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u01d4\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016"+
		"\u01dc\b\u0016\u0003\u0016\u01de\b\u0016\u0001\u0017\u0001\u0017\u0003"+
		"\u0017\u01e2\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u01e7"+
		"\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003"+
		"\u0018\u01ee\b\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0003\u0019\u01f3"+
		"\b\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0003\u001a\u01f8\b\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0003\u001b\u01fe\b\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u0204\b\u001c"+
		"\n\u001c\f\u001c\u0207\t\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u0213\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u0225\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0005\u001d\u022c\b\u001d\n\u001d\f\u001d\u022f\t\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u0235\b\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0004\u001f\u023e\b\u001f\u000b\u001f\f\u001f\u023f\u0001"+
		"\u001f\u0001\u001f\u0003\u001f\u0244\b\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0004"+
		"\u001f\u024e\b\u001f\u000b\u001f\f\u001f\u024f\u0001\u001f\u0001\u001f"+
		"\u0003\u001f\u0254\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u0258\b"+
		"\u001f\u0001 \u0001 \u0001 \u0001 \u0003 \u025e\b \u0001!\u0001!\u0001"+
		"!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u0268\b!\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0003\"\u0273\b\"\u0001"+
		"#\u0001#\u0001$\u0001$\u0001$\u0001$\u0005$\u027b\b$\n$\f$\u027e\t$\u0001"+
		"%\u0001%\u0001%\u0001%\u0001%\u0003%\u0285\b%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u0291\b&\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u02a5"+
		"\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u02da\b\'\u0001"+
		"\'\u0001\'\u0003\'\u02de\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u02fc\b\'\u0001(\u0001(\u0001"+
		"(\u0001(\u0003(\u0302\b(\u0001(\u0001(\u0003(\u0306\b(\u0001(\u0001(\u0001"+
		")\u0001)\u0001)\u0001)\u0003)\u030e\b)\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001*\u0003*\u0317\b*\u0001*\u0001*\u0001*\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0003,\u032a\b,\u0001,\u0001,\u0001,\u0001-\u0001-\u0001-\u0001"+
		"-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u033a\b.\u0001"+
		".\u0001.\u0001.\u0001/\u0001/\u0001/\u0001/\u0001/\u00010\u00010\u0001"+
		"0\u00010\u00010\u00010\u00030\u034a\b0\u00010\u00010\u00010\u00011\u0001"+
		"1\u00011\u00011\u00011\u00012\u00012\u00012\u00012\u00012\u00013\u0001"+
		"3\u00013\u00013\u00013\u00014\u00014\u00014\u00014\u00014\u00034\u0363"+
		"\b4\u00014\u00014\u00014\u00014\u00034\u0369\b4\u00014\u00014\u00034\u036d"+
		"\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00034\u0379\b4\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00035\u0388\b5\u00035\u038a\b5\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00035\u0397\b5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00035\u03a7\b5\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00035\u03af\b5\u00015\u00015\u00035\u03b3"+
		"\b5\u00016\u00036\u03b6\b6\u00016\u00016\u00017\u00017\u00017\u00017\u0001"+
		"7\u00037\u03bf\b7\u00017\u00037\u03c2\b7\u00017\u00017\u00018\u00018\u0001"+
		"9\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00019\u00039\u03d4\b9\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0003"+
		":\u03dc\b:\u0001:\u0001:\u0001:\u0001:\u0001:\u0003:\u03e3\b:\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0003:\u03ea\b:\u0001:\u0001:\u0001:\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0003:\u03f4\b:\u0001:\u0001:\u0001:\u0001:\u0001"+
		":\u0003:\u03fb\b:\u0003:\u03fd\b:\u0001:\u0003:\u0400\b:\u0001:\u0001"+
		":\u0005:\u0404\b:\n:\f:\u0407\t:\u0001:\u0003:\u040a\b:\u0003:\u040c\b"+
		":\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0003<\u0422\b<\u0001=\u0001=\u0001=\u0003=\u0427\b=\u0001=\u0001=\u0003"+
		"=\u042b\b=\u0001=\u0001=\u0003=\u042f\b=\u0001=\u0001=\u0003=\u0433\b"+
		"=\u0001>\u0001>\u0001?\u0001?\u0001@\u0001@\u0001@\u0001@\u0001@\u0003"+
		"@\u043e\b@\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0003A\u044f\bA\u0001B\u0001"+
		"B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0003B\u045b"+
		"\bB\u0001C\u0001C\u0001C\u0000\u0002\u0018:D\u0000\u0002\u0004\u0006\b"+
		"\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02"+
		"468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0000"+
		"\u0016\u0002\u0000\u0156\u0156\u0158\u0158\u0002\u0000\u00e8\u00e8\u0123"+
		"\u0123\u0003\u0000ccss\u0096\u0096\u0003\u0000\'\'))\u009c\u009c\u0002"+
		"\u0000\u00d8\u00d8\u00f7\u00f7\u0002\u0000--PP\u0001\u0000\u01f7\u01f9"+
		"\u0002\u0000\u01eb\u01eb\u01fa\u01fb\u0001\u0000\u01fa\u01fb\u0003\u0000"+
		"\u00d9\u00d9\u0143\u0143\u0157\u0157\u0006\u0000\u00bf\u00bf\u0107\u0107"+
		"\u0109\u0109\u016a\u016b\u016d\u016d\u017d\u017d\u0002\u0000\u00e9\u00e9"+
		"\u00fe\u00fe\u0002\u0000\u00fc\u00fc\u00ff\u00ff\u0002\u0000\u00cd\u00cd"+
		"\u0130\u0130\u0001\u0000\u0131\u0132\u0002\u0000))RR\b\u0000\u00d3\u00d4"+
		"\u00f3\u00f4\u010a\u010f\u011f\u0120\u013e\u013f\u015b\u015c\u0180\u0181"+
		"\u0201\u0202\u0002\u0000\u000e\u0011!\"\u0001\u0000\u01dd\u01de\u0002"+
		"\u0000\u01d8\u01d8\u01dd\u01dd3\u0000\u0001\u0007\t\u001f!!#\'+-00446"+
		"6EGPPRRWXbbfgiippxy~~\u0083\u0084\u0086\u0086\u0088\u0088\u0099\u0099"+
		"\u00a0\u00a0\u00af\u00af\u00b1\u00b2\u00bf\u00bf\u00cc\u00cc\u00cf\u00cf"+
		"\u00d3\u00d3\u00e8\u00e8\u00f3\u00f3\u00fd\u00fd\u0107\u0107\u0109\u010a"+
		"\u010c\u010c\u010e\u010e\u0110\u0110\u011f\u011f\u0123\u0123\u0125\u0125"+
		"\u0127\u0127\u013e\u013e\u0156\u0156\u0158\u0158\u015b\u015b\u016d\u016d"+
		"\u0180\u0180\u0182\u0182\u0194\u0194\u01d8\u01d8\u0200\u0201\u0001\u0000"+
		"\u01e3\u01ea\u04f6\u0000\u0089\u0001\u0000\u0000\u0000\u0002\u008d\u0001"+
		"\u0000\u0000\u0000\u0004\u009d\u0001\u0000\u0000\u0000\u0006\u00a3\u0001"+
		"\u0000\u0000\u0000\b\u00ac\u0001\u0000\u0000\u0000\n\u00af\u0001\u0000"+
		"\u0000\u0000\f\u00bd\u0001\u0000\u0000\u0000\u000e\u00dc\u0001\u0000\u0000"+
		"\u0000\u0010\u00e3\u0001\u0000\u0000\u0000\u0012\u00f5\u0001\u0000\u0000"+
		"\u0000\u0014\u0118\u0001\u0000\u0000\u0000\u0016\u012b\u0001\u0000\u0000"+
		"\u0000\u0018\u012d\u0001\u0000\u0000\u0000\u001a\u017e\u0001\u0000\u0000"+
		"\u0000\u001c\u0192\u0001\u0000\u0000\u0000\u001e\u0194\u0001\u0000\u0000"+
		"\u0000 \u01a2\u0001\u0000\u0000\u0000\"\u01aa\u0001\u0000\u0000\u0000"+
		"$\u01b5\u0001\u0000\u0000\u0000&\u01c0\u0001\u0000\u0000\u0000(\u01c4"+
		"\u0001\u0000\u0000\u0000*\u01d3\u0001\u0000\u0000\u0000,\u01dd\u0001\u0000"+
		"\u0000\u0000.\u01df\u0001\u0000\u0000\u00000\u01ed\u0001\u0000\u0000\u0000"+
		"2\u01f2\u0001\u0000\u0000\u00004\u01f7\u0001\u0000\u0000\u00006\u01fd"+
		"\u0001\u0000\u0000\u00008\u01ff\u0001\u0000\u0000\u0000:\u0212\u0001\u0000"+
		"\u0000\u0000<\u0234\u0001\u0000\u0000\u0000>\u0257\u0001\u0000\u0000\u0000"+
		"@\u025d\u0001\u0000\u0000\u0000B\u0267\u0001\u0000\u0000\u0000D\u0272"+
		"\u0001\u0000\u0000\u0000F\u0274\u0001\u0000\u0000\u0000H\u0276\u0001\u0000"+
		"\u0000\u0000J\u027f\u0001\u0000\u0000\u0000L\u0290\u0001\u0000\u0000\u0000"+
		"N\u02fb\u0001\u0000\u0000\u0000P\u0301\u0001\u0000\u0000\u0000R\u030d"+
		"\u0001\u0000\u0000\u0000T\u0316\u0001\u0000\u0000\u0000V\u031b\u0001\u0000"+
		"\u0000\u0000X\u0329\u0001\u0000\u0000\u0000Z\u032e\u0001\u0000\u0000\u0000"+
		"\\\u0339\u0001\u0000\u0000\u0000^\u033e\u0001\u0000\u0000\u0000`\u0349"+
		"\u0001\u0000\u0000\u0000b\u034e\u0001\u0000\u0000\u0000d\u0353\u0001\u0000"+
		"\u0000\u0000f\u0358\u0001\u0000\u0000\u0000h\u0378\u0001\u0000\u0000\u0000"+
		"j\u03b2\u0001\u0000\u0000\u0000l\u03b5\u0001\u0000\u0000\u0000n\u03b9"+
		"\u0001\u0000\u0000\u0000p\u03c5\u0001\u0000\u0000\u0000r\u03d3\u0001\u0000"+
		"\u0000\u0000t\u03d5\u0001\u0000\u0000\u0000v\u040d\u0001\u0000\u0000\u0000"+
		"x\u0421\u0001\u0000\u0000\u0000z\u0432\u0001\u0000\u0000\u0000|\u0434"+
		"\u0001\u0000\u0000\u0000~\u0436\u0001\u0000\u0000\u0000\u0080\u043d\u0001"+
		"\u0000\u0000\u0000\u0082\u044e\u0001\u0000\u0000\u0000\u0084\u045a\u0001"+
		"\u0000\u0000\u0000\u0086\u045c\u0001\u0000\u0000\u0000\u0088\u008a\u0003"+
		"H$\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000"+
		"\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008c\u0003\u0002\u0001"+
		"\u0000\u008c\u0001\u0001\u0000\u0000\u0000\u008d\u008f\u0003\u0004\u0002"+
		"\u0000\u008e\u0090\u0003\u0006\u0003\u0000\u008f\u008e\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0092\u0001\u0000\u0000"+
		"\u0000\u0091\u0093\u0003\b\u0004\u0000\u0092\u0091\u0001\u0000\u0000\u0000"+
		"\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0095\u0001\u0000\u0000\u0000"+
		"\u0094\u0096\u0003\n\u0005\u0000\u0095\u0094\u0001\u0000\u0000\u0000\u0095"+
		"\u0096\u0001\u0000\u0000\u0000\u0096\u0098\u0001\u0000\u0000\u0000\u0097"+
		"\u0099\u0003\f\u0006\u0000\u0098\u0097\u0001\u0000\u0000\u0000\u0098\u0099"+
		"\u0001\u0000\u0000\u0000\u0099\u009b\u0001\u0000\u0000\u0000\u009a\u009c"+
		"\u0003\u000e\u0007\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009b\u009c"+
		"\u0001\u0000\u0000\u0000\u009c\u0003\u0001\u0000\u0000\u0000\u009d\u009f"+
		"\u0005\u0099\u0000\u0000\u009e\u00a0\u0005R\u0000\u0000\u009f\u009e\u0001"+
		"\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001"+
		"\u0000\u0000\u0000\u00a1\u00a2\u0003\"\u0011\u0000\u00a2\u0005\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a4\u0005b\u0000\u0000\u00a4\u00a9\u0003\u0010\b"+
		"\u0000\u00a5\u00a6\u0005\u01f3\u0000\u0000\u00a6\u00a8\u0003\u0010\b\u0000"+
		"\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000"+
		"\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000"+
		"\u00aa\u0007\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000"+
		"\u00ac\u00ad\u0005\u00b2\u0000\u0000\u00ad\u00ae\u0003\u0018\f\u0000\u00ae"+
		"\t\u0001\u0000\u0000\u0000\u00af\u00b0\u0005f\u0000\u0000\u00b0\u00b1"+
		"\u00054\u0000\u0000\u00b1\u00b6\u0003:\u001d\u0000\u00b2\u00b3\u0005\u01f3"+
		"\u0000\u0000\u00b3\u00b5\u0003:\u001d\u0000\u00b4\u00b2\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b8\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00bb\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005g\u0000\u0000"+
		"\u00ba\u00bc\u0003\u0018\f\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bb"+
		"\u00bc\u0001\u0000\u0000\u0000\u00bc\u000b\u0001\u0000\u0000\u0000\u00bd"+
		"\u00be\u0005\u0084\u0000\u0000\u00be\u00bf\u00054\u0000\u0000\u00bf\u00c4"+
		"\u0003.\u0017\u0000\u00c0\u00c1\u0005\u01f3\u0000\u0000\u00c1\u00c3\u0003"+
		".\u0017\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c3\u00c6\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000"+
		"\u0000\u0000\u00c5\r\u0001\u0000\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c8\u0005\u0200\u0000\u0000\u00c8\u00c9\u0003:\u001d\u0000"+
		"\u00c9\u00ca\u0005~\u0000\u0000\u00ca\u00cb\u0003:\u001d\u0000\u00cb\u00dd"+
		"\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005\u0200\u0000\u0000\u00cd\u00ce"+
		"\u0003:\u001d\u0000\u00ce\u00cf\u0005\u01f3\u0000\u0000\u00cf\u00d0\u0003"+
		":\u001d\u0000\u00d0\u00dd\u0001\u0000\u0000\u0000\u00d1\u00d2\u0005~\u0000"+
		"\u0000\u00d2\u00d3\u0003:\u001d\u0000\u00d3\u00da\u0007\u0000\u0000\u0000"+
		"\u00d4\u00d5\u0005^\u0000\u0000\u00d5\u00d6\u0007\u0001\u0000\u0000\u00d6"+
		"\u00d7\u0003:\u001d\u0000\u00d7\u00d8\u0007\u0000\u0000\u0000\u00d8\u00d9"+
		"\u0005\u0127\u0000\u0000\u00d9\u00db\u0001\u0000\u0000\u0000\u00da\u00d4"+
		"\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000\u0000\u00db\u00dd"+
		"\u0001\u0000\u0000\u0000\u00dc\u00c7\u0001\u0000\u0000\u0000\u00dc\u00cc"+
		"\u0001\u0000\u0000\u0000\u00dc\u00d1\u0001\u0000\u0000\u0000\u00dd\u000f"+
		"\u0001\u0000\u0000\u0000\u00de\u00e4\u0003\u0012\t\u0000\u00df\u00e0\u0005"+
		"\u01f1\u0000\u0000\u00e0\u00e1\u0003\u0010\b\u0000\u00e1\u00e2\u0005\u01f2"+
		"\u0000\u0000\u00e2\u00e4\u0001\u0000\u0000\u0000\u00e3\u00de\u0001\u0000"+
		"\u0000\u0000\u00e3\u00df\u0001\u0000\u0000\u0000\u00e4\u0011\u0001\u0000"+
		"\u0000\u0000\u00e5\u00e9\u0003\u0014\n\u0000\u00e6\u00e8\u0003\u0016\u000b"+
		"\u0000\u00e7\u00e6\u0001\u0000\u0000\u0000\u00e8\u00eb\u0001\u0000\u0000"+
		"\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00e9\u00ea\u0001\u0000\u0000"+
		"\u0000\u00ea\u00f6\u0001\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000"+
		"\u0000\u00ec\u00ed\u0005\u01f1\u0000\u0000\u00ed\u00ee\u0003\u0012\t\u0000"+
		"\u00ee\u00f2\u0005\u01f2\u0000\u0000\u00ef\u00f1\u0003\u0016\u000b\u0000"+
		"\u00f0\u00ef\u0001\u0000\u0000\u0000\u00f1\u00f4\u0001\u0000\u0000\u0000"+
		"\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000"+
		"\u00f3\u00f6\u0001\u0000\u0000\u0000\u00f4\u00f2\u0001\u0000\u0000\u0000"+
		"\u00f5\u00e5\u0001\u0000\u0000\u0000\u00f5\u00ec\u0001\u0000\u0000\u0000"+
		"\u00f6\u0013\u0001\u0000\u0000\u0000\u00f7\u00f9\u00030\u0018\u0000\u00f8"+
		"\u00fa\u00032\u0019\u0000\u00f9\u00f8\u0001\u0000\u0000\u0000\u00f9\u00fa"+
		"\u0001\u0000\u0000\u0000\u00fa\u0119\u0001\u0000\u0000\u0000\u00fb\u00fc"+
		"\u0005\u01f1\u0000\u0000\u00fc\u00fd\u0003\u001c\u000e\u0000\u00fd\u0102"+
		"\u0005\u01f2\u0000\u0000\u00fe\u0100\u00032\u0019\u0000\u00ff\u0101\u0003"+
		"8\u001c\u0000\u0100\u00ff\u0001\u0000\u0000\u0000\u0100\u0101\u0001\u0000"+
		"\u0000\u0000\u0101\u0103\u0001\u0000\u0000\u0000\u0102\u00fe\u0001\u0000"+
		"\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000\u0103\u0119\u0001\u0000"+
		"\u0000\u0000\u0104\u0109\u0003L&\u0000\u0105\u0107\u00032\u0019\u0000"+
		"\u0106\u0108\u00038\u001c\u0000\u0107\u0106\u0001\u0000\u0000\u0000\u0107"+
		"\u0108\u0001\u0000\u0000\u0000\u0108\u010a\u0001\u0000\u0000\u0000\u0109"+
		"\u0105\u0001\u0000\u0000\u0000\u0109\u010a\u0001\u0000\u0000\u0000\u010a"+
		"\u0119\u0001\u0000\u0000\u0000\u010b\u010d\u0005\u01d7\u0000\u0000\u010c"+
		"\u010e\u00032\u0019\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010d\u010e"+
		"\u0001\u0000\u0000\u0000\u010e\u0119\u0001\u0000\u0000\u0000\u010f\u0110"+
		"\u0005\u01d7\u0000\u0000\u0110\u0111\u0005\u01ec\u0000\u0000\u0111\u0116"+
		"\u0003L&\u0000\u0112\u0114\u00032\u0019\u0000\u0113\u0115\u00038\u001c"+
		"\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115\u0117\u0001\u0000\u0000\u0000\u0116\u0112\u0001\u0000\u0000"+
		"\u0000\u0116\u0117\u0001\u0000\u0000\u0000\u0117\u0119\u0001\u0000\u0000"+
		"\u0000\u0118\u00f7\u0001\u0000\u0000\u0000\u0118\u00fb\u0001\u0000\u0000"+
		"\u0000\u0118\u0104\u0001\u0000\u0000\u0000\u0118\u010b\u0001\u0000\u0000"+
		"\u0000\u0118\u010f\u0001\u0000\u0000\u0000\u0119\u0015\u0001\u0000\u0000"+
		"\u0000\u011a\u011c\u0005l\u0000\u0000\u011b\u011a\u0001\u0000\u0000\u0000"+
		"\u011b\u011c\u0001\u0000\u0000\u0000\u011c\u0122\u0001\u0000\u0000\u0000"+
		"\u011d\u011f\u0007\u0002\u0000\u0000\u011e\u0120\u0005\u0085\u0000\u0000"+
		"\u011f\u011e\u0001\u0000\u0000\u0000\u011f\u0120\u0001\u0000\u0000\u0000"+
		"\u0120\u0122\u0001\u0000\u0000\u0000\u0121\u011b\u0001\u0000\u0000\u0000"+
		"\u0121\u011d\u0001\u0000\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000"+
		"\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u0124\u0005q\u0000\u0000\u0124"+
		"\u0125\u0003\u0010\b\u0000\u0125\u0126\u0005\u0080\u0000\u0000\u0126\u0127"+
		"\u0003\u0018\f\u0000\u0127\u012c\u0001\u0000\u0000\u0000\u0128\u0129\u0005"+
		"C\u0000\u0000\u0129\u012a\u0005q\u0000\u0000\u012a\u012c\u0003\u0010\b"+
		"\u0000\u012b\u0121\u0001\u0000\u0000\u0000\u012b\u0128\u0001\u0000\u0000"+
		"\u0000\u012c\u0017\u0001\u0000\u0000\u0000\u012d\u012f\u0006\f\uffff\uffff"+
		"\u0000\u012e\u0130\u0005x\u0000\u0000\u012f\u012e\u0001\u0000\u0000\u0000"+
		"\u012f\u0130\u0001\u0000\u0000\u0000\u0130\u0136\u0001\u0000\u0000\u0000"+
		"\u0131\u0137\u0003\u001a\r\u0000\u0132\u0133\u0005\u01f1\u0000\u0000\u0133"+
		"\u0134\u0003\u0018\f\u0000\u0134\u0135\u0005\u01f2\u0000\u0000\u0135\u0137"+
		"\u0001\u0000\u0000\u0000\u0136\u0131\u0001\u0000\u0000\u0000\u0136\u0132"+
		"\u0001\u0000\u0000\u0000\u0137\u0140\u0001\u0000\u0000\u0000\u0138\u0139"+
		"\n\u0002\u0000\u0000\u0139\u013a\u0005+\u0000\u0000\u013a\u013f\u0003"+
		"\u0018\f\u0003\u013b\u013c\n\u0001\u0000\u0000\u013c\u013d\u0005\u0083"+
		"\u0000\u0000\u013d\u013f\u0003\u0018\f\u0002\u013e\u0138\u0001\u0000\u0000"+
		"\u0000\u013e\u013b\u0001\u0000\u0000\u0000\u013f\u0142\u0001\u0000\u0000"+
		"\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0140\u0141\u0001\u0000\u0000"+
		"\u0000\u0141\u0019\u0001\u0000\u0000\u0000\u0142\u0140\u0001\u0000\u0000"+
		"\u0000\u0143\u0145\u0005x\u0000\u0000\u0144\u0143\u0001\u0000\u0000\u0000"+
		"\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000"+
		"\u0146\u0147\u0005\\\u0000\u0000\u0147\u0148\u0005\u01f1\u0000\u0000\u0148"+
		"\u0149\u0003F#\u0000\u0149\u014a\u0005\u01f2\u0000\u0000\u014a\u017f\u0001"+
		"\u0000\u0000\u0000\u014b\u014c\u0003:\u001d\u0000\u014c\u014d\u0003\u0082"+
		"A\u0000\u014d\u014e\u0003:\u001d\u0000\u014e\u017f\u0001\u0000\u0000\u0000"+
		"\u014f\u0150\u0003:\u001d\u0000\u0150\u0152\u0003\u0082A\u0000\u0151\u0153"+
		"\u0007\u0003\u0000\u0000\u0152\u0151\u0001\u0000\u0000\u0000\u0152\u0153"+
		"\u0001\u0000\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0154\u0155"+
		"\u0005\u01f1\u0000\u0000\u0155\u0156\u0003F#\u0000\u0156\u0157\u0005\u01f2"+
		"\u0000\u0000\u0157\u017f\u0001\u0000\u0000\u0000\u0158\u015a\u0003:\u001d"+
		"\u0000\u0159\u015b\u0005x\u0000\u0000\u015a\u0159\u0001\u0000\u0000\u0000"+
		"\u015a\u015b\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000\u0000"+
		"\u015c\u015d\u00051\u0000\u0000\u015d\u015e\u0003:\u001d\u0000\u015e\u015f"+
		"\u0005+\u0000\u0000\u015f\u0160\u0003:\u001d\u0000\u0160\u017f\u0001\u0000"+
		"\u0000\u0000\u0161\u0163\u0003:\u001d\u0000\u0162\u0164\u0005x\u0000\u0000"+
		"\u0163\u0162\u0001\u0000\u0000\u0000\u0163\u0164\u0001\u0000\u0000\u0000"+
		"\u0164\u0165\u0001\u0000\u0000\u0000\u0165\u0166\u0005j\u0000\u0000\u0166"+
		"\u0169\u0005\u01f1\u0000\u0000\u0167\u016a\u0003F#\u0000\u0168\u016a\u0003"+
		" \u0010\u0000\u0169\u0167\u0001\u0000\u0000\u0000\u0169\u0168\u0001\u0000"+
		"\u0000\u0000\u016a\u016b\u0001\u0000\u0000\u0000\u016b\u016c\u0005\u01f2"+
		"\u0000\u0000\u016c\u017f\u0001\u0000\u0000\u0000\u016d\u016f\u0003:\u001d"+
		"\u0000\u016e\u0170\u0005x\u0000\u0000\u016f\u016e\u0001\u0000\u0000\u0000"+
		"\u016f\u0170\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000"+
		"\u0171\u0172\u0005t\u0000\u0000\u0172\u0175\u0003:\u001d\u0000\u0173\u0174"+
		"\u0005Y\u0000\u0000\u0174\u0176\u0003:\u001d\u0000\u0175\u0173\u0001\u0000"+
		"\u0000\u0000\u0175\u0176\u0001\u0000\u0000\u0000\u0176\u017f\u0001\u0000"+
		"\u0000\u0000\u0177\u0178\u0003:\u001d\u0000\u0178\u017a\u0005p\u0000\u0000"+
		"\u0179\u017b\u0005x\u0000\u0000\u017a\u0179\u0001\u0000\u0000\u0000\u017a"+
		"\u017b\u0001\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c"+
		"\u017d\u0005y\u0000\u0000\u017d\u017f\u0001\u0000\u0000\u0000\u017e\u0144"+
		"\u0001\u0000\u0000\u0000\u017e\u014b\u0001\u0000\u0000\u0000\u017e\u014f"+
		"\u0001\u0000\u0000\u0000\u017e\u0158\u0001\u0000\u0000\u0000\u017e\u0161"+
		"\u0001\u0000\u0000\u0000\u017e\u016d\u0001\u0000\u0000\u0000\u017e\u0177"+
		"\u0001\u0000\u0000\u0000\u017f\u001b\u0001\u0000\u0000\u0000\u0180\u0193"+
		"\u0003F#\u0000\u0181\u0182\u0005\u01f1\u0000\u0000\u0182\u0188\u0003F"+
		"#\u0000\u0183\u0184\u0005\u00a9\u0000\u0000\u0184\u0185\u0005)\u0000\u0000"+
		"\u0185\u0187\u0003F#\u0000\u0186\u0183\u0001\u0000\u0000\u0000\u0187\u018a"+
		"\u0001\u0000\u0000\u0000\u0188\u0186\u0001\u0000\u0000\u0000\u0188\u0189"+
		"\u0001\u0000\u0000\u0000\u0189\u018b\u0001\u0000\u0000\u0000\u018a\u0188"+
		"\u0001\u0000\u0000\u0000\u018b\u018c\u0005\u01f2\u0000\u0000\u018c\u0193"+
		"\u0001\u0000\u0000\u0000\u018d\u0193\u0003\u001e\u000f\u0000\u018e\u018f"+
		"\u0005\u01f1\u0000\u0000\u018f\u0190\u0003\u001e\u000f\u0000\u0190\u0191"+
		"\u0005\u01f2\u0000\u0000\u0191\u0193\u0001\u0000\u0000\u0000\u0192\u0180"+
		"\u0001\u0000\u0000\u0000\u0192\u0181\u0001\u0000\u0000\u0000\u0192\u018d"+
		"\u0001\u0000\u0000\u0000\u0192\u018e\u0001\u0000\u0000\u0000\u0193\u001d"+
		"\u0001\u0000\u0000\u0000\u0194\u0195\u0005\u00ae\u0000\u0000\u0195\u0196"+
		"\u0005\u01f1\u0000\u0000\u0196\u0197\u0003 \u0010\u0000\u0197\u019f\u0005"+
		"\u01f2\u0000\u0000\u0198\u0199\u0005\u01f3\u0000\u0000\u0199\u019a\u0005"+
		"\u01f1\u0000\u0000\u019a\u019b\u0003 \u0010\u0000\u019b\u019c\u0005\u01f2"+
		"\u0000\u0000\u019c\u019e\u0001\u0000\u0000\u0000\u019d\u0198\u0001\u0000"+
		"\u0000\u0000\u019e\u01a1\u0001\u0000\u0000\u0000\u019f\u019d\u0001\u0000"+
		"\u0000\u0000\u019f\u01a0\u0001\u0000\u0000\u0000\u01a0\u001f\u0001\u0000"+
		"\u0000\u0000\u01a1\u019f\u0001\u0000\u0000\u0000\u01a2\u01a7\u0003:\u001d"+
		"\u0000\u01a3\u01a4\u0005\u01f3\u0000\u0000\u01a4\u01a6\u0003:\u001d\u0000"+
		"\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a6\u01a9\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a7\u01a8\u0001\u0000\u0000\u0000"+
		"\u01a8!\u0001\u0000\u0000\u0000\u01a9\u01a7\u0001\u0000\u0000\u0000\u01aa"+
		"\u01af\u0003$\u0012\u0000\u01ab\u01ac\u0005\u01f3\u0000\u0000\u01ac\u01ae"+
		"\u0003$\u0012\u0000\u01ad\u01ab\u0001\u0000\u0000\u0000\u01ae\u01b1\u0001"+
		"\u0000\u0000\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01af\u01b0\u0001"+
		"\u0000\u0000\u0000\u01b0#\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000"+
		"\u0000\u0000\u01b2\u01b6\u0003&\u0013\u0000\u01b3\u01b6\u0003(\u0014\u0000"+
		"\u01b4\u01b6\u0003,\u0016\u0000\u01b5\u01b2\u0001\u0000\u0000\u0000\u01b5"+
		"\u01b3\u0001\u0000\u0000\u0000\u01b5\u01b4\u0001\u0000\u0000\u0000\u01b6"+
		"%\u0001\u0000\u0000\u0000\u01b7\u01b8\u00030\u0018\u0000\u01b8\u01b9\u0005"+
		"\u01ec\u0000\u0000\u01b9\u01bb\u0001\u0000\u0000\u0000\u01ba\u01b7\u0001"+
		"\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001"+
		"\u0000\u0000\u0000\u01bc\u01c1\u0005\u01f7\u0000\u0000\u01bd\u01be\u0007"+
		"\u0004\u0000\u0000\u01be\u01bf\u0005\u01ec\u0000\u0000\u01bf\u01c1\u0005"+
		"\u01f7\u0000\u0000\u01c0\u01ba\u0001\u0000\u0000\u0000\u01c0\u01bd\u0001"+
		"\u0000\u0000\u0000\u01c1\'\u0001\u0000\u0000\u0000\u01c2\u01c5\u0003*"+
		"\u0015\u0000\u01c3\u01c5\u0005y\u0000\u0000\u01c4\u01c2\u0001\u0000\u0000"+
		"\u0000\u01c4\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c7\u0001\u0000\u0000"+
		"\u0000\u01c6\u01c8\u00034\u001a\u0000\u01c7\u01c6\u0001\u0000\u0000\u0000"+
		"\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8)\u0001\u0000\u0000\u0000\u01c9"+
		"\u01ca\u0007\u0004\u0000\u0000\u01ca\u01cb\u0005\u01ec\u0000\u0000\u01cb"+
		"\u01d4\u0003\u0080@\u0000\u01cc\u01cd\u00030\u0018\u0000\u01cd\u01ce\u0005"+
		"\u01ec\u0000\u0000\u01ce\u01d0\u0001\u0000\u0000\u0000\u01cf\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cf\u01d0\u0001\u0000\u0000\u0000\u01d0\u01d1\u0001"+
		"\u0000\u0000\u0000\u01d1\u01d4\u0003\u0080@\u0000\u01d2\u01d4\u0003\u0080"+
		"@\u0000\u01d3\u01c9\u0001\u0000\u0000\u0000\u01d3\u01cf\u0001\u0000\u0000"+
		"\u0000\u01d3\u01d2\u0001\u0000\u0000\u0000\u01d4+\u0001\u0000\u0000\u0000"+
		"\u01d5\u01d6\u00036\u001b\u0000\u01d6\u01d7\u0005\u01df\u0000\u0000\u01d7"+
		"\u01d8\u0003:\u001d\u0000\u01d8\u01de\u0001\u0000\u0000\u0000\u01d9\u01db"+
		"\u0003:\u001d\u0000\u01da\u01dc\u00034\u001a\u0000\u01db\u01da\u0001\u0000"+
		"\u0000\u0000\u01db\u01dc\u0001\u0000\u0000\u0000\u01dc\u01de\u0001\u0000"+
		"\u0000\u0000\u01dd\u01d5\u0001\u0000\u0000\u0000\u01dd\u01d9\u0001\u0000"+
		"\u0000\u0000\u01de-\u0001\u0000\u0000\u0000\u01df\u01e1\u0003:\u001d\u0000"+
		"\u01e0\u01e2\u0007\u0005\u0000\u0000\u01e1\u01e0\u0001\u0000\u0000\u0000"+
		"\u01e1\u01e2\u0001\u0000\u0000\u0000\u01e2/\u0001\u0000\u0000\u0000\u01e3"+
		"\u01e4\u0003\u0080@\u0000\u01e4\u01e6\u0005\u01ec\u0000\u0000\u01e5\u01e7"+
		"\u0003\u0080@\u0000\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e6\u01e7\u0001"+
		"\u0000\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8\u01e9\u0005"+
		"\u01ec\u0000\u0000\u01e9\u01ee\u0001\u0000\u0000\u0000\u01ea\u01eb\u0003"+
		"\u0080@\u0000\u01eb\u01ec\u0005\u01ec\u0000\u0000\u01ec\u01ee\u0001\u0000"+
		"\u0000\u0000\u01ed\u01e3\u0001\u0000\u0000\u0000\u01ed\u01ea\u0001\u0000"+
		"\u0000\u0000\u01ed\u01ee\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000"+
		"\u0000\u0000\u01ef\u01f0\u0003\u0080@\u0000\u01f01\u0001\u0000\u0000\u0000"+
		"\u01f1\u01f3\u0005,\u0000\u0000\u01f2\u01f1\u0001\u0000\u0000\u0000\u01f2"+
		"\u01f3\u0001\u0000\u0000\u0000\u01f3\u01f4\u0001\u0000\u0000\u0000\u01f4"+
		"\u01f5\u0003\u0080@\u0000\u01f53\u0001\u0000\u0000\u0000\u01f6\u01f8\u0005"+
		",\u0000\u0000\u01f7\u01f6\u0001\u0000\u0000\u0000\u01f7\u01f8\u0001\u0000"+
		"\u0000\u0000\u01f8\u01f9\u0001\u0000\u0000\u0000\u01f9\u01fa\u00036\u001b"+
		"\u0000\u01fa5\u0001\u0000\u0000\u0000\u01fb\u01fe\u0003\u0080@\u0000\u01fc"+
		"\u01fe\u0005\u01da\u0000\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd"+
		"\u01fc\u0001\u0000\u0000\u0000\u01fe7\u0001\u0000\u0000\u0000\u01ff\u0200"+
		"\u0005\u01f1\u0000\u0000\u0200\u0205\u00036\u001b\u0000\u0201\u0202\u0005"+
		"\u01f3\u0000\u0000\u0202\u0204\u00036\u001b\u0000\u0203\u0201\u0001\u0000"+
		"\u0000\u0000\u0204\u0207\u0001\u0000\u0000\u0000\u0205\u0203\u0001\u0000"+
		"\u0000\u0000\u0205\u0206\u0001\u0000\u0000\u0000\u0206\u0208\u0001\u0000"+
		"\u0000\u0000\u0207\u0205\u0001\u0000\u0000\u0000\u0208\u0209\u0005\u01f2"+
		"\u0000\u0000\u02099\u0001\u0000\u0000\u0000\u020a\u020b\u0006\u001d\uffff"+
		"\uffff\u0000\u020b\u0213\u0003<\u001e\u0000\u020c\u0213\u0003L&\u0000"+
		"\u020d\u0213\u0003>\u001f\u0000\u020e\u0213\u0003*\u0015\u0000\u020f\u0213"+
		"\u0003B!\u0000\u0210\u0213\u0003@ \u0000\u0211\u0213\u0003n7\u0000\u0212"+
		"\u020a\u0001\u0000\u0000\u0000\u0212\u020c\u0001\u0000\u0000\u0000\u0212"+
		"\u020d\u0001\u0000\u0000\u0000\u0212\u020e\u0001\u0000\u0000\u0000\u0212"+
		"\u020f\u0001\u0000\u0000\u0000\u0212\u0210\u0001\u0000\u0000\u0000\u0212"+
		"\u0211\u0001\u0000\u0000\u0000\u0213\u022d\u0001\u0000\u0000\u0000\u0214"+
		"\u0215\n\u0005\u0000\u0000\u0215\u0216\u0007\u0006\u0000\u0000\u0216\u022c"+
		"\u0003:\u001d\u0006\u0217\u0218\n\u0004\u0000\u0000\u0218\u0219\u0003"+
		"\u0084B\u0000\u0219\u021a\u0003:\u001d\u0005\u021a\u022c\u0001\u0000\u0000"+
		"\u0000\u021b\u021c\n\u0003\u0000\u0000\u021c\u021d\u0007\u0007\u0000\u0000"+
		"\u021d\u022c\u0003:\u001d\u0004\u021e\u021f\n\u000b\u0000\u0000\u021f"+
		"\u0224\u0005\u01ec\u0000\u0000\u0220\u0225\u0003V+\u0000\u0221\u0225\u0003"+
		"Z-\u0000\u0222\u0225\u0003^/\u0000\u0223\u0225\u0003b1\u0000\u0224\u0220"+
		"\u0001\u0000\u0000\u0000\u0224\u0221\u0001\u0000\u0000\u0000\u0224\u0222"+
		"\u0001\u0000\u0000\u0000\u0224\u0223\u0001\u0000\u0000\u0000\u0225\u022c"+
		"\u0001\u0000\u0000\u0000\u0226\u0227\n\n\u0000\u0000\u0227\u0228\u0005"+
		":\u0000\u0000\u0228\u022c\u0003\u0080@\u0000\u0229\u022a\n\u0002\u0000"+
		"\u0000\u022a\u022c\u0003d2\u0000\u022b\u0214\u0001\u0000\u0000\u0000\u022b"+
		"\u0217\u0001\u0000\u0000\u0000\u022b\u021b\u0001\u0000\u0000\u0000\u022b"+
		"\u021e\u0001\u0000\u0000\u0000\u022b\u0226\u0001\u0000\u0000\u0000\u022b"+
		"\u0229\u0001\u0000\u0000\u0000\u022c\u022f\u0001\u0000\u0000\u0000\u022d"+
		"\u022b\u0001\u0000\u0000\u0000\u022d\u022e\u0001\u0000\u0000\u0000\u022e"+
		";\u0001\u0000\u0000\u0000\u022f\u022d\u0001\u0000\u0000\u0000\u0230\u0235"+
		"\u0005M\u0000\u0000\u0231\u0235\u0005y\u0000\u0000\u0232\u0235\u0005\u01d7"+
		"\u0000\u0000\u0233\u0235\u0003z=\u0000\u0234\u0230\u0001\u0000\u0000\u0000"+
		"\u0234\u0231\u0001\u0000\u0000\u0000\u0234\u0232\u0001\u0000\u0000\u0000"+
		"\u0234\u0233\u0001\u0000\u0000\u0000\u0235=\u0001\u0000\u0000\u0000\u0236"+
		"\u0237\u00056\u0000\u0000\u0237\u023d\u0003:\u001d\u0000\u0238\u0239\u0005"+
		"\u00b1\u0000\u0000\u0239\u023a\u0003:\u001d\u0000\u023a\u023b\u0005\u00a0"+
		"\u0000\u0000\u023b\u023c\u0003:\u001d\u0000\u023c\u023e\u0001\u0000\u0000"+
		"\u0000\u023d\u0238\u0001\u0000\u0000\u0000\u023e\u023f\u0001\u0000\u0000"+
		"\u0000\u023f\u023d\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000\u0000"+
		"\u0000\u0240\u0243\u0001\u0000\u0000\u0000\u0241\u0242\u0005W\u0000\u0000"+
		"\u0242\u0244\u0003:\u001d\u0000\u0243\u0241\u0001\u0000\u0000\u0000\u0243"+
		"\u0244\u0001\u0000\u0000\u0000\u0244\u0245\u0001\u0000\u0000\u0000\u0245"+
		"\u0246\u0005X\u0000\u0000\u0246\u0258\u0001\u0000\u0000\u0000\u0247\u024d"+
		"\u00056\u0000\u0000\u0248\u0249\u0005\u00b1\u0000\u0000\u0249\u024a\u0003"+
		"\u0018\f\u0000\u024a\u024b\u0005\u00a0\u0000\u0000\u024b\u024c\u0003:"+
		"\u001d\u0000\u024c\u024e\u0001\u0000\u0000\u0000\u024d\u0248\u0001\u0000"+
		"\u0000\u0000\u024e\u024f\u0001\u0000\u0000\u0000\u024f\u024d\u0001\u0000"+
		"\u0000\u0000\u024f\u0250\u0001\u0000\u0000\u0000\u0250\u0253\u0001\u0000"+
		"\u0000\u0000\u0251\u0252\u0005W\u0000\u0000\u0252\u0254\u0003:\u001d\u0000"+
		"\u0253\u0251\u0001\u0000\u0000\u0000\u0253\u0254\u0001\u0000\u0000\u0000"+
		"\u0254\u0255\u0001\u0000\u0000\u0000\u0255\u0256\u0005X\u0000\u0000\u0256"+
		"\u0258\u0001\u0000\u0000\u0000\u0257\u0236\u0001\u0000\u0000\u0000\u0257"+
		"\u0247\u0001\u0000\u0000\u0000\u0258?\u0001\u0000\u0000\u0000\u0259\u025a"+
		"\u0005\u01fc\u0000\u0000\u025a\u025e\u0003:\u001d\u0000\u025b\u025c\u0007"+
		"\b\u0000\u0000\u025c\u025e\u0003:\u001d\u0000\u025d\u0259\u0001\u0000"+
		"\u0000\u0000\u025d\u025b\u0001\u0000\u0000\u0000\u025eA\u0001\u0000\u0000"+
		"\u0000\u025f\u0260\u0005\u01f1\u0000\u0000\u0260\u0261\u0003:\u001d\u0000"+
		"\u0261\u0262\u0005\u01f2\u0000\u0000\u0262\u0268\u0001\u0000\u0000\u0000"+
		"\u0263\u0264\u0005\u01f1\u0000\u0000\u0264\u0265\u0003F#\u0000\u0265\u0266"+
		"\u0005\u01f2\u0000\u0000\u0266\u0268\u0001\u0000\u0000\u0000\u0267\u025f"+
		"\u0001\u0000\u0000\u0000\u0267\u0263\u0001\u0000\u0000\u0000\u0268C\u0001"+
		"\u0000\u0000\u0000\u0269\u0273\u0005y\u0000\u0000\u026a\u0273\u0005\u00a6"+
		"\u0000\u0000\u026b\u0273\u0005\u00a7\u0000\u0000\u026c\u0273\u0003z=\u0000"+
		"\u026d\u0273\u0003L&\u0000\u026e\u026f\u0005\u01f1\u0000\u0000\u026f\u0270"+
		"\u0003D\"\u0000\u0270\u0271\u0005\u01f2\u0000\u0000\u0271\u0273\u0001"+
		"\u0000\u0000\u0000\u0272\u0269\u0001\u0000\u0000\u0000\u0272\u026a\u0001"+
		"\u0000\u0000\u0000\u0272\u026b\u0001\u0000\u0000\u0000\u0272\u026c\u0001"+
		"\u0000\u0000\u0000\u0272\u026d\u0001\u0000\u0000\u0000\u0272\u026e\u0001"+
		"\u0000\u0000\u0000\u0273E\u0001\u0000\u0000\u0000\u0274\u0275\u0003\u0002"+
		"\u0001\u0000\u0275G\u0001\u0000\u0000\u0000\u0276\u0277\u0005\u00b4\u0000"+
		"\u0000\u0277\u027c\u0003J%\u0000\u0278\u0279\u0005\u01f3\u0000\u0000\u0279"+
		"\u027b\u0003J%\u0000\u027a\u0278\u0001\u0000\u0000\u0000\u027b\u027e\u0001"+
		"\u0000\u0000\u0000\u027c\u027a\u0001\u0000\u0000\u0000\u027c\u027d\u0001"+
		"\u0000\u0000\u0000\u027dI\u0001\u0000\u0000\u0000\u027e\u027c\u0001\u0000"+
		"\u0000\u0000\u027f\u0284\u0003\u0080@\u0000\u0280\u0281\u0005\u01f1\u0000"+
		"\u0000\u0281\u0282\u0003\"\u0011\u0000\u0282\u0283\u0005\u01f2\u0000\u0000"+
		"\u0283\u0285\u0001\u0000\u0000\u0000\u0284\u0280\u0001\u0000\u0000\u0000"+
		"\u0284\u0285\u0001\u0000\u0000\u0000\u0285\u0286\u0001\u0000\u0000\u0000"+
		"\u0286\u0287\u0005,\u0000\u0000\u0287\u0288\u0005\u01f1\u0000\u0000\u0288"+
		"\u0289\u0003\u0002\u0001\u0000\u0289\u028a\u0005\u01f2\u0000\u0000\u028a"+
		"K\u0001\u0000\u0000\u0000\u028b\u0291\u0003f3\u0000\u028c\u0291\u0003"+
		"h4\u0000\u028d\u0291\u0003j5\u0000\u028e\u0291\u0003N\'\u0000\u028f\u0291"+
		"\u0003P(\u0000\u0290\u028b\u0001\u0000\u0000\u0000\u0290\u028c\u0001\u0000"+
		"\u0000\u0000\u0290\u028d\u0001\u0000\u0000\u0000\u0290\u028e\u0001\u0000"+
		"\u0000\u0000\u0290\u028f\u0001\u0000\u0000\u0000\u0291M\u0001\u0000\u0000"+
		"\u0000\u0292\u0293\u0005\u00c1\u0000\u0000\u0293\u0294\u0005\u01f1\u0000"+
		"\u0000\u0294\u0295\u0005\u01f7\u0000\u0000\u0295\u02fc\u0005\u01f2\u0000"+
		"\u0000\u0296\u0297\u0005\u00c4\u0000\u0000\u0297\u0298\u0005\u01f1\u0000"+
		"\u0000\u0298\u0299\u0003:\u001d\u0000\u0299\u029a\u0005,\u0000\u0000\u029a"+
		"\u029b\u0003x<\u0000\u029b\u029c\u0005\u01f2\u0000\u0000\u029c\u02fc\u0001"+
		"\u0000\u0000\u0000\u029d\u029e\u0005A\u0000\u0000\u029e\u029f\u0005\u01f1"+
		"\u0000\u0000\u029f\u02a0\u0003x<\u0000\u02a0\u02a1\u0005\u01f3\u0000\u0000"+
		"\u02a1\u02a4\u0003:\u001d\u0000\u02a2\u02a3\u0005\u01f3\u0000\u0000\u02a3"+
		"\u02a5\u0003:\u001d\u0000\u02a4\u02a2\u0001\u0000\u0000\u0000\u02a4\u02a5"+
		"\u0001\u0000\u0000\u0000\u02a5\u02a6\u0001\u0000\u0000\u0000\u02a6\u02a7"+
		"\u0005\u01f2\u0000\u0000\u02a7\u02fc\u0001\u0000\u0000\u0000\u02a8\u02a9"+
		"\u0005\u00c8\u0000\u0000\u02a9\u02aa\u0005\u01f1\u0000\u0000\u02aa\u02ab"+
		"\u0005\u01f7\u0000\u0000\u02ab\u02fc\u0005\u01f2\u0000\u0000\u02ac\u02ad"+
		"\u00059\u0000\u0000\u02ad\u02ae\u0005\u01f1\u0000\u0000\u02ae\u02af\u0003"+
		" \u0010\u0000\u02af\u02b0\u0005\u01f2\u0000\u0000\u02b0\u02fc\u0001\u0000"+
		"\u0000\u0000\u02b1\u02fc\u0005G\u0000\u0000\u02b2\u02fc\u0005E\u0000\u0000"+
		"\u02b3\u02fc\u0005H\u0000\u0000\u02b4\u02b5\u0005\u00cf\u0000\u0000\u02b5"+
		"\u02b6\u0005\u01f1\u0000\u0000\u02b6\u02b7\u0005\u01d9\u0000\u0000\u02b7"+
		"\u02b8\u0005\u01f3\u0000\u0000\u02b8\u02b9\u0003:\u001d\u0000\u02b9\u02ba"+
		"\u0005\u01f3\u0000\u0000\u02ba\u02bb\u0003:\u001d\u0000\u02bb\u02bc\u0005"+
		"\u01f2\u0000\u0000\u02bc\u02fc\u0001\u0000\u0000\u0000\u02bd\u02be\u0005"+
		"\u00d0\u0000\u0000\u02be\u02bf\u0005\u01f1\u0000\u0000\u02bf\u02c0\u0005"+
		"\u01d9\u0000\u0000\u02c0\u02c1\u0005\u01f3\u0000\u0000\u02c1\u02c2\u0003"+
		":\u001d\u0000\u02c2\u02c3\u0005\u01f3\u0000\u0000\u02c3\u02c4\u0003:\u001d"+
		"\u0000\u02c4\u02c5\u0005\u01f2\u0000\u0000\u02c5\u02fc\u0001\u0000\u0000"+
		"\u0000\u02c6\u02c7\u0005\u00d1\u0000\u0000\u02c7\u02c8\u0005\u01f1\u0000"+
		"\u0000\u02c8\u02c9\u0005\u01d9\u0000\u0000\u02c9\u02ca\u0005\u01f3\u0000"+
		"\u0000\u02ca\u02cb\u0003:\u001d\u0000\u02cb\u02cc\u0005\u01f2\u0000\u0000"+
		"\u02cc\u02fc\u0001\u0000\u0000\u0000\u02cd\u02ce\u0005\u00d2\u0000\u0000"+
		"\u02ce\u02cf\u0005\u01f1\u0000\u0000\u02cf\u02d0\u0005\u01d9\u0000\u0000"+
		"\u02d0\u02d1\u0005\u01f3\u0000\u0000\u02d1\u02d2\u0003:\u001d\u0000\u02d2"+
		"\u02d3\u0005\u01f2\u0000\u0000\u02d3\u02fc\u0001\u0000\u0000\u0000\u02d4"+
		"\u02d5\u0005h\u0000\u0000\u02d5\u02d6\u0005\u01f1\u0000\u0000\u02d6\u02d9"+
		"\u0003x<\u0000\u02d7\u02d8\u0005\u01f3\u0000\u0000\u02d8\u02da\u0005\u01d8"+
		"\u0000\u0000\u02d9\u02d7\u0001\u0000\u0000\u0000\u02d9\u02da\u0001\u0000"+
		"\u0000\u0000\u02da\u02dd\u0001\u0000\u0000\u0000\u02db\u02dc\u0005\u01f3"+
		"\u0000\u0000\u02dc\u02de\u0005\u01d8\u0000\u0000\u02dd\u02db\u0001\u0000"+
		"\u0000\u0000\u02dd\u02de\u0001\u0000\u0000\u0000\u02de\u02df\u0001\u0000"+
		"\u0000\u0000\u02df\u02e0\u0005\u01f2\u0000\u0000\u02e0\u02fc\u0001\u0000"+
		"\u0000\u0000\u02e1\u02e2\u0005z\u0000\u0000\u02e2\u02e3\u0005\u01f1\u0000"+
		"\u0000\u02e3\u02e4\u0003:\u001d\u0000\u02e4\u02e5\u0005\u01f3\u0000\u0000"+
		"\u02e5\u02e6\u0003:\u001d\u0000\u02e6\u02e7\u0005\u01f2\u0000\u0000\u02e7"+
		"\u02fc\u0001\u0000\u0000\u0000\u02e8\u02fc\u0005\u009e\u0000\u0000\u02e9"+
		"\u02fc\u0005\u00ad\u0000\u0000\u02ea\u02eb\u0005\u019b\u0000\u0000\u02eb"+
		"\u02ec\u0005\u01f1\u0000\u0000\u02ec\u02ed\u0003:\u001d\u0000\u02ed\u02ee"+
		"\u0005\u01f3\u0000\u0000\u02ee\u02ef\u0003:\u001d\u0000\u02ef\u02f0\u0005"+
		"\u01f2\u0000\u0000\u02f0\u02fc\u0001\u0000\u0000\u0000\u02f1\u02fc\u0003"+
		"R)\u0000\u02f2\u02f3\u0005\u0194\u0000\u0000\u02f3\u02f4\u0005\u01f1\u0000"+
		"\u0000\u02f4\u02f5\u0003\u0018\f\u0000\u02f5\u02f6\u0005\u01f3\u0000\u0000"+
		"\u02f6\u02f7\u0003:\u001d\u0000\u02f7\u02f8\u0005\u01f3\u0000\u0000\u02f8"+
		"\u02f9\u0003:\u001d\u0000\u02f9\u02fa\u0005\u01f2\u0000\u0000\u02fa\u02fc"+
		"\u0001\u0000\u0000\u0000\u02fb\u0292\u0001\u0000\u0000\u0000\u02fb\u0296"+
		"\u0001\u0000\u0000\u0000\u02fb\u029d\u0001\u0000\u0000\u0000\u02fb\u02a8"+
		"\u0001\u0000\u0000\u0000\u02fb\u02ac\u0001\u0000\u0000\u0000\u02fb\u02b1"+
		"\u0001\u0000\u0000\u0000\u02fb\u02b2\u0001\u0000\u0000\u0000\u02fb\u02b3"+
		"\u0001\u0000\u0000\u0000\u02fb\u02b4\u0001\u0000\u0000\u0000\u02fb\u02bd"+
		"\u0001\u0000\u0000\u0000\u02fb\u02c6\u0001\u0000\u0000\u0000\u02fb\u02cd"+
		"\u0001\u0000\u0000\u0000\u02fb\u02d4\u0001\u0000\u0000\u0000\u02fb\u02e1"+
		"\u0001\u0000\u0000\u0000\u02fb\u02e8\u0001\u0000\u0000\u0000\u02fb\u02e9"+
		"\u0001\u0000\u0000\u0000\u02fb\u02ea\u0001\u0000\u0000\u0000\u02fb\u02f1"+
		"\u0001\u0000\u0000\u0000\u02fb\u02f2\u0001\u0000\u0000\u0000\u02fcO\u0001"+
		"\u0000\u0000\u0000\u02fd\u0302\u00030\u0018\u0000\u02fe\u0302\u0005\u0096"+
		"\u0000\u0000\u02ff\u0302\u0005s\u0000\u0000\u0300\u0302\u0005\u00c8\u0000"+
		"\u0000\u0301\u02fd\u0001\u0000\u0000\u0000\u0301\u02fe\u0001\u0000\u0000"+
		"\u0000\u0301\u02ff\u0001\u0000\u0000\u0000\u0301\u0300\u0001\u0000\u0000"+
		"\u0000\u0302\u0303\u0001\u0000\u0000\u0000\u0303\u0305\u0005\u01f1\u0000"+
		"\u0000\u0304\u0306\u0003 \u0010\u0000\u0305\u0304\u0001\u0000\u0000\u0000"+
		"\u0305\u0306\u0001\u0000\u0000\u0000\u0306\u0307\u0001\u0000\u0000\u0000"+
		"\u0307\u0308\u0005\u01f2\u0000\u0000\u0308Q\u0001\u0000\u0000\u0000\u0309"+
		"\u030e\u0003T*\u0000\u030a\u030e\u0003X,\u0000\u030b\u030e\u0003\\.\u0000"+
		"\u030c\u030e\u0003`0\u0000\u030d\u0309\u0001\u0000\u0000\u0000\u030d\u030a"+
		"\u0001\u0000\u0000\u0000\u030d\u030b\u0001\u0000\u0000\u0000\u030d\u030c"+
		"\u0001\u0000\u0000\u0000\u030eS\u0001\u0000\u0000\u0000\u030f\u0317\u0005"+
		"\u01d7\u0000\u0000\u0310\u0317\u0003\u0080@\u0000\u0311\u0317\u0003X,"+
		"\u0000\u0312\u0313\u0005\u01f1\u0000\u0000\u0313\u0314\u0003F#\u0000\u0314"+
		"\u0315\u0005\u01f2\u0000\u0000\u0315\u0317\u0001\u0000\u0000\u0000\u0316"+
		"\u030f\u0001\u0000\u0000\u0000\u0316\u0310\u0001\u0000\u0000\u0000\u0316"+
		"\u0311\u0001\u0000\u0000\u0000\u0316\u0312\u0001\u0000\u0000\u0000\u0317"+
		"\u0318\u0001\u0000\u0000\u0000\u0318\u0319\u0005\u01ec\u0000\u0000\u0319"+
		"\u031a\u0003V+\u0000\u031aU\u0001\u0000\u0000\u0000\u031b\u031c\u0005"+
		"\u017c\u0000\u0000\u031c\u031d\u0005\u01f1\u0000\u0000\u031d\u031e\u0005"+
		"\u01da\u0000\u0000\u031e\u031f\u0005\u01f3\u0000\u0000\u031f\u0320\u0005"+
		"\u01da\u0000\u0000\u0320\u0321\u0005\u01f2\u0000\u0000\u0321W\u0001\u0000"+
		"\u0000\u0000\u0322\u032a\u0005\u01d7\u0000\u0000\u0323\u032a\u0003\u0080"+
		"@\u0000\u0324\u032a\u00030\u0018\u0000\u0325\u0326\u0005\u01f1\u0000\u0000"+
		"\u0326\u0327\u0003F#\u0000\u0327\u0328\u0005\u01f2\u0000\u0000\u0328\u032a"+
		"\u0001\u0000\u0000\u0000\u0329\u0322\u0001\u0000\u0000\u0000\u0329\u0323"+
		"\u0001\u0000\u0000\u0000\u0329\u0324\u0001\u0000\u0000\u0000\u0329\u0325"+
		"\u0001\u0000\u0000\u0000\u032a\u032b\u0001\u0000\u0000\u0000\u032b\u032c"+
		"\u0005\u01ec\u0000\u0000\u032c\u032d\u0003Z-\u0000\u032dY\u0001\u0000"+
		"\u0000\u0000\u032e\u032f\u0005\u0140\u0000\u0000\u032f\u0330\u0005\u01f1"+
		"\u0000\u0000\u0330\u0331\u0005\u01da\u0000\u0000\u0331\u0332\u0005\u01f2"+
		"\u0000\u0000\u0332[\u0001\u0000\u0000\u0000\u0333\u033a\u0005\u01d7\u0000"+
		"\u0000\u0334\u033a\u0003\u0080@\u0000\u0335\u0336\u0005\u01f1\u0000\u0000"+
		"\u0336\u0337\u0003F#\u0000\u0337\u0338\u0005\u01f2\u0000\u0000\u0338\u033a"+
		"\u0001\u0000\u0000\u0000\u0339\u0333\u0001\u0000\u0000\u0000\u0339\u0334"+
		"\u0001\u0000\u0000\u0000\u0339\u0335\u0001\u0000\u0000\u0000\u033a\u033b"+
		"\u0001\u0000\u0000\u0000\u033b\u033c\u0005\u01ec\u0000\u0000\u033c\u033d"+
		"\u0003^/\u0000\u033d]\u0001\u0000\u0000\u0000\u033e\u033f\u0005\u00e3"+
		"\u0000\u0000\u033f\u0340\u0005\u01f1\u0000\u0000\u0340\u0341\u0005\u01da"+
		"\u0000\u0000\u0341\u0342\u0005\u01f2\u0000\u0000\u0342_\u0001\u0000\u0000"+
		"\u0000\u0343\u034a\u0005\u01d7\u0000\u0000\u0344\u034a\u0003\u0080@\u0000"+
		"\u0345\u0346\u0005\u01f1\u0000\u0000\u0346\u0347\u0003F#\u0000\u0347\u0348"+
		"\u0005\u01f2\u0000\u0000\u0348\u034a\u0001\u0000\u0000\u0000\u0349\u0343"+
		"\u0001\u0000\u0000\u0000\u0349\u0344\u0001\u0000\u0000\u0000\u0349\u0345"+
		"\u0001\u0000\u0000\u0000\u034a\u034b\u0001\u0000\u0000\u0000\u034b\u034c"+
		"\u0005\u01ec\u0000\u0000\u034c\u034d\u0003b1\u0000\u034da\u0001\u0000"+
		"\u0000\u0000\u034e\u034f\u0005\u011e\u0000\u0000\u034f\u0350\u0005\u01f1"+
		"\u0000\u0000\u0350\u0351\u0005\u01da\u0000\u0000\u0351\u0352\u0005\u01f2"+
		"\u0000\u0000\u0352c\u0001\u0000\u0000\u0000\u0353\u0354\u0005\u00b8\u0000"+
		"\u0000\u0354\u0355\u0005\u001b\u0000\u0000\u0355\u0356\u0005\u0182\u0000"+
		"\u0000\u0356\u0357\u0003:\u001d\u0000\u0357e\u0001\u0000\u0000\u0000\u0358"+
		"\u0359\u0007\t\u0000\u0000\u0359\u035a\u0005\u01f1\u0000\u0000\u035a\u035b"+
		"\u0005\u01f2\u0000\u0000\u035b\u035c\u0003n7\u0000\u035cg\u0001\u0000"+
		"\u0000\u0000\u035d\u035e\u0007\n\u0000\u0000\u035e\u035f\u0005\u01f1\u0000"+
		"\u0000\u035f\u0360\u0003l6\u0000\u0360\u0362\u0005\u01f2\u0000\u0000\u0361"+
		"\u0363\u0003n7\u0000\u0362\u0361\u0001\u0000\u0000\u0000\u0362\u0363\u0001"+
		"\u0000\u0000\u0000\u0363\u0379\u0001\u0000\u0000\u0000\u0364\u0365\u0005"+
		"\u00cc\u0000\u0000\u0365\u0368\u0005\u01f1\u0000\u0000\u0366\u0369\u0005"+
		"\u01f7\u0000\u0000\u0367\u0369\u0003l6\u0000\u0368\u0366\u0001\u0000\u0000"+
		"\u0000\u0368\u0367\u0001\u0000\u0000\u0000\u0369\u036a\u0001\u0000\u0000"+
		"\u0000\u036a\u036c\u0005\u01f2\u0000\u0000\u036b\u036d\u0003n7\u0000\u036c"+
		"\u036b\u0001\u0000\u0000\u0000\u036c\u036d\u0001\u0000\u0000\u0000\u036d"+
		"\u0379\u0001\u0000\u0000\u0000\u036e\u036f\u0005\u00f0\u0000\u0000\u036f"+
		"\u0370\u0005\u01f1\u0000\u0000\u0370\u0371\u0003:\u001d\u0000\u0371\u0372"+
		"\u0005\u01f2\u0000\u0000\u0372\u0379\u0001\u0000\u0000\u0000\u0373\u0374"+
		"\u0005\u00f1\u0000\u0000\u0374\u0375\u0005\u01f1\u0000\u0000\u0375\u0376"+
		"\u0003 \u0010\u0000\u0376\u0377\u0005\u01f2\u0000\u0000\u0377\u0379\u0001"+
		"\u0000\u0000\u0000\u0378\u035d\u0001\u0000\u0000\u0000\u0378\u0364\u0001"+
		"\u0000\u0000\u0000\u0378\u036e\u0001\u0000\u0000\u0000\u0378\u0373\u0001"+
		"\u0000\u0000\u0000\u0379i\u0001\u0000\u0000\u0000\u037a\u037b\u0007\u000b"+
		"\u0000\u0000\u037b\u037c\u0005\u01f1\u0000\u0000\u037c\u037d\u0003:\u001d"+
		"\u0000\u037d\u037e\u0005\u01f2\u0000\u0000\u037e\u037f\u0003n7\u0000\u037f"+
		"\u03b3\u0001\u0000\u0000\u0000\u0380\u0381\u0007\f\u0000\u0000\u0381\u0382"+
		"\u0005\u01f1\u0000\u0000\u0382\u0389\u0003:\u001d\u0000\u0383\u0384\u0005"+
		"\u01f3\u0000\u0000\u0384\u0387\u0003:\u001d\u0000\u0385\u0386\u0005\u01f3"+
		"\u0000\u0000\u0386\u0388\u0003:\u001d\u0000\u0387\u0385\u0001\u0000\u0000"+
		"\u0000\u0387\u0388\u0001\u0000\u0000\u0000\u0388\u038a\u0001\u0000\u0000"+
		"\u0000\u0389\u0383\u0001\u0000\u0000\u0000\u0389\u038a\u0001\u0000\u0000"+
		"\u0000\u038a\u038b\u0001\u0000\u0000\u0000\u038b\u038c\u0005\u01f2\u0000"+
		"\u0000\u038c\u038d\u0003n7\u0000\u038d\u03b3\u0001\u0000\u0000\u0000\u038e"+
		"\u038f\u0007\r\u0000\u0000\u038f\u0390\u0005\u01f1\u0000\u0000\u0390\u0391"+
		"\u0005\u01f2\u0000\u0000\u0391\u0392\u0005\u0086\u0000\u0000\u0392\u0396"+
		"\u0005\u01f1\u0000\u0000\u0393\u0394\u0005\u012d\u0000\u0000\u0394\u0395"+
		"\u00054\u0000\u0000\u0395\u0397\u0003 \u0010\u0000\u0396\u0393\u0001\u0000"+
		"\u0000\u0000\u0396\u0397\u0001\u0000\u0000\u0000\u0397\u0398\u0001\u0000"+
		"\u0000\u0000\u0398\u0399\u0003\f\u0006\u0000\u0399\u039a\u0005\u01f2\u0000"+
		"\u0000\u039a\u03b3\u0001\u0000\u0000\u0000\u039b\u039c\u0007\u000e\u0000"+
		"\u0000\u039c\u039d\u0005\u01f1\u0000\u0000\u039d\u039e\u0003:\u001d\u0000"+
		"\u039e\u039f\u0005\u01f2\u0000\u0000\u039f\u03a0\u0005\u00b5\u0000\u0000"+
		"\u03a0\u03a1\u0005f\u0000\u0000\u03a1\u03a2\u0005\u01f1\u0000\u0000\u03a2"+
		"\u03a3\u0005\u0084\u0000\u0000\u03a3\u03a4\u00054\u0000\u0000\u03a4\u03a6"+
		"\u0003:\u001d\u0000\u03a5\u03a7\u0007\u0005\u0000\u0000\u03a6\u03a5\u0001"+
		"\u0000\u0000\u0000\u03a6\u03a7\u0001\u0000\u0000\u0000\u03a7\u03a8\u0001"+
		"\u0000\u0000\u0000\u03a8\u03a9\u0005\u01f2\u0000\u0000\u03a9\u03aa\u0005"+
		"\u0086\u0000\u0000\u03aa\u03ae\u0005\u01f1\u0000\u0000\u03ab\u03ac\u0005"+
		"\u012d\u0000\u0000\u03ac\u03ad\u00054\u0000\u0000\u03ad\u03af\u0003 \u0010"+
		"\u0000\u03ae\u03ab\u0001\u0000\u0000\u0000\u03ae\u03af\u0001\u0000\u0000"+
		"\u0000\u03af\u03b0\u0001\u0000\u0000\u0000\u03b0\u03b1\u0005\u01f2\u0000"+
		"\u0000\u03b1\u03b3\u0001\u0000\u0000\u0000\u03b2\u037a\u0001\u0000\u0000"+
		"\u0000\u03b2\u0380\u0001\u0000\u0000\u0000\u03b2\u038e\u0001\u0000\u0000"+
		"\u0000\u03b2\u039b\u0001\u0000\u0000\u0000\u03b3k\u0001\u0000\u0000\u0000"+
		"\u03b4\u03b6\u0007\u000f\u0000\u0000\u03b5\u03b4\u0001\u0000\u0000\u0000"+
		"\u03b5\u03b6\u0001\u0000\u0000\u0000\u03b6\u03b7\u0001\u0000\u0000\u0000"+
		"\u03b7\u03b8\u0003:\u001d\u0000\u03b8m\u0001\u0000\u0000\u0000\u03b9\u03ba"+
		"\u0005\u0086\u0000\u0000\u03ba\u03be\u0005\u01f1\u0000\u0000\u03bb\u03bc"+
		"\u0005\u012d\u0000\u0000\u03bc\u03bd\u00054\u0000\u0000\u03bd\u03bf\u0003"+
		" \u0010\u0000\u03be\u03bb\u0001\u0000\u0000\u0000\u03be\u03bf\u0001\u0000"+
		"\u0000\u0000\u03bf\u03c1\u0001\u0000\u0000\u0000\u03c0\u03c2\u0003\f\u0006"+
		"\u0000\u03c1\u03c0\u0001\u0000\u0000\u0000\u03c1\u03c2\u0001\u0000\u0000"+
		"\u0000\u03c2\u03c3\u0001\u0000\u0000\u0000\u03c3\u03c4\u0005\u01f2\u0000"+
		"\u0000\u03c4o\u0001\u0000\u0000\u0000\u03c5\u03c6\u0007\u0010\u0000\u0000"+
		"\u03c6q\u0001\u0000\u0000\u0000\u03c7\u03d4\u0003p8\u0000\u03c8\u03d4"+
		"\u0005\u0112\u0000\u0000\u03c9\u03d4\u0005\u0113\u0000\u0000\u03ca\u03d4"+
		"\u0005\u0114\u0000\u0000\u03cb\u03d4\u0005\u0115\u0000\u0000\u03cc\u03d4"+
		"\u0005\u0116\u0000\u0000\u03cd\u03d4\u0005\u0117\u0000\u0000\u03ce\u03d4"+
		"\u0005\u0118\u0000\u0000\u03cf\u03d4\u0005\u0119\u0000\u0000\u03d0\u03d4"+
		"\u0005\u011a\u0000\u0000\u03d1\u03d4\u0005\u011b\u0000\u0000\u03d2\u03d4"+
		"\u0005\u011c\u0000\u0000\u03d3\u03c7\u0001\u0000\u0000\u0000\u03d3\u03c8"+
		"\u0001\u0000\u0000\u0000\u03d3\u03c9\u0001\u0000\u0000\u0000\u03d3\u03ca"+
		"\u0001\u0000\u0000\u0000\u03d3\u03cb\u0001\u0000\u0000\u0000\u03d3\u03cc"+
		"\u0001\u0000\u0000\u0000\u03d3\u03cd\u0001\u0000\u0000\u0000\u03d3\u03ce"+
		"\u0001\u0000\u0000\u0000\u03d3\u03cf\u0001\u0000\u0000\u0000\u03d3\u03d0"+
		"\u0001\u0000\u0000\u0000\u03d3\u03d1\u0001\u0000\u0000\u0000\u03d3\u03d2"+
		"\u0001\u0000\u0000\u0000\u03d4s\u0001\u0000\u0000\u0000\u03d5\u040b\u0005"+
		"\u001e\u0000\u0000\u03d6\u03d7\u0003:\u001d\u0000\u03d7\u03db\u0005\u00d3"+
		"\u0000\u0000\u03d8\u03d9\u0005\u01f1\u0000\u0000\u03d9\u03da\u0005\u0203"+
		"\u0000\u0000\u03da\u03dc\u0005\u01f2\u0000\u0000\u03db\u03d8\u0001\u0000"+
		"\u0000\u0000\u03db\u03dc\u0001\u0000\u0000\u0000\u03dc\u03dd\u0001\u0000"+
		"\u0000\u0000\u03dd\u03de\u0005\u00a1\u0000\u0000\u03de\u03e2\u0005\u015b"+
		"\u0000\u0000\u03df\u03e0\u0005\u01f1\u0000\u0000\u03e0\u03e1\u0005\u0203"+
		"\u0000\u0000\u03e1\u03e3\u0005\u01f2\u0000\u0000\u03e2\u03df\u0001\u0000"+
		"\u0000\u0000\u03e2\u03e3\u0001\u0000\u0000\u0000\u03e3\u040c\u0001\u0000"+
		"\u0000\u0000\u03e4\u03e5\u0003:\u001d\u0000\u03e5\u03e9\u0005\u0180\u0000"+
		"\u0000\u03e6\u03e7\u0005\u01f1\u0000\u0000\u03e7\u03e8\u0005\u0203\u0000"+
		"\u0000\u03e8\u03ea\u0005\u01f2\u0000\u0000\u03e9\u03e6\u0001\u0000\u0000"+
		"\u0000\u03e9\u03ea\u0001\u0000\u0000\u0000\u03ea\u03eb\u0001\u0000\u0000"+
		"\u0000\u03eb\u03ec\u0005\u00a1\u0000\u0000\u03ec\u03ed\u0005\u011f\u0000"+
		"\u0000\u03ed\u040c\u0001\u0000\u0000\u0000\u03ee\u03ef\u0003:\u001d\u0000"+
		"\u03ef\u03f3\u0003p8\u0000\u03f0\u03f1\u0005\u01f1\u0000\u0000\u03f1\u03f2"+
		"\u0005\u0203\u0000\u0000\u03f2\u03f4\u0005\u01f2\u0000\u0000\u03f3\u03f0"+
		"\u0001\u0000\u0000\u0000\u03f3\u03f4\u0001\u0000\u0000\u0000\u03f4\u03fc"+
		"\u0001\u0000\u0000\u0000\u03f5\u03f6\u0005\u00a1\u0000\u0000\u03f6\u03fa"+
		"\u0003p8\u0000\u03f7\u03f8\u0005\u01f1\u0000\u0000\u03f8\u03f9\u0005\u0203"+
		"\u0000\u0000\u03f9\u03fb\u0005\u01f2\u0000\u0000\u03fa\u03f7\u0001\u0000"+
		"\u0000\u0000\u03fa\u03fb\u0001\u0000\u0000\u0000\u03fb\u03fd\u0001\u0000"+
		"\u0000\u0000\u03fc\u03f5\u0001\u0000\u0000\u0000\u03fc\u03fd\u0001\u0000"+
		"\u0000\u0000\u03fd\u040c\u0001\u0000\u0000\u0000\u03fe\u0400\u0005\u01d5"+
		"\u0000\u0000\u03ff\u03fe\u0001\u0000\u0000\u0000\u03ff\u0400\u0001\u0000"+
		"\u0000\u0000\u0400\u0401\u0001\u0000\u0000\u0000\u0401\u0405\u0003v;\u0000"+
		"\u0402\u0404\u0003v;\u0000\u0403\u0402\u0001\u0000\u0000\u0000\u0404\u0407"+
		"\u0001\u0000\u0000\u0000\u0405\u0403\u0001\u0000\u0000\u0000\u0405\u0406"+
		"\u0001\u0000\u0000\u0000\u0406\u0409\u0001\u0000\u0000\u0000\u0407\u0405"+
		"\u0001\u0000\u0000\u0000\u0408\u040a\u0005\u01d5\u0000\u0000\u0409\u0408"+
		"\u0001\u0000\u0000\u0000\u0409\u040a\u0001\u0000\u0000\u0000\u040a\u040c"+
		"\u0001\u0000\u0000\u0000\u040b\u03d6\u0001\u0000\u0000\u0000\u040b\u03e4"+
		"\u0001\u0000\u0000\u0000\u040b\u03ee\u0001\u0000\u0000\u0000\u040b\u03ff"+
		"\u0001\u0000\u0000\u0000\u040cu\u0001\u0000\u0000\u0000\u040d\u040e\u0003"+
		":\u001d\u0000\u040e\u040f\u0003r9\u0000\u040fw\u0001\u0000\u0000\u0000"+
		"\u0410\u0411\u0007\u0011\u0000\u0000\u0411\u0412\u0005\u01f1\u0000\u0000"+
		"\u0412\u0413\u0005\u01d8\u0000\u0000\u0413\u0422\u0005\u01f2\u0000\u0000"+
		"\u0414\u0415\u0003\u0080@\u0000\u0415\u0416\u0005\u01f1\u0000\u0000\u0416"+
		"\u0417\u0005\u01d8\u0000\u0000\u0417\u0418\u0005\u01f3\u0000\u0000\u0418"+
		"\u0419\u0005\u01d8\u0000\u0000\u0419\u041a\u0005\u01f2\u0000\u0000\u041a"+
		"\u0422\u0001\u0000\u0000\u0000\u041b\u041c\u0003\u0080@\u0000\u041c\u041d"+
		"\u0005\u01f1\u0000\u0000\u041d\u041e\u0005\u01d8\u0000\u0000\u041e\u041f"+
		"\u0005\u01f2\u0000\u0000\u041f\u0422\u0001\u0000\u0000\u0000\u0420\u0422"+
		"\u0003\u0080@\u0000\u0421\u0410\u0001\u0000\u0000\u0000\u0421\u0414\u0001"+
		"\u0000\u0000\u0000\u0421\u041b\u0001\u0000\u0000\u0000\u0421\u0420\u0001"+
		"\u0000\u0000\u0000\u0422y\u0001\u0000\u0000\u0000\u0423\u0433\u0005\u01da"+
		"\u0000\u0000\u0424\u0433\u0005\u01dc\u0000\u0000\u0425\u0427\u0003|>\u0000"+
		"\u0426\u0425\u0001\u0000\u0000\u0000\u0426\u0427\u0001\u0000\u0000\u0000"+
		"\u0427\u0428\u0001\u0000\u0000\u0000\u0428\u0433\u0005\u01d8\u0000\u0000"+
		"\u0429\u042b\u0003|>\u0000\u042a\u0429\u0001\u0000\u0000\u0000\u042a\u042b"+
		"\u0001\u0000\u0000\u0000\u042b\u042c\u0001\u0000\u0000\u0000\u042c\u0433"+
		"\u0007\u0012\u0000\u0000\u042d\u042f\u0003|>\u0000\u042e\u042d\u0001\u0000"+
		"\u0000\u0000\u042e\u042f\u0001\u0000\u0000\u0000\u042f\u0430\u0001\u0000"+
		"\u0000\u0000\u0430\u0431\u0005\u01f0\u0000\u0000\u0431\u0433\u0007\u0013"+
		"\u0000\u0000\u0432\u0423\u0001\u0000\u0000\u0000\u0432\u0424\u0001\u0000"+
		"\u0000\u0000\u0432\u0426\u0001\u0000\u0000\u0000\u0432\u042a\u0001\u0000"+
		"\u0000\u0000\u0432\u042e\u0001\u0000\u0000\u0000\u0433{\u0001\u0000\u0000"+
		"\u0000\u0434\u0435\u0007\b\u0000\u0000\u0435}\u0001\u0000\u0000\u0000"+
		"\u0436\u0437\u0007\u0014\u0000\u0000\u0437\u007f\u0001\u0000\u0000\u0000"+
		"\u0438\u043e\u0005\u01d9\u0000\u0000\u0439\u043e\u0005\u01d4\u0000\u0000"+
		"\u043a\u043e\u0005\u01d3\u0000\u0000\u043b\u043e\u0005\u01d6\u0000\u0000"+
		"\u043c\u043e\u0003~?\u0000\u043d\u0438\u0001\u0000\u0000\u0000\u043d\u0439"+
		"\u0001\u0000\u0000\u0000\u043d\u043a\u0001\u0000\u0000\u0000\u043d\u043b"+
		"\u0001\u0000\u0000\u0000\u043d\u043c\u0001\u0000\u0000\u0000\u043e\u0081"+
		"\u0001\u0000\u0000\u0000\u043f\u044f\u0005\u01df\u0000\u0000\u0440\u044f"+
		"\u0005\u01e0\u0000\u0000\u0441\u044f\u0005\u01e1\u0000\u0000\u0442\u0443"+
		"\u0005\u01e1\u0000\u0000\u0443\u044f\u0005\u01df\u0000\u0000\u0444\u0445"+
		"\u0005\u01e0\u0000\u0000\u0445\u044f\u0005\u01df\u0000\u0000\u0446\u0447"+
		"\u0005\u01e1\u0000\u0000\u0447\u044f\u0005\u01e0\u0000\u0000\u0448\u0449"+
		"\u0005\u01e2\u0000\u0000\u0449\u044f\u0005\u01df\u0000\u0000\u044a\u044b"+
		"\u0005\u01e2\u0000\u0000\u044b\u044f\u0005\u01e0\u0000\u0000\u044c\u044d"+
		"\u0005\u01e2\u0000\u0000\u044d\u044f\u0005\u01e1\u0000\u0000\u044e\u043f"+
		"\u0001\u0000\u0000\u0000\u044e\u0440\u0001\u0000\u0000\u0000\u044e\u0441"+
		"\u0001\u0000\u0000\u0000\u044e\u0442\u0001\u0000\u0000\u0000\u044e\u0444"+
		"\u0001\u0000\u0000\u0000\u044e\u0446\u0001\u0000\u0000\u0000\u044e\u0448"+
		"\u0001\u0000\u0000\u0000\u044e\u044a\u0001\u0000\u0000\u0000\u044e\u044c"+
		"\u0001\u0000\u0000\u0000\u044f\u0083\u0001\u0000\u0000\u0000\u0450\u0451"+
		"\u0005\u01e0\u0000\u0000\u0451\u0452\u0005\u01e0\u0000\u0000\u0452\u045b"+
		"\u0005\u01e0\u0000\u0000\u0453\u0454\u0005\u01e0\u0000\u0000\u0454\u045b"+
		"\u0005\u01e0\u0000\u0000\u0455\u0456\u0005\u01e1\u0000\u0000\u0456\u045b"+
		"\u0005\u01e1\u0000\u0000\u0457\u045b\u0005\u01fe\u0000\u0000\u0458\u045b"+
		"\u0005\u01fd\u0000\u0000\u0459\u045b\u0005\u01ff\u0000\u0000\u045a\u0450"+
		"\u0001\u0000\u0000\u0000\u045a\u0453\u0001\u0000\u0000\u0000\u045a\u0455"+
		"\u0001\u0000\u0000\u0000\u045a\u0457\u0001\u0000\u0000\u0000\u045a\u0458"+
		"\u0001\u0000\u0000\u0000\u045a\u0459\u0001\u0000\u0000\u0000\u045b\u0085"+
		"\u0001\u0000\u0000\u0000\u045c\u045d\u0007\u0015\u0000\u0000\u045d\u0087"+
		"\u0001\u0000\u0000\u0000{\u0089\u008f\u0092\u0095\u0098\u009b\u009f\u00a9"+
		"\u00b6\u00bb\u00c4\u00da\u00dc\u00e3\u00e9\u00f2\u00f5\u00f9\u0100\u0102"+
		"\u0107\u0109\u010d\u0114\u0116\u0118\u011b\u011f\u0121\u012b\u012f\u0136"+
		"\u013e\u0140\u0144\u0152\u015a\u0163\u0169\u016f\u0175\u017a\u017e\u0188"+
		"\u0192\u019f\u01a7\u01af\u01b5\u01ba\u01c0\u01c4\u01c7\u01cf\u01d3\u01db"+
		"\u01dd\u01e1\u01e6\u01ed\u01f2\u01f7\u01fd\u0205\u0212\u0224\u022b\u022d"+
		"\u0234\u023f\u0243\u024f\u0253\u0257\u025d\u0267\u0272\u027c\u0284\u0290"+
		"\u02a4\u02d9\u02dd\u02fb\u0301\u0305\u030d\u0316\u0329\u0339\u0349\u0362"+
		"\u0368\u036c\u0378\u0387\u0389\u0396\u03a6\u03ae\u03b2\u03b5\u03be\u03c1"+
		"\u03d3\u03db\u03e2\u03e9\u03f3\u03fa\u03fc\u03ff\u0405\u0409\u040b\u0421"+
		"\u0426\u042a\u042e\u0432\u043d\u044e\u045a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}