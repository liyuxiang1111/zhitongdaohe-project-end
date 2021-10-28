package com.project.group.vo;


import com.project.group.dao.pojo.ProjectBody;
import com.project.group.dao.pojo.ProjectReceiveRequest;
import com.project.group.dao.pojo.User;
import lombok.Data;

import java.util.List;

@Data
public class PersonCenterVo {

    private User user;

    private List<ProjectReceiveRequest> requestList;

    private List<ProjectVo> projectVoList;

    private Integer userJoined;     //加入的

    private Integer userCreate;     //创建的

    private Integer userAllProjectNum; //总

}
