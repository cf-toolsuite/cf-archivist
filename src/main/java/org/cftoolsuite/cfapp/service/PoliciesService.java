package org.cftoolsuite.cfapp.service;

import java.util.Map;

import org.cftoolsuite.cfapp.domain.Policies;
import org.cftoolsuite.cfapp.task.PolicyExecutorTask;

import reactor.core.publisher.Mono;

public interface PoliciesService {

    Mono<Void> deleteAll();
    Mono<Void> deleteQueryPolicyById(String id);
    Mono<Policies> findAll();
    Mono<Policies> findAllQueryPolicies();
    Mono<Policies> findQueryPolicyById(String id);
    Mono<Policies> save(Policies entity);
    Mono<Map<String, Class<? extends PolicyExecutorTask>>> getTaskMap();
}
