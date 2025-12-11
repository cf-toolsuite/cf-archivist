package org.cftoolsuite.cfapp.service;

import org.cftoolsuite.cfapp.domain.SpaceUsers;
import org.cftoolsuite.cfapp.repository.R2dbcSpaceUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class R2dbcSpaceUsersService implements SpaceUsersService {

    private final R2dbcSpaceUsersRepository repo;

    @Autowired
    public R2dbcSpaceUsersService(
            R2dbcSpaceUsersRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll() {
        return repo.deleteAll();
    }

    @Override
    public Flux<SpaceUsers> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public Mono<SpaceUsers> save(SpaceUsers entity) {
        return repo
                .save(entity)
                .onErrorContinue(
                        (ex, data) -> log.error(String.format("Problem saving space user %s.", entity), ex));
    }

}
