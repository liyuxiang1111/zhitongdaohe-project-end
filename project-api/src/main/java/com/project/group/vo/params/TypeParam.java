package com.project.group.vo.params;


import com.project.group.dao.pojo.Project;
import lombok.Data;

import java.util.List;

@Data
public class TypeParam {

    private List<Project> projectList;

    private String description;

    private String typeImg; //标签图片

    private String typeName;


}
