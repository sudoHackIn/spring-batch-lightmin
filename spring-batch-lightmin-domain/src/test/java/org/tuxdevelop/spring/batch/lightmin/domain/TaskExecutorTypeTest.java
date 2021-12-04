package org.tuxdevelop.spring.batch.lightmin.domain;


import org.junit.jupiter.api.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class TaskExecutorTypeTest {

    @Test
    public void getByIdSYNCHRONOUSTest() {
        final TaskExecutorType taskExecutorType = TaskExecutorType.getById(1L);
        assertThat(taskExecutorType).isEqualTo(TaskExecutorType.SYNCHRONOUS);
    }

    @Test
    public void getByIdASYNCHRONOUSTest() {
        final TaskExecutorType taskExecutorType = TaskExecutorType.getById(2L);
        assertThat(taskExecutorType).isEqualTo(TaskExecutorType.ASYNCHRONOUS);
    }

    @Test
    public void getByIdUnknownTest() {
        assertThrows(SpringBatchLightminApplicationException.class, () -> TaskExecutorType.getById(-100L));
    }
}
