package io.pivotal.cfapp.config;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "cf.policies.git")
public class GitSettings {

    private String uri = "";
    private String username;
    private String password = "";
    private String commit;
    private Set<String> filePaths;

    public boolean isAuthenticated() {
        return StringUtils.isNotBlank(getUsername());
    }

    public boolean isPinnedCommit() {
        return StringUtils.isNotBlank(getCommit());
    }

    public boolean isVersionManaged() {
        return StringUtils.isNotBlank(uri);
    }

}
