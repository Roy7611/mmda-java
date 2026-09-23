package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.metadata.MetaDataType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MetaDataType 元数据类型行映射器
 **/
public class MetaDataTypeRowMapper implements RowMapper<MetaDataType> {
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaDataType
	 * @throws SQLException
	 */
	@Override
	public MetaDataType mapRow(ResultSet rs, int rowNum) throws SQLException {
		MetaDataType t = new MetaDataType();
		t.setDataType(rs.getInt("dataType"));
		t.setDataTypeName(rs.getString("dataTypeName"));
		t.setCsharpType(rs.getString("csharpType"));
		t.setJavaType(rs.getString("javaType"));
		t.setDartType(rs.getString("dartType"));
		t.setSwiftType(rs.getString("swiftType"));
		int jdbcType = rs.getInt("jdbcType");
		if (rs.wasNull())
			t.setJdbcType(null);
		else
			t.setJdbcType(jdbcType);

		int mssqlType = rs.getInt("mssqlType");
		if (rs.wasNull())
			t.setMssqlType(null);
		else
			t.setMssqlType(mssqlType);
		t.setMssqlName(rs.getString("mssqlName"));
		int mysqlType = rs.getInt("mysqlType");
		if (rs.wasNull())
			t.setMysqlType(null);
		else
			t.setMysqlType(mysqlType);
		t.setMysqlName(rs.getString("mysqlName"));
		int oracleType = rs.getInt("oracleType");
		if (rs.wasNull())
			t.setOracleType(null);
		else
			t.setOracleType(oracleType);
		t.setOracleName(rs.getString("oracleName"));
		int sqliteType = rs.getInt("sqliteType");
		if (rs.wasNull())
			t.setSqliteType(null);
		else
			t.setSqliteType(sqliteType);
		t.setSqliteName(rs.getString("sqliteName"));
		//2026.4.6 extended
		int pgsqlType = rs.getInt("pgsqlType");
		if (rs.wasNull())
			t.setPgsqlType(null);
		else
			t.setPgsqlType(pgsqlType);
		t.setPgsqlName(rs.getString("pgsqlName"));

		int kingbaseType = rs.getInt("kingbaseType");
		if (rs.wasNull())
			t.setKingbaseType(null);
		else
			t.setKingbaseType(kingbaseType);
		t.setKingbaseName(rs.getString("kingbaseName"));

		int dmType = rs.getInt("dmType");
		if (rs.wasNull())
			t.setDmType(null);
		else
			t.setDmType(dmType);
		t.setDmName(rs.getString("dmName"));
		return t;
	}
}
