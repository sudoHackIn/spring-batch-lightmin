package org.tuxdevelop.spring.batch.lightmin.client.discovery;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.ZookeeperMetadataExtender;

public class LightminZookeeperDiscoveryPropertiesMetaDataExtendingBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ZookeeperDiscoveryProperties) {
            new ZookeeperMetadataExtender((ZookeeperDiscoveryProperties) bean).extendMetaData();
        }
        return bean;
    }
}