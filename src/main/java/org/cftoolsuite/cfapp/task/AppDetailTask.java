package org.cftoolsuite.cfapp.task;

import java.util.List;

import org.cftoolsuite.cfapp.domain.AppDetail;
import org.cftoolsuite.cfapp.event.AppDetailRetrievedEvent;
import org.cftoolsuite.cfapp.event.SnapshotDetailRetrievedEvent;
import org.cftoolsuite.cfapp.service.AppDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AppDetailTask implements ApplicationListener<SnapshotDetailRetrievedEvent> {

    private final AppDetailService service;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public AppDetailTask(
            AppDetailService service,
            ApplicationEventPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    public void collect(List<AppDetail> applications) {
        log.info("AppDetailTask started");
        Flux
            .fromIterable(applications)
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new AppDetailRetrievedEvent(this).detail(result));
                    log.info("AppDetailTask completed");
                },
                error -> {
                    log.error("AppDetailTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(SnapshotDetailRetrievedEvent event) {
        collect(List.copyOf(event.getApplications()));
    }

}
