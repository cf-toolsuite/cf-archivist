package io.pivotal.cfapp.repository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import io.pivotal.cfapp.domain.ServiceInstanceDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcServiceInstanceDetailRepository {

    private final R2dbcEntityOperations client;

    @Autowired
    public R2dbcServiceInstanceDetailRepository(R2dbcEntityOperations client) {
        this.client = client;
    }

    public Mono<Void> deleteAll() {
        return
            client
                .delete(ServiceInstanceDetail.class)
                .all()
                .then();
    }

    public Flux<ServiceInstanceDetail> findAll() {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("service"), Order.asc("service_name"), Order.desc("collection_time"));
        return
            client
                .select(ServiceInstanceDetail.class)
                .matching(Query.empty().sort(order))
                .all();
    }

    public Flux<ServiceInstanceDetail> findByDateRange(LocalDate start, LocalDate end) {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("service"), Order.asc("service_name"), Order.desc("collection_time"));
        return
            client
                .select(ServiceInstanceDetail.class)
                .matching(Query.query(CommonCriteria.dateRange("collection_time", start, end)).sort(order))
                .all();
    }

    public Mono<ServiceInstanceDetail> save(ServiceInstanceDetail entity) {
        return
            client
                .insert(entity);
    }
}
