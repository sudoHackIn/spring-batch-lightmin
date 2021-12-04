package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.repository.*;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {RemoteJobConfigurationRepositoryConfigurationProperties.class})
public class RemoteJobConfigurationRepositoryConfiguration {

    private final RemoteJobConfigurationRepositoryConfigurationProperties properties;

    private RestTemplate restTemplate;

    public RemoteJobConfigurationRepositoryConfiguration(final RemoteJobConfigurationRepositoryConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JobConfigurationRepository remoteJobConfigurationRepository(RemoteJobConfigurationRepositoryLocator locator) {
        configureRestTemplate();
        return new RemoteJobConfigurationRepository(this.properties, this.restTemplate, locator);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.repository.remote",
            value = "discover-remote-repository",
            havingValue = "false", matchIfMissing = true)
    public RemoteJobConfigurationRepositoryLocator urlRemoteJobConfigurationRepositoryLocator() {
        return new UrlRemoteJobConfigurationRepositoryLocator(this.properties);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.repository.remote",
            value = "discover-remote-repository",
            havingValue = "true")
    public RemoteJobConfigurationRepositoryLocator discoveryRemoteJobConfigurationRepositoryLocator(
            final DiscoveryClient discoveryClient) {
        return new DiscoveryRemoteJobConfigurationRepositoryLocator(this.properties, discoveryClient);
    }

    @Autowired(required = false)
    protected void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected void configureRestTemplate() {
        if (this.restTemplate != null) {
            log.info("RestTemplate already configured for RemoteJobConfigurationRepository");
        } else {
            final String userName = this.properties.getUsername();
            final String password = this.properties.getPassword();
            final RestTemplate restTemplate = new RestTemplate();
            if (StringUtils.hasText(userName)) {
                restTemplate.setInterceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(userName, password)
                        )
                );
            }
            this.restTemplate = restTemplate;
        }
    }
}
