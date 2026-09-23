package cloud.mmda.core.sql.schema;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * MySql数据库Schema提供者
 */
public final class MySqlSchemaProvider implements SchemaProvider {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<Column> colRowMapper;
    private RowMapper<Table> tbRowMapper;

    public MySqlSchemaProvider(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.colRowMapper = createColRowMapper();
        this.tbRowMapper = new RowMapper<Table>() {
            @Override
            public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
                Table t = new Table();
                t.setDb(rs.getString("TABLE_SCHEMA"));
                t.setName(rs.getString("TABLE_NAME"));
                t.setType(rs.getString("TABLE_TYPE"));
                t.setComment(rs.getString("TABLE_COMMENT"));
                t.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                t.setLastModified(rs.getTimestamp("UPDATE_TIME"));
                return t;
            }

        };
    }
    @Override
    public DataSource getDataSource(){
        return jdbcTemplate.getDataSource();
    }
    @Override
    public List<String> getDatabases() throws DataAccessException{
        String sql = "select `SCHEMA_NAME` from `INFORMATION_SCHEMA`.`SCHEMATA`";
        return this.jdbcTemplate.queryForList(sql,String.class);
    }

    @Override
    public List<Table> getTables(String dbName) throws DataAccessException {
        String sql = "select * from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA` = ?";
        return this.jdbcTemplate.query(sql, new Object[] { dbName }, tbRowMapper);
    }

    @Override
    public Table getTable(String dbName, String tableName, boolean withColumns) throws DataAccessException {
        String sql = "select * from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA` = ? and `TABLE_NAME` = ?";
        Table table = this.jdbcTemplate.queryForObject(sql, new Object[] { dbName, tableName }, tbRowMapper);
        if(withColumns) table.setColumns(getColumns(dbName, tableName));
        return table;
    }

    @Override
    public List<String> getPrimaryKeys(String dbName, String tableName) throws DataAccessException {
        String sql = "SELECT COLUMN_NAME \n" +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE \n" +
                "WHERE CONSTRAINT_SCHEMA=? AND TABLE_NAME=? \n" +
                "\tAND CONSTRAINT_NAME='PRIMARY' \n" +
                "ORDER BY ORDINAL_POSITION";
        return this.jdbcTemplate.queryForList(sql, new Object[] { dbName, tableName },String.class);
    }
    @Override
    public List<Column> getColumns(String dbName, String tableName) throws DataAccessException {
        String sql = "select * from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA` = ? and `TABLE_NAME` = ?";
        return this.jdbcTemplate.query(sql, new Object[] { dbName, tableName }, colRowMapper);
    }


    private RowMapper<Column> createColRowMapper(){
        return new RowMapper<Column>() {
            @Override
            public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
                Column col = new Column();
                col.setName(rs.getString("COLUMN_NAME"));
                col.setIdx(rs.getInt("ORDINAL_POSITION"));
                String k = rs.getString("COLUMN_KEY");
                if (!rs.wasNull() && k.startsWith("PRI")) {
                    col.setKey(true);
                }
                else{
                    col.setKey(false);
                }
                col.setDataType(rs.getString("DATA_TYPE"));
                String n = rs.getString("IS_NULLABLE");
                if (n.startsWith("YES"))
                    col.setNullable(true);
                else
                    col.setNullable(false);
                col.setUnsigned(rs.getString("COLUMN_TYPE").indexOf("unsigned") != -1);
                col.setComment(rs.getString("COLUMN_COMMENT"));
                long len = rs.getLong("CHARACTER_MAXIMUM_LENGTH");
                if (rs.wasNull() || len > 8000)
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
                    col.setNumericScale((byte) ns);
                String g = rs.getString("IS_GENERATED");
                if (g.startsWith("ALWAYS")) {
                    col.setComputed(true);
                    String formula = rs.getString("GENERATION_EXPRESSION");
                    col.setFormula(formula.replaceAll("`",""));
                }
                else {
                    col.setComputed(false);
                    col.setFormula(null);
                }
                String extra = rs.getString("EXTRA");
                if (!rs.wasNull() && extra.startsWith("auto_increment")) {
                    col.setAutoIncr(true);
                }
                col.setDefaultVal(rs.getString("COLUMN_DEFAULT"));
                if("NULL".equals(col.getDefaultVal())) col.setDefaultVal(null);
                return col;
            }
        };
    }
}
