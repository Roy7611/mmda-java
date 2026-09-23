package cloud.mmda.core.sql.expressions;

@FunctionalInterface
public interface SqlExpVisitor {
    void visit(SqlExp exp);
}
