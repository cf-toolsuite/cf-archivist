package org.cftoolsuite.cfapp.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.cftoolsuite.cfapp.domain.AppRelationship;
import org.cftoolsuite.cfapp.event.AppRelationshipRetrievedEvent;
import org.cftoolsuite.cfapp.event.SnapshotDetailRetrievedEvent;
import org.cftoolsuite.cfapp.service.AppRelationshipService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AppRelationshipTask implements ApplicationListener<SnapshotDetailRetrievedEvent> {

    private AppRelationshipService service;
    private ApplicationEventPublisher publisher;

    @Autowired
    public AppRelationshipTask(
            AppRelationshipService service,
            ApplicationEventPublisher publisher
            ) {
        this.service = service;
        this.publisher = publisher;
    }

    public void collect(List<AppRelationship> relationships) {
        log.info("AppRelationshipTask started");
        Flux
            .fromIterable(relationships)
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new AppRelationshipRetrievedEvent(this).relations(result));
                    log.info("AppRelationshipTask completed");
                },
                error -> {
                    log.error("AppRelationshipTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(SnapshotDetailRetrievedEvent event) {
        collect(List.copyOf(event.getApplicationRelationships()));
    }

}
