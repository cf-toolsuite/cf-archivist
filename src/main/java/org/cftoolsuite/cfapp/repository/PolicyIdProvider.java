package org.cftoolsuite.cfapp.repository;

import org.cftoolsuite.cfapp.client.GitClient;
import org.cftoolsuite.cfapp.config.GitSettings;
import org.cftoolsuite.cfapp.domain.QueryPolicy;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
