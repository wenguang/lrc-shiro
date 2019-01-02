package lrcshiro03.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationException;

public class MyRealm2 extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = "liu"; //用户名及salt1
        String salt2 = "0072273a5d87322163795118fdd7c45e";
        String password = "be320beca57748ab9632c4121ccac0db"; //加密后的密码
        SimpleAuthenticationInfo ai = new SimpleAuthenticationInfo(username, password, getName());
        /**
         * 注意：这里的salt2是随机盐，而不是Hash的盐，它可与一个私盐混合形成Hash的盐。
         * 通常在随机盐会被存储在数据库或另的安全的地方待以后做校验用。
         * 这里一定要调用setCredentialsSalt，ByteSource.Util.bytes(username+salt2)得到Hash的盐，
         * 有了盐和Test中输入的密码明文，就可以传给ini文件中的credentialsMatcher，
         * credentialsMatcher就按ini配置的md5算法2阶的验证方法去检验，若得到上面给出的加密后的密码即成功。
         * 若用JdbcRealm，就是从数据库获取加密后的密码与Hash的盐
         */
        ai.setCredentialsSalt(ByteSource.Util.bytes(username+salt2)); //盐是用户名+随机数
        return ai;
    }
}
