package cloud.mmda.core.sql.schema;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class SqlServerSchemaProvider implements SchemaProvider {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Column> colRowMapper;
    private RowMapper<Table> tbRowMapper;

    public SqlServerSchemaProvider(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.colRowMapper = createColRowMapper();
        this.tbRowMapper = new RowMapper<Table>() {
            @Override
            public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
                Table t = new Table();

                t.setName(rs.getString("TABLE_NAME"));

//                t.setComment(rs.getString("TABLE_COMMENT"));//SqlServer无此字段
                return t;
            }

        };
    }
    @Override
    public DataSource getDataSource(){
        return jdbcTemplate.getDataSource();
    }
    @Override
    public List<String> getDatabases() throws DataAccessException {
        String sql = "SELECT name FROM sys.databases";
        return this.jdbcTemplate.queryForList(sql,String.class);
    }

    @Override
    public List<Table> getTables(String dbName) {
        String sql = "select * from INFORMATION_SCHEMA.TABLES where TABLE_CATALOG = ?";
        return this.jdbcTemplate.query(sql, new Object[] { dbName }, tbRowMapper);
    }

    @Override
    public Table getTable(String dbName, String tableName, boolean withColumns) {
        String sql = "select * from INFORMATION_SCHEMA.TABLES where TABLE_CATALOG = ? and TABLE_NAME = ?";
        Table table = this.jdbcTemplate.queryForObject(sql, new Object[] { dbName, tableName }, tbRowMapper);
        if(withColumns) table.setColumns(getColumns(dbName, tableName));
        return table;
    }
    @Override
    public List<String> getPrimaryKeys(String dbName, String tableName) throws DataAccessException {
        String sql = "select b.COLUMN_NAME\n" +
                "from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a \n" +
                "\tinner join INFORMATION_SCHEMA.KEY_COLUMN_USAGE b on a.CONSTRAINT_CATALOG=b.CONSTRAINT_CATALOG and a.CONSTRAINT_NAME=b.CONSTRAINT_NAME\n" +
                "where a.CONSTRAINT_TYPE = 'PRIMARY KEY' and a.CONSTRAINT_CATALOG=? and a.table_name=?\n" +
                "order by b.ORDINAL_POSITION";
        return this.jdbcTemplate.queryForList(sql, new Object[] { dbName, tableName },String.class);
    }
    @Override
    public List<Column> getColumns(String dbName, String tableName) {
        String sql = "select * from INFORMATION_SCHEMA.COLUMNS where TABLE_CATALOG = ? and TABLE_NAME = ?";
        return this.jdbcTemplate.query(sql, new Object[] { dbName, tableName }, colRowMapper);
    }

    private RowMapper<Column> createColRowMapper(){
        return new RowMapper<Column>() {
            @Override
            public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
                Column col = new Column();
                col.setName(rs.getString("COLUMN_NAME"));
                col.setIdx(rs.getInt("ORDINAL_POSITION"));
                //SqlServer无此字段
//                String k = rs.getString("COLUMN_KEY");
//                if (!rs.wasNull() && k.startsWith("PRI")) {
//                    col.setKey(true);
//                }
                col.setDataType(rs.getString("DATA_TYPE"));
                String n = rs.getString("IS_NULLABLE");
                if (n.startsWith("YES"))
                    col.setNullable(true);
                else
                    col.setNullable(false);
                //SqlServer无此字段
//                col.setUnsigned(rs.getString("COLUMN_TYPE").indexOf("unsigned") != -1);
//                col.setComment(rs.getString("COLUMN_COMMENT"));
                long len = rs.getLong("CHARACTER_MAXIMUM_LENGTH");
                if (rs.wasNull() || len > 8000 || len<0)//-1
                    col.setMaxLength(null);
                else
                    col.setMaxLength(len);
                long np = rs.getLong("NUMERIC_PRECISION");
                if (rs.wasNull() || np > Byte.MAX_VALUE)
                    col.setNumericPrecision(null);
                else
                    col.setNumericPrecision((byte) np);
                long ns = rs.getLong("NUMERIC_SCALE");
                if (rs.wasNull() || np > Byte.MAX_VALUE)
                    col.setNumericScale(null);
                else
                    col.setNumericScale((byte) np);
                //SqlServer无此字段
//                String g = rs.getString("IS_GENERATED");
//                if (g.startsWith("ALWAYS"))
//                    col.setComputed(true);
//                else
//                    col.setComputed(false);
//                String extra = rs.getString("EXTRA");
//                if (!rs.wasNull() && extra.startsWith("auto_increment")) {
//                    col.setAutoIncr(true);
//                }
                col.setDefaultVal(rs.getString("COLUMN_DEFAULT"));
                return col;
            }
        };
    }
}
