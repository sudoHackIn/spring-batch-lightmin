package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.zookeeper.discovery.ConditionalOnZookeeperDiscoveryEnabled;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientDiscoveryCore;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.ZookeeperMetadataExtender;

@Configuration
@ConditionalOnZookeeperDiscoveryEnabled
@EnableLightminClientDiscoveryCore
@ConditionalOnClass(ZookeeperDiscoveryClient.class)
public class ZookeeperLightminClientDiscoveryConfiguration {

    @Bean
    public MetaDataExtender metaDataExtender(final ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        return new ZookeeperMetadataExtender(zookeeperDiscoveryProperties);
    }

    @Bean
    @ConditionalOnMissingBean({ZookeeperRegistration.class})
    public ServiceInstanceRegistration serviceInstanceRegistration(ApplicationContext context, ZookeeperDiscoveryProperties properties, MetaDataExtender extender) {
        extender.extendMetaData();
        String appName = context.getEnvironment().getProperty("spring.application.name", "application");
        String host = properties.getInstanceHost();
        if (!StringUtils.hasText(host)) {
            throw new IllegalStateException("instanceHost must not be empty");
        } else {
            properties.getMetadata().put("instance_status", properties.getInitialStatus());
            ZookeeperInstance zookeeperInstance = new ZookeeperInstance(context.getId(), appName, properties.getMetadata());
            ServiceInstanceRegistration.RegistrationBuilder builder = ServiceInstanceRegistration.builder().address(host).name(appName).payload(zookeeperInstance).uriSpec(properties.getUriSpec());
            if (properties.getInstanceSslPort() != null) {
                builder.sslPort(properties.getInstanceSslPort());
            }

            if (properties.getInstanceId() != null) {
                builder.id(properties.getInstanceId());
            }

            return builder.build();
        }
    }
}
