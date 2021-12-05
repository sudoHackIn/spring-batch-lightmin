package org.tuxdevelop.spring.batch.lightmin.client.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;

/**
 * Utility class, that update {@link LightminClientProperties} with registered ports from {@link WebServer}
 *
 * @author Marcel Becker
 * @author Vladiskav Khakin
 * @since 0.5
 */
@Slf4j
public final class EventUtil {

    private EventUtil() {
    }

    public static void updatePorts(final ServletWebServerInitializedEvent event, final LightminClientProperties lightminClientProperties) {
        ServletWebServerApplicationContext context = event.getApplicationContext();
        WebServer webServer = event.getWebServer();
        // if management.server.port contains different from server.port value, will be created separated WebApplicationContext
        if ("management".equals(((ConfigurableWebServerApplicationContext) context).getServerNamespace())) {
            Integer managementPort = webServer.getPort();
            log.info("Using management port {} for lightmin  client application registration", managementPort);
            lightminClientProperties.setManagementPort(managementPort);
        } else {
            Integer serverPort = webServer.getPort();
            log.info("Using server port {} for lightmin  client application registration", serverPort);
            lightminClientProperties.setServerPort(serverPort);
            if (lightminClientProperties.getManagementPort() == null) {
                lightminClientProperties.setManagementPort(serverPort);
            }
        }
    }
}
