package com.project.group.dao.pojo;


import lombok.Data;

@Data
public class ProjectReceiveRequest {

    private Integer requestId;

    private Integer projectId;

    private String userName;

    private String userLabel;

    private  String userLargestStudy;

    private String userIntroduce;

    private Long userId;

}
