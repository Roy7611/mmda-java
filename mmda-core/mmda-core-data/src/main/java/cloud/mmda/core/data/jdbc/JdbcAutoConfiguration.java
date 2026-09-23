package cloud.mmda.core.data.jdbc;

import cloud.mmda.core.data.jdbc.repository.*;
import org.hibernate.annotations.JdbcType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AutoConfiguration(after = DataSource.class)
@ConditionalOnMissingBean(JdbcAutoConfiguration.class)
@ComponentScan(basePackageClasses ={
        AttachmentRepository.class,
        AuditTrailRepository.class,
        ChangeLogRepository.class,
        CustomizedQueryRepository.class,
        FlowTrailRepository.class,
        NoticeRepository.class,
        ReportTemplateRepository.class
})
public class JdbcAutoConfiguration {

    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(AttachmentRepository.class)
    public AttachmentRepository attachmentRepository(DataSource dataSource) {
        return new AttachmentRepository(dataSource);
    }

    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(AuditTrailRepository.class)
    public AuditTrailRepository auditTrailRepository(DataSource dataSource) {
        return new AuditTrailRepository(dataSource);
    }
    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(ChangeLogRepository.class)
    public ChangeLogRepository changeLogRepository(DataSource dataSource) {
        return new ChangeLogRepository(dataSource);
    }
    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(CustomizedQueryRepository.class)
    public CustomizedQueryRepository customizedQueryRepository(DataSource dataSource) {
        return new CustomizedQueryRepository(dataSource);
    }
    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(FlowTrailRepository.class)
    public FlowTrailRepository flowTrailRepository(DataSource dataSource) {
        return new FlowTrailRepository(dataSource);
    }
    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(NoticeRepository.class)
    public NoticeRepository noticeRepository(DataSource dataSource) {
        return new NoticeRepository(dataSource);
    }
    @Bean
    @ConditionalOnClass(DataSource.class)
    @ConditionalOnMissingBean(ReportTemplateRepository.class)
    public ReportTemplateRepository reportTemplateRepository(DataSource dataSource) {
        return new ReportTemplateRepository(dataSource);
    }
}
