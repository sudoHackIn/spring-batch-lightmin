package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class ListenerStatusTest {


    @Test
    public void testGetByValueACTIVE() {
        final ListenerStatus result = ListenerStatus.getByValue("ACTIVE");
        assertThat(result).isEqualTo(ListenerStatus.ACTIVE);
    }

    @Test
    public void testGetByValueSTOPPED() {
        final ListenerStatus result = ListenerStatus.getByValue("STOPPED");
        assertThat(result).isEqualTo(ListenerStatus.STOPPED);
    }

    @Test
    public void getByValueUnknownTest() {
        assertThrows(IllegalArgumentException.class, () -> ListenerStatus.getByValue("UNKNOWN"));
    }
}
