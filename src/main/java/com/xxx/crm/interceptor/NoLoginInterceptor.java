package com.xxx.crm.interceptor;

import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        if(null == userId || null == userService.selectByPrimaryKey(userId)){

            throw new LoginException();
        }
        //放行
        return true;
    }
}
