package io.pivotal.cfapp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.event.SnapshotDetailRetrievedEvent;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SnapshotDetailTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public SnapshotDetailTask(
            ArchivistClient client,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.publisher = publisher;
    }

    public void collect() {
        log.info("SnapshotDetailTask started");
        client.getDetail()
            .subscribe(
                result -> {
                    publisher.publishEvent(
                        new SnapshotDetailRetrievedEvent(this)
                            .applications(result.getApplications())
                            .serviceInstances(result.getServiceInstances())
                            .applicationRelationships(result.getApplicationRelationships()));
                    log.info("SnapshotDetailTask completed");
                },
                error -> {
                    log.error("SnapshotDetailTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect();
    }

}
