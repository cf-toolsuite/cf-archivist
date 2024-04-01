package org.cftoolsuite.cfapp.domain;

import java.util.Set;

public class TimeKeeperUtil {

    private final Set<TimeKeeper> timeKeepers;

    public TimeKeeperUtil(Set<TimeKeeper> timeKeepers) {
        this.timeKeepers = timeKeepers;
    }

    public AppDetail enrich(AppDetail input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> AppDetail.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }

    public ServiceInstanceDetail enrich(ServiceInstanceDetail input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> ServiceInstanceDetail.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }

    public AppRelationship enrich(AppRelationship input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> AppRelationship.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }

    public Organization enrich(Organization input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> Organization.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }

    public Space enrich(Space input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> Space.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }

    public SpaceUsers enrich(SpaceUsers input) {
        return timeKeepers
                .stream()
                    .filter(tk -> tk.getFoundation().equalsIgnoreCase(input.getFoundation()))
                    .findFirst()
                    .map(r -> SpaceUsers.from(input).collectionDateTime(r.getCollectionDateTime()).build())
                    .orElse(input);
    }
}
