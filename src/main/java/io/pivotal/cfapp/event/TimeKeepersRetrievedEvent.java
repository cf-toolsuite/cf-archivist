package io.pivotal.cfapp.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import io.pivotal.cfapp.domain.TimeKeeper;

public class TimeKeepersRetrievedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private List<TimeKeeper> timeKeepers;

    public TimeKeepersRetrievedEvent(Object source) {
        super(source);
    }

    public List<TimeKeeper> getTimeKeepers() {
        return timeKeepers;
    }

    public TimeKeepersRetrievedEvent timeKeepers(List<TimeKeeper> timeKeepers) {
        this.timeKeepers = timeKeepers;
        return this;
    }

}
