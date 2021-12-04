package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.BatchJobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobFeServiceTest {

    private static final String APPLICATION_ID = "test_app";

    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private JobServerService jobServerService;
    @InjectMocks
    private JobFeService jobFeService;

    @Test
    public void testFindById() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_ID)).thenReturn(lightminClientApplication);
        when(this.jobServerService.getJobInfo(anyString(), any())).thenReturn(ApiTestHelper.createJobInfo("test_job", 2));
        final List<BatchJobInfoModel> result = this.jobFeService.findById(APPLICATION_ID);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(2);
    }
}
