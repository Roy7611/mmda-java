package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.metadata.MetaObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MetaObject 元对象行映射器
 **/
public class MetaObjectRowMapper implements RowMapper<MetaObject> {
	/**
	 * 映射函数
	 **/
	@Override
	public MetaObject mapRow(ResultSet rs, int rowNum) throws SQLException {
		MetaObject t = new MetaObject();
		mapRow(t, rs, rowNum);
		return t;
	}

	public static void mapRow(MetaObject t, ResultSet rs, int rowNum) throws SQLException {
	    t.setRowNum(rowNum);
		t.setDbSchema(rs.getString("dbSchema"));
		t.setObjName(rs.getString("objName"));
		t.setNameSpace(rs.getString("nameSpace"));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setObjType(rs.getString("objType"));
		t.setFixedFilter(rs.getString("fixedFilter"));
		t.setUniqueKey(rs.getString("uniqueKey"));
		t.setPartitionKey(rs.getString("partitionKey"));
		t.setPartitioned(rs.getBoolean("partitioned"));
		t.setNameCol(rs.getString("nameCol"));
		t.setParentIdCol(rs.getString("parentIdCol"));
		t.setGroupByCol(rs.getString("groupByCol"));
		t.setThumbnailCol(rs.getString("thumbnailCol"));
		long minID = rs.getLong("minID");
		if (rs.wasNull())
			t.setMinID(null);
		else
			t.setMinID(minID);
		long maxID = rs.getLong("maxID");
		if (rs.wasNull())
			t.setMaxID(null);
		else
			t.setMaxID(maxID);
		t.setExtendType(ExtensionType.valueOf(rs.getByte("extendType")));
		t.setSuperName(rs.getString("superName"));
		t.setDiscrimination(rs.getString("discrimination"));
		t.setDescription(rs.getString("description"));
		t.setCreateDate(rs.getTimestamp("createDate"));
		t.setLastModified(rs.getTimestamp("lastModified"));
	}
}
