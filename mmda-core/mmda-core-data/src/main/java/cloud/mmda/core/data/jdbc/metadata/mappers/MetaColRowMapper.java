package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.*;
import cloud.mmda.core.metadata.MetaCol;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MetaCol 元对象行映射器
 **/
public class MetaColRowMapper implements RowMapper<MetaCol> {
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaCol
	 * @throws SQLException
	 */
	@Override
	public MetaCol mapRow(ResultSet rs, int rowNum) throws SQLException {
		MetaCol t = new MetaCol();
		t.setRowNum(rowNum);
		t.setDbSchema(rs.getString("dbSchema"));
		t.setObjName(rs.getString("objName"));
		t.setColName(rs.getString("colName"));
		t.setKey(rs.getBoolean("isKey"));
		t.setGenerated(rs.getBoolean("isGenerated"));
		t.setColIdx(rs.getInt("colIdx"));
		t.setGroupLabel(rs.getString("groupLabel"));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setMergeLabel(rs.getString("mergeLabel"));
		t.setMergePrefix(rs.getString("mergePrefix"));
		t.setDataType(DataType.valueOf(rs.getInt("dataType")));
		int maxLength = rs.getInt("maxLength");
		if(rs.wasNull())
			t.setMaxLength(null);
		else
			t.setMaxLength(maxLength);
		t.setUnsigned(rs.getBoolean("isUnsigned"));
		int numericPrecision = rs.getInt("numericPrecision");
		if(rs.wasNull())
			t.setNumericPrecision(null);
		else
			t.setNumericPrecision(numericPrecision);
		int numericScale = rs.getInt("numericScale");
		if(rs.wasNull())
			t.setNumericScale(null);
		else
			t.setNumericScale(numericScale);
		t.setNullable(rs.getBoolean("nullable"));
		t.setComputed(rs.getBoolean("computed"));
		t.setFormula(rs.getString("formula"));
		t.setConstraint(rs.getString("constraint"));
		t.setDefaultVal(rs.getString("defaultVal"));
		t.setExtendType(ExtensionType.valueOf(rs.getByte("extendType")));
		t.setEnumSet(rs.getString("enumSet"));
		t.setNullIfEmpty(rs.getBoolean("nullIfEmpty"));
		t.setFilterable(rs.getBoolean("filterable"));
		t.setFieldName(rs.getString("fieldName"));
		t.setListed(rs.getBoolean("listed"));
		t.setAggregationSet(EnumBitSet.valueOf(FieldAggregation.class,rs.getInt("aggregationSet")));
		t.setReadOnly(rs.getBoolean("readOnly"));
		t.setHidden(rs.getBoolean("hidden"));
		t.setDescription(rs.getString("description"));
		return t;
	}
}
