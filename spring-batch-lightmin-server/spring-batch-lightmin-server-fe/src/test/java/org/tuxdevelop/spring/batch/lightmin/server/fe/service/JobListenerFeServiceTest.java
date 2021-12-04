package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.ListenerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobListenerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobListenerFeServiceTest {

    private static final String APPLICATION_INSTANCE_ID = "testApp";
    private static final Long JC_ID = 1L;

    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private AdminServerService adminServerService;
    @InjectMocks
    private JobListenerFeService jobListenerFeService;

    @Test
    public void testGetMapJobConfigurationModel() {

        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final Map<String, JobConfigurations> jobConfigurationsMap = new HashMap<>();

        ApiTestHelper.addJobConfigurations(jobConfigurationsMap,
                APPLICATION_INSTANCE_ID,
                ApiTestHelper.createJobListenerConfigurations(5));

        when(this.adminServerService.getJobConfigurationsMap(lightminClientApplication)).thenReturn(jobConfigurationsMap);

        final MapJobListenerConfigurationModel result =
                this.jobListenerFeService.getMapJobConfigurationModel(APPLICATION_INSTANCE_ID);


        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getJobConfigurations()).isNotNull();
        BDDAssertions.then(result.getJobConfigurations().size()).isEqualTo(1);

    }

    @Test
    public void testGetJobConfigurationModel() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(
                ApiTestHelper.createJobListenerConfiguration("/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER));

        when(this.adminServerService.getJobConfiguration(JC_ID, lightminClientApplication)).thenReturn(jobConfiguration);

        final ListenerJobConfigurationModel result =
                this.jobListenerFeService.getJobConfigurationModel(JC_ID, APPLICATION_INSTANCE_ID);

        BDDAssertions.then(result).isNotNull();
    }

    @Test
    public void testStartListener() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobListenerFeService.startListener(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).startJobConfigurationScheduler(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testStopListener() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobListenerFeService.stopListener(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).stopJobConfigurationScheduler(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobListenerFeService.deleteListenerConfiguration(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).deleteJobConfiguration(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final ListenerJobConfigurationModel listenerJobConfigurationModel =
                ServiceTestHelper.createListenerJobConfigurationModel("testJob");

        try {
            this.jobListenerFeService.addListenerConfiguration(listenerJobConfigurationModel, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).
                    saveJobConfiguration(this.jobListenerFeService.map(
                            listenerJobConfigurationModel), lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final ListenerJobConfigurationModel listenerJobConfigurationModel =
                ServiceTestHelper.createListenerJobConfigurationModel("testJob");

        try {
            this.jobListenerFeService.updateListenerConfiguration(listenerJobConfigurationModel, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).
                    updateJobConfiguration(this.jobListenerFeService.map(
                            listenerJobConfigurationModel), lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
