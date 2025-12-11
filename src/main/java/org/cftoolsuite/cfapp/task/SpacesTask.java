package org.cftoolsuite.cfapp.task;

import java.util.Set;

import org.cftoolsuite.cfapp.client.ArchivistClient;
import org.cftoolsuite.cfapp.domain.TimeKeeper;
import org.cftoolsuite.cfapp.domain.TimeKeeperUtil;
import org.cftoolsuite.cfapp.event.SpacesRetrievedEvent;
import org.cftoolsuite.cfapp.event.TimeKeepersRetrievedEvent;
import org.cftoolsuite.cfapp.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class SpacesTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private final SpaceService service;
    private ApplicationEventPublisher publisher;

    @Autowired
    public SpacesTask(
            ArchivistClient client,
            SpaceService service,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.service = service;
        this.publisher = publisher;
    }

    public void collect(Set<TimeKeeper> timeKeepers) {
        log.info("SpaceTask started");
        TimeKeeperUtil tku = new TimeKeeperUtil(timeKeepers);
        client.getSpaces()
            .flatMapMany(Flux::fromIterable)
            .map(s -> tku.enrich(s))
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new SpacesRetrievedEvent(this).spaces(result));
                    log.info("SpaceTask completed");
                    log.trace("Retrieved {} organizations", result.size());
                },
                error -> {
                    log.error("SpaceTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect(event.getTimeKeepers());
    }

}
