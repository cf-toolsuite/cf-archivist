package io.pivotal.cfapp.repository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import io.pivotal.cfapp.domain.AppDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcAppDetailRepository {

    private final R2dbcEntityOperations client;

    @Autowired
    public R2dbcAppDetailRepository(R2dbcEntityOperations client) {
        this.client = client;
    }

    public Mono<Void> deleteAll() {
        return
            client
                .delete(AppDetail.class)
                .all()
                .then();
    }

    public Flux<AppDetail> findAll() {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("app_name"), Order.desc("collection_time"));
        return
            client
                .select(AppDetail.class)
                .matching(Query.empty().sort(order))
                .all();
    }

    public Flux<AppDetail> findByDateRange(LocalDate start, LocalDate end) {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("app_name"), Order.desc("collection_time"));
        return
            client
                .select(AppDetail.class)
                .matching(Query.query(CommonCriteria.dateRange("collection_time", start, end)).sort(order))
                .all();
    }

    public Mono<AppDetail> save(AppDetail entity) {
        return
            client
                .insert(entity);
    }

}
