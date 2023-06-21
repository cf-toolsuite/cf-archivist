package io.pivotal.cfapp.config;

import java.util.Map;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfService;
import io.pivotal.cfenv.spring.boot.CfEnvProcessor;
import io.pivotal.cfenv.spring.boot.CfEnvProcessorProperties;

public class ArchivistCfEnvProcessor implements CfEnvProcessor {

    private static final String SERVICE_NAME = "cf-archivist-secrets";

    private static void addOrUpdatePropertyValue(String propertyName, String credentialName, CfCredentials cfCredentials, Map<String, Object> properties) {
        Object credential = cfCredentials.getMap().get(credentialName);
        if (credential != null) {
            properties.put(propertyName, credential);
        }
    }

    private static void addPropertyValue(String propertyName, Object propertyValue, Map<String, Object> properties) {
        properties.put(propertyName, propertyValue);
    }

    @Override
    public boolean accept(CfService service) {
        return
                service.getName().equalsIgnoreCase(SERVICE_NAME);
    }

    @Override
    public CfEnvProcessorProperties getProperties() {
        return
                CfEnvProcessorProperties
                .builder()
                .serviceName(SERVICE_NAME)
                .build();
    }

    @Override
    public void process(CfCredentials cfCredentials, Map<String, Object> properties) {
        addPropertyValue("credhub.url", "https://credhub.service.cf.internal:8844", properties);
        addOrUpdatePropertyValue("spring.mail.host", "MAIL_HOST", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.mail.port", "MAIL_PORT", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.mail.username", "MAIL_USERNAME", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.mail.password", "MAIL_PASSWORD", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.mail.properties.mail.smtp.auth", "MAIL_SMTP_AUTH_ENABLED", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.mail.properties.mail.smtp.starttls.enable", "MAIL_SMTP_STARTTLS_ENABLED", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.sendgrid.api-key", "SENDGRID_API-KEY", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.r2dbc.url", "R2DBC_URL", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.r2dbc.username", "R2DBC_USERNAME", cfCredentials, properties);
        addOrUpdatePropertyValue("spring.r2dbc.password", "R2DBC_PASSWORD", cfCredentials, properties);
        addOrUpdatePropertyValue("notification.engine", "NOTIFICATION_ENGINE", cfCredentials, properties);
        addOrUpdatePropertyValue("cf.policies.git.uri", "CF_POLICIES_GIT_URI", cfCredentials, properties);
        addOrUpdatePropertyValue("cf.policies.git.username", "CF_POLICIES_GIT_USERNAME", cfCredentials, properties);
        addOrUpdatePropertyValue("cf.policies.git.password", "CF_POLICIES_GIT_PASSWORD", cfCredentials, properties);
        addOrUpdatePropertyValue("cf.policies.git.commit", "CF_POLICIES_GIT_COMMIT", cfCredentials, properties);
        addOrUpdatePropertyValue("cf.policies.git.filePaths", "CF_POLICIES_GIT_FILE-PATHS", cfCredentials, properties);
        addOrUpdatePropertyValue("pivnet.enabled", "PIVNET_ENABLED", cfCredentials, properties);
        addOrUpdatePropertyValue("cron.collection", "CRON_COLLECTION", cfCredentials, properties);
        addOrUpdatePropertyValue("cron.collection", "CRON_EXECUTION", cfCredentials, properties);
        addOrUpdatePropertyValue("management.endpoints.web.exposure.include", "EXPOSED_ACTUATOR_ENDPOINTS", cfCredentials, properties);
    }
}
