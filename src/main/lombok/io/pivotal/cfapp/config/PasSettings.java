package io.pivotal.cfapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "cf")
public class PasSettings {

    private boolean sslValidationSkipped;
    private Integer connectionPoolSize;
    private String connectionTimeout;

}
