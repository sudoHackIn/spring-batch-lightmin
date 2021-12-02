package org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation;


import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration.ZookeeperLightminClientDiscoveryConfiguration;

import java.lang.annotation.*;

/**
 * @author Khakin Vladislav
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ZookeeperLightminClientDiscoveryConfiguration.class})
public @interface EnableLightminClientZookeeper {
}
