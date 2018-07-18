package com.crimps.shiroskill.controller;

import com.crimps.shiroskill.common.Const;
import com.crimps.shiroskill.domain.entity.SysUser;
import com.crimps.shiroskill.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("login")
public class LoginController extends BaseController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${loginRetryLimit}")
    Integer loginRetryLimit;

    @Autowired
    SysUserService sysUserService;
    @Autowired
    private EhCacheManager ehCacheManager;

    /**
     * 用户登陆验证
     *
     * @param userName 用户名
     * @param passWord 密码
     * @param rememberMe 是否记住我
     * @return
     */
    @RequestMapping("validate")
    @ResponseBody
    public Map<String, Object> loginValidate(HttpServletRequest request, String userName, String passWord, Boolean rememberMe){
        try{
            UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord, rememberMe);
            SecurityUtils.getSubject().login(token);
            //登录成功，在session添加用户信息
            SysUser sysUser = sysUserService.findByUsername(userName);
            sysUser.setPassword(null);
            sysUser.setSalt(null);
            request.getSession().setAttribute(Const.SYS_USER, sysUser);
            return getResultMap(true, "登陆成功");
        }catch (UnknownAccountException | IncorrectCredentialsException e){
            //登录方法中，获取失败次数，并设置友情提示信息
            String loginTip = "用户名或密码错误";
            Cache<String, AtomicInteger> passwordRetryCache= ehCacheManager.getCache("passwordRetryCache");
            if(null != passwordRetryCache){
                int retryNum=(passwordRetryCache.get(userName)==null?0:passwordRetryCache.get(userName)).intValue();
                logger.debug("输错次数："+retryNum);
                if(retryNum > 0 && retryNum < loginRetryLimit){
                    loginTip = "用户名或密码错误" + retryNum + "次,再输错" + (loginRetryLimit - retryNum) + "次账号将锁定";
                }
            }
            return getResultMap(false, loginTip);
        }catch (ExcessiveAttemptsException e){
            return getResultMap(false, e.getMessage());
        }catch (Exception e){
            return getResultMap(false, "系统异常");
        }
    }

    /**
     * 解锁用户登陆限制
     * @param userName 用户名
     * @return true|false
     */
    @RequestMapping("unlock-user-login")
    @ResponseBody
    @RequiresPermissions("login:unlock")
    public boolean unLockUserLogin(String userName){
        try{
            Cache<String, AtomicInteger> passwordRetryCache= ehCacheManager.getCache("passwordRetryCache");
            passwordRetryCache.remove(userName);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
