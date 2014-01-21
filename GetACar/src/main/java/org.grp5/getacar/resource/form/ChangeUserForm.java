package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.User;

import javax.validation.Valid;

/**
 *
 */
public class ChangeUserForm {
    private User user = new User();

    @Valid
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
