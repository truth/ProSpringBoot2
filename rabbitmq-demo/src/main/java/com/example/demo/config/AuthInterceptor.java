package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String token = null;
        token = request.getParameter("token");
        if(token==null) {
            String path = request.getHeader("X-Original-URI");
            UrlUtil.UrlEntity urlEntity = UrlUtil.parse(path);
            token = urlEntity.getParameter("token"); //解析参数
        }
        if (!StringUtils.hasLength(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
