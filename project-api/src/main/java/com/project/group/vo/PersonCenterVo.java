package com.project.group.vo;


import com.project.group.dao.pojo.ProjectReceiveRequest;
import com.project.group.dao.pojo.User;
import lombok.Data;

import java.util.List;

@Data
public class PersonCenterVo {

    private User user;

    private List<ProjectReceiveRequest> requestList;

    private List<ProjectLessVo> projectLessVoList;

}
