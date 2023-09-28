package io.pivotal.cfapp.service;

import io.pivotal.cfapp.domain.AppRelationship;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppRelationshipService {

    Mono<Void> deleteAll();

    Flux<AppRelationship> findAll();

    Mono<AppRelationship> save(AppRelationship entity);
}
