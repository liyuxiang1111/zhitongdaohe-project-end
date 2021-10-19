package com.project.group.vo;


import com.project.group.dao.pojo.ProjectBody;
import com.project.group.dao.pojo.User;
import lombok.Data;

@Data
public class ProjectVo {

    private Integer projectId;//项目id

    private User author; //作者信息

    private String projectName;//项目名称

    private String projectTypeName;//项目类型

    private String projectImg;//项目封面

    private ProjectBody projectBody;//项目主体

    private Integer visitNumber;//项目成员数量

    private String province;//项目省份

    private String createTime;//项目创建时间

}
