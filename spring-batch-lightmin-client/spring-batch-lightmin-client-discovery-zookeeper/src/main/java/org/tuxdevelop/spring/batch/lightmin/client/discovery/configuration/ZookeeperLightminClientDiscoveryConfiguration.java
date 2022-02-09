package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.zookeeper.discovery.ConditionalOnZookeeperDiscoveryEnabled;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.LightminZookeeperDiscoveryPropertiesMetaDataExtendingBeanPostProcessor;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientDiscoveryCore;

@Configuration
@ConditionalOnZookeeperDiscoveryEnabled
@EnableLightminClientDiscoveryCore
@ConditionalOnClass(ZookeeperDiscoveryClient.class)
@Slf4j
@Import(LightminZookeeperDiscoveryPropertiesMetaDataExtendingBeanPostProcessor.class)
public class ZookeeperLightminClientDiscoveryConfiguration {
}
