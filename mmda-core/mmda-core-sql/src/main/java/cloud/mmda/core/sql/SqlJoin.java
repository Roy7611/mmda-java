package cloud.mmda.core.sql;

import cloud.mmda.core.enums.DisplayShape;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.expressions.SqlExp;
import cloud.mmda.core.sql.expressions.SqlExpVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * SQL 连接定义两个元对象之间的关联关系，你可以临时创建也可从元关系{@link MetaRelation}解析
 */
@Getter
public final class SqlJoin {
    private final MetaObject master;
    private final MetaObject relative;
    private final SqlJoinType joinType;
    private final SqlCriteriaExp joinOnExp;
    private MetaRelation relation;

    public String getName(){
        return relation.getRelationName();
    }

    private SqlJoin(final MetaObject master, final String name, final MetaObject relative, SqlJoinType joinType, boolean hasMany, final SqlCriteriaExp joinOnExp) {
        this.master = master;
        this.relative = relative;
        this.joinType = joinType;
        this.joinOnExp = joinOnExp;
        this.relation = buildRelation(name, joinOnExp, hasMany);
    }
    private SqlJoin(final MetaObject master, final MetaRelation relation, final MetaObject relative) {
        this.master = master;
        this.relative = relative;
        this.joinType = SqlJoinType.parse(relation.getJoinType());
        this.joinOnExp = buildJoinExp(relation);
        this.relation = relation;
    }

    private SqlCriteriaExp buildJoinExp(final MetaRelation relation) {
        Map<Object,Object> map = new LinkedHashMap<>();
        if(relation.getJoinOn().indexOf(OR) != -1){
            var orEquals = relation.getJoinOn().split(OR);
            for(var eq : orEquals){
                var operands = eq.split("=");
                var leftCol = relative.getCol(operands[0].trim());
                var rightCol = master.getCol(operands[1].replace("@","").trim());
                map.put(leftCol, rightCol);
            }
            return SqlExp.allEqualsOr(map);
        }
        else{
            var andEquals = relation.getJoinOn().split(AND);
            for(var eq : andEquals){
                var operands = eq.split("=");
                var leftCol = relative.getCol(operands[0].trim());
                var rightCol = master.getCol(operands[1].replace("@","").trim());
                map.put(leftCol, rightCol);
            }
            return SqlExp.allEqualsAnd(map);
        }
    }
    private MetaRelation buildRelation(final String name, final SqlCriteriaExp joinOnExp, boolean hasMany){
        var relation = new MetaRelation();
        relation.setRelationName(name);
        relation.setRelationType(hasMany? MetaRelationType.HAS_MANY : MetaRelationType.HAS_ONE);
        relation.setDbSchema(master.getDbSchema());
        relation.setObjName(master.getObjName());
        relation.setRelativeObjName(relative.getObjName());
        relation.setRelativeDbSchema(relative.getDbSchema());
        relation.setDisplayShape(DisplayShape.LIST);
        relation.setJoinType(joinType.getText());

        //连接表达式：orderID=@orderID AND itemID=@itemID
        var visitor = new SqlJoinExpVisitor(this, (col)-> "@"+col.getColName(), col -> col.getColName());
        visitor.visit(joinOnExp);
        relation.setJoinOn(visitor.toString());

        return relation;
    }

    public String compile(final SqlDialect dialect){
        var visitor = new SqlJoinExpVisitor(
                this,
                col -> "t." + dialect.quote(col.getColName()),
                col -> relation.getRelationName() + "." + dialect.quote(col.getColName())
        );
        visitor.visit(joinOnExp);
        return visitor.toString();
    }
    /**
     * SQL连接表达式参观者，用于将连接条件表达式编译为SQL字符串表达式
     */
    public static class SqlJoinExpVisitor implements SqlExpVisitor {
        private final StringBuilder sb = new StringBuilder();
        private final SqlJoin join;
        private final Function<MetaCol,String> masterColStringer;
        private final Function<MetaCol,String> relativeColStringer;
        @Getter
        private final List<SqlArg> argList = new ArrayList<>();

        public SqlJoinExpVisitor(SqlJoin join, Function<MetaCol,String> masterColStringer, Function<MetaCol,String> relativeColStringer) {
            this.join = join;
            this.masterColStringer = masterColStringer;
            this.relativeColStringer = relativeColStringer;
        }

        @Override
        public final void visit(SqlExp exp) {
            visitOperand(exp.getLhs());
            sb.append(exp.getOp().getText());
            visitOperand(exp.getRhs());
        }

        private void visitOperand(final Object operand) {
            if (operand instanceof SqlExp exp) visit(exp);
            else if (operand instanceof MetaCol col) {
                if(col.getObjName().equals(join.getMaster().getObjName())){
                    sb.append(masterColStringer.apply(col));
                    argList.add(SqlArg.of(col));
                }
                else{
                    sb.append(relativeColStringer.apply(col));
                }
            } else {
                sb.append(operand);
            }
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }

    public static SqlJoin hasOne(final MetaObject master, final String relationName, final MetaObject relative, final SqlCriteriaExp joinOnExp) {
        return new SqlJoin(master, relationName, relative, SqlJoinType.INNER_JOIN, false, joinOnExp);
    }
    public static SqlJoin hasOneOptional(final MetaObject master, final String relationName, final MetaObject relative, final SqlCriteriaExp joinOnExp) {
        return new SqlJoin(master, relationName, relative, SqlJoinType.LEFT_JOIN, false, joinOnExp);
    }
    public static SqlJoin hasMany(final MetaObject master, final String relationName, final MetaObject relative, final SqlCriteriaExp joinOnExp) {
        return new SqlJoin(master, relationName, relative, SqlJoinType.INNER_JOIN, true, joinOnExp);
    }
    public static SqlJoin hasManyOptional(final MetaObject master, final String relationName, final MetaObject relative, final SqlCriteriaExp joinOnExp) {
        return new SqlJoin(master, relationName, relative, SqlJoinType.LEFT_JOIN, true, joinOnExp);
    }
    public static SqlJoin of(final MetaObject master, final MetaRelation relation, final MetaObject relative){
        return new SqlJoin(master, relation, relative);
    }


}
