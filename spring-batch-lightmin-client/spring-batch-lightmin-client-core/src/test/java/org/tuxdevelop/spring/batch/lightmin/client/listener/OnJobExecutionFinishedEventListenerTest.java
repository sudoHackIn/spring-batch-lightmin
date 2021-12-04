package org.tuxdevelop.spring.batch.lightmin.client.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteStepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class OnJobExecutionFinishedEventListenerTest {


    @Mock
    private RemoteJobExecutionEventPublisher jobExecutionEventPublisher;

    @Mock
    private RemoteStepExecutionEventPublisher remoteStepExecutionEventPublisher;
    @InjectMocks
    private OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener;

    @Test
    public void testOnApplicationEventJobExecution() {
        final JobInstance instance = new JobInstance(1L, "testJob");
        final JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setJobInstance(instance);
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
        final JobExecutionEvent jobExecutionEvent = new JobExecutionEvent(jobExecution, "testApplication");

        this.onJobExecutionFinishedEventListener.onApplicationEvent(jobExecutionEvent);
        Mockito.verify(this.jobExecutionEventPublisher, Mockito.times(1))
                .publishEvent(any(JobExecutionEventInfo.class));
    }
}

