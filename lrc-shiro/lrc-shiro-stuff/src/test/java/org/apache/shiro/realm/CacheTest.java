package org.apache.shiro.realm;

import lrcshiro04.BaseTest;
import org.apache.shiro.realm.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.junit.Test;

public class CacheTest extends BaseTest {

    @Test
    public void testClearCachedAuthenticationInfo() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        userService.changePassword(u1.getId(), password + "1");

        RealmSecurityManager securityManager = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm)securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());

        login("classpath:shiro.ini", u1.getUsername(), password + "1");
    }

    @Test
    public void testClearCachedAuthorizationInfo() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        subject().checkRole(r1.getRole());
        userService.correlationRoles(u1.getId(), r2.getId());

        RealmSecurityManager securityManager = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm)securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthorizationInfo(subject().getPrincipals());

        subject().checkRole(r2.getRole());
    }
}
