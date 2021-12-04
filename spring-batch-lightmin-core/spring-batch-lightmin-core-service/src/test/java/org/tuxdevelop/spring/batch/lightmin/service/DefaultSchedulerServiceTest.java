package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultSchedulerServiceTest {


    @Mock
    private BeanRegistrar beanRegistrar;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobRegistry jobRegistry;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Scheduler scheduler;
    @InjectMocks
    private DefaultSchedulerService schedulerService;

    private Job sampleJob;

    @Test
    public void registerSchedulerForJobPeriodTest() throws NoSuchJobException {
        when(this.jobRegistry.getJob("sampleJob")).thenReturn(this.sampleJob);

        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final String beanName = this.schedulerService.registerSchedulerForJob(jobConfiguration);
        assertThat(beanName).startsWith("sampleJob-PERIOD");
    }

    @Test
    public void registerSchedulerForJobCronTest() throws NoSuchJobException {
        when(this.jobRegistry.getJob("sampleJob")).thenReturn(this.sampleJob);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration("* * *" +
                        " * * *",
                null, null, JobSchedulerType.CRON);

        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final String beanName = this.schedulerService.registerSchedulerForJob(jobConfiguration);
        assertThat(beanName).startsWith("sampleJob-CRON");
    }

    @Test
    public void unregisterSchedulerForJobTest() {
        doNothing().when(this.beanRegistrar).unregisterBean(anyString());
        this.schedulerService.unregisterSchedulerForJob("sampleJob");
        verify(this.beanRegistrar, times(1)).unregisterBean("sampleJob");
    }

    @Test
    public void refreshSchedulerForJobTest() {
        final String beanName = "schedulerBean";
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName(beanName);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(DomainTestHelper.createJob("sampleJob"));
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.NONE);
        schedulerConstructorWrapper.setJobLauncher(new SimpleJobLauncher());
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        //scheduler = new PeriodScheduler(schedulerConstructorWrapper);
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        this.schedulerService.refreshSchedulerForJob(jobConfiguration);
        verify(this.scheduler, times(1)).terminate();
    }

    @Test
    public void refreshSchedulerForJobCronTest() {
        final String beanName = "schedulerBean";
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration("some " +
                        "Cron",
                null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(DomainTestHelper.createJob("sampleJob"));
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.NONE);
        schedulerConstructorWrapper.setJobLauncher(new SimpleJobLauncher());
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        //scheduler = new PeriodScheduler(schedulerConstructorWrapper);
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        this.schedulerService.refreshSchedulerForJob(jobConfiguration);
        verify(this.scheduler, times(1)).terminate();
    }

    @Test
    public void scheduleTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        this.schedulerService.schedule(beanName, Boolean.FALSE);
        verify(this.scheduler, times(1)).schedule();
    }

    @Test
    public void scheduleRUNNINGTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        this.schedulerService.schedule(beanName, Boolean.FALSE);
        verify(this.scheduler, times(0)).schedule();
    }

    @Test
    public void scheduleRUNNINGForceTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        this.schedulerService.schedule(beanName, Boolean.TRUE);
        verify(this.scheduler, times(1)).schedule();
    }

    @Test
    public void scheduleUnknownTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        assertThrows(SpringBatchLightminConfigurationException.class, () -> this.schedulerService.schedule(beanName, Boolean.FALSE));
    }

    @Test
    public void terminateTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        this.schedulerService.terminate(beanName);
        verify(this.scheduler, times(1)).terminate();
    }

    @Test
    public void terminateSTOPPEDTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.STOPPED);
        this.schedulerService.terminate(beanName);
        verify(this.scheduler, times(0)).terminate();
    }

    @Test
    public void terminateUnknownTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        assertThrows(SpringBatchLightminConfigurationException.class, () -> this.schedulerService.terminate(beanName));
    }

    @Test
    public void getSchedulerStatusTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(this.applicationContext.getBean(beanName, Scheduler.class)).thenReturn(this.scheduler);
        when(this.scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.STOPPED);
        final SchedulerStatus schedulerStatus = this.schedulerService.getSchedulerStatus(beanName);
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.STOPPED);
    }

    @Test
    public void getSchedulerStatusUnknownTest() {
        final String beanName = "schedulerBean";
        when(this.applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        assertThrows(SpringBatchLightminConfigurationException.class, () -> this.schedulerService.getSchedulerStatus(beanName));
    }

    @BeforeEach
    public void init() {
        this.sampleJob = DomainTestHelper.createJob("sampleJob");
        this.schedulerService.setApplicationContext(applicationContext);
    }
}