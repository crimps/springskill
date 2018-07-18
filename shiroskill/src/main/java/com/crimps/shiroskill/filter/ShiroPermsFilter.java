package com.crimps.shiroskill.filter;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShiroPermsFilter extends PermissionsAuthorizationFilter {

    private Logger logger = LoggerFactory.getLogger(ShiroPermsFilter.class);

    /**
     * shiro认证perms资源失败后回调方法
     *
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
            logger.info("无访问权限-ajax请求");
            //如果是ajax返回指定格式数据
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            Gson gson = new Gson();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", "-1");
            resultMap.put("msg", "无访问权限");
            httpServletResponse.getWriter().write(gson.toJson(resultMap));
        }
        else{
            logger.info("无访问权限-普通请求");
            //如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/403");
        }
        return false;
    }
}
