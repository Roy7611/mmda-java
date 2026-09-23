package cloud.mmda.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mmda")
public class MmdaProperties {

    /**
     * 数据库
     */
    @Getter @Setter
    private String database;

    /**
     * 元数据库
     */
    @Getter @Setter
    private String metabase;
}
