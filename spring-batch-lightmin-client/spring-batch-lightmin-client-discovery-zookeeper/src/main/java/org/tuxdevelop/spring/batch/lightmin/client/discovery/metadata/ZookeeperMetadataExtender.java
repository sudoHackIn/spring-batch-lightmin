package org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ZookeeperMetadataExtender implements MetaDataExtender {

    private final ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    public ZookeeperMetadataExtender(ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        this.zookeeperDiscoveryProperties = zookeeperDiscoveryProperties;
    }

    @Override
    public void extendMetaData() {
        if (this.zookeeperDiscoveryProperties.getMetadata() != null) {
            log.debug("zookeeper meta data map already initialized");
        } else {
            final Map<String, String> metaData = new HashMap<>();
            this.zookeeperDiscoveryProperties.setMetadata(metaData);
        }
        this.zookeeperDiscoveryProperties.getMetadata().put(LIGHTMIN_CLIENT_META_DATA_KEY, LIGHTMIN_CLIENT_META_DATA_VALUE);
    }
}
