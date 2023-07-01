package io.pivotal.cfapp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.event.OrganizationsRetrievedEvent;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;
import io.pivotal.cfapp.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class OrganizationsTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private final OrganizationService service;
    private ApplicationEventPublisher publisher;

    @Autowired
    public OrganizationsTask(
            ArchivistClient client,
            OrganizationService service,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.service = service;
        this.publisher = publisher;
    }

    public void collect() {
        log.info("OrganizationTask started");
        client.getOrganizations()
            .flatMapMany(Flux::fromIterable)
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new OrganizationsRetrievedEvent(this).organizations(result));
                    log.info("OrganizationTask completed");
                    log.trace("Retrieved {} organizations", result.size());
                },
                error -> {
                    log.error("OrganizationTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect();
    }

}
