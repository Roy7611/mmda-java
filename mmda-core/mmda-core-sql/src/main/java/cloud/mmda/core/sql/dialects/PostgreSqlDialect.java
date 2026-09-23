package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.SqlCmd;
import cloud.mmda.core.sql.SqlMetaContext;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.PostgreSqlDataTypes;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;
import static cloud.mmda.core.sql.SqlConsts.PARENTHESES_END;

/**
 * PostgreSql 方言
 *
 * @see
 */
public class PostgreSqlDialect extends AnsiSqlDialect {
    public PostgreSqlDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "PostgreSql";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return PostgreSqlDataTypes.getType(genericDataType);
    }

    /**
     * 不支持在查询中手动指定分区，但依赖强大的“分区剪枝”机制
     * @return
     */
    @Override
    public boolean supportNamedPartition() {
        return false;
    }

    //region UPDATE

    /**
     * {@inheritDoc} UPDATE FROM
     * <pre>
     *     {@code
     *     [ WITH [ RECURSIVE ] with_query [, ...] ]
     *     UPDATE [ ONLY ] table_name [ * ] [ [ AS ] alias ]
     *     SET { column_name = { expression | DEFAULT } |
     *           ( column_name [, ...] ) = [ ROW ] ( { expression | DEFAULT } [, ...] ) |
     *           ( column_name [, ...] ) = ( sub-SELECT )
     *         } [, ...]
     *     [ FROM from_item [, ...] ]
     *     [ WHERE condition | WHERE CURRENT OF cursor_name ]
     *     [ RETURNING [ WITH ( { OLD | NEW } AS output_alias [, ...] ) ]
     *                 { * | output_expression [ [ AS ] output_name ] } [, ...] ]
     *     }
     * </pre>
     *
     * PostgreSql 支持 RETURNING clause
     *
     * @see <a href="https://www.postgresql.org/docs/current/sql-update.html">PostgreSql 官方文档</a>
     */
    @Override
    public SqlCmd update(final SqlMetaContext context, final LinkedHashMap<MetaCol, Object> assignments, SqlCriteriaExp condition, boolean namedParameter) {
        return super.update(context, assignments, condition, namedParameter);
    }

    //endregion of UPDATE

    //region CREATE TABLE

    /**
     * {@inheritDoc}
     * <pre>
     *
     * </pre>
     * @see <a href="https://www.postgresql.org/docs/current/sql-createtable.html">官方文档</a>
     */
    @Override
    public StringBuilder createTable(MetaObject metaObj, boolean ifNotExists, boolean dropIfExists, boolean withComment) {
        var sb = super.createTable(metaObj, ifNotExists, dropIfExists, withComment)
                .append(TABLESPACE).append("pg_default") //表空间处理
                .append(SEMICOLON).append(LINE_SEPARATOR);

        //授权 ALTER TABLE IF EXISTS base."Partner"
        //    OWNER to postgres;
        sb.append(ALTER_TABLE).append(IF_EXISTS).append(quoteObject(metaObj))
                .append(" OWNER TO postgres")
                .append(SEMICOLON).append(LINE_SEPARATOR);
        //单独加注释
        if(withComment){
            commentObject(sb, metaObj);
        }
        //创建索引
        metaObj.getIndexes().stream()
                .forEach(idx -> createIndex(sb.append(LINE_SEPARATOR), idx, false, false));
        return sb;
    }

    /**
     * {@inheritDoc}
     * <pre>
     *     {@code
     * [ CONSTRAINT constraint_name ]
     * { CHECK ( expression ) [ NO INHERIT ] |
     *   NOT NULL column_name [ NO INHERIT ] |
     *   UNIQUE [ NULLS [ NOT ] DISTINCT ] ( column_name [, ... ] [, column_name WITHOUT OVERLAPS ] ) index_parameters |
     *   PRIMARY KEY ( column_name [, ... ] [, column_name WITHOUT OVERLAPS ] ) index_parameters |
     *   EXCLUDE [ USING index_method ] ( exclude_element WITH operator [, ... ] ) index_parameters [ WHERE ( predicate ) ] |
     *   FOREIGN KEY ( column_name [, ... ] [, PERIOD column_name ] ) REFERENCES reftable [ ( refcolumn [, ... ] [, PERIOD refcolumn ] ) ]
     *     [ MATCH FULL | MATCH PARTIAL | MATCH SIMPLE ] [ ON DELETE referential_action ] [ ON UPDATE referential_action ] }
     * [ DEFERRABLE | NOT DEFERRABLE ] [ INITIALLY DEFERRED | INITIALLY IMMEDIATE ] [ ENFORCED | NOT ENFORCED ]
     *     }
     * </pre>
     */
    @Override
    protected void addTableConstraints(StringBuilder sb, MetaObject metaObj) {
        //pk
        definePrimaryKey(sb.append(TAB), metaObj);
        //indexes 单独创建

        //fk 单独创建

        //ck

        //unique
    }

    /**
     * @inheritDoc
     * @param sb
     * @param metaObj
     * @return CONSTRAINT "PK_Partner" PRIMARY KEY ("partnerID")
     */
    @Override
    public StringBuilder definePrimaryKey(StringBuilder sb, MetaObject metaObj) {
        var pkName = quote("PK_" + metaObj.getObjName());
        return super.definePrimaryKey(sb.append(CONSTRAINT).append(pkName).append(SPACE), metaObj);
    }

    //endregion of CREATE TABLE

    //region CREATE INDEX


    /**
     * {@inheritDoc}
     * <block>
     *     <pre>
     *      CREATE INDEX IF NOT EXISTS "IDX_Partner_code"
     *       ON base."Partner" USING btree
     *       ("partnerCode" COLLATE pg_catalog."default" ASC NULLS FIRST)
     *       WITH (fillfactor=100, deduplicate_items=False)
     *          TABLESPACE pg_default;
     *     </pre>
     * </block>
     */
    @Override
    public StringBuilder createIndex(StringBuilder sb, MetaIndex index, boolean ifNotExists, boolean dropIfExists) {
        if(dropIfExists) dropIndex(sb, index, true);
        var colList = index.getCols().stream()
                .map(c -> quote(c.colName()) + SPACE + c.sortOrder() + (c.nullsLast() ? NULLS_LAST : NULLS_FIRST))
                .collect(Collectors.joining(COMMA));
        return sb.append(CREATE_INDEX).append(quote(index.getIndexName()))
                .append(ifNotExists? SPACE + IF_NOT_EXISTS : SPACE)
                .append(ON).append(quoteObject(index.getDbSchema(),index.getObjName()))
                .append(PARENTHESES_START).append(LINE_SEPARATOR)
                .append(TAB).append(colList)
                .append(PARENTHESES_END).append(SEMICOLON)
//                .append(" WITH (fillfactor=100, deduplicate_items=False)")
                ;
    }
    //endregion of CREATE INDEX
}
