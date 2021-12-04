package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.event.JobExecutionEventModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobExecutionEventFeServiceTest {

    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private EventService eventService;
    @InjectMocks
    private JobExecutionEventFeService jobExecutionEventFeService;

    @Test
    public void testGetJobExecutionEventModels() {
        final int pageSize = 5;
        final List<JobExecutionEventInfo> jobExecutionEventInfos =
                ServiceTestHelper.createJobExecutionEvents(pageSize, "test_application");
        when(this.eventService.getAllJobExecutionEvents(0, pageSize)).thenReturn(jobExecutionEventInfos);

        final ContentPageModel<List<JobExecutionEventModel>> result =
                this.jobExecutionEventFeService.getJobExecutionEventModels(0, pageSize);

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getValue()).isNotNull();
        BDDAssertions.then(result.getValue().size()).isEqualTo(pageSize);
    }
}
