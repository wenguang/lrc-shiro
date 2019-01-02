package lrcshiro01;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthenticatorTest {

    private void login(String iniFile, String username, String password) {

        // factory.getInstance()默认返回DefaultSecurityManager类
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(iniFile);
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
    }

    @Test
    public void testAllSuccessfulStrategy() {
        login("classpath:authenc-strategy-allsuccess.ini", "pan", "123");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());

        subject.logout();
    }
    //*/

    /**
    private void login(String username, String password) {

        //
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        //设置authenticator和authentication strategy
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        defaultSecurityManager.setAuthenticator(modularRealmAuthenticator);

        //设置realm
        ArrayList realms = new ArrayList();
        realms.add(new Realm1());
        realms.add(new Realm2());
        realms.add(new Realm3());
        defaultSecurityManager.setRealms(realms);

        //全局绑定
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
    }

    @Test
    public void testAllSuccessStrategry() {
        login("pan", "123");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());

        subject.logout();
    }
    */
}
