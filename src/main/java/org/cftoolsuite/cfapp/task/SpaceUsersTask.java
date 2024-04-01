package org.cftoolsuite.cfapp.task;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.cftoolsuite.cfapp.client.ArchivistClient;
import org.cftoolsuite.cfapp.domain.TimeKeeper;
import org.cftoolsuite.cfapp.domain.TimeKeeperUtil;
import org.cftoolsuite.cfapp.event.SpaceUsersRetrievedEvent;
import org.cftoolsuite.cfapp.event.TimeKeepersRetrievedEvent;
import org.cftoolsuite.cfapp.service.SpaceUsersService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class SpaceUsersTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private SpaceUsersService service;
    private ApplicationEventPublisher publisher;

    @Autowired
    public SpaceUsersTask(
            ArchivistClient client,
            SpaceUsersService service,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.service = service;
        this.publisher = publisher;
    }

    public void collect(Set<TimeKeeper> timeKeepers) {
        log.info("SpaceUsersTask started");
        TimeKeeperUtil tku = new TimeKeeperUtil(timeKeepers);
        client.getSpaceUsers()
            .flatMapMany(Flux::fromIterable)
            .map(su -> tku.enrich(su))
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new SpaceUsersRetrievedEvent(this).spaceUsers(result));
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
        collect(event.getTimeKeepers());
    }

}
