package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.group.dao.mapper.ProjectMapper;
import com.project.group.dao.mapper.TypeMapper;
import com.project.group.dao.pojo.Project;
import com.project.group.dao.pojo.Type;
import com.project.group.service.TypeService;
import com.project.group.vo.Result;
import com.project.group.vo.params.TypeParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeMapper typeMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public Result TypeDetailById(Long id) {
        Type type = typeMapper.selectById(id);
        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Project::getProjectTypeId,id);
        List<Project> projectList = projectMapper.selectList(lambdaQueryWrapper);
        TypeParam typeParam = new TypeParam();
        typeParam.setProjectList(projectList);
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

}
