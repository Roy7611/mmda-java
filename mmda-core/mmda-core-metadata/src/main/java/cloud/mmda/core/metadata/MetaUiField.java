package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.FieldAggregation;
import cloud.mmda.core.enums.FieldAlignment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 元域，指用户界面中的一个最小单元，如输入框。
 * 默认情况每个字段{@link MetaCol}引用一个，枚举类型fieldName使用枚举类名称。
 * @author roshion.luo
 * @since 2021.7.1
 */
public class MetaUiField implements Serializable {

    /**
     * 域顺序号
     */
    @Getter
    @Setter
    private int fieldIdx;
    /**
     * 域名称
     */
    @NotBlank
    @Size(min=1,max=36)
    @Getter
    @Setter
    private String fieldName;
    /**
     * 分组标签
     */
    @JsonIgnore
    @Size(max=60)
    @Getter @Setter
    private String groupLabel;
    /**
     * 显示标签
     */
    @Size(max=60)
    @Getter @Setter
    private String displayLabel;
    /**
     * 合并标签，多个字段连接在一起显示
     */
    @Size(max = 30)
    @Getter @Setter
    private String mergeLabel;
    /**
     * 合并前缀，例如换行、空格
     */
    @Size(max = 30)
    @Getter @Setter
    private String mergePrefix;

    /**
     * 重要，在最简列表显示
     */
    @NotNull
    @Getter @Setter
    private boolean emphasized;
    /**
     * 列出，桌面端列表显示
     */
    @NotNull
    @Getter @Setter
    private boolean listed;

    /**
     * 聚合设置
     */
    @NotNull
    @Getter @Setter
    @JsonSerialize(converter = FieldAggregation.SetToIntConverter.class)
    @JsonDeserialize(converter = FieldAggregation.IntToSetConverter.class)
    private EnumBitSet<FieldAggregation> aggregationSet;
    /**
     * 列显示宽度，像素无关值
     */
    @Min(0)
    @Getter @Setter
    private Integer listSize;
    /**
     * 对齐方式：0;LEFT;左对齐|1;RIGHT;右对齐|2;CENTER;居中|3;JUSTIFY;两边|4;START;开始|5;END;结束
     */
    @Getter @Setter
    private FieldAlignment align;
    /**
     * 可排序，通常是有索引的字段支持排序
     */
    @NotNull
    @Getter @Setter
    private boolean sortable;

    /**
     * 渲染器
     */
    @Size(max=30)
    @Getter @Setter
    private String renderer;
    /**
     * 显示格式
     */
    @Size(max=255)
    @Getter @Setter
    private String formatter;
    /**
     * 后缀文本，如单位或者绑定单位字段$unit
     */
    @Size(max=30)
    @Getter @Setter
    private String suffix;
    /**
     * 只读
     */
    @NotNull
    @Getter @Setter
    private boolean readOnly;
    /**
     * 编辑器
     */
    @Size(max=30)
    @Getter @Setter
    private String editor;
    /**
     * 可选项
     * 例如：0;Default|1;Activated
     */
    @Getter @Setter
    @Size(max=512)
    private String selectOptions;
    /**
     * 校验规则
     */
    @Size(max=255)
    @Getter @Setter
    private String validationRules;

    /**
     * 输入提示
     */
    @Size(max=255)
    @Getter @Setter
    private String placeholder;
    /**
     * 空值显示文本
     */
    @Size(max=30)
    @Getter @Setter
    private String nullDisplayText;

    /**
     * 隐藏
     */
    @NotNull
    @Getter @Setter
    private boolean hidden;

    /**
     * 工具提示
     */
    @Size(max=255)
    @Getter @Setter
    private String tooltip;

    /**
     * 数据绑定
     */
    @Size(max=255)
    @Getter @Setter
    private String dataBinding;

    //region metacol 属性
    /**
     * 是否主键
     */
    @NotNull
    @Getter @Setter
    private boolean primaryKey;
    /**
     * 数据类型，参考{@link MetaDataType}
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private int dataType;
    /**
     * 是否无符号，用于整型
     */
    @NotNull
    @Getter @Setter
    private boolean isUnsigned;
    /**
     * 字符串最大长度，用于CHAR/VARCHAR
     */
    @Min(0)
    @Getter @Setter
    private Integer maxLength;

    /**
     * 数值精度
     */
    @Min(0)
    @Getter @Setter
    private Byte numericPrecision;

    /**
     * 小数位数
     */
    @Min(0)
    @Getter @Setter
    private Byte numericScale;
    /**
     * 是否可为空值
     */
    @NotNull
    @Getter @Setter
    private boolean nullable;
    /**
     * 计算公式，仅当computed=true有效
     */
    @Getter @Setter
    private String formula;
    /**
     * 缺省值
     */
    @Size(max = 50)
    @Getter @Setter
    private String defaultVal;

    //endregion
    /**
     * 域别名，实际上fieldName = MetaCol.colName，
     * 而fieldNameAlias是实际的唯一域名称，国际化时根据此名称比较
     */
    @Getter
    @Setter
    private String fieldNameAlias;

    @JsonIgnore
    public String getGroupLabelOrDefault(){
        if(groupLabel==null) return "_";
        return groupLabel;
    }

    /**
     * 尝试合并多租户版本
     * @param i18nt 多语言，多租户版本的配置
     * @return 匹配域名称后设置成功返回true
     */
    public boolean trySetI18nt(MetaUiFieldI18nt i18nt){
        if(!i18nt.getFieldName().equals(fieldNameAlias)) return false;

        if(i18nt.getDisplayLabel()!=null) displayLabel = i18nt.getDisplayLabel();
        if(i18nt.getMergeLabel()!=null) mergeLabel = i18nt.getMergeLabel();
        if(i18nt.getMergePrefix()!=null) mergePrefix = i18nt.getMergePrefix();
        if(i18nt.getPlaceholder()!=null) placeholder = i18nt.getPlaceholder();
        if(i18nt.getSelectOptions()!=null) selectOptions = i18nt.getSelectOptions();
        if(i18nt.getNullDisplayText()!=null) nullDisplayText = i18nt.getNullDisplayText();
        if(i18nt.getTooltip()!=null) tooltip = i18nt.getTooltip();
        return true;
    }
}
