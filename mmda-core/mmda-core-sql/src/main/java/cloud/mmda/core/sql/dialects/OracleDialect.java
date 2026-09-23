package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.SqlCmd;
import cloud.mmda.core.sql.SqlMetaContext;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.OracleDataTypes;

import java.util.LinkedHashMap;

public class OracleDialect extends AnsiSqlDialect {

    public OracleDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "Oracle";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return OracleDataTypes.getType(genericDataType);
    }

    //region UPDATE

    /**
     * {@inheritDoc} UPDATE FROM 语法
     *
     * @see <a href="https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/UPDATE.html">官方语法文档</a>
     */
    @Override
    public SqlCmd update(SqlMetaContext context, LinkedHashMap<MetaCol, Object> assignments, SqlCriteriaExp condition, boolean namedParameter) {
        return super.update(context, assignments, condition, namedParameter);
    }

    //endregion of UPDATE

    //region DDL


    //endregion of DDL
}
