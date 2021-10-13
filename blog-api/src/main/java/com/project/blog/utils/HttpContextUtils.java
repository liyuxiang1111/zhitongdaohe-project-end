package com.project.blog.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContextUtils {
    public static javax.servlet.http.HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
