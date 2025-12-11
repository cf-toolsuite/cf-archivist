package org.cftoolsuite.cfapp.event;

import java.util.Set;

import org.cftoolsuite.cfapp.domain.TimeKeeper;
import org.springframework.context.ApplicationEvent;

public class TimeKeepersRetrievedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private Set<TimeKeeper> timeKeepers;

    public TimeKeepersRetrievedEvent(Object source) {
        super(source);
    }

    public Set<TimeKeeper> getTimeKeepers() {
        return timeKeepers;
    }

    public TimeKeepersRetrievedEvent timeKeepers(Set<TimeKeeper> timeKeepers) {
        this.timeKeepers = timeKeepers;
        return this;
    }

}
