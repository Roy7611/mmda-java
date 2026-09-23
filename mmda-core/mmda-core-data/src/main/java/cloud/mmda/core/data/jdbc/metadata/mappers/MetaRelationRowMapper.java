package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.DisplayShape;
import cloud.mmda.core.enums.FetchMode;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.metadata.MetaRelation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MetaRelation 元关系行映射器
 * 2023.10.04 增加canHave, limit, sort 字段
 **/
public class MetaRelationRowMapper implements RowMapper<MetaRelation> {
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaRelation
	 * @throws SQLException
	 */
	@Override
	public MetaRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
		MetaRelation t = new MetaRelation();
		t.setRowNum(rowNum);
		t.setDbSchema(rs.getString("dbSchema"));
		t.setObjName(rs.getString("objName"));
		t.setRelationName(rs.getString("relationName"));
		t.setRelationType(MetaRelationType.valueOf(rs.getByte("relationType")));
		int relationIdx = rs.getInt("relationIdx");
		if(rs.wasNull())
			t.setRelationIdx(null);
		else
			t.setRelationIdx(relationIdx);
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setRelativeObjName(rs.getString("relativeObjName"));
		t.setDisplayShape(DisplayShape.valueOf(rs.getByte("displayShape")));
		t.setShapeKey(rs.getString("shapeKey"));
		t.setReadOnly(rs.getBoolean("readOnly"));
		t.setJoinOn(rs.getString("joinOn"));
		t.setCanHave(rs.getString("canHave"));
		t.setRequiredAny(rs.getBoolean("requiredAny"));
		t.setSequenceKey(rs.getString("sequenceKey"));
		boolean secondary = rs.getBoolean("secondary");
		if(rs.wasNull())
			t.setSecondary(null);
		else
			t.setSecondary(secondary);
		t.setFetchMode(FetchMode.valueOf(rs.getByte("fetchMode")));
		t.setDefaultFilter(rs.getString("defaultFilter"));
		t.setDefaultGroupBy(rs.getString("defaultGroupBy"));
		int defaultPageSize = rs.getInt("defaultPageSize");
		if(rs.wasNull())
			t.setDefaultPageSize(null);
		else
			t.setDefaultPageSize(defaultPageSize);
		t.setDefaultSort(rs.getString("defaultSort"));
		t.setAggregates(rs.getString("aggregates"));
		return t;
	}
}
