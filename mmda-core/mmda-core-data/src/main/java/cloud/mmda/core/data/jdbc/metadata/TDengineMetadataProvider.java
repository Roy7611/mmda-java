package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.sql.SqlDateTime;
import cloud.mmda.core.data.sql.TDengineDateTime;
import cloud.mmda.core.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class TDengineMetadataProvider extends SqlMetadataProvider{
    private final static char Q = '`';//关键字引号
    private final TDengineDateTime sqlDateTime=new TDengineDateTime();

    @Autowired
    public TDengineMetadataProvider(DataSource ds, String defaultDbName, String metadataDbName, RedisConnectionFactory factory) {
        super(ds,defaultDbName,metadataDbName, factory);
    }
    @Override
    public SqlDateTime getSqlDateTime() {
        return sqlDateTime;
    }

    @Override
    public String toPagerSql(Paginator paginator) {
        return String.format(" ORDER BY %1$s OFFSET %2$d*%3$d ROWS FETCH NEXT %3$d ROWS ONLY",
                paginator.getOrderBy(),
                paginator.getPageNo()-1,
                paginator.getPageSize()
        );
    }

    @Override
    public String isNullFuncName() {
        return  "ISNULL";
    }

    @Override
    public String call(String func, int argsNum) {
        return "call " + func+"(" + BaseUtil.repeat("?",argsNum,",") + ")";

    }
//是否支持命名分区
    @Override
    public boolean supportNamedPartition() {
        return true;
    }
//是否支持按字段排序
    @Override
    public boolean supportOrderByField() {
        return true;
    }
//关键字加''
    @Override
    public String quoteName(String name) {
         return Q+name+Q;
    }
//参数占位符
    @Override
    public char getParamPlaceholder() {
         return '?';
    }
}
