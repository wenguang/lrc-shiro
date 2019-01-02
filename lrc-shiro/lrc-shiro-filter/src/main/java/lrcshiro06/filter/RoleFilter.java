package lrcshiro06.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleFilter extends AccessControlFilter {

    private String loginUrl = "/login.jsp";
    private String unauthorizedUrl = "unauthorized.jsp";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        String[] roles = (String[])mappedValue;
        if (roles == null) {
            //如果没有设置角色参数，默认成功
            return true;
        }
        for (String role : roles) {
            if (getSubject(request, response).hasRole(role)) {
                return true;
            }
        }

        //返回false, 跳到onAccessDenied处理
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated()) {
            // 若未登录，则重定向到登录页
            saveRequest(request);
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            // 若有未授权页，就重定向到未授权页；若没有，则返回401未授权状态码
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        return false;
    }
}
