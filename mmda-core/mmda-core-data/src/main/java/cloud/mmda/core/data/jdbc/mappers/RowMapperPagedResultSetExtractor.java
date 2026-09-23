package cloud.mmda.core.data.jdbc.mappers;

import cloud.mmda.core.data.pagination.Paginator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperPagedResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
    private final RowMapper<T> rowMapper;

    private final Paginator paginator;

    public RowMapperPagedResultSetExtractor(RowMapper<T> rowMapper, Paginator paginator){
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper=rowMapper;
        this.paginator = paginator;
    }
    public RowMapperPagedResultSetExtractor(RowMapper<T> rowMapper){
        this(rowMapper,null);
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        int rowNum = (paginator ==null ? 1 : paginator.getFrom());
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }
        return results;
    }
}
