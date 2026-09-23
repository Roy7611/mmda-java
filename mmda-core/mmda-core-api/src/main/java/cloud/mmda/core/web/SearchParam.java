package cloud.mmda.core.web;

import cloud.mmda.core.data.pagination.SortSet;
import lombok.Data;

@Data
public class SearchParam {
    private int pageSize;
    private int pageNo;
    private SortSet sorts;

}
