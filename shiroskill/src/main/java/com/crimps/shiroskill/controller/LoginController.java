package com.crimps.shiroskill.controller;

import com.crimps.shiroskill.common.Const;
import com.crimps.shiroskill.domain.entity.SysUser;
import com.crimps.shiroskill.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("login")
public class LoginController extends BaseController {

    @Autowired
    SysUserService sysUserService;

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
            return getResultMap(false, "用户名/密码错误");
        }catch (ExcessiveAttemptsException e){
            return getResultMap(false, e.getMessage());
        }catch (Exception e){
            return getResultMap(false, "系统异常");
        }
    }
}
