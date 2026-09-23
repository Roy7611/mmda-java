package cloud.mmda.core.data.conversion;

import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.metadata.MetaEnum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 枚举数据类型处理器
 * @param <E> 枚举类型
 */
@Deprecated(since = "在 MetaCol.getResult 中实现了已经")
public class EnumTypeHandler<E extends Enum<E> & EnumValue<?>> extends DataTypeHandler<E> {
    private final MetaEnum _metaEnum;
    private final Class<E> _enumClass;
    public EnumTypeHandler(final MetaEnum metaEnum, final Class<E> enumClass) {
        _metaEnum = metaEnum;
        _enumClass = enumClass;
    }

    @Override
    public Class<E> targetClass() {
        return _enumClass;
    }

    /**
     * 将字符串 source 转化为枚举
     * @param source 字符串可以是枚举值或者枚举成员名称，例如 "1" 或者 "BLOCKED"
     * @return 枚举，例如 BLOCKED
     */
    @Override
    public E convert(String source) {
        if(DataTypeHandler.isNullLikeOrEmpty(source)) return null;
        if(Character.isDigit(source.charAt(0))){
            //source may be an integer value
            int value = Integer.parseInt(source);
            var name = _metaEnum.nameOf(value);
            return Enum.valueOf(_enumClass, name);
        }
        else{
            return Enum.valueOf(_enumClass, source);
        }
    }

    @Override
    public E getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
        var value = rs.getInt(colName);
        if(nullable && rs.wasNull()) return null;

        var name = _metaEnum.nameOf(value);
        return Enum.valueOf(_enumClass, name);
    }

    @Override
    public E getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
        var value = rs.getInt(colIdx);
        if(nullable && rs.wasNull()) return null;

        var name = _metaEnum.nameOf(value);
        return Enum.valueOf(_enumClass, name);
    }

    @Override
    public void setParameter(PreparedStatement ps, int paramIndex, E value) throws SQLException {
        var underlyingValue = value.getValue();
        if(underlyingValue == null) ps.setNull(paramIndex, Types.INTEGER);
        else ps.setObject(paramIndex,underlyingValue,Types.INTEGER);
    }
}
