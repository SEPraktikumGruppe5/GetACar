package org.grp5.getacar.resource.form;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 *
 */
public class LoginForm {
    private String loginName;
    private String password;
    private DateTime timeSimulation;

    @NotNull
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull
    public DateTime getTimeSimulation() {
        return timeSimulation;
    }

    public void setTimeSimulation(DateTime timeSimulation) {
        this.timeSimulation = timeSimulation;
    }
}
