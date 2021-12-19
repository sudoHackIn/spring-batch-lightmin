package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;

/**
 * Specific Lightmin {@link JobExplorerFactoryBean}
 * that creates * {@link LightminJobExplorer}
 *
 * @author Vladislav Khakin
 */
public class LightminJobExplorerFactoryBean extends JobExplorerFactoryBean {
    @Override
    public JobExplorer getObject() throws Exception {
        return new LightminJobExplorer(
                createJobInstanceDao(), createJobExecutionDao(), createStepExecutionDao(), createExecutionContextDao()
        );
    }
}
