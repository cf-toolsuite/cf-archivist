package io.pivotal.cfapp.task;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.pivotal.cfapp.domain.TimeKeepers;
import io.pivotal.cfapp.event.MetricCacheRefreshedEvent;
import io.pivotal.cfapp.repository.MetricCache;
import io.pivotal.cfapp.service.SnapshotService;
import io.pivotal.cfapp.service.TimeKeeperService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MetricCacheRefreshTask implements ApplicationListener<ApplicationEvent> {

    private final MetricCacheReadyToBeRefreshedDecider decider;
    private final ObjectMapper mapper;
    private final MetricCache cache;
    private final SnapshotService snapshotService;
    private final TimeKeeperService tkService;
    private ApplicationEventPublisher publisher;

    @Autowired
    public MetricCacheRefreshTask(
        MetricCacheReadyToBeRefreshedDecider decider,
        ObjectMapper mapper,
        MetricCache cache,
        SnapshotService snapshotService,
        TimeKeeperService tkService,
        ApplicationEventPublisher publisher) {
        this.decider = decider;
        this.mapper = mapper;
        this.cache = cache;
        this.snapshotService = snapshotService;
        this.tkService = tkService;
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.trace("MetricCacheRefreshTask alerted on {}", event.getClass().getName());
        decider.informReadinessDecision(event);
        if (decider.isReady()) {
            refreshCache();
            decider.reset();
        } else {
            log.trace("... and waiting on remaining collection tasks to complete");
        }
    }

    private void refreshCache() {
        log.info("MetricCacheRefreshTask started");
        snapshotService
            .assembleSnapshotDetail()
                .doOnNext(r -> {
                    log.trace(mapWithException("SnapshotDetail", r));
                    cache.setSnapshotDetail(r);
                })
            .thenMany(tkService
                    .findAll()
                    .collect(Collectors.toSet())
                        .doOnNext(r -> {
                        log.trace(mapWithException("TimeKeepers", r));
                        cache.setTimeKeepers(TimeKeepers.builder().timeKeepers(r).build());
                })
            )
            .subscribe(
                result -> {
                    publisher.publishEvent(new MetricCacheRefreshedEvent(this));
                    log.info("MetricCacheRefreshTask completed");
                },
                error -> {
                    log.error("MetricCacheRefreshTask terminated with error", error);
                }
            );

    }

    private String mapWithException(String type, Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("Problem mapping " + type);
        }
    }

}