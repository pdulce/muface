package com.mfc.infra.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "arch")
public class ArqConfigProperties {


    private boolean eventBrokerActive;
    private String applicationId;

    public boolean isEventBrokerActive() {
        return eventBrokerActive;
    }

    public void setEventBrokerActive(boolean eventbrokerActive) {
        this.eventBrokerActive = eventbrokerActive;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

}
