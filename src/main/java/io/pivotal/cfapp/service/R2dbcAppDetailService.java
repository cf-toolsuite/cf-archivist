package io.pivotal.cfapp.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pivotal.cfapp.domain.AppDetail;
import io.pivotal.cfapp.repository.R2dbcAppDetailRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class R2dbcAppDetailService implements AppDetailService {

    private R2dbcAppDetailRepository repo;

    @Autowired
    public R2dbcAppDetailService(R2dbcAppDetailRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll() {
        return repo.deleteAll();
    }

    @Override
    public Flux<AppDetail> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public Mono<AppDetail> save(AppDetail entity) {
        return repo
                .save(entity)
                .onErrorContinue(
                        (ex, data) -> log.error(String.format("Problem saving application %s.", entity), ex));
    }

}
