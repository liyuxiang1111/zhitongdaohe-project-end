package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.group.dao.mapper.*;
import com.project.group.dao.pojo.*;
import com.project.group.service.PersonService;
import com.project.group.service.SysUserService;
import com.project.group.utils.UserThreadLocal;
import com.project.group.vo.PersonCenterVo;
import com.project.group.vo.ProjectLessVo;
import com.project.group.vo.ProjectVo;
import com.project.group.vo.Result;
import org.aspectj.weaver.patterns.PerObject;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class PersonServiceImpl implements PersonService {


    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ReceiveRequestMapper receiveRequestMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ProjectBodyMapper projectBodyMapper;


    @Override
    public Result intoPersonCenter(User user) {
        LambdaQueryWrapper<Project> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
//        User user = UserThreadLocal.get();
        lambdaQueryWrapper1.eq(Project::getUserId,user.getUserId());
        List<Project> projectList = projectMapper.selectList(lambdaQueryWrapper1);
        PersonCenterVo personCenterVo = new PersonCenterVo();
        personCenterVo.setUser(user);
        //        查创建的数量
        LambdaQueryWrapper<Project> create = new LambdaQueryWrapper<>();
        create.eq(Project::getUserId,user.getUserId());
        Integer countCreate = projectMapper.selectCount(create);
        personCenterVo.setUserCreate(countCreate);
//        查参加的数量
        LambdaQueryWrapper<ProjectMember> join = new LambdaQueryWrapper<>();
        join.eq(ProjectMember::getUserName,user.getUserName());
        Integer countJoin = memberMapper.selectCount(join);
        personCenterVo.setUserCreate(countJoin);
        personCenterVo.setUserAllProjectNum(countJoin+countCreate);
        personCenterVo.setProjectVoList(copyList(projectList));
        LambdaQueryWrapper<ProjectReceiveRequest> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(ProjectReceiveRequest::getUserId,user.getUserId());
        List<ProjectReceiveRequest> receiveList = receiveRequestMapper.selectList(lambdaQueryWrapper2);
        personCenterVo.setRequestList(receiveList);
        return Result.success(personCenterVo);
    }


    /**
     * 重载
     * @param records
     * @return
     */
    private List<ProjectVo> copyList(List<Project> records) {
        List<ProjectVo> projectVoList = new ArrayList<>();
        for(Project record : records){
            projectVoList.add(copy(record));
        }
        return projectVoList;
    }


    private ProjectVo copy(Project project){
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        projectVo.setCreateTime(new DateTime(project.getCreateTime()).toString("yyyy-MM-dd"));
        projectVo.setProjectImg(project.getProjectImg());
        Integer bodyId = project.getProjectBodyId();
        projectVo.setProjectBody(projectBodyMapper.selectById(bodyId));
        return projectVo;
    }
}
