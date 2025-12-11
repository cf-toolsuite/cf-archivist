package org.cftoolsuite.cfapp.client;

import java.util.List;

import org.cftoolsuite.cfapp.domain.Organization;
import org.cftoolsuite.cfapp.domain.SnapshotDetail;
import org.cftoolsuite.cfapp.domain.Space;
import org.cftoolsuite.cfapp.domain.SpaceUsers;
import org.cftoolsuite.cfapp.domain.TimeKeepers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "archivistClient.timeKeepers", fallbackMethod = "fallbackForTimeKeepers")
    public Mono<TimeKeepers> getTimeKeepers() {
        return client
                .get()
                    .uri("/collect")
                    .retrieve()
                    .bodyToMono(TimeKeepers.class);
    }

    protected Mono<TimeKeepers> fallbackForTimeKeepers(Exception e) {
        log.warn("Could not obtain results from call to /collect", e);
        return Mono.just(TimeKeepers.builder().build());
    }

    @CircuitBreaker(name = "archivistClient.organizations", fallbackMethod = "fallbackForOrganizations")
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
                            log.warn(String.format("Could not obtain organizations from %s", uri), e);
                            return Mono.empty();
                        }
                    );
    }

    protected Mono<List<Space>> fallbackForOrganizations(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/organizations", e);
        return Mono.just(List.of(Space.builder().build()));
    }

    @CircuitBreaker(name = "archivistClient.spaces", fallbackMethod = "fallbackForSpaces")
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
                            log.warn(String.format("Could not obtain organizations from %s", uri), e);
                            return Mono.empty();
                        }
                    );
    }

    protected Mono<List<Space>> fallbackForSpaces(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/spaces", e);
        return Mono.just(List.of(Space.builder().build()));
    }

    @CircuitBreaker(name = "archivistClient.detail", fallbackMethod = "fallbackForDetail")
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

    @CircuitBreaker(name = "archivistClient.spaceUsers", fallbackMethod = "fallbackForSpaceUsers")
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
                            log.warn(String.format("Could not obtain SpaceUsers from %s", uri), e);
                            return Mono.just(List.of(SpaceUsers.builder().build()));
                        }
                    );
    }

    protected Mono<List<SpaceUsers>> fallbackForSpaceUsers(Exception e) {
        log.warn("Could not obtain results from call to /snapshot/spaces/users", e);
        return Mono.just(List.of(SpaceUsers.builder().build()));
    }

}
