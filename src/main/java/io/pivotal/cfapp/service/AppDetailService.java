package io.pivotal.cfapp.service;

import java.time.LocalDate;

import io.pivotal.cfapp.domain.AppDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppDetailService {

    Mono<Void> deleteAll();

    Flux<AppDetail> findAll();

    Mono<AppDetail> findByAppId(String appId);

    Flux<AppDetail> findByDateRange(LocalDate start, LocalDate end);

    Mono<AppDetail> save(AppDetail entity);
}
