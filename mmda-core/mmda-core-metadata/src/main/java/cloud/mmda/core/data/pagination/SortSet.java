package cloud.mmda.core.data.pagination;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated(since = "Sort是可以多字段排序的")
public class SortSet {
    private final String sortLabel;
    private final Set<Sort> sortSet;

    private SortSet(final String label, Set<Sort> sorts){
        this.sortLabel=label;
        this.sortSet=sorts;
    }

    public final String getSortLabel(){
        return sortLabel;
    }
    public final Set<Sort> getSortSet(){
        return Collections.unmodifiableSet(sortSet);
    }
    public final boolean isActive(){
        return false;
    }
    public final String getSortBy(){
        return sortSet.stream()
                .map(s->s.toString())
                .collect(Collectors.joining(","));
    }
    /**
     * 构建排序设置
     * @param label 排序名称，如：升序，降序
     * @param sorts 排序设置，如：t.lastModified DESC,t.itemType
     */
    public static SortSet of(String label, String sorts){
        LinkedHashSet<Sort> sortSet = new LinkedHashSet<>();
        String[] sortArr = sorts.split(",");
        for(String s : sortArr){
            sortSet.addLast(Sort.parse(s));
        }
        return new SortSet(label,sortSet);
    }

    public static SortSet of(final String label, Sort sort){
        LinkedHashSet<Sort> sortSet = new LinkedHashSet<>();
        sortSet.addLast(sort);
        return new SortSet(label,sortSet);
    }
}
