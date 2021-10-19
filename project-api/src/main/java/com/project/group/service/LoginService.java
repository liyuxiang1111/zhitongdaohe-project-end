package com.project.group.service;

import com.project.group.dao.pojo.User;
import com.project.group.vo.Result;
import com.project.group.vo.params.LoginParam;
import com.project.group.vo.params.RegisterParam;

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
    User checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param registerParam
     * @return
     */
    Result register(RegisterParam registerParam);

}
