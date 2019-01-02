package lrcshiro02;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

public class AuthorizationTest {

    @Test
    public void testIniPermission() {
        login("classpath:role-permission.ini", "zhang", "123");

        Assert.assertEquals(true, subject().hasRole("role1"));

        //判断拥有权限：user:create
//        Assert.assertTrue(subject().isPermitted("user1:update"));
//        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
//        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
//        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
//        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看
//
//        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限
//
//        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }

    @Test
    public void testAuthz() {
        login("classpath:authorization.ini", "zhang", "123");

        DefaultSecurityManager defaultSecurityManager = (DefaultSecurityManager)SecurityUtils.getSecurityManager();
        MyRealm myRealm = (MyRealm)defaultSecurityManager.getRealms().toArray()[0];
        myRealm.doGetAuthorizationInfo(subject().getPrincipals());

        /**
         * ?? 当执行subject的hasRole或isPermitted方法时，自定义的Realm的重载方法doGetAuthorizationInfo会被调用
         */
        Assert.assertEquals(true, subject().hasRole("role1"));
        subject().isPermitted("user1:*");


    }

    @Test
    public void testJdbcAuthz() {
        login("classpath:jdbc-authz.ini", "zhang", "123");

        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

    }




    /**-------------------------------------------------------------------------*/
    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();
        ThreadContext.unbindSecurityManager();
    }

    private void login(String iniFile, String username, String password) {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(iniFile);
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            /**
             * 执行这句时，定义的MyRealm的重载方法doGetAuthenticationInfo就会被调用
             */
            subject.login(token);
        } catch (AuthenticationException e) {

        }
    }

    private Subject subject() {
        return SecurityUtils.getSubject();
    }
}
