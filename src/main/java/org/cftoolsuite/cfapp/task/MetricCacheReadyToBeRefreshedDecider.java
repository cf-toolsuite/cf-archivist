package org.cftoolsuite.cfapp.task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import org.cftoolsuite.cfapp.event.AppDetailRetrievedEvent;
import org.cftoolsuite.cfapp.event.AppRelationshipRetrievedEvent;
import org.cftoolsuite.cfapp.event.OrganizationsRetrievedEvent;
import org.cftoolsuite.cfapp.event.ServiceInstanceDetailRetrievedEvent;
import org.cftoolsuite.cfapp.event.SpaceUsersRetrievedEvent;
import org.cftoolsuite.cfapp.event.SpacesRetrievedEvent;
import org.cftoolsuite.cfapp.event.TimeKeepersRetrievedEvent;

@Component
public class MetricCacheReadyToBeRefreshedDecider {

    private AtomicInteger decision = new AtomicInteger();

    private final static List<Class<? extends ApplicationEvent>> EVENT_TYPES =
        List.of(AppDetailRetrievedEvent.class, AppRelationshipRetrievedEvent.class, OrganizationsRetrievedEvent.class,
            ServiceInstanceDetailRetrievedEvent.class, SpacesRetrievedEvent.class, SpaceUsersRetrievedEvent.class, TimeKeepersRetrievedEvent.class);

    public int informReadinessDecision(ApplicationEvent event) {
        boolean matchingEvent = false;
        for (Class<? extends ApplicationEvent> eventType : EVENT_TYPES) {
            if (eventType.isInstance(event)) {
                matchingEvent = true;
                break;
            }
        }
        return matchingEvent ? decision.incrementAndGet(): decision.get();
    }

    public boolean isReady() {
        return decision.get() == EVENT_TYPES.size();
    }

    public void reset() {
        decision.set(0);
    }

}
