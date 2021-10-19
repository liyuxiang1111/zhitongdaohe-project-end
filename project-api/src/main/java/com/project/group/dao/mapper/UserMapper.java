package com.project.group.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.group.dao.pojo.User;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 查找同名用户
     * @param userName
     * @param userId
     * @return
     */
    User findUserName(String userName, Long userId);
}
