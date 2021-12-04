package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultStepServiceTest {

    private static final String STEP_NAME = "sampleStep";

    @Mock
    private JobExplorer jobExplorer;
    @InjectMocks
    private DefaultStepService stepService;

    @Test
    public void getStepExecutionTest() {
        final JobExecution jobExecution = DomainTestHelper.createJobExecution(10L);
        final Long stepExecutionId = 20L;
        when(this.jobExplorer.getStepExecution(jobExecution.getId(), stepExecutionId)).thenReturn(
                DomainTestHelper.createStepExecution(STEP_NAME, jobExecution));
        final StepExecution stepExecution = this.stepService.getStepExecution(jobExecution, stepExecutionId);
        assertThat(stepExecution).isNotNull();
        assertThat(stepExecution.getStepName()).isEqualTo(STEP_NAME);
    }

    @Test
    public void testAttachStepExecutions() {
        final JobExecution jobExecution = DomainTestHelper.createJobExecution(20L);
        when(this.jobExplorer.getJobExecution(jobExecution.getId())).thenReturn(jobExecution);
        this.stepService.attachStepExecutions(jobExecution);
        verify(this.jobExplorer, times(1)).getJobExecution(jobExecution.getId());
    }
}
