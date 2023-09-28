package io.pivotal.cfapp.task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.event.AppDetailRetrievedEvent;
import io.pivotal.cfapp.event.AppRelationshipRetrievedEvent;
import io.pivotal.cfapp.event.OrganizationsRetrievedEvent;
import io.pivotal.cfapp.event.ServiceInstanceDetailRetrievedEvent;
import io.pivotal.cfapp.event.SpaceUsersRetrievedEvent;
import io.pivotal.cfapp.event.SpacesRetrievedEvent;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;

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
