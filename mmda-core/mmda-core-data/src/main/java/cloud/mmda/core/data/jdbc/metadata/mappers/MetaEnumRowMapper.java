package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetaEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * 元枚举RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-21 00:28:29.0
 * 
 */
public class MetaEnumRowMapper implements RowMapper<MetaEnum> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaEnum
	 * @throws SQLException
	 */
	@Override
	public MetaEnum mapRow(ResultSet rs, int rowNum) throws SQLException{
		MetaEnum t = new MetaEnum();
		t.setRowNum(rowNum);
		t.setEnumClass(rs.getString("enumClass"));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setNamespace(rs.getString("nameSpace"));
		t.setEnumString(rs.getString("enumString"));
		t.setDataType(DataType.valueOf(rs.getInt("dataType")));
		t.setBitwise(rs.getBoolean("bitwise"));
		t.setCreateDate(rs.getTimestamp("createDate"));
		t.setLastModified(rs.getTimestamp("lastModified"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
