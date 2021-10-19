package com.project.group.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.group.dao.dos.Archives;
import com.project.group.dao.pojo.Project;
import com.project.group.vo.Page;

import java.util.Iterator;
import java.util.List;


public interface ProjectMapper extends BaseMapper<Project> {


    List<Project> listProject(Integer pageNum , Integer pageSize , String projectName, String searchTime, Integer projectTypeId, Integer memberNum , Integer userSex ,  String userStudy );
}

