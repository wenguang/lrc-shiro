package lrcshiro04.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import lrcshiro04.entity.User;

public class MyRealm3 implements Realm {
    public String getName() {
        return "c"; //realm name 为 “c”
    }

    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = new User("zhang", "123");
        return new SimpleAuthenticationInfo(
                user, //身份 User类型
                "123",   //凭据
                getName() //Realm Name
        );
    }
}
