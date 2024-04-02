package org.cftoolsuite.cfapp.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import org.cftoolsuite.cfapp.domain.Space;

public class SpacesRetrievedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private List<Space> spaces;

    public SpacesRetrievedEvent(Object source) {
        super(source);
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public SpacesRetrievedEvent spaces(List<Space> spaces) {
        this.spaces = spaces;
        return this;
    }

}
