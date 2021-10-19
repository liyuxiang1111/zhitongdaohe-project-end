package com.project.group.vo.params;


import lombok.Data;

import java.io.File;

@Data
public class ChangeParam {

    private String userName; //用户名字

    private String userPwd; // 用户密码

    private File userImg; // 用户要修改的图片

    private String userContent; // 用户简介

    private Integer userSex; // 用户性别

    private String userEmail; // 用户邮箱

    private String userLargestStudy; // 最高学历

    private String userTelephone; // 用户电话

}
