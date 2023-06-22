package io.pivotal.cfapp.service;

import java.time.LocalDate;

import io.pivotal.cfapp.domain.ServiceInstanceDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceInstanceDetailService {

    Mono<Void> deleteAll();

    Flux<ServiceInstanceDetail> findAll();

    Flux<ServiceInstanceDetail> findByDateRange(LocalDate start, LocalDate end);

    Mono<ServiceInstanceDetail> save(ServiceInstanceDetail entity);

}
