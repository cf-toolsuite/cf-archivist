package io.pivotal.cfapp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.pivotal.cfapp.repository.MetricCache;
import io.pivotal.cfapp.service.SnapshotService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix="cron", name="enabled", havingValue="true")
public class MetricCacheRefreshTask implements ApplicationListener<ApplicationEvent> {

    private final MetricCacheReadyToBeRefreshedDecider decider;
    private final ObjectMapper mapper;
    private final MetricCache cache;
    private final SnapshotService service;

    @Autowired
    public MetricCacheRefreshTask(
        MetricCacheReadyToBeRefreshedDecider decider,
        ObjectMapper mapper,
        MetricCache cache,
        SnapshotService service) {
        this.decider = decider;
        this.mapper = mapper;
        this.cache = cache;
        this.service = service;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        decider.informReadinessDecision(event);
        if (decider.isReady()) {
            refreshCache();
            decider.reset();
        }
    }

    private void refreshCache() {
        log.info("MetricCacheRefreshTask started");
        service
            .assembleSnapshotDetail()
                .doOnNext(r -> {
                    log.trace(mapWithException("SnapshotDetail", r));
                    cache.setSnapshotDetail(r);
                })
            .subscribe(e -> log.info("MetricCacheRefreshTask completed"));
    }

    private String mapWithException(String type, Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("Problem mapping " + type);
        }
    }

}