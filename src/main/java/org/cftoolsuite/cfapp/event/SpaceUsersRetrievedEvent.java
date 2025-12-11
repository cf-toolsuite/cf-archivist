package org.cftoolsuite.cfapp.event;

import java.util.List;

import org.cftoolsuite.cfapp.domain.SpaceUsers;
import org.springframework.context.ApplicationEvent;

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
