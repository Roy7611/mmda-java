package cloud.mmda.core.data.sql;

import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.data.pagination.*;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.util.List;

/**
 * Sql语句自定义虚拟实体
 * 多表连接查询，可自定义查询语句
 * @param <T>
 * @param <K>
 */
public abstract class SqlEntityRepository<T extends Entity<K>, K> extends EntityRepository<T, K> {
//	@Autowired
	public SqlEntityRepository(DataSource dataSource){
		super(dataSource);
	}

	//子类必须实现以下函数
	protected abstract String getSqlSelectByKey();
	protected abstract String getSqlSelectAll();
	protected abstract String getSqlSelectScalar(String colName);
	protected abstract String getSqlCountAll();
	protected abstract String getAliasDotName(MetaCol col);

	@Override
	public T find(K k) throws DataAccessException {
		String sql = getSqlSelectByKey();
		return jdbcTemplate.queryForObject(sql, toKeyArgArray(k), rowMapper);
	}


	/**
	 * 构建条件语句，findAllBy函数使用
	 * @param condition 条件表达式
	 * @return
	 */
	@Override
	public String buildCondition(SqlExpression condition, char placeholder){
		if(placeholder == '?') placeholder = metadataProvider.getParamPlaceholder();
		return condition.buildExpression(c -> getAliasDotName(c), placeholder);
	}

	public String buildCondition(SqlExpression condition){
		return buildCondition(condition, '?');
	}

	@Override
	public T findFirst(SqlExpression condition) throws DataAccessException {
		String sql = getSqlSelectAll();
		String where = buildCondition(condition);
		sql = SqlQuery.buildSqlWith(sql,where,1,defaultSort, metadataProvider);
		return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), rowMapper);
	}

	@Override
	public T findFirst(String condition, Object... args) throws DataAccessException {
		String sql = getSqlSelectAll();
		sql = SqlQuery.buildSqlWith(sql,condition,1, defaultSort,metadataProvider);
		return jdbcTemplate.queryForObject(sql, args, rowMapper);
	}


	@Override
	public <U> U findScalar(String colName, SqlExpression condition, Class<U> requiredType) throws DataAccessException {
		String sql = getSqlSelectScalar(colName);
		String where = buildCondition(condition);
		sql = SqlQuery.buildSqlWith(sql,where,1, defaultSort,metadataProvider);
		return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), requiredType);
	}

	@Override
	public List<T> findAll() throws DataAccessException {
		String sql = getSqlSelectAll();
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<T> findAll(Sort sort) throws DataAccessException {
		String sql = getSqlSelectAll();
		sql = SqlQuery.buildSqlWith(sql,sort);
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<T> findAllIn(String field, String list, boolean ordered) throws DataAccessException {
		String condition = field + " IN(" + list +")";
		if(ordered) condition += " ORDER BY FIELD("+field+"," + list +")";
		String sql = getSqlSelectAll();
		sql = SqlQuery.buildSqlWith(sql,condition);
		return  jdbcTemplate.query(sql,rowMapper);
	}

	@Override
	public PagedList<T> findAll(Paginator paginator) throws DataAccessException {
		int rc = count();
		paginator.setRecordCount(rc);
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(), paginator, metadataProvider);
		List<T> data = jdbcTemplate.query(sql, rowMapper);
		return new PagedList<T>(data, paginator);
	}

	@Override
	public List<T> findAllBy(SqlExpression condition) throws DataAccessException {
		String where = buildCondition(condition);
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(),where);
		return jdbcTemplate.query(sql, condition.getArgValues().toArray(), rowMapper);
	}

	@Override
	public List<T> findAllBy(SqlExpression condition, Sort sort) throws DataAccessException {
		if(sort == null){
			throw new IllegalArgumentException("sort cannot be null");
		}
		String where = buildCondition(condition);
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(),where, sort);
		return jdbcTemplate.query(sql, condition.getArgValues().toArray(), rowMapper);
	}

	@Override
	public List<T> findAllBy(String condition, Object... args) throws DataAccessException {
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(),condition);
		return jdbcTemplate.query(sql, args, rowMapper);
	}

	@Override
	public List<T> findAllBy(String condition, Sort sort, Object... args) throws DataAccessException {
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(),condition, sort);
		return jdbcTemplate.query(sql, args, rowMapper);
	}

	@Override
	public PagedList<T> findAllBy(Paginator paginator, SqlExpression condition) throws DataAccessException {
		String where = buildCondition(condition);
		String countSql = SqlQuery.buildSqlToCount(getSqlCountAll(),where);
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(), where, paginator, metadataProvider);
		Object[] argValueArray = condition.getArgValues().toArray();
		int rc = jdbcTemplate.queryForObject(countSql, argValueArray, Integer.class);
		paginator.setRecordCount(rc);

		List<T> data = jdbcTemplate.query(sql, argValueArray, rowMapper);
		return new PagedList<T>(data, paginator);
	}

	@Override
	public PagedList<T> findAllBy(Paginator paginator, String condition, Object... args) throws DataAccessException {
		String countSql = SqlQuery.buildSqlToCount(getSqlCountAll(),condition);
		String sql = SqlQuery.buildSqlWith(getSqlSelectAll(), condition, paginator, metadataProvider);
		int rc = jdbcTemplate.queryForObject(countSql, args, Integer.class);
		paginator.setRecordCount(rc);

		List<T> data = jdbcTemplate.query(sql, args, rowMapper);
		return new PagedList<T>(data, paginator);
	}

	@Override
	public int count() throws DataAccessException {
		String sql = getSqlCountAll();
		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public int count(SqlExpression condition) throws DataAccessException {
		String sql = getSqlCountAll();
		String where = buildCondition(condition);
		sql = SqlQuery.buildSqlWith(sql, where);
		return this.jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), Integer.class);
	}

	@Override
	public int count(String condition, Object... args) throws DataAccessException {
		String sql = getSqlCountAll();
		sql = SqlQuery.buildSqlWith(sql, condition);
		return this.jdbcTemplate.queryForObject(sql,args, Integer.class);
	}

}
