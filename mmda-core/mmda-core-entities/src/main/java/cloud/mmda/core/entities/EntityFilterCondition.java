package cloud.mmda.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 实体过滤器条件
 */
@Data
@AllArgsConstructor
public class EntityFilterCondition {
    public EntityFilterCondition(String condition,String displayLabel){
        this.condition=condition;
        this.displayLabel = displayLabel;
        this.fallback=false;
        this.active=false;
    }
    public EntityFilterCondition(String condition,String displayLabel, boolean fallback){
        this.condition=condition;
        this.displayLabel = displayLabel;
        this.fallback=fallback;
        this.active=false;
    }
    //查询条件：DATEDIFF(day, startDate, GETDATE()) = 0
    private String condition;
    //显示文本：今天
    private String displayLabel;
    //是否默认值条件，如t.status=0，便于新建后回到激活此条件过滤，显示给用户看
    private boolean fallback;
    //是否激活，激活的过滤条件纳入查询语句
    private boolean active;
}
