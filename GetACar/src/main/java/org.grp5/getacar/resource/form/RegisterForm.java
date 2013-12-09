package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.User;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

/**
 *
 */
public class RegisterForm {
    private User user = new User();
    private boolean acceptTOS = false;

    @Valid
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @AssertTrue
    public boolean isAcceptTOS() {
        return acceptTOS;
    }

    public void setAcceptTOS(boolean acceptTOS) {
        this.acceptTOS = acceptTOS;
    }
}
