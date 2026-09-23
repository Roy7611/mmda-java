package cloud.mmda.core.entities;

import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;

public record EntityRelation<T,R>(Class<T> target, Class<R> relatedClass, String relationName, MetaRelationType relationType, SqlCriteriaExp relationship) {
}
