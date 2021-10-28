package com.project.group.vo.params;


import com.project.group.dao.pojo.Project;
import com.project.group.vo.ProjectLessVo;
import com.project.group.vo.ProjectVo;
import lombok.Data;

import java.util.List;

@Data
public class TypeParam {
    private List<ProjectVo> projectList;

    private String description;

    private String typeImg; //标签图片

    private String typeName;


}
