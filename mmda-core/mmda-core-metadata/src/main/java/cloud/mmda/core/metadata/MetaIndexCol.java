package cloud.mmda.core.metadata;

import cloud.mmda.core.data.pagination.Sort;

public record MetaIndexCol(String colName, Sort.Order sortOrder, boolean nullsLast) {

}
