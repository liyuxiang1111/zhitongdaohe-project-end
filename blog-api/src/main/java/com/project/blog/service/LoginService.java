package com.project.blog.service;

import com.project.blog.dao.pojo.SysUser;
import com.project.blog.vo.Result;
import com.project.blog.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 校验token
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
