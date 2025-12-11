package org.cftoolsuite.cfapp.repository;

import java.time.LocalDate;

import org.cftoolsuite.cfapp.domain.SpaceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcSpaceUsersRepository {

    private final R2dbcEntityOperations client;

    @Autowired
    public R2dbcSpaceUsersRepository(R2dbcEntityOperations client) {
        this.client = client;
    }

    public Mono<Void> deleteAll() {
        return
            client
                .delete(SpaceUsers.class)
                .all()
                .then();
    }

    public Flux<SpaceUsers> findAll() {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.desc("collection_time"));
        return
            client
                .select(SpaceUsers.class)
                .matching(Query.empty().sort(order))
                .all();
    }

    public Flux<SpaceUsers> findByDateRange(LocalDate start, LocalDate end) {
        Sort order = Sort.by(Order.asc("organization"), Order.asc("space"), Order.desc("collection_time"));
        return
            client
                .select(SpaceUsers.class)
                .matching(Query.query(CommonCriteria.dateRange("collection_time", start, end)).sort(order))
                .all();
    }

    public Mono<SpaceUsers> save(SpaceUsers entity) {
        return
            client
                .insert(entity);
    }

}
