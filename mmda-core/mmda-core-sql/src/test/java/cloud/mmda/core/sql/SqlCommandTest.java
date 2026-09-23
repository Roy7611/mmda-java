package cloud.mmda.core.sql;

import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.dialects.MySqlDialect;
import cloud.mmda.core.sql.dialects.OracleDialect;
import cloud.mmda.core.sql.dialects.PostgreSqlDialect;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.expressions.SqlExp;
import cloud.mmda.core.sql.mock.MockMetadataProvider;
import cloud.mmda.core.sql.mock.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlCommandTest {
    private MetadataProvider provider = new MockMetadataProvider();
    private SqlDialect mySqlDialect = new MySqlDialect(provider);
    private SqlDialect pgSqlDialect = new PostgreSqlDialect(provider);
    private SqlDialect oracleSqlDialect = new OracleDialect(provider);

    @Test
    void delete() {
        var metaObj = provider.getMetaObject(Partner.class);
        var cmd = mySqlDialect.delete(metaObj, SqlExp.col(Partner::getPartnerName).contains("申扬"));
        var sql = cmd.sql();
        System.out.println(sql);
        assertEquals("DELETE FROM crm.`Partner` AS t WHERE (t.`partnerID` BETWEEN ? AND ?) AND (t.`partnerName` LIKE ?)", sql);
    }

    @Test
    void create(){
        var metaObj = provider.getMetaObject(Partner.class);
        var cmd = oracleSqlDialect.insert(metaObj, false);
        var sql = cmd.sql();
        System.out.println(sql);
        assertEquals("INSERT INTO crm.\"Partner\"(\"partnerID\",\"partnerNo\",\"partnerName\",\"orderCount\") VALUES (?,?,?,?)", sql);
    }

    @Test
    void update(){
        var metaObj = provider.getMetaObject(Partner.class);
        var cmd = mySqlDialect.update(metaObj, SqlExp.col(Partner::getPartnerName).isNotNull(), false);
        var sql = cmd.sql();
        System.out.println(sql);
        assertEquals("UPDATE t FROM crm.`Partner` AS t SET t.`partnerNo` = ? WHERE (t.`partnerID` BETWEEN ? AND ?) AND (t.`partnerName` IS NOT NULL)", sql);
    }
}