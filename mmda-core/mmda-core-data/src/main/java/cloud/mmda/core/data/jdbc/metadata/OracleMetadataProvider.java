package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.sql.OracleDateTime;
import cloud.mmda.core.data.sql.SqlDateTime;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.data.pagination.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Oracle 元数据提供者
 * @author roshion
 */
@Component
public class OracleMetadataProvider extends SqlMetadataProvider {
    private final OracleDateTime sqlDateTime;
    @Autowired
    public OracleMetadataProvider(DataSource ds, String defaultDbName, String metadataDbName, RedisConnectionFactory factory){
        super(ds,defaultDbName,metadataDbName,factory);
        sqlDateTime = new OracleDateTime();
    }

    @Override
    public final String quoteName(String name) {
        return name;
    }


    @Override
    public final String isNullFuncName(){
        return "NVL";
    }
    @Override
    public final char getParamPlaceholder() {
        return '?';
    }

    @Override
    public final String getCurrentTimestampFunc() {
        return "CURRENT_TIMESTAMP()";
    }
    @Override
    public final SqlDateTime getSqlDateTime(){ return sqlDateTime;}

    @Override
    public final boolean supportNamedPartition(){
        return true;
    }
    @Override
    public final boolean supportOrderByField(){
        return false;
    }
    @Override
    public String call(String func, int argsNum){
        return "call " + func+"(" + BaseUtil.repeat("?",argsNum,",") + ")";
    }

    @Override
    public String toPagerSql(Paginator paginator){
        return String.format(" ORDER BY %1$s OFFSET %2$d*%3$d ROWS FETCH NEXT %3$d ROWS ONLY",
                paginator.getOrderBy(),
                paginator.getPageNo()-1,
                paginator.getPageSize()
        );
    }

}
