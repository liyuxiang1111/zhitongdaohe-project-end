package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.group.dao.mapper.MemberMapper;
import com.project.group.dao.mapper.ProjectBodyMapper;
import com.project.group.dao.mapper.ProjectMapper;
import com.project.group.dao.mapper.TypeMapper;
import com.project.group.dao.pojo.Project;
import com.project.group.dao.pojo.ProjectMember;
import com.project.group.dao.pojo.Type;
import com.project.group.service.ProjectService;
import com.project.group.service.SysUserService;
import com.project.group.service.TypeService;
import com.project.group.vo.ProjectVo;
import com.project.group.vo.Result;
import com.project.group.vo.params.TypeParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeMapper typeMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ProjectBodyMapper projectBodyMapper;
    @Override
    public Result TypeDetailById(Long id) {
        Type type = typeMapper.selectById(id);
        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Project::getProjectTypeId,id);
        List<Project> projectList = projectMapper.selectList(lambdaQueryWrapper);
        List<ProjectVo> projectVoList = copyList(projectList,type.getTypeName());
        TypeParam typeParam = new TypeParam();
        typeParam.setProjectList(projectVoList);
        typeParam.setTypeImg(type.getTypeImg());
        typeParam.setDescription(type.getDescription());
        typeParam.setTypeName(type.getTypeName());
        return Result.success(typeParam);
    }



    @Override
    public Result hotType(int limit) {
        List<Type> types = typeMapper.findHostType(limit);
        return Result.success(types);
    }

    //    public TypeVo copy(Type type){
//        TypeVo typeVo = new TypeVo();
//        typeVo.setId(String.valueOf(type.getId()));
//        BeanUtils.copyProperties(type, typeVo);
//        return typeVo;
//    }


    /**
     * 重载
     * @param records
     * @return
     */
    private List<ProjectVo> copyList(List<Project> records,String typeName) {
        List<ProjectVo> projectVoList = new ArrayList<>();
        for(Project record : records){
            projectVoList.add(copy(record,typeName));
        }
        return projectVoList;
    }


    private ProjectVo copy(Project project,String typeName){
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        projectVo.setProjectTypeName(typeName);
        projectVo.setCreateTime(new DateTime(project.getCreateTime()).toString("yyyy-MM-dd HH:mm"));
        projectVo.setProjectImg(project.getProjectImg());
        //并不是所有的接口都需要标签 作者信息
        LambdaQueryWrapper<Type> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Type::getId,project.getProjectTypeId());
        LambdaQueryWrapper<ProjectMember> members = new LambdaQueryWrapper<>();
        members.eq(ProjectMember::getProjectId,project.getProjectId());
        Integer integer = memberMapper.selectCount(members);
        projectVo.setMemberNum(integer);
        Type type = typeMapper.selectOne(lambdaQueryWrapper1);
            projectVo.setProjectName(project.getProjectName());
            Long authorId = project.getUserId();
            projectVo.setAuthor(sysUserService.findUserBid(authorId));
            Integer bodyId = project.getProjectBodyId();
            projectVo.setProjectBody(projectBodyMapper.selectById(bodyId));
        return projectVo;
    }


}
