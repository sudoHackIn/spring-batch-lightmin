package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {JdbcJobConfigurationRepositoryConfigurationProperties.class})
public class JdbcJobConfigurationRepositoryConfiguration {

    private final JdbcJobConfigurationRepositoryConfigurationProperties properties;
    private final DataSource dataSource;

    public JdbcJobConfigurationRepositoryConfiguration(DataSource dataSource,
                                                       final JdbcJobConfigurationRepositoryConfigurationProperties properties) {
        this.properties = properties;
        this.dataSource = dataSource;
    }

    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new JdbcJobConfigurationRepository(new JdbcTemplate(dataSource), this.properties);
    }

    @Bean(name = "lightminTransactionManager")
    public PlatformTransactionManager lightminTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
