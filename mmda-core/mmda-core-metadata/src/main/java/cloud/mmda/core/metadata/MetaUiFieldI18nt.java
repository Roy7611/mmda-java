package cloud.mmda.core.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * 元域国际化多租户版本
 * @author roshion.luo
 */
public class MetaUiFieldI18nt extends MetaEntity {
    /**
     * 域名称
     */
    @NotBlank
    @Size(min=1,max=36)
    @Getter
    @Setter
    private String fieldName;
    /**
     * 语言区域
     */
    @NotBlank
    @Size(min=1,max=10)
    @Getter @Setter
    private String locale;
    /**
     * 租户标识
     */
    @Getter @Setter
    private int tenantID;
    /**
     * 显示标签
     */
    @Size(max=30)
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
    @Size(max = 1)
    @Getter @Setter
    private String mergePrefix;
    /**
     * 输入提示
     */
    @Size(max=255)
    @Getter @Setter
    private String placeholder;
    /**
     * 后缀文本，如单位或者绑定单位字段$unit
     */
    @Size(max=30)
    @Getter @Setter
    private String suffix;
    /**
     * 输入提示
     */
    @Size(max=512)
    @Getter @Setter
    private String selectOptions;

    /**
     * 空值显示文本
     */
    @Size(max=30)
    @Getter @Setter
    private String nullDisplayText;
    /**
     * 输入提示
     */
    @Size(max=255)
    @Getter @Setter
    private String tooltip;


    /**
     * 主键类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable{
        /**
         * 字段或关系名称
         */
        @NotBlank
        @Size(min=1,max=60)
        @Getter @Setter
        private String fieldName;
        /**
         * 语言区域
         */
        @NotBlank
        @Size(min=1,max=10)
        @Getter @Setter
        private String locale;
        /**
         * 租户标识
         */
        @Getter @Setter
        private int tenantID;
        /**
         * 转为逗号隔开的字符串形式
         * @return 返回字符串，如k1,k2
         */
        @Override
        public String toString() {
            return fieldName + "," + locale + "," + tenantID;
        }

        /**
         * 解析字符串
         * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
         * @return 主键对象
         */
        public static final Key valueOf(String ks) {
            Objects.requireNonNull(ks);
            String[] keys = ks.split(",");
            return new Key(keys[0],keys[1],Short.parseShort(keys[2]));
        }
    }

    /**
     * 作为主键
     */
    public final Key asKey(){
        return new Key(fieldName, locale, tenantID);
    }
}
