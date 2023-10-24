package com.petralib.auth;

import java.util.Collections;
import java.util.EnumSet;

public enum Role {
    NONE(),
    USER(UserAction.READ),
    MANAGER(UserAction.READ, UserAction.WRITE),
    OWNER(UserAction.READ, UserAction.WRITE, UserAction.DELETE, UserAction.EDIT);

    private final EnumSet<UserAction> userActions;

    Role(UserAction ... userActions) {
        this.userActions = EnumSet.noneOf(UserAction.class);
        Collections.addAll(this.userActions, userActions);
    }

    public boolean isActionAccepted(UserAction userAction){
        return userActions.contains(userAction);
    }
}
