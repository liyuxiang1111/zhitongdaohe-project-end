package com.project.blog.service;

import com.project.blog.dao.pojo.SysUser;
import com.project.blog.vo.Result;
import com.project.blog.vo.UserVo;

public interface SysUserService {
    SysUser findUserBid(Long id);


    SysUser findUser(String account, String password);

    /**
     * 更具token查询用户信息
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 查看是否存在这个user
     * @param account
     * @return
     */
    SysUser findUserAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 按id查找对应作者 返回文章页面的user信息
     * @param id
     * @return
     */

    UserVo findUserVoById(Long id);
}
