package org.cftoolsuite.cfapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;

import org.cftoolsuite.cfapp.domain.Policies;
import org.cftoolsuite.cfapp.domain.QueryPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcPoliciesRepository {

    private final R2dbcEntityOperations dbClient;
    private final PolicyIdProvider idProvider;

    @Autowired
    public R2dbcPoliciesRepository(
            R2dbcEntityOperations dbClient,
            PolicyIdProvider idProvider) {
        this.dbClient = dbClient;
        this.idProvider = idProvider;
    }

    public Mono<Void> deleteAll() {
        return
                dbClient
                .delete(QueryPolicy.class).all()
                .then();
    }

    public Mono<Void> deleteQueryPolicyById(String id) {
        return
                dbClient
                .delete(QueryPolicy.class)
                .matching(org.springframework.data.relational.core.query.Query.query(Criteria.where("id").is(id)))
                .all()
                .then();
    }

    public Mono<Policies> findAll() {
        List<QueryPolicy> queryPolicies = new ArrayList<>();
        return
                Flux
                .from(dbClient.select(QueryPolicy.class).all())
                .map(qp -> queryPolicies.add(qp))
                .then(Mono.just(Policies.builder().queryPolicies(queryPolicies).build()))
                .flatMap(p -> p.isEmpty() ? Mono.empty(): Mono.just(p));
    }

    public Mono<Policies> findAllQueryPolicies() {
        return
                dbClient
                .select(QueryPolicy.class)
                .all()
                .collectList()
                .map(qps -> Policies.builder().queryPolicies(qps).build())
                .flatMap(p -> p.isEmpty() ? Mono.empty(): Mono.just(p));
    }

    public Mono<Policies> findQueryPolicyById(String id) {
        List<QueryPolicy> queryPolicies = new ArrayList<>();
        return
                Flux
                .from(dbClient
                        .select(QueryPolicy.class)
                        .matching(org.springframework.data.relational.core.query.Query.query(Criteria.where("id").is(id)))
                        .all())
                .map(qp -> queryPolicies.add(qp))
                .then(Mono.just(Policies.builder().queryPolicies(queryPolicies).build()))
                .flatMap(p -> p.isEmpty() ? Mono.empty(): Mono.just(p));
    }

    public Mono<Policies> save(Policies entity) {
        List<QueryPolicy> queryPolicies =
                entity.getQueryPolicies().stream()
                .map(p -> idProvider.seedQueryPolicy(p)).collect(Collectors.toList());

        return Flux.fromIterable(queryPolicies)
                .concatMap(this::saveQueryPolicy)
                .then(
                        Mono.just(
                                Policies
                                .builder()
                                .queryPolicies(queryPolicies)
                                .build()
                                )
                        );
    }

    private Mono<QueryPolicy> saveQueryPolicy(QueryPolicy qp) {
        return
                dbClient
                .insert(qp);
    }
}
