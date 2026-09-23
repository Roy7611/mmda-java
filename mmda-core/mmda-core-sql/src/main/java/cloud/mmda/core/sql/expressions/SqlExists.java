package cloud.mmda.core.sql.expressions;

import cloud.mmda.core.sql.SqlQuery;

public class SqlExists extends SqlCriteriaExp {
    public SqlExists(SqlQuery subQuery, boolean not) {
        super(subQuery, not ? SqlOp.EXISTS : SqlOp.NOT_EXISTS, null);
    }
}
