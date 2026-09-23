package cloud.mmda.core.config;

import cloud.mmda.core.data.jdbc.JdbcAutoConfiguration;
import cloud.mmda.core.data.jdbc.repository.*;
import cloud.mmda.core.services.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@AutoConfiguration(after = {JdbcAutoConfiguration.class,RedisConnectionFactory.class})
@ConditionalOnMissingBean(MmdaAutoConfiguration.class)
@Import(JdbcAutoConfiguration.class)
@ComponentScan(basePackageClasses ={
        AttachmentService.class,
        AuditTrailService.class,
        ChangeLogService.class,
        CustomizedQueryService.class,
        FlowTrailService.class,
        NoticeService.class,
        ReportTemplateService.class,
        CustomizedCacheService.class,
        BackgroundTaskService.class
})
@EnableConfigurationProperties
public class MmdaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AttachmentService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public AttachmentService attachmentService(final AttachmentRepository repository, RedisConnectionFactory factory) {
        return new AttachmentService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(AuditTrailService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public AuditTrailService auditTrailService(final AuditTrailRepository repository, RedisConnectionFactory factory) {
        return new AuditTrailService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(ChangeLogService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public ChangeLogService changeLogService(final ChangeLogRepository repository, RedisConnectionFactory factory) {
        return new ChangeLogService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(CustomizedQueryService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public CustomizedQueryService customizedQueryService(final CustomizedQueryRepository repository, RedisConnectionFactory factory) {
        return new CustomizedQueryService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(FlowTrailService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public FlowTrailService flowTrailService(final FlowTrailRepository repository, RedisConnectionFactory factory) {
        return new FlowTrailService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(NoticeService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public NoticeService noticeService(final NoticeRepository repository, RedisConnectionFactory factory) {
        return new NoticeService(repository, factory);
    }
    @Bean
    @ConditionalOnMissingBean(ReportTemplateService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public ReportTemplateService reportTemplateService(final ReportTemplateRepository repository, RedisConnectionFactory factory) {
        return new ReportTemplateService(repository, factory);
    }

    @Bean
    @ConditionalOnMissingBean(CustomizedCacheService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public CustomizedCacheService redisService(RedisConnectionFactory connectionFactory) {
        return new CustomizedCacheService(connectionFactory);
    }
    @Bean
    @ConditionalOnMissingBean(BackgroundTaskService.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public BackgroundTaskService backgroundTaskService(final BackgroundTaskRepository repository, RedisConnectionFactory factory) {
        return new BackgroundTaskService(repository, factory);
    }
}
