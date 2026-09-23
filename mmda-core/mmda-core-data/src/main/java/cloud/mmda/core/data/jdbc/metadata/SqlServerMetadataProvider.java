/**
 * 
 */
package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.sql.SqlDateTime;
import cloud.mmda.core.data.sql.SqlServerDateTime;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql Server 元数据提供者
 * @author roshion
 *
 */
@Component
public class SqlServerMetadataProvider extends SqlMetadataProvider {
	private static final String DB_OWNER = "dbo";
	private static final SqlServerDateTime sqlDateTime = new SqlServerDateTime();
	@Autowired
	public SqlServerMetadataProvider(DataSource ds, String defaultDbName, String metadataDbName, RedisConnectionFactory factory){
		super(ds,defaultDbName,metadataDbName,factory);
	}


	@Override
	public final String quoteName(String name){
		return "["+name+"]";
	}

	@Override
	public final String getFrom(String objName){
		if(objName.indexOf(".")!=-1){
			String[] obj = objName.split("\\.");
			return obj[0] + "." + DB_OWNER + "." + obj[1];
		}
		return super.getFrom(objName);
	}
	@Override
	public final String getFullObjName(MetaObject metaObj){
		return quoteName(metaObj.getDbSchema())+"."+ DB_OWNER + "." +quoteName(metaObj.getObjName());
	}
	@Override
	public final String isNullFuncName(){
		return "ISNULL";
	}
    /**
     * 参数占位符
	 * @return
     */
	@Override
	public final char getParamPlaceholder(){
		return '?';
	}
	@Override
	public final SqlDateTime getSqlDateTime(){ return sqlDateTime;}

	@Override
	public final boolean supportNamedPartition(){
		return false;
	}
	@Override
	public final boolean supportOrderByField(){
		return false;
	}
	@Override
	protected String selectAll(String from){
		return SELECT_ALL_FROM
				+ metadataDbName + ".dbo." + from;
	}

	@Override
	protected String countAll(String from){
		return COUNT_ALL_FROM
				+ metadataDbName + ".dbo." + from;
	}

	@Override
	public String call(String func, int argsNum){
		return "EXEC " + func + " " + BaseUtil.repeat("?", argsNum,",") ;
	}

	@Override
	public String toPagerSql(Paginator paginator){
		return String.format(" ORDER BY %1$s OFFSET %2$d*%3$d ROWS FETCH NEXT %3$d ROWS ONLY",
				paginator.getOrderBy(),
				paginator.getPageNo()-1,
				paginator.getPageSize()
		);
	}
	@Override
	public String escapeSpecialCharacters(String value) {
		Pattern pattern = Pattern.compile("[%_]");
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			return matcher.replaceAll("[$0]");
		}
		return value;
	}
}
