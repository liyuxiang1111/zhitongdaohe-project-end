package com.project.group.vo;


import lombok.Data;

/**
 * 个人中心的一个请求
 */

@Data
public class UserRequestVo {

    private Integer projectId;

    private String userName;

    private Integer userSex;

    private String userLargestStudy;

    private String userIntroduce;

    private String userId;

}
