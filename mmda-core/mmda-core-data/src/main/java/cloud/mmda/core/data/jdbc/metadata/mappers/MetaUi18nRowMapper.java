package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.metadata.MetaUi18n;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * 国际化翻译RowMapper
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2024-07-21 00:28:30.0
 * 
 */
public class MetaUi18nRowMapper implements RowMapper<MetaUi18n> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 映射函数
	 * @param rs 结果集
	 * @param rowNum 行号
	 * @return MetaUi18n
	 * @throws SQLException
	 */
	@Override
	public MetaUi18n mapRow(ResultSet rs, int rowNum) throws SQLException{
		MetaUi18n t = new MetaUi18n();
		t.setRowNum(rowNum);
		t.setWords(rs.getString("words"));
		t.setLocale(rs.getString("locale"));
		t.setTranslation(rs.getString("translation"));
		return t;
	}
	//endregion of ~GENERATED PARTS END

}
