package cloud.mmda.core.sql;

import cloud.mmda.core.metadata.MetaContext;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 已经将元关系转化为SQL连接的上下文
 */
public interface SqlMetaContext extends MetaContext {
    List<SqlJoin> getJoins();
}
