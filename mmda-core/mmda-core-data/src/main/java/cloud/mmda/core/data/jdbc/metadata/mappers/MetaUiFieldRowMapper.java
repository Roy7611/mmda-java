package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.FieldAggregation;
import cloud.mmda.core.metadata.MetaUiField;
import cloud.mmda.core.enums.FieldAlignment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MetaUiFieldRowMapper implements RowMapper<MetaUiField> {
    @Override
    public MetaUiField mapRow(ResultSet rs, int rowNum) throws SQLException {
        MetaUiField t = new MetaUiField();
        t.setFieldIdx(rs.getInt("fieldIdx"));
        t.setFieldName(rs.getString("fieldName"));
        t.setFieldNameAlias(rs.getString("fieldNameAlias"));
        t.setGroupLabel(rs.getString("groupLabel"));
        t.setDisplayLabel(rs.getString("displayLabel"));
        t.setMergeLabel(rs.getString("mergeLabel"));
        t.setMergePrefix(rs.getString("mergePrefix"));
        t.setEmphasized(rs.getBoolean("emphasized"));
        t.setListed(rs.getBoolean("listed"));
        t.setAggregationSet(EnumBitSet.valueOf(FieldAggregation.class,rs.getInt("aggregationSet")));
        int listSize = rs.getInt("listSize");
        if(rs.wasNull())
            t.setListSize(null);
        else
            t.setListSize(listSize);
        t.setAlign(FieldAlignment.valueOf(rs.getByte("align")));
        t.setSortable(rs.getBoolean("sortable"));
        t.setRenderer(rs.getString("renderer"));
        t.setFormatter(rs.getString("formatter"));
        t.setSuffix(rs.getString("suffix"));
        t.setReadOnly(rs.getBoolean("readOnly"));
        t.setEditor(rs.getString("editor"));
        t.setSelectOptions(rs.getString("selectOptions"));
        t.setValidationRules(rs.getString("validationRules"));
        t.setPlaceholder(rs.getString("placeholder"));
        t.setNullDisplayText(rs.getString("nullDisplayText"));
        t.setHidden(rs.getBoolean("hidden"));
        t.setTooltip(rs.getString("tooltip"));
        t.setDataBinding(rs.getString("dataBinding"));

        t.setPrimaryKey(rs.getBoolean("primaryKey"));
        t.setDataType(rs.getInt("dataType"));
        t.setUnsigned(rs.getBoolean("isUnsigned"));
        int maxLength = rs.getInt("maxLength");
        if (rs.wasNull())
            t.setMaxLength(null);
        else
            t.setMaxLength(maxLength);
        byte numericPrecision = rs.getByte("numericPrecision");
        if (rs.wasNull())
            t.setNumericPrecision(null);
        else
            t.setNumericPrecision(numericPrecision);
        byte numericScale = rs.getByte("numericScale");
        if (rs.wasNull())
            t.setNumericScale(null);
        else
            t.setNumericScale(numericScale);
        t.setNullable(rs.getBoolean("nullable"));
        t.setFormula(rs.getString("formula"));
        t.setDefaultVal(rs.getString("defaultVal"));
        return t;
    }
}
