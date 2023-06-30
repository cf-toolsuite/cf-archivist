package io.pivotal.cfapp.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.pivotal.cfapp.domain.Demographics;
import io.pivotal.cfapp.domain.Organization;
import io.pivotal.cfapp.domain.SnapshotDetail;
import io.pivotal.cfapp.domain.SnapshotSummary;
import io.pivotal.cfapp.domain.Space;
import io.pivotal.cfapp.domain.SpaceUsers;
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

    @CircuitBreaker(name = "hooverClient.organizations", fallbackMethod = "fallbackForOrganizations")
    public Mono<List<Organization>> getOrganizations() {
        String uri = "/snapshot/organizations";
        return client
                .get()
                    .uri(uri)
                    .retrieve()
                    .toEntityList(Organization.class)
                    .map(response -> response.getBody())
                    .onErrorResume(
                        WebClientResponseException.class,
                        e -> {
                            log.warn("Could not obtain organizations from {}", uri);
                            return Mono.empty();
                        }
                    );
    }

    protected Mono<List<Space>> fallbackForOrganizations(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/organizations", e);
        return Mono.just(List.of(Space.builder().build()));
    }

    @CircuitBreaker(name = "hooverClient.spaces", fallbackMethod = "fallbackForSpaces")
    public Mono<List<Space>> getSpaces() {
        String uri = "/snapshot/spaces";
        return client
                .get()
                    .uri(uri)
                    .retrieve()
                    .toEntityList(Space.class)
                    .map(response -> response.getBody())
                    .onErrorResume(
                        WebClientResponseException.class,
                        e -> {
                            log.warn("Could not obtain organizations from {}", uri);
                            return Mono.empty();
                        }
                    );
    }

    protected Mono<List<Space>> fallbackForSpaces(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/spaces", e);
        return Mono.just(List.of(Space.builder().build()));
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

    @CircuitBreaker(name = "hooverClient.spaceUsers", fallbackMethod = "fallbackForSpaceUsers")
    public Mono<List<SpaceUsers>> getSpaceUsers() {
        String uri = "/snapshot/spaces/users";
        return client
                .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(SpaceUsers.class)
                    .collectList()
                    .onErrorResume(
                        WebClientResponseException.class,
                        e -> {
                            log.warn("Could not obtain SpaceUsers from {}", uri);
                            return Mono.just(List.of(SpaceUsers.builder().build()));
                        }
                    );
    }

    protected Mono<List<SpaceUsers>> fallbackForSpaceUsers(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/spaces/users", e);
        return Mono.just(List.of(SpaceUsers.builder().build()));
    }

}
