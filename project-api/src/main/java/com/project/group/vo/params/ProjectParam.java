package com.project.group.vo.params;

import com.project.group.dao.pojo.Type;
import com.project.group.vo.TypeVo;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ProjectParam {

    private Long id;

    private ProjectBodyParam body;

    private String projectName;

    private Integer province;

    private File projectImg;

    private Integer typeId;

}
