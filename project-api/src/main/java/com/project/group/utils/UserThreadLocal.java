package com.project.group.utils;

import com.project.group.dao.pojo.User;

public class UserThreadLocal {

    //私有的不希望他构造
    private UserThreadLocal(){}

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    /**
     * user 放入
     * @param user
     */
    public static void put(User user){
        LOCAL.set(user);
    }

    /**
     * user 取出
     * @return
     */
    public static User get(){
        return LOCAL.get();
    }

    /**
     * 移除 user
     */
    public static void remove(){
        LOCAL.remove();
    }
}
