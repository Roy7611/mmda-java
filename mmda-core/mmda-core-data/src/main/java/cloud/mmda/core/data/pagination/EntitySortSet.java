package cloud.mmda.core.data.pagination;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntitySortSet implements Serializable {
    /**
     * 排序名称，比如 sortByLastModified
     */
    @Getter
    @Setter
    private String sortName;

    /**
     * 排序标题，最后修改
     */
    @Getter @Setter
    private String sortTitle;

    /**
     * 排序选项，比如升序，降序
     */
    @Getter @Setter
    private List<SortSet> sortSets;

    public EntitySortSet(){
        this.sortSets = new ArrayList<>();
    }
    public EntitySortSet(String name, String title){
        this();
        sortName = name;
        sortTitle = title;
    }
    public EntitySortSet(String name, String title, SortSet...sortSets){
        sortName = name;
        sortTitle = title;
        this.sortSets = Arrays.asList(sortSets);
    }
}
