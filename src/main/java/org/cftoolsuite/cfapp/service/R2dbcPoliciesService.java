package org.cftoolsuite.cfapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import org.cftoolsuite.cfapp.config.GitSettings;
import org.cftoolsuite.cfapp.domain.Policies;
import org.cftoolsuite.cfapp.domain.QueryPolicy;
import org.cftoolsuite.cfapp.repository.R2dbcPoliciesRepository;
import org.cftoolsuite.cfapp.task.PolicyExecutorTask;
import org.cftoolsuite.cfapp.task.QueryPolicyExecutorTask;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class R2dbcPoliciesService implements PoliciesService {

    private static final String UNSUPPORTED_OP_MESSAGE = "Policies are managed in a git repository.";

    private final R2dbcPoliciesRepository repo;
    private final GitSettings settings;

    public R2dbcPoliciesService(
            R2dbcPoliciesRepository repo,
            GitSettings settings) {
        this.repo = repo;
        this.settings = settings;
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll() {
        return repo.deleteAll();
    }

    @Override
    @Transactional
    public Mono<Void> deleteQueryPolicyById(String id) {
        if (settings.isVersionManaged()) {
            throw new UnsupportedOperationException(UNSUPPORTED_OP_MESSAGE);
        }
        return repo.deleteQueryPolicyById(id);
    }

    @Override
    public Mono<Policies> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Policies> findAllQueryPolicies() {
        return repo.findAllQueryPolicies();
    }

    @Override
    public Mono<Policies> findQueryPolicyById(String id) {
        return repo.findQueryPolicyById(id);
    }

    @Override
    @Transactional
    public Mono<Policies> save(Policies entity) {
        return repo.save(entity);
    }

    @Override
    public Mono<Map<String, Class<? extends PolicyExecutorTask>>> getTaskMap() {
        Mono<Policies> policiesMono = repo.findAll();
        Flux<Map<String, Class<? extends PolicyExecutorTask>>> mapsFlux = Flux.merge(
            policiesMono
                .flatMapMany(p -> Flux.fromIterable(p.getQueryPolicies()))
                .collectMap(QueryPolicy::getId, qp -> QueryPolicyExecutorTask.class)
        );
        return
            mapsFlux
                .reduce(
                    new HashMap<String, Class<? extends PolicyExecutorTask>>(), (acc, map) -> {
                        acc.putAll(map);
                        return acc;
                    }
                );
    }

}
