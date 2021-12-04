package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.ConsulMetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;
import org.tuxdevelop.test.configuration.ConsulITConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
//todo add consul test containers
@SpringBootTest(classes = {ConsulITConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LightminClientDiscoveryConfigurationConsulIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testConfiguration() {
        final MetaDataExtender metaDataExtender = this.applicationContext.getBean(MetaDataExtender.class);
        assertThat(metaDataExtender instanceof ConsulMetaDataExtender).isTrue();
    }

}
