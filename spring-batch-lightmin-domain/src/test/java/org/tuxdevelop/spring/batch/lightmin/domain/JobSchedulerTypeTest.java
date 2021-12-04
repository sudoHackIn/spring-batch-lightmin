package org.tuxdevelop.spring.batch.lightmin.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import static org.junit.Assert.assertThrows;

public class JobSchedulerTypeTest {

    @Test
    public void getByIdCronTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(1L);
        Assertions.assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.CRON);
    }

    @Test
    public void getByIdPeriodTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(2L);
        Assertions.assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.PERIOD);
    }

    @Test
    public void getByIdUnknownTest() {
        assertThrows(SpringBatchLightminConfigurationException.class, ()->JobSchedulerType.getById(-1000L));
    }
}
