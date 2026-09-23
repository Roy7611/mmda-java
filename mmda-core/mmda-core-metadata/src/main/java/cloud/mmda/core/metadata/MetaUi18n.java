package cloud.mmda.core.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 元界面多语言翻译
 * @author roshion.luo
 * @since 2021.7.1
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MetaUi18n extends MetaEntity {
    private String words;
    private String locale;
    private String translation;

    public final String getWordsNLocale(){
        return words+":"+locale;
    }
}
