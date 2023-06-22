package io.pivotal.cfapp.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "cf")
public class PasSettings {

    public static final String SYSTEM_ORG = "system";
    // user accounts are typically email addresses, so we'll define a regex to match on recognizable email pattern
    // @see https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private static final String DEFAULT_ACCOUNT_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    private boolean sslValidationSkipped;
    private Integer connectionPoolSize;
    private String connectionTimeout;
    private String accountRegex;

    public String getAccountRegex() {
        return StringUtils.isNotBlank(accountRegex) ? accountRegex: DEFAULT_ACCOUNT_REGEX;
    }

}
