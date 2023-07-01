package io.pivotal.cfapp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;
import io.pivotal.cfapp.service.SpaceUsersService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class SpaceUsersTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private SpaceUsersService service;

    @Autowired
    public SpaceUsersTask(
            ArchivistClient client,
            SpaceUsersService service) {
        this.client = client;
        this.service = service;
    }

    public void collect() {
        log.info("SpaceUsersTask started");
        client.getSpaceUsers()
            .flatMapMany(Flux::fromIterable)
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    log.info("SpaceUsersTask completed");
                    log.trace("Retrieved {} space user records", result.size());
                },
                error -> {
                    log.error("SpaceUsersTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect();
    }

}
