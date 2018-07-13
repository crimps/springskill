package com.crimps.shiroskill.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShiroPermsFilter extends PermissionsAuthorizationFilter {
    /**
     * shiro认证perms资源失败后回调方法
     * @param servletRequest request
     * @param servletResponse response
     * @return true|false
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestedWith = httpServletRequest.getHeader("X-Requested-With");
        if (StringUtils.isNotEmpty(requestedWith) && StringUtils.equals(requestedWith, "XMLHttpRequest")) {
            System.out.println("############## ajax");
            //如果是ajax返回指定格式数据
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
//            httpServletResponse.getWriter().write(JSONObject.toJSONString(responseHeader));
        } else {
            System.out.println("############# url");
            //如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/403");
        }
        return false;
    }
}
