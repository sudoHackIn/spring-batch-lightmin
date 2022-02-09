package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnLightminClientApplicationChangedEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.service.LightminMetricServerEventListenerBean;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.server.support.UrlApplicationRegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.service.MetricServiceBean;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminServerCoreProperties.class})
public class LightminServerCoreConfiguration {

    @Bean
    @ConditionalOnMissingBean(RegistrationBean.class)
    public RegistrationBean registrationBean(final LightminApplicationRepository lightminApplicationRepository) {
        return new UrlApplicationRegistrationBean(lightminApplicationRepository);
    }

    @Bean
    @ConditionalOnMissingBean(value = {EventService.class})
    public EventService eventService(@Qualifier("jobExecutionEventRepository") final JobExecutionEventRepository jobExecutionEventRepository) {
        return new EventServiceBean(jobExecutionEventRepository);
    }

    @Bean
    @ConditionalOnMissingBean(value = {JournalServiceBean.class})
    public JournalServiceBean journalServiceBean(final JournalRepository journalRepository) {
        return new JournalServiceBean(journalRepository);
    }

    @Bean
    @ConditionalOnMissingBean(name = "clientRestTemplate")
    public RestTemplate clientRestTemplate(final LightminServerCoreProperties lightminServerCoreProperties, RestTemplateBuilder builder) {
        return RestTemplateFactory.getRestTemplate(lightminServerCoreProperties, builder);
    }

    @Bean(name = "lightminServerSchedulerTransactionManager")
    @ConditionalOnMissingBean(name = "lightminServerSchedulerTransactionManager")
    public PlatformTransactionManager lightminServerSchedulerTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public OnLightminClientApplicationChangedEventListener onLightminClientApplicationChangedEventListener(
            final LightminApplicationRepository applicationRepository,
            final LightminServerCoreProperties properties) {
        return new OnLightminClientApplicationChangedEventListener(applicationRepository, properties);
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.server",
            name = "metrics-enabled",
            havingValue = "true",
            matchIfMissing = true)
    static class ServerMetricsConfiguration {
        @Bean
        @ConditionalOnMissingBean(value = MeterRegistryCustomizer.class)
        public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
            return registry -> registry.config();
        }

        @Bean("serverMetricService")
        @ConditionalOnMissingBean(name = "serverMetricService")
        public MetricService metricService(final MeterRegistry registry) {
            return new MetricServiceBean(registry);
        }

        @Bean
        @ConditionalOnMissingBean(value = LightminMetricServerEventListenerBean.class)
        public LightminMetricServerEventListenerBean metricEventServiceListenerBean(@Qualifier("serverMetricService") final MetricService metricService) {
            return new LightminMetricServerEventListenerBean(metricService);
        }
    }

    static class RestTemplateFactory {

        static RestTemplate getRestTemplate(final LightminServerCoreProperties lightminServerCoreProperties, RestTemplateBuilder builder) {

            if (lightminServerCoreProperties.getClientUserName() != null) {
                builder.interceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(lightminServerCoreProperties.getClientUserName(),
                                        lightminServerCoreProperties.getClientPassword()))
                );
            }
            return builder.build();
        }
    }
}
