package org.tuxdevelop.spring.batch.lightmin.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import static org.junit.Assert.assertThrows;

public class ListenerStatusTest {


    @Test
    public void testGetByValueACTIVE() {
        final ListenerStatus result = ListenerStatus.getByValue("ACTIVE");
        Assertions.assertThat(result).isEqualTo(ListenerStatus.ACTIVE);
    }

    @Test
    public void testGetByValueSTOPPED() {
        final ListenerStatus result = ListenerStatus.getByValue("STOPPED");
        Assertions.assertThat(result).isEqualTo(ListenerStatus.STOPPED);
    }

    @Test
    public void getByValueUnknownTest() {
        assertThrows(SpringBatchLightminApplicationException.class, () -> ListenerStatus.getByValue("UNKNOWN"));
    }
}
