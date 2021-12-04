package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public abstract class JobConfigurationRepositoryControllerIT {

    private static final String APPLICATION_NAME = "sample_application";

    @Test
    public void testGetJobConfiguration() {
        final JobConfiguration savedJobConfiguration = this.createNewJobConfiguration();
        try {
            final JobConfiguration response = this.getJobConfigurationRepository().getJobConfiguration(savedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
            assertThat(response.getJobConfigurationId()).isEqualTo(savedJobConfiguration.getJobConfigurationId());
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetJobConfigurations() {
        this.createNewJobConfiguration();
        try {
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("sampleJob", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        try {
            final String newJobName = "updated_job_name";
            jobConfiguration.setJobName(newJobName);
            this.getJobConfigurationRepository().update(jobConfiguration, APPLICATION_NAME);
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("updated_job_name", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        try {
            this.getJobConfigurationRepository().delete(jobConfiguration, APPLICATION_NAME);
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("sampleJob", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response.contains(jobConfiguration)).isFalse();
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            //ok, jobConfiguration is deleted
        }
    }

    @Test
    public void testGetAllJobConfigurations() {
        this.createNewJobConfiguration();
        final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getAllJobConfigurations(APPLICATION_NAME);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
    }


    @Test
    public void testGetAllJobConfigurationsByJobNames() {
        final JobConfiguration jobConfiguration = this.createNewJobConfigurationWithoutSave();
        final String newJobName = "otherJob";
        jobConfiguration.setJobName(newJobName);
        //save otherJob
        this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        //save sampleJob
        this.createNewJobConfiguration();
        final List<String> jobNames = new ArrayList<>();
        jobNames.add("sampleJob");
        jobNames.add("otherJob");
        final Collection<JobConfiguration> response = this.getJobConfigurationRepository()
                .getAllJobConfigurationsByJobNames(jobNames, APPLICATION_NAME);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
    }

    private JobConfiguration createNewJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null, 1000L, 100L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("mySampleBean_" + System.currentTimeMillis());
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        return this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
    }

    private JobConfiguration createNewJobConfigurationWithoutSave() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null, 1000L, 100L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("mySampleBean_" + System.currentTimeMillis());
        return DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
    }

    public abstract JobConfigurationRepository getJobConfigurationRepository();

    public abstract ITJobConfigurationRepository getITItJdbcJobConfigurationRepository();

    @BeforeEach
    public void init() {
        this.getITItJdbcJobConfigurationRepository().clean(APPLICATION_NAME);
    }
}
