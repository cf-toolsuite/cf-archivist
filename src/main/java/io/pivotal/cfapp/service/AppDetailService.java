package io.pivotal.cfapp.service;

import io.pivotal.cfapp.domain.AppDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppDetailService {

    Mono<Void> deleteAll();

    Flux<AppDetail> findAll();

    Mono<AppDetail> save(AppDetail entity);
}
