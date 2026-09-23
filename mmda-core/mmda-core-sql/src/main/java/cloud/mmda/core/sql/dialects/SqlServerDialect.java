package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.SqlCmd;
import cloud.mmda.core.sql.SqlMetaContext;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.SqlServerDataTypes;

import java.util.LinkedHashMap;

public class SqlServerDialect extends AnsiSqlDialect {
    private static final String DEFAULT_DB_OWNER = "dbo";

    public SqlServerDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "SqlServer";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return SqlServerDataTypes.getType(genericDataType);
    }

    @Override
    public String quote(String identifier) {
        return "[" + identifier + "]";
    }

    @Override
    public String unquote(String identifier) {
        var sb = new StringBuilder();
        for (int i = 0; i < identifier.length(); i++) {
            var ch = identifier.charAt(i);
            if(ch== '[' || ch == ']') continue;
            sb.append(ch);
        }
        return sb.toString();
    }

    @Override
    public String quoteObject(String schema, String identifier) {
        return String.join(".",schema, DEFAULT_DB_OWNER, quote(identifier));
    }


    //region UPDATE

    /**
     * {@inheritDoc}
     * <p>
     *     Sql Server 一条语句限定只能更新一张表，UPDATE FROM 语法
     * </p>
     *
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/queries/update-transact-sql?view=sql-server-ver17">官方语法文档</a>
     */
    @Override
    public SqlCmd update(final SqlMetaContext context, LinkedHashMap<MetaCol, Object> assignments, SqlCriteriaExp condition, boolean namedParameter) {
        return super.update(context, assignments, condition, namedParameter);
    }

    //endregion of UPDATE
}
