package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;

import java.util.List;

/**
 * LightminJobExplorer is a {@link SimpleJobExplorer}
 * that do not populate {@link org.springframework.batch.item.ExecutionContext} in {@link org.springframework.batch.core.StepExecution}
 * for a performance reason (work's very slow for job with big amount of step execution).
 * ExecutionContext info is not provided to UI now.
 *
 * @author Vladislav Khakin
 */
public class LightminJobExplorer extends SimpleJobExplorer {

    private final JobExecutionDao jobExecutionDao;
    private final JobInstanceDao jobInstanceDao;
    private final StepExecutionDao stepExecutionDao;
    private final ExecutionContextDao ecDao;

    public LightminJobExplorer(JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao ecDao) {
        super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
        this.jobExecutionDao = jobExecutionDao;
        this.jobInstanceDao = jobInstanceDao;
        this.stepExecutionDao = stepExecutionDao;
        this.ecDao = ecDao;
    }

    /* retrieve jobExeucition info without step execution */
    @Override
    public List<JobExecution> getJobExecutions(JobInstance jobInstance) {
        List<JobExecution> executions = jobExecutionDao.findJobExecutions(jobInstance);
        for (JobExecution jobExecution : executions) {
            getJobExecutionDependencies(jobExecution);
        }
        return executions;
    }

    protected void getJobExecutionDependencies(JobExecution jobExecution) {
        JobInstance jobInstance = jobInstanceDao.getJobInstance(jobExecution);
        stepExecutionDao.addStepExecutions(jobExecution);
        jobExecution.setJobInstance(jobInstance);
        jobExecution.setExecutionContext(ecDao.getExecutionContext(jobExecution));

    }
}
