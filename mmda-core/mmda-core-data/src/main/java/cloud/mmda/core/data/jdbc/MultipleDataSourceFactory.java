package cloud.mmda.core.data.jdbc;

import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MultipleDataSourceFactory implements DataSourceLookup {

    private static final Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
    @Override
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        if(!dataSourceMap.containsKey(dataSourceName))
            throw new DataSourceLookupFailureException(dataSourceName+" not found");
        return dataSourceMap.get(dataSourceName);
    }

}
