package io.pivotal.cfapp.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import io.pivotal.cfapp.domain.SpaceUsers;

public class SpaceUsersRetrievedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private List<SpaceUsers> spaceUsers;

    public SpaceUsersRetrievedEvent(Object source) {
        super(source);
    }

    public List<SpaceUsers> getSpaceUsers() {
        return spaceUsers;
    }

    public SpaceUsersRetrievedEvent spaceUsers(List<SpaceUsers> spaceUsers) {
        this.spaceUsers = spaceUsers;
        return this;
    }

}
