package org.cftoolsuite.cfapp.service;

import java.time.LocalDateTime;

import org.cftoolsuite.cfapp.domain.TimeKeeper;
import org.cftoolsuite.cfapp.repository.R2dbcTimeKeeperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TimeKeeperService {

    private final R2dbcTimeKeeperRepository repo;

    @Autowired
    public TimeKeeperService(R2dbcTimeKeeperRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Mono<Void> deleteAll() {
        return repo.deleteAll();
    }

    public Flux<TimeKeeper> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Mono<TimeKeeper> save(String foundation, LocalDateTime collectionDateTime) {
        return
                repo
                .save(foundation, collectionDateTime)
                .onErrorContinue(
                        (ex, data) -> log.error(String.format("Problem saving collection date/time %s from %s.", collectionDateTime, foundation), ex));
    }
}
