package com.project.group.handler;


import com.alibaba.fastjson.JSON;
import com.project.group.dao.pojo.User;
import com.project.group.service.LoginService;
import com.project.group.utils.UserThreadLocal;
import com.project.group.vo.ErrorCode;
import com.project.group.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j //日志配置
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行方法之前进行执行
        /**
         * 1.需要判断请求的接口路径 是否为handler (controller方法)
         * 2. 判断token是否为空 ; 未登录
         * 3. 如果token 不为空 那么进行登录验证 checktoken
         * 4. 如果认证通过 放行就号
         */
        if(!(handler instanceof HandlerMethod)){
            //handler 可能是requestResourceHandler springboot 程序 访问静态资源 默认是classpath/static
            return true;
        }
        String token =  request.getHeader("Authorization");


        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "用户未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        User user = loginService.checkToken(token);
        if(user == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=uft-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登录验证成功!!!
        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除userthreadlocal
        /**
         * 如果不删除 ThreadLoacl 那么就会有内存泄露的风险
         */
        UserThreadLocal.remove();
    }


}
