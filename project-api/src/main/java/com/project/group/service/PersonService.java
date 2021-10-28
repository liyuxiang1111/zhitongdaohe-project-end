package com.project.group.service;

import com.project.group.dao.pojo.User;
import com.project.group.vo.Result;

public interface PersonService {

    /**
     * 进入个人中心
     * @return
     */

    Result intoPersonCenter(User user);
}
