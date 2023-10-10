package io.pivotal.cfapp.task;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.domain.TimeKeeper;
import io.pivotal.cfapp.domain.TimeKeeperUtil;
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

    public void collect(Set<TimeKeeper> timeKeepers) {
        log.info("SnapshotDetailTask started");
        TimeKeeperUtil tku = new TimeKeeperUtil(timeKeepers);
        client.getDetail()
            .subscribe(
                result -> {
                    publisher.publishEvent(
                        new SnapshotDetailRetrievedEvent(this)
                            .applications(result.getApplications().stream().map(app -> tku.enrich(app)).collect(Collectors.toList()))
                            .serviceInstances(result.getServiceInstances().stream().map(si -> tku.enrich(si)).collect(Collectors.toList()))
                            .applicationRelationships(result.getApplicationRelationships().stream().map(rel -> tku.enrich(rel)).collect(Collectors.toList())));
                    log.info("SnapshotDetailTask completed");
                },
                error -> {
                    log.error("SnapshotDetailTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect(event.getTimeKeepers());
    }

}
