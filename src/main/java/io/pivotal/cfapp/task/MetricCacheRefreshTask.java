package io.pivotal.cfapp.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.repository.MetricCache;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix="cron", name="enabled", havingValue="true")
public class MetricCacheRefreshTask implements ApplicationRunner {

    private final ArchivistClient archivistClient;
    private final ObjectMapper mapper;
    private final MetricCache cache;


    @Autowired
    public MetricCacheRefreshTask(
        ArchivistClient archivistClient,
        ObjectMapper mapper,
        MetricCache cache) {
        this.archivistClient = archivistClient;
        this.mapper = mapper;
        this.cache = cache;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        refreshCache();
    }

    @Scheduled(cron = "${cron.collection}")
    protected void refreshCache() {
        log.info("MetricCacheRefreshTask started");
        archivistClient
            .getSummary()
                .doOnNext(r -> {
                    log.trace(mapWithException("SnapshotSummary", r));
                    cache.setSnapshotSummary(r);
                })
            .then(archivistClient.getDetail())
                .doOnNext(r -> {
                    log.trace(mapWithException("SnapshotDetail", r));
                    cache.setSnapshotDetail(r);
                })
            .then(archivistClient.getApplicationReport())
                .doOnNext(r -> {
                    log.trace(mapWithException("AppUsageReport", r));
                    cache.setAppUsage(r);
                })
            .then(archivistClient.getServiceReport())
                .doOnNext(r -> {
                    log.trace(mapWithException("ServiceUsageReport", r));
                    cache.setServiceUsage(r);
                })
            .then(archivistClient.getTaskReport())
                .doOnNext(r -> {
                    log.trace(mapWithException("TaskUsageReport", r));
                    cache.setTaskUsage(r);
                })
            .then(archivistClient.getDemographics())
                .doOnNext(r -> {
                    log.trace(mapWithException("Demographics", r));
                    cache.setDemographics(r);
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