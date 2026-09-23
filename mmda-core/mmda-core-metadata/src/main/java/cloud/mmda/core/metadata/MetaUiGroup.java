package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.DisplayShape;
import cloud.mmda.core.enums.FetchMode;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.utils.BaseUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MetaUiGroup implements Serializable {
    /**
     * 子表组
     * @param relation
     * @param translatedGroupLabel
     */
    public MetaUiGroup(MetaRelation relation, String translatedGroupLabel){
        Objects.requireNonNull(relation);
        if(BaseUtil.hasText(translatedGroupLabel))
            this.groupLabel = translatedGroupLabel;//收货清单
        else
            this.groupLabel = relation.getDisplayLabel();
        this.groupName = relation.getRelationName(); //items
        this.relObjName = relation.getRelativeObjName();//ReceivingNoteItem
        this.many = (relation.getRelationType()== MetaRelationType.HAS_MANY);//true
        this.displayShape = relation.getDisplayShape();//树形和层次编码视图支持
        this.shapeKey = relation.getShapeKey();//用于计算形状的父字段和编码字段
        this.groupIdx = 1+relation.getRelationIdx();
        this.joinOn = relation.getJoinOn();
        this.sequenceKey = relation.getSequenceKey();
        this.requiredAny = relation.isRequiredAny();
        this.secondary = relation.getSecondary();//次要组
        this.readOnly = relation.isReadOnly();
        this.canHave = relation.getCanHave();
        this.defaultFilter = relation.getDefaultFilter();
        this.defaultPageSize = relation.getDefaultPageSize();
        this.defaultSort = relation.getDefaultSort();
        this.aggregates = relation.getAggregates();//聚合设置
        this.expanded = true;
    }

    /**
     * 主表组
     * @param groupLabel
     * @param relObjName
     * @param fields
     */
    public MetaUiGroup(String groupLabel, String relObjName, List<MetaUiField> fields){
        Objects.requireNonNull(groupLabel);
        Objects.requireNonNull(relObjName);
        this.relObjName = relObjName;//ReceivingNoteItem
        this.many = false;
        this.requiredAny = true;
        this.readOnly = false;
        //拆分解析
        //a1.主要信息 => groupName.groupLabel
        int dotPos = groupLabel.indexOf(".");
        if(dotPos!=-1) {
            this.groupName = groupLabel.substring(0,dotPos);
            this.groupLabel = groupLabel.substring(dotPos+1);
            if(dotPos<2) this.groupName += "0";
        }
        else{
            this.groupName = "t0";
            this.groupLabel = groupLabel;
        }

        //a~j解析为primary(00~19),k~t解析为summary(20~29), u~z解析为(30~35)
        int groupChar = this.groupName.charAt(0);
        int indexChar = this.groupName.charAt(1);
        this.groupIdx = (groupChar - 'a') + (indexChar - '0');
        this.fields = fields.stream().sorted(Comparator.comparingInt(MetaUiField::getFieldIdx))
                .collect(Collectors.toList());
        this.secondary = groupIdx>=20;
        this.expanded = true; //('s'-groupChar)>0;
    }
    /**
     * 分组标签，如：订单信息
     */
    @NotBlank
    @Size(min = 1, max = 60)
    @Getter
    @Setter
    private String groupLabel;

    /**
     * 组名称，如p1,items
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter
    @Setter
    private String groupName;
    /**
     * 对象名称，通常是表名
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter
    @Setter
    private String relObjName;
    /**
     * 是否一对多，子表否
     */
    @Getter
    @Setter
    private boolean many;
    /**
     * 子表显示形状：0;LIST;列表|1;TREE;树形|2;HIERARCHY;层次
     */
    @Getter @Setter
    private DisplayShape displayShape;
    @Getter @Setter
    private String shapeKey;
    /**
     * 子表组的序号
     */
    @Getter
    @Setter
    private Integer groupIdx;
    /**
     * 连接条件，形式为messageID=@messageID
     */
    @Getter
    @Setter
    private String joinOn;
    /**
     * 子表项次字段，自动+1
     */
    @Getter @Setter
    private String sequenceKey;
    /**
     * 要求子表必须有至少一个元素
     */
    @Getter @Setter
    private boolean requiredAny;
    /**
     * 只读否
     */
    @Getter @Setter
    private boolean readOnly;
    /**
     * 默认是否展开
     */
    @Getter
    @Setter
    private boolean expanded;
    /**
     * 次要组，排在概要组下面
     */
    @Getter @Setter
    private boolean secondary;
    @Getter
    @Setter
    private String canHave;
    @Getter
    @Setter
    private String defaultFilter;
    @Getter
    @Setter
    private Integer defaultPageSize;
    @Getter
    @Setter
    private String defaultSort;
    /**
     * 加载模式：0;EAGER;急加载|0;LAZY;懒加载
     */
    @Getter @Setter
    private FetchMode fetchMode;
    /**
     * 聚合设置。例如SUM(fld1),COUNT(fld2)
     */
    @Getter @Setter
    private String aggregates;
    /**
     * 字段
     */
    @Getter @Setter
    private List<MetaUiField> fields;

    /**
     * 子表组的元界面
     */
    @Getter @Setter
    private MetaUi groupUi;

}
