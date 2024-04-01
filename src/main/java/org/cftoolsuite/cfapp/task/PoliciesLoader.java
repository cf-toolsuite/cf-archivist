package org.cftoolsuite.cfapp.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.cftoolsuite.cfapp.client.GitClient;
import org.cftoolsuite.cfapp.config.GitSettings;
import org.cftoolsuite.cfapp.domain.Policies;
import org.cftoolsuite.cfapp.domain.PoliciesValidator;
import org.cftoolsuite.cfapp.domain.QueryPolicy;
import org.cftoolsuite.cfapp.event.DatabaseCreatedEvent;
import org.cftoolsuite.cfapp.service.PoliciesService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(
    prefix = "cf.policies.git", name = "uri"
)
public class PoliciesLoader implements ApplicationListener<DatabaseCreatedEvent> {

    private static final String QUERY_POLICY_SUFFIX = "-QP.json";

    private final GitClient client;
    private final PoliciesService service;
    private final GitSettings settings;
    private final PoliciesValidator validator;
    private final ObjectMapper mapper;

    @Autowired
    public PoliciesLoader(
            GitClient client,
            PoliciesService service,
            GitSettings settings,
            PoliciesValidator validator,
            ObjectMapper mapper
            ) {
        this.client = client;
        this.service = service;
        this.settings = settings;
        this.validator = validator;
        this.mapper = mapper;
    }

    public void load() {
        log.info("PoliciesLoader started");
        Repository repo = client.getRepository(settings);
        if (repo != null) {
            String uri = settings.getUri();
            List<QueryPolicy> queryPolicies = new ArrayList<>();
            String commit = client.orLatestCommit(settings.getCommit(), repo);
            log.info("-- Fetching policies from {} using commit {}", uri, commit);
            settings
            .getFilePaths()
            .stream()
            .filter(fp -> !fp.startsWith("#"))
            .forEach(fp -> {
                String fileContent;
                try {
                    fileContent = client.readFile(repo, commit, fp);
                    if (fp.endsWith(QUERY_POLICY_SUFFIX)) {
                        QueryPolicy policy = mapper.readValue(fileContent, QueryPolicy.class);
                        if (validator.validate(policy)) {
                            queryPolicies.add(policy);
                        }
                    } else {
                        log.warn(
                                "Policy file {} does not adhere to naming convention. File name must end with one of {}.",
                                fp, List.of(QUERY_POLICY_SUFFIX));
                    }
                } catch (IOException e1) {
                    log.warn("Could not read {} from {} with commit {} ", fp, uri, commit);
                }
            });
            service
            .deleteAll()
            .then(service.save(
                    Policies
                    .builder()
                    .queryPolicies(queryPolicies)
                    .build()
                    ))
            .subscribe(
                    result -> {
                        log.info("PoliciesLoader completed");
                        log.info(
                                String.format("-- Loaded  %d query policies.",
                                        result.getQueryPolicies().size()
                                        )
                                );
                    },
                    error -> {
                        log.error("PoliciesLoader terminated with error", error);
                    }
                    );
        } else {
            log.error("PoliciesLoader terminated because it could not connect to Git repository");
        }
    }

    @Override
    public void onApplicationEvent(DatabaseCreatedEvent event) {
        load();
    }

}
