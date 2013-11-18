package org.grp5.getacar.domain;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * User domain object.
 */
@Entity
@Table(name = "benutzer")
@AttributeOverride(name = "id", column = @Column(name = "b_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class User extends BaseDomainObject {

    private String login;
    private String password;
    private String email;
    private Boolean active;
    private String firstName;
    private String lastName;
    private List<Role> roles = Lists.newArrayList();

    @Basic(optional = false)
    @Column(name = "b_login", columnDefinition = "varchar(20)")
    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic(optional = false)
    @Column(name = "b_passwort", columnDefinition = "text")
    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic(optional = false)
    @Column(name = "b_email", columnDefinition = "varchar(50)")
    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic(optional = false)
    @Column(name = "b_aktiv", columnDefinition = "bit(1) default 0")
    @NotNull
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic(optional = false)
    @Column(name = "b_vorname", columnDefinition = "varchar(30)")
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic(optional = false)
    @Column(name = "b_nachname", columnDefinition = "varchar(30)")
    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String actualCoordinates) {
        this.lastName = actualCoordinates;
    }

    @ManyToMany
    @JoinTable(
            name = "benutzer_rolle",
            joinColumns = @JoinColumn(name = "benutzer_b_id",
                    columnDefinition = "int(10) unsigned NOT NULL"),
            inverseJoinColumns = @JoinColumn(name = "rolle_ro_id",
                    columnDefinition = "int(10) unsigned NOT NULL"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"benutzer_b_id", "rolle_ro_id"})
    )
    @NotNull
    @Size(min = 1)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}