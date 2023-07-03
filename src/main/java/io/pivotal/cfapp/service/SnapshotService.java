package io.pivotal.cfapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pivotal.cfapp.domain.SnapshotDetail;
import reactor.core.publisher.Mono;

@Service
public class SnapshotService {

    private final AppDetailService appDetailService;
    private final ServiceInstanceDetailService siDetailService;
    private final AppRelationshipService appRelationshipService;

    @Autowired
    public SnapshotService(
            AppDetailService appDetailService,
            ServiceInstanceDetailService siDetailService,
            AppRelationshipService appRelationshipService,
            SpaceUsersService spaceUsersService
            ) {
        this.appDetailService = appDetailService;
        this.siDetailService = siDetailService;
        this.appRelationshipService = appRelationshipService;
    }

    public Mono<SnapshotDetail> assembleSnapshotDetail() {
        return appDetailService
                .findAll()
                .collectList()
                .map(ad -> SnapshotDetail.builder().applications(ad))
                .flatMap(b -> siDetailService
                        .findAll()
                        .collectList()
                        .map(sid -> b.serviceInstances(sid)))
                .flatMap(b -> appRelationshipService
                        .findAll()
                        .collectList()
                        .map(ar -> b.applicationRelationships(ar).build()));
    }
}
