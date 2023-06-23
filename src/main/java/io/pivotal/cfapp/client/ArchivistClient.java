package io.pivotal.cfapp.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.pivotal.cfapp.domain.Demographics;
import io.pivotal.cfapp.domain.SnapshotDetail;
import io.pivotal.cfapp.domain.SnapshotSummary;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ArchivistClient {

    private final WebClient client;

    @Autowired
    public ArchivistClient(WebClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "hooverClient.detail", fallbackMethod = "fallbackForDetail")
    public Mono<SnapshotDetail> getDetail() {
        return client
                .get()
                    .uri("/snapshot/detail")
                    .retrieve()
                    .bodyToMono(SnapshotDetail.class);
    }

    protected Mono<SnapshotDetail> fallbackForDetail(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/detail", e);
        return Mono.just(SnapshotDetail.builder().build());
    }

    @CircuitBreaker(name = "hooverClient.summary", fallbackMethod = "fallbackForSummary")
    public Mono<SnapshotSummary> getSummary() {
        return client
                .get()
                    .uri("/snapshot/summary")
                    .retrieve()
                    .bodyToMono(SnapshotSummary.class);
    }

    protected Mono<SnapshotSummary> fallbackForSummary(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/summary", e);
        return Mono.just(SnapshotSummary.builder().build());
    }

    @CircuitBreaker(name = "hooverClient.demographics", fallbackMethod = "fallbackForDemographics")
    public Mono<Demographics> getDemographics() {
        return client
                .get()
                    .uri("/snapshot/demographics")
                    .retrieve()
                    .bodyToMono(Demographics.class);
    }

    protected Mono<Demographics> fallbackForDemographics(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/demographics", e);
        return Mono.just(Demographics.builder().build());
    }

}
