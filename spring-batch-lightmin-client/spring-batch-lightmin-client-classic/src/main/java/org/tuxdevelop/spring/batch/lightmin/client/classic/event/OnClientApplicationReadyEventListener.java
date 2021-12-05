package org.tuxdevelop.spring.batch.lightmin.client.classic.event;


import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientApplicationRegistrationService;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.util.EventUtil;

public class OnClientApplicationReadyEventListener implements ApplicationListener<ServletWebServerInitializedEvent> {


    private final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService;
    private final LightminClientProperties lightminClientProperties;

    public OnClientApplicationReadyEventListener(
            final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService,
            final LightminClientProperties lightminClientProperties) {
        this.lightminClientApplicationRegistrationService = lightminClientApplicationRegistrationService;
        this.lightminClientProperties = lightminClientProperties;
    }

    @Override
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        EventUtil.updatePorts(event, this.lightminClientProperties);
        this.lightminClientApplicationRegistrationService.startRegisterTask();
    }
}
