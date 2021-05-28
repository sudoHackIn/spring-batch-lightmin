package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class JpaBatchConfigurerConfiguration implements ApplicationContextAware {

    private final SpringBatchLightminBatchConfigurationProperties properties;

    private ApplicationContext applicationContext;

    @Autowired
    public JpaBatchConfigurerConfiguration(final SpringBatchLightminBatchConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public BatchConfigurer batchConfigurer( final EntityManagerFactory entityManagerFactory,
            final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        final DataSource dataSource = this.getDataSource();
        final String tablePrefix = this.properties.getTablePrefix();
        return new JpaSpringBatchLightminBatchConfigurer(transactionManagerCustomizers.getIfAvailable(), dataSource, tablePrefix, entityManagerFactory);
    }

    DataSource getDataSource() {
        return this.applicationContext.getBean(this.properties.getDataSourceName(), DataSource.class);
    }

    //TODO remove ApplicationContext search
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}