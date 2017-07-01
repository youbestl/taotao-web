package com.taotao.web.interceptors;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserTreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JARVIS on 2017/6/30.
 */
public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        UserTreadLocal.set(null); //清空本地线程中user对象 --->涉及到 tomcat 线程池的原理，如果不清空有可能拿到未清空user数据的线程
        String loginUrl = this.userService.TAOTAO_SSO_URL + "/user/login.html";
        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);

        if (StringUtils.isEmpty(token)) {
            //未登录跳转到登录页面
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        User user = this.userService.queryByToken(token);
        if (null == user) {
            //登录超时
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        UserTreadLocal.set(user); //将用户信息放入本地线程中
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
