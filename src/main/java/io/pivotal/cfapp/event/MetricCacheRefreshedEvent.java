package io.pivotal.cfapp.event;

import org.springframework.context.ApplicationEvent;

public class MetricCacheRefreshedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public MetricCacheRefreshedEvent(Object source) {
        super(source);
    }

}
