package org.cftoolsuite.cfapp.repository;

import java.time.LocalDate;

import org.cftoolsuite.cfapp.domain.AppRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcAppRelationshipRepository {

    private final R2dbcEntityOperations client;

    @Autowired
    public R2dbcAppRelationshipRepository(R2dbcEntityOperations client) {
        this.client = client;
    }

    public Mono<Void> deleteAll() {
        return
            client
                .delete(AppRelationship.class)
                .all()
                .then();
    }

    public Flux<AppRelationship> findAll() {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("app_name"), Order.desc("collection_time"));
        return
            client
                .select(AppRelationship.class)
                .matching(Query.empty().sort(order))
                .all();
    }

    public Flux<AppRelationship> findByDateRange(LocalDate start, LocalDate end) {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.asc("app_name"), Order.desc("collection_time"));
        return
            client
                .select(AppRelationship.class)
                .matching(Query.query(CommonCriteria.dateRange("collection_time", start, end)).sort(order))
                .all();
    }

    public Mono<AppRelationship> save(AppRelationship entity) {
        return
            client
                .insert(entity);
    }

}
