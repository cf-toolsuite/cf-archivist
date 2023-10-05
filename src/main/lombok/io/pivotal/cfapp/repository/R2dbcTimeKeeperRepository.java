package io.pivotal.cfapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import io.pivotal.cfapp.domain.TimeKeeper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcTimeKeeperRepository {

    private final R2dbcEntityOperations client;

    public R2dbcTimeKeeperRepository(R2dbcEntityOperations client) {
        this.client = client;
    }

    public Mono<Void> deleteAll() {
        return
            client
                .delete(TimeKeeper.class)
                .all()
                .then();
    }

    public Flux<TimeKeeper> findAll() {
        Sort order = Sort.by(Order.asc("foundation"), Order.desc("collection_time"));
        return
            client
                .select(TimeKeeper.class)
                .matching(Query.empty().sort(order))
                .all();
    }

    public Flux<TimeKeeper> findEarliestCollectionDateTimeByFoundation() {
        String sql = "SELECT foundation, MIN(collection_time) AS collection_time FROM timekeeper GROUP BY foundation";
        return
            client
                .getDatabaseClient()
                .sql(sql)
                .map((row, metadata)
                        -> TimeKeeper.builder()
                            .foundation(row.get("foundation", String.class))
                            .collectionDateTime(row.get("collection_time", LocalDateTime.class))
                            .build())
                .all();
    }

    public Flux<TimeKeeper> findLatestCollectionDateTimeByFoundation() {
        String sql = "SELECT foundation, MAX(collection_time) AS collection_time FROM timekeeper GROUP BY foundation";
        return
            client
                .getDatabaseClient()
                .sql(sql)
                .map((row, metadata)
                        -> TimeKeeper.builder()
                            .foundation(row.get("foundation", String.class))
                            .collectionDateTime(row.get("collection_time", LocalDateTime.class))
                            .build())
                .all();
    }

    public Flux<TimeKeeper> findByDateRange(LocalDate start, LocalDate end) {
        return
            client
                .select(TimeKeeper.class)
                .matching(Query.query(CommonCriteria.dateRange("collection_time", start, end)))
                .all();
    }

    public Mono<TimeKeeper> save(String foundation, LocalDateTime collectionDateTime) {
        return
            client
                .insert(TimeKeeper.builder().foundation(foundation).collectionDateTime(collectionDateTime).build());
    }
}
