package cloud.mmda.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体过滤器
 *
 * 用于列表页快速过滤
 */
public class EntityFilter implements Serializable {
    public EntityFilter(){
        filterConditions = new ArrayList<>();
    }
    public EntityFilter(String name, String title){
        this();
        filterName = name;
        filterTitle = title;
        fixed = false;
    }
    public EntityFilter(String name, String title, boolean fixed){
        this(name,title);
        this.fixed = fixed;
    }
    /**
     * 过滤器名称
     */
    @Getter @Setter
    private String filterName;

    /**
     * 过滤器标题
     */
    @Getter @Setter
    private String filterTitle;

    /**
     * 是否默认
     */
    @Getter @Setter
    private boolean fixed;
    /**
     * 当前过滤条件表达式
     *
     * 例如：{ {"mode=1","模式1"}, {"mode=2","模式2"}, {"mode=3","模式3"} }
     */
    @Getter @Setter
    private List<EntityFilterCondition> filterConditions;


    /**
     * 添加过滤条件
     * @param condition 条件Sql表达式
     * @param text 中文名称
     */
    public void addFilterCondition(String condition, String text){
        filterConditions.add(new EntityFilterCondition(condition,text));
    }
    public void addFilterCondition(String condition, String text,boolean fallback){
        filterConditions.add(new EntityFilterCondition(condition,text,fallback));
    }
    /**
     * 获取当前过滤条件
     * @return
     */
    @JsonIgnore
    public String getActiveCondition(){
        if(filterConditions ==null || filterConditions.isEmpty())
            return null;

        return  filterConditions.stream()
                .filter(cond->cond.isActive())
                .map(cond->"("+cond.getCondition()+")")
                .collect(Collectors.joining(" OR "));
    }
    /**
     * 当前过滤条件显示标签
     *
     * 例如：模式1 模式3
     * @return 过滤条件显示标签
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<String> getActiveLabels(){
        if(filterConditions ==null || filterConditions.isEmpty())
            return null;

        return  filterConditions.stream()
                .filter(cond->cond.isActive())
                .map(cond->cond.getDisplayLabel())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String json = JsonUtil.toJson(this);
        return BaseUtil.toBase64String(json);
    }

    /**
     * 从Base64字符串解析
     * @param base64
     * @return
     */
    public static EntityFilter parse(String base64){
        String json = BaseUtil.fromBase64String(base64);
        return JsonUtil.fromJson(json, EntityFilter.class);
    }
}
