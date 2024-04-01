package org.cftoolsuite.cfapp.service;

import org.cftoolsuite.cfapp.domain.Policies;
import reactor.core.publisher.Mono;

public interface PoliciesService {

    Mono<Void> deleteAll();
    Mono<Void> deleteQueryPolicyById(String id);
    Mono<Policies> findAll();
    Mono<Policies> findAllQueryPolicies();
    Mono<Policies> findQueryPolicyById(String id);
    Mono<Policies> save(Policies entity);
}
