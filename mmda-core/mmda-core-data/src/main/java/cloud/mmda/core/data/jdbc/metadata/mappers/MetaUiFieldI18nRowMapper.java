package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.metadata.MetaUiFieldI18n;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * 元域国际化RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-21 00:28:30.0
 * 
 */
public class MetaUiFieldI18nRowMapper implements RowMapper<MetaUiFieldI18n> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaUiFieldI18n
	 * @throws SQLException
	 */
	@Override
	public MetaUiFieldI18n mapRow(ResultSet rs, int rowNum) throws SQLException{
		MetaUiFieldI18n t = new MetaUiFieldI18n();
		t.setRowNum(rowNum);
		t.setFieldName(rs.getString("fieldName"));
		t.setLocale(rs.getString("locale"));
		t.setDisplayLabel(rs.getString("displayLabel"));
		t.setMergeLabel(rs.getString("mergeLabel"));
		t.setMergePrefix(rs.getString("mergePrefix"));
		t.setPlaceholder(rs.getString("placeholder"));
		int listSize = rs.getInt("listSize");
		if(rs.wasNull())
			t.setListSize(null);
		else
			t.setListSize(listSize);
		t.setSuffix(rs.getString("suffix"));
		t.setSelectOptions(rs.getString("selectOptions"));
		t.setNullDisplayText(rs.getString("nullDisplayText"));
		t.setTooltip(rs.getString("tooltip"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
