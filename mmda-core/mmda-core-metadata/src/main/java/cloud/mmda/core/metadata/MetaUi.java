package cloud.mmda.core.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 元界面，每个{@link MetaObject}生成一个
 * @author roshion.luo
 * @since 2021.7.1
 */
public class MetaUi implements Serializable {
    public MetaUi(){
        tenantID = 0;
        locale = "zh-Hans";
    }
    /**
     * 数据库名称
     */
    @NotNull
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String dbSchema;
    /**
     * 对象名称
     */
    @NotNull
    @Size(min = 1, max = 30)
    @Getter
    @Setter
    private String objName;

    /**
     * 默认显示标签
     */
    @Size(max = 30)
    @Getter @Setter
    private String displayLabel;


    /**
     * 固定的过滤器，如statusFilter
     */
    @Size(max = 30)
    @Getter @Setter
    private String fixedFilter;
    /**
     * 租户内唯一索引字段，可生成findByXxx函数，查重等
     */
    @Size(max = 30)
    @Getter @Setter
    private String uniqueKey;

    /**
     * 组的主键字段，UI层需要
     */
    @NotBlank
    @Size(min = 1, max = 100)
    @Getter
    @Setter
    private String primaryKey;
    /**
     * 语言区域，如en,zh-Hans,zh-Hant
     */
    @NotBlank
    @Size(min=1,max=10)
    @Getter @Setter
    private String locale;
    /**
     * 租户标识，0表示默认租户，公用
     */
    @Getter @Setter
    private int tenantID;
    /**
     * 最后修改时间
     */
    @Getter @Setter
    private Timestamp lastModified;

    /**
     * 主分组，显示重要数据
     */
    @Getter @Setter
    private List<MetaUiGroup> groups;

    /**
     * 是否已经组装
     */
    @Getter @Setter
    private boolean assembled;

    /**
     * 获取主表字段集合，用于组装
     * @return
     */
    @JsonIgnore
    public final List<MetaUiField> getMasterFields(){
        List<MetaUiField> collectedFields = new ArrayList<>();
        for(MetaUiGroup group : groups){
            if(group.isMany()) continue;
            collectedFields.addAll(group.getFields());
        }
        return Collections.unmodifiableList(collectedFields);
    }

    /**
     * 获取所有字段集合
     * @return
     */
    @JsonIgnore
    public List<MetaUiField> getAllFields(){
        List<MetaUiField> collectedFields = new ArrayList<>();
        for(MetaUiGroup group : groups){
            if(group.isMany())
                if(group.getGroupUi()!=null) collectedFields.addAll(group.getGroupUi().getMasterFields());
            else
                collectedFields.addAll(group.getFields());
        }
        return Collections.unmodifiableList(collectedFields);
    }
}
