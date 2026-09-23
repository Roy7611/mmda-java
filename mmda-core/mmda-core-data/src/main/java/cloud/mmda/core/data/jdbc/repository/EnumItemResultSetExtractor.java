package cloud.mmda.core.data.jdbc.repository;

import cloud.mmda.core.metadata.MetaEnumMember;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnumItemResultSetExtractor implements ResultSetExtractor<List<MetaEnumMember>> {
    @Override
    public List<MetaEnumMember> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<MetaEnumMember> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new MetaEnumMember(rs.getString(1),rs.getString(2)));
        }
        return results;
    }
}
