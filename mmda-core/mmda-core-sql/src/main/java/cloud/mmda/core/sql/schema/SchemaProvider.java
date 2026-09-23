package cloud.mmda.core.sql.schema;

import javax.sql.DataSource;
import java.util.List;

public interface SchemaProvider {
    /**
     * 获取数据源
     * @return
     */
    DataSource getDataSource();
    /**
     * 获取所有数据库
     * @return
     */
    List<String> getDatabases();
    /**
     * 获取所有表
     * @param dbName
     * @return
     */
    List<Table> getTables(String dbName);

    /**
     * 获取一张表
     * @param dbName
     * @param tableName
     * @return
     */
    Table getTable(String dbName, String tableName, boolean withColumns);
    default Table getTable(String dbName, String tableName){
        return getTable(dbName, tableName,false);
    }

    /**
     * 获取一张表的主键
     * @param dbName
     * @param tableName
     * @return
     */
    List<String> getPrimaryKeys(String dbName, String tableName);

    /**
     * 获取一张表的所有字段
     * @param dbName
     * @param tableName
     * @return
     */
    List<Column> getColumns(String dbName, String tableName);


}
