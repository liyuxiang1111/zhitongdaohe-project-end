package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.group.dao.mapper.PersonMapper;
import com.project.group.dao.mapper.ProjectMapper;
import com.project.group.dao.mapper.ReceiveRequestMapper;
import com.project.group.dao.pojo.Project;
import com.project.group.dao.pojo.ProjectReceiveRequest;
import com.project.group.dao.pojo.Type;
import com.project.group.dao.pojo.User;
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


    @Override
    public Result intoPersonCenter() {
        LambdaQueryWrapper<Project> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        User user = new User();
        user.setUserId(1L);
        user.setUserName("lisi");
//        User user = UserThreadLocal.get();
        lambdaQueryWrapper1.eq(Project::getUserId,user.getUserId());
        List<Project> projectList = projectMapper.selectList(lambdaQueryWrapper1);
        PersonCenterVo personCenterVo = new PersonCenterVo();
        personCenterVo.setUser(user);
        UserThreadLocal.remove();
        personCenterVo.setProjectLessVoList(copyList(projectList));
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
    private List<ProjectLessVo> copyList(List<Project> records) {
        List<ProjectLessVo> projectLessVos = new ArrayList<>();
        for(Project record : records){
            projectLessVos.add(copy(record));
        }
        return projectLessVos;
    }


    private ProjectLessVo copy(Project project){
        ProjectLessVo projectLessVo = new ProjectLessVo();
        BeanUtils.copyProperties(project, projectLessVo);
        projectLessVo.setProjectImg(project.getProjectImg());
        //并不是所有的接口都需要标签 作者信息
        return projectLessVo;
    }
}
