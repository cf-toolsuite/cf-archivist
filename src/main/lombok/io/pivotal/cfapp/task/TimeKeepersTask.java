package io.pivotal.cfapp.task;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.event.DatabaseCreatedEvent;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;
import io.pivotal.cfapp.service.TimeKeeperService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class TimeKeepersTask implements ApplicationListener<DatabaseCreatedEvent> {

    private final ArchivistClient client;
    private final TimeKeeperService tkService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public TimeKeepersTask(
            ArchivistClient client,
            TimeKeeperService tkService,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.tkService = tkService;
        this.publisher = publisher;
    }

    public void collect() {
        log.info("TimeKeepersTask started");
        client.getTimeKeepers()
            .flatMapMany(t -> Flux.fromIterable(t.getTimeKeepers()))
            .flatMap(tk -> tkService.save(tk.getFoundation(), tk.getCollectionDateTime()))
            .collect(Collectors.toSet())
            .subscribe(
                result -> {
                    publisher.publishEvent(new TimeKeepersRetrievedEvent(this).timeKeepers(result));
                    log.info("TimeKeepersTask completed");
                    log.trace("Retrieved time keepers are {}", result);
                },
                error -> {
                    log.error("TimeKeepersTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(DatabaseCreatedEvent event) {
        collect();
    }

    @Scheduled(cron = "${cron.collection}")
    protected void runTask() {
        collect();
    }

}
