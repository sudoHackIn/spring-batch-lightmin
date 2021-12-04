package org.tuxdevelop.test.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientConsul;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableLightminClientConsul
@EnableLightminMapConfigurationRepository
public class ConsulITConfiguration {

    public static void main(final String[] args) {
        SpringApplication.run(ConsulITConfiguration.class, args);
    }

}
