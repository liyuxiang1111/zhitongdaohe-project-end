package com.project.group.vo.params;


import lombok.Data;

import java.util.List;

@Data
public class PageParams {

    private int pageNum;

    private int pageSize;

    private String projectName;

    private String searchDay;

    private List<Integer> province;

    private Integer projectTypeId;

    private Integer userSex;

    private String userStudy;

    private Integer memberNum;

}
