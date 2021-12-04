package org.tuxdevelop.spring.batch.lightmin.domain;

import org.junit.jupiter.api.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertThrows;

public class JobSchedulerConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobSchedulerConfiguration.class);
    }

    @Test
    public void validateForPeriodTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        try {
            jobSchedulerConfiguration.validate();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateForCronTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.CRON);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        try {
            jobSchedulerConfiguration.validate();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateSchedulerNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(null);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validate);
    }

    @Test
    public void validateTaskExecutorNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.CRON);
        jobSchedulerConfiguration.setTaskExecutorType(null);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validate);
    }

    @Test
    public void validateCronTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        try {
            jobSchedulerConfiguration.validateCron();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateCronExpressionNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression(null);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validate);
    }

    @Test
    public void validateCronExpressionNotValidTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("invalid");
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validateCronExpressionFixedDelaySetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setFixedDelay(1000L);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validateCronExpressionInitialDelaySetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        jobSchedulerConfiguration.setInitialDelay(1000L);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validatePeriodTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        try {
            jobSchedulerConfiguration.validatePeriod();
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validatePeriodFixedDelayNullTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validatePeriodFixedDelayNegativTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(-1000L);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validatePeriodInitialDelayNegativTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(-1000L);
        jobSchedulerConfiguration.setInitialDelay(-1000L);
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }

    @Test
    public void validatePeriodCronExpressionSetTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(1000L);
        jobSchedulerConfiguration.setInitialDelay(1000L);
        jobSchedulerConfiguration.setCronExpression("0 0 12 * * ?");
        assertThrows(SpringBatchLightminApplicationException.class, jobSchedulerConfiguration::validateCron);
    }
}
