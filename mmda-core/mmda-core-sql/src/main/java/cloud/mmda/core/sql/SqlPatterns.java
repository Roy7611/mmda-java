package cloud.mmda.core.sql;

import cloud.mmda.core.sql.expressions.SqlOp;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * SQL正则表达式用于解析SQL语句片段
 * <p>
 *     未来使用<a href="https://github.com/antlr/antlr4">ANTLR4</a>开发{@code SqlParser}
 * </p>
 * @see <a href="https://regexkit.com/java-regex-tester">测试网址</a>
 */
public final class SqlPatterns {

    private static final String META_JOIN_EXP = "(?<lhs>\\w+)\\s*=\\s*(?<rhs>@\\w+)";

    private static final String JOIN_EXP = "(?<lhs>(\\w+\\.)?\\w+)=(?<rhs>(\\w+\\.)?\\w+)(\\s+(?<andOr>AND|OR)\\s+(?<lhsn>(\\w+\\.)?\\w+)=(?<rhsn>(\\w+\\.)?\\w+))*";
    private static final String JOIN_ON = "(?<joinType>(INNER|LEFT|RIGHT|FULL|CROSS)(\\s+\\w+)?\\s+JOIN)\\s+(?<rel>\\w+(\\.\\w+)?)(\\s+AS)?(\\s+(?<relAlias>\\w+))?\\s+ON\\s+(?<joinExp>"+JOIN_EXP+")";
    private static final String FROM = "FROM\\s+(?<source>\\w+(\\.\\w+)?)(\\s+AS)?(\\s+(?<alias>\\w+))?";
    private static final String FROM_JOIN_ON = FROM +JOIN_ON+"*";
    private static final String WHERE_CONDITION = "WHERE\\s+(?<condition>\\.+)";
    private static final String ORDER_BY = "ORDER\\s+BY\\s+(?<sort>(\\w+\\.)?\\w+(\\s+(?<order>ASC|DESC))?(\\s+,\\s+(?<thenSort>\\w+\\.)?\\w+(\\s+(?<thenOrder>ASC|DESC))?)?)";

    //表达式
    private static final String LEFT_OPERAND = "(?<lhs>(\\w+\\.)?\\w+)";
    private static final String RIGHT_OPERAND = "(?<rhs>.+)";
    private static final String COMPARISON_OPERATORS = "(?<cmp>(!=|<>|>=|<=|=|>|<|BETWEEN|LIKE|NOT IN|IN|NOT EXISTS|EXISTS))";
    private static final String COMPARISON_EXP = LEFT_OPERAND + "\\s*"+COMPARISON_OPERATORS+"\\s*" + RIGHT_OPERAND;

    private static final String CALCULATORS = "(?<op>\\+|\\-|\\*|\\/|%|<<|>>|>>>|&|\\||\\^|\\~)";

    /**
     * 识别{@link cloud.mmda.core.metadata.MetaRelation}中的连接等式{@code orderID=@orderID}
     * 左边字段和右边字段然后加上表别名和引号，便于构建可执行的SQL连接表达式{@code items.`orderID`=t.`orderID`}
     */
    public static final Pattern META_JOIN_EXP_PATTERN = Pattern.compile(META_JOIN_EXP);
    /**
     * 识别{@code FROM table AS t}，得出{@code {source = table, alias = t}}
     */
    public static final Pattern FROM_PATTERN = Pattern.compile(FROM, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    /**
     * 识别SQL连接语句{@code INNER JOIN table AS b ON id=@id ...}
     */
    public static final Pattern JOIN_ON_PATTERN = Pattern.compile(JOIN_ON, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    public static final Pattern ORDER_BY_PATTERN = Pattern.compile(ORDER_BY, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern COMPARISON_EXP_PATTERN = Pattern.compile(COMPARISON_EXP);
    private SqlPatterns() {}
}
