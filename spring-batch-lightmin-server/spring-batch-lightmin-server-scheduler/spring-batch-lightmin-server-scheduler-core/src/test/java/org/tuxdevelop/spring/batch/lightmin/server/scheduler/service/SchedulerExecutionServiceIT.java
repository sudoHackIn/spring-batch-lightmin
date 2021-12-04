package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecutionTestHelper;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.SchedulerCoreITConfiguration;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertThrows;

@Slf4j
@SpringBootTest(classes = {SchedulerCoreITConfiguration.class})
public class SchedulerExecutionServiceIT extends CommonServiceIT {

    @Autowired
    private SchedulerExecutionService schedulerExecutionService;
    @Autowired
    private CleanUpRepository cleanUpRepository;

    @MockBean
    private JobServerService jobServerService;

    @Test
    public void testSave() {

        final SchedulerExecution schedulerExecution = this.getNewSchedulerExecution();
        final SchedulerExecution result = this.schedulerExecutionService.save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

    }

    @Test
    public void testSaveNull() {
        assertThrows(SchedulerValidationException.class, () -> this.schedulerExecutionService.save(null));
    }

    @Test
    public void testCreateNextExecution() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        final String cron = "0 0/2 0 ? * * ";
        final List<SchedulerExecution> beforeCreation =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        this.schedulerExecutionService.createNextExecution(schedulerExecution, cron);
        final List<SchedulerExecution> result =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(beforeCreation.size() + 1);
    }

    @Test
    public void testCreateNextExecutionNull() {
        assertThrows(SchedulerValidationException.class, () -> this.schedulerExecutionService.createNextExecution(null, "0 0/2 0 ? * *"));
    }

    @Test
    public void testDelete() {
        final SchedulerExecution saved = this.getNewSchedulerExecution();
        this.schedulerExecutionService.deleteExecution(saved.getId());
        try {
            this.schedulerExecutionService.findById(saved.getId());
            fail("SchedulerExecution did not get deleted");
        } catch (final SchedulerRuntimException e) {
            log.debug("Deleted SchedulerExecution, everything fine!");
        }
    }


    @Test
    public void testDeleteExecutionStateRunning() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        schedulerExecution.setState(ExecutionStatus.RUNNING);
        final SchedulerExecution saved = this.schedulerExecutionService.save(schedulerExecution);
        assertThrows(SchedulerRuntimException.class, () -> this.schedulerExecutionService.deleteExecution(saved.getId()));
    }

    @Test
    public void testFindById() {
        final SchedulerExecution saved = this.getNewSchedulerExecution();
        final SchedulerExecution found = this.schedulerExecutionService.findById(saved.getId());
        BDDAssertions.then(found).isEqualTo(saved);
    }

    @Test
    public void testFindByIdNotFound() {
        assertThrows(SchedulerRuntimException.class, () -> this.schedulerExecutionService.findById(-1L));
    }

    @Test
    public void testGetNextFireTime() {
        final String cron = "0 0/2 0 ? * * ";
        final Date now = new Date();
        final Date fireDate = this.schedulerExecutionService.getNextFireTime(cron);
        BDDAssertions.then(fireDate).isAfter(now);
    }

    @Test
    public void testGetNextFireTimeUnparsableExpression() {
        final String cron = "0 0/2 0 ?";
        assertThrows(SchedulerRuntimException.class,() -> this.schedulerExecutionService.getNextFireTime(cron));
    }

    @Override
    protected CleanUpRepository cleanUpRepository() {
        return this.cleanUpRepository;
    }

    private SchedulerExecution getNewSchedulerExecution() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        return this.schedulerExecutionService.save(schedulerExecution);
    }
}