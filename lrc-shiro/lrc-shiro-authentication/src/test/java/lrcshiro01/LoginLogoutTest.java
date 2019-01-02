package lrcshiro01;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

public class LoginLogoutTest {

    /**
     * 在text-realm.ini中定义的用户名密码对应，来初始化的SecurityManager，它底下是用的是org.apache.shiro.realm.text.IniRealm
     */
    @Test
    public void testTextIniRealm() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:text-realm.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            // 如果身份验证失败请捕获AuthenticationException或其子类，
            // 常见的如： DisabledAccountException（禁用的帐号）、
            // LockedAccountException（锁定的帐号）、
            // UnknownAccountException（错误的帐号）、
            // ExcessiveAttemptsException（登录失败次数过多）、
            // IncorrectCredentialsException （错误的凭证）、
            // ExpiredCredentialsException（过期的凭证）等
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();

    }

    /**
     * custom-realm.ini给SecurityManager指定了自定义的realm
     * 那认证去按指定的realm的逻辑定义去执行了
     */
    @Test
    public void testCustomRealm() {

        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:custom-realm.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("pan", "123");
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());

        subject.logout();
    }

    /**
     * custom-multi-realm.ini指定多个realm
     */
    @Test
    public void testCustomMultiRealms() {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:custom-multi-realm.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("ivorygx", "123");
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());

        subject.logout();
    }

    /**
     * jdbc-realm.ini指定SecurityManager的realms为JdbcRealm
     * 在jdbc-realm.ini配置了jdbcRealm的dataSource
     * 注意：以下JdbcRealm的源码
     *     protected static final String DEFAULT_AUTHENTICATION_QUERY = "select password from users where username = ?";
     *     protected static final String DEFAULT_SALTED_AUTHENTICATION_QUERY = "select password, password_salt from users where username = ?";
     *     protected static final String DEFAULT_USER_ROLES_QUERY = "select role_name from user_roles where username = ?";
     *     protected static final String DEFAULT_PERMISSIONS_QUERY = "select permission from roles_permissions where role_name = ?";
     *
     * 这里可看出我们定义的表名只能为users，字段只能是username、password、password_salt
     */
    @Test
    public void testJdbcRealm() {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:jdbc-realm.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());

        subject.logout();
    }
}
