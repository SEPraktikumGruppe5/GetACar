package org.grp5.getacar.persistence;

import com.google.common.collect.Lists;
import org.grp5.getacar.persistence.validation.LoginNameNotExistent;
import org.grp5.getacar.persistence.validation.PasswordsMatch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import java.util.List;

/**
 * User entity class.
 */
@Entity
@Table(name = "benutzer")
@AttributeOverride(name = "id", column = @Column(name = "b_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
@LoginNameNotExistent.List(
        @LoginNameNotExistent(loginNameField = "loginName")
)
@PasswordsMatch.List(
        @PasswordsMatch(password = "password", passwordRepeat = "passwordRepeat")
)
public class User extends BaseEntity {

    private String loginName;
    private String password;
    private String passwordRepeat;
    private String email;
    private Boolean active;
    private String firstName;
    private String lastName;
    private List<Role> roles = Lists.newArrayList();

    @Basic(optional = false)
    @Column(name = "b_login", columnDefinition = "varchar(20)")
    @Size(min = 4, max = 20)
    @Pattern(regexp="[a-zA-Z]*")
    @NotNull
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Basic(optional = false)
    @Column(name = "b_passwort", columnDefinition = "text")
    @Size(min = 4, max = 20)
    // TODO requirements for good passwords 
    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    @Basic(optional = false)
    @Column(name = "b_email", columnDefinition = "varchar(50)")
    @Size(min = 6, max = 50)
    @Email
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
    @Size(min = 2, max = 30)
    @Pattern(regexp="[a-zA-Z]*")
    @NotNull    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic(optional = false)
    @Column(name = "b_nachname", columnDefinition = "varchar(30)")
    @Size(min = 2, max = 30)
    @Pattern(regexp="[a-zA-Z]*")
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

    /**
     * Makes sure that the passwordRepeat field is set so that validation does not fail when changing the user.
     */
    @PostLoad
    private void postLoad() {
        passwordRepeat = password;
    }
}