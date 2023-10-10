package io.pivotal.cfapp.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import io.pivotal.cfapp.domain.AppDetail;
import io.pivotal.cfapp.domain.AppRelationship;
import io.pivotal.cfapp.domain.ServiceInstanceDetail;

public class SnapshotDetailRetrievedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private List<AppDetail> applications;
    private List<ServiceInstanceDetail> serviceInstances;
    private List<AppRelationship> applicationRelationships;

    public SnapshotDetailRetrievedEvent(Object source) {
        super(source);
    }

    public SnapshotDetailRetrievedEvent applications(List<AppDetail> applications) {
        this.applications = applications;
        return this;
    }

    public List<AppDetail> getApplications() {
        return applications;
    }

    public SnapshotDetailRetrievedEvent serviceInstances(List<ServiceInstanceDetail> serviceInstances) {
        this.serviceInstances = serviceInstances;
        return this;
    }

    public List<ServiceInstanceDetail> getServiceInstances() {
        return serviceInstances;
    }

    public SnapshotDetailRetrievedEvent applicationRelationships(List<AppRelationship> applicationRelationships) {
        this.applicationRelationships = applicationRelationships;
        return this;
    }

    public List<AppRelationship> getApplicationRelationships() {
        return applicationRelationships;
    }
}
