package com.project.blog.utils;

import com.project.blog.dao.pojo.SysUser;

public class UserThreadLocal {

    //私有的不希望他构造
    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    /**
     * user 放入
     * @param sysUser
     */
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    /**
     * user 取出
     * @return
     */
    public static SysUser get(){
        return LOCAL.get();
    }

    /**
     * 移除 user
     */
    public static void remove(){
        LOCAL.remove();
    }
}
