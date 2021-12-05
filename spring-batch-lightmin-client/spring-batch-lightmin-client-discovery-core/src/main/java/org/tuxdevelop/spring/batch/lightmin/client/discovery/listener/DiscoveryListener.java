package org.tuxdevelop.spring.batch.lightmin.client.discovery.listener;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.util.EventUtil;

/**
 * {@link org.springframework.context.ApplicationListener<ServletWebServerInitializedEvent>}
 * that updated {@link LightminClientProperties} with actual server and management port.
 * @author Marcel Becker
 * @author Vladislav Khakin
 * @since 0.5
 */
public class DiscoveryListener {

    private final LightminClientProperties lightminClientProperties;

    public DiscoveryListener(final LightminClientProperties lightminClientProperties) {
        this.lightminClientProperties = lightminClientProperties;
    }

    @EventListener(value = {ServletWebServerInitializedEvent.class})
    public void onContextRefreshedEvent(ServletWebServerInitializedEvent event) {
        EventUtil.updatePorts(event, this.lightminClientProperties);
    }

}
