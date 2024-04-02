package org.cftoolsuite.cfapp.service;

import org.cftoolsuite.cfapp.domain.ServiceInstanceDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceInstanceDetailService {

    Mono<Void> deleteAll();

    Flux<ServiceInstanceDetail> findAll();

    Mono<ServiceInstanceDetail> save(ServiceInstanceDetail entity);

}
