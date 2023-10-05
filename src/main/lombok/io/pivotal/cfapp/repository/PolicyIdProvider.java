package io.pivotal.cfapp.repository;

import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.GitClient;
import io.pivotal.cfapp.config.GitSettings;
import io.pivotal.cfapp.domain.QueryPolicy;

@Component
public class PolicyIdProvider {

    private final GitSettings settings;
    private final String commit;

    public PolicyIdProvider(
            GitSettings settings,
            @Autowired(required = false) GitClient client
            ) {
        this.settings = settings;
        if (client != null && settings.isVersionManaged()) {
            Repository repo = client.getRepository(settings);
            this.commit =
                    settings.isPinnedCommit()
                    ? settings.getCommit()
                            : client.orLatestCommit(settings.getCommit(), repo);
        } else {
            this.commit = settings.getCommit();
        }
    }

    public QueryPolicy seedQueryPolicy(QueryPolicy policy) {
        return settings.isVersionManaged() ? QueryPolicy.seedWith(policy, commit): QueryPolicy.seed(policy);
    }

}
