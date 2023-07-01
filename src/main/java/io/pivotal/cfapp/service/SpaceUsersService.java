package io.pivotal.cfapp.service;

import io.pivotal.cfapp.domain.SpaceUsers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceUsersService {

    Mono<Void> deleteAll();

    Flux<SpaceUsers> findAll();

    Mono<SpaceUsers> save(SpaceUsers entity);

}
