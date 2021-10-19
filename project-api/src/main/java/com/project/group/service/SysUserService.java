package com.project.group.service;

import com.project.group.dao.pojo.User;
import com.project.group.vo.Result;
import com.project.group.vo.UserVo;
import com.project.group.vo.params.RegisterParam;

import javax.servlet.http.HttpServletRequest;

public interface SysUserService {
    User findUserBid(Long id);


    User findUser(String account, String password);

    /**
     * 更具token查询用户信息
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 查看是否存在这个user
     * @param name
     * @return
     */
    User findUserName(String name);

    /**
     * 保存用户
     * @param user
     */
    void save(User user);

    /**
     * 按id查找对应作者 返回文章页面的user信息
     * @param id
     * @return
     */

    UserVo findUserVoById(Long id);


    /**
     * 修改用户信息
     * @param request
     * @return
     */
    Result changeUserInformation(HttpServletRequest request);
}
