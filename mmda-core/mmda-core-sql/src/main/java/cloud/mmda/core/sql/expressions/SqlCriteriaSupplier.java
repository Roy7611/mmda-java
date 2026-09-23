package cloud.mmda.core.sql.expressions;

import java.util.Objects;
import java.util.function.Supplier;

public record SqlCriteriaSupplier(String name, Supplier<SqlCriteriaExp> delegate) {
    public SqlCriteriaSupplier{
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(delegate, "delegate supplier cannot be null");
    }

    /**
     * 提供空条件，表示不限制查询条件
     */
    public static SqlCriteriaSupplier EMPTY = new SqlCriteriaSupplier("", ()->null);
}
