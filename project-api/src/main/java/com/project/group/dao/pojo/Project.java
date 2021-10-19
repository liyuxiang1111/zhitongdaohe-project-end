package com.project.group.dao.pojo;

import lombok.Data;

@Data
public class Project {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Integer projectId;//项目id

    private Integer projectTypeId; //项目类型id

    private Long userId;//用户id

    private String projectName;//项目名称

    private Integer projectBodyId; //项目内容id

    private String projectImg;//项目封面

    private Integer visitNumber;//项目成员数量

    private Integer province;//项目省份

    private Long createTime;//项目创建时间
}