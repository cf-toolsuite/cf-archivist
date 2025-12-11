package org.cftoolsuite.cfapp.controller;

import org.cftoolsuite.cfapp.event.DatabaseCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@ConditionalOnProperty(prefix="refresh", name="enabled", havingValue="true")
public class MetricCacheRefreshController {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public MetricCacheRefreshController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/cache/refresh")
    public Mono<ResponseEntity<Void>> refreshCache() {
        publisher.publishEvent(new DatabaseCreatedEvent(this));
        return Mono.just(ResponseEntity.noContent().build());
    }
}