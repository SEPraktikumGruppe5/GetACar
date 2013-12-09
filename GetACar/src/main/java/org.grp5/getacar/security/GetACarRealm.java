package org.grp5.getacar.security;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.onami.logging.core.InjectLogger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.grp5.getacar.persistence.Role;
import org.grp5.getacar.persistence.User;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Application specific {@link AuthorizingRealm}.
 */
public class GetACarRealm extends AuthorizingRealm {

    /**
     * Used to store the password information of the user trying to log in internally in this realm
     */
    private final class LoginPasswordInformation {

        private String login; // login name
        private String password; // password including salt

        public LoginPasswordInformation(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    /**
     * Used to store the roleNames of the user trying to access a resource internally in this realm
     */
    private final class UserPermissionsRolesInformation {

        private Set<String> roleNames;      // role names of the user

        private UserPermissionsRolesInformation(Set<String> roleNames) {
            this.roleNames = roleNames;
        }
    }

    private final Provider<UserDAO> userDAOProvider;

    @InjectLogger
    Logger logger;

    @Inject
    public GetACarRealm(CacheManager cacheManager, CredentialsMatcher matcher, Provider<UserDAO> userDAOProvider) {
        super(cacheManager, matcher);
        this.userDAOProvider = userDAOProvider;

        setCachingEnabled(true);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //identify account to log to
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;

        String login = userPassToken.getUsername();

        if (login == null) {
            logger.debug("Login is null.");
            return null;
        }

        // read password hash including salt from db
        LoginPasswordInformation loginPasswordInformation = getPasswordInformationForUser(login);

        if (loginPasswordInformation == null) {
            logger.debug("No account found for user [" + login + "]");
            return null;
        }
        // return salted credentials
        return new SimpleAuthenticationInfo(loginPasswordInformation.login, loginPasswordInformation.password, null,
                getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // null user names are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument can not be null.");
        }
        String login = (String) getAvailablePrincipal(principals);
        UserDAO userDAO = userDAOProvider.get();
        User user = userDAO.findByLoginName(login);
        // Retrieve permissions for the user
        UserPermissionsRolesInformation permissionsRolesInformation = getRoleNames(user);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(permissionsRolesInformation.roleNames);
        return info;
    }

    private LoginPasswordInformation getPasswordInformationForUser(String login) {

        UserDAO userDAO = userDAOProvider.get();
        User user = userDAO.findByLoginName(login);
        if (user == null) {
            return null;
        }

        return new LoginPasswordInformation(user.getLoginName(), user.getPassword());
    }

    protected UserPermissionsRolesInformation getRoleNames(User user) {
        Set<String> roleNames = Sets.newLinkedHashSet();
        for (Role role : user.getRoles()) {
            roleNames.add(role.getName());
        }
        return new UserPermissionsRolesInformation(roleNames);
    }
}
