package cloud.mmda.core.sql;

import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.dialects.MySqlDialect;
import cloud.mmda.core.sql.dialects.OracleDialect;
import cloud.mmda.core.sql.dialects.PostgreSqlDialect;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.mock.MockMetadataProvider;
import cloud.mmda.core.sql.mock.Partner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cloud.mmda.core.sql.expressions.SqlExp.col;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SqlQueryTest {


    private MetadataProvider provider = new MockMetadataProvider();
    private SqlDialect mySqlDialect = new MySqlDialect(provider);
    private SqlDialect pgSqlDialect = new PostgreSqlDialect(provider);
    private SqlDialect oracleSqlDialect = new OracleDialect(provider);

    @Test
    @DisplayName("SqlQuery.create 创建")
    void testCreate() {
        var query = SqlQuery.create(provider)
                .from(Partner.class).with(Partner::getCreator)
                .where(col(Partner::getPartnerID).between(1, 100))
                .orderBy(Partner::getPartnerNo)
                .selectAll();

        var sql = query.compile(mySqlDialect);
        System.out.println(sql);
        assertNotNull(query);
    }

}