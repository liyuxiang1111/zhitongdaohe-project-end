package com.project.group.dao.pojo;


import lombok.Data;

@Data
public class Member {

    private Integer projectId;//项目id

    private Integer memberId;//成员id

    private Integer createUserId;//创建用户id

    private String userName;//用户名字

    private String memberLabel;//用户标签

    private Integer userSex;//用户性别

    private String userLargestStudy;//用户最高学历

    private Integer memberAssionment;//用户权限

    private String joinDay;//用户加入日期

    private String memberImg;//用户头部

}
