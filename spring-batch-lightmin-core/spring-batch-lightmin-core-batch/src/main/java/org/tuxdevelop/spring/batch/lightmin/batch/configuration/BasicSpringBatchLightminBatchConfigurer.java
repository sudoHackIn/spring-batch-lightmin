package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.sql.DataSource;

@Slf4j
public class BasicSpringBatchLightminBatchConfigurer implements BatchConfigurer, InitializingBean {

    private final TransactionManagerCustomizers transactionManagerCustomizers;
    private final String tablePrefix;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;
    private DataSource dataSource;

    public BasicSpringBatchLightminBatchConfigurer(final TransactionManagerCustomizers transactionManagerCustomizers,
                                                   final DataSource dataSource,
                                                   final String tablePrefix) {
        this.dataSource = dataSource;
        this.tablePrefix = tablePrefix;
        this.transactionManagerCustomizers = transactionManagerCustomizers;
    }

    @Override
    public JobRepository getJobRepository() {
        return this.jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() {
        return this.jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() {
        return this.jobExplorer;
    }

    @Override
    public void afterPropertiesSet() {
        this.initialize();
    }

    protected void initialize() {
        try {
            this.transactionManager = this.buildTransactionManager();
            this.jobRepository = this.createJobRepository();
            this.jobLauncher = this.createJobLauncher();
            this.jobExplorer = this.createJobExplorer();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: " + e.getMessage());
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    private JobExplorer createJobExplorer() throws Exception {
        PropertyMapper map = PropertyMapper.get();
        final JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        map.from(this.tablePrefix).whenHasText().to(jobExplorerFactoryBean::setTablePrefix);
        jobExplorerFactoryBean.setDataSource(this.dataSource);
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }

    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        PropertyMapper map = PropertyMapper.get();
        map.from(this.dataSource).to(factory::setDataSource);
        map.from(this::determineIsolationLevel).whenNonNull().to(factory::setIsolationLevelForCreate);
        map.from(this.tablePrefix).whenHasText().to(factory::setTablePrefix);
        map.from(this::getTransactionManager).to(factory::setTransactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    protected String determineIsolationLevel() {
        return null;
    }

    private PlatformTransactionManager buildTransactionManager() {
        PlatformTransactionManager transactionManager = this.createTransactionManager();
        if (this.transactionManagerCustomizers != null) {
            this.transactionManagerCustomizers.customize(transactionManager);
        }
        return transactionManager;
    }

    protected PlatformTransactionManager createTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }
}
