package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.MapJobConfigurationRepository;

@Configuration
public class MapJobConfigurationRepositoryConfiguration  {

    @Bean
    @ConditionalOnMissingBean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new MapJobConfigurationRepository();
    }
}
