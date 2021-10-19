package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.group.dao.mapper.MemberMapper;
import com.project.group.dao.pojo.Member;
import com.project.group.service.MemberService;
import com.project.group.vo.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;


    @Override
    public Result memberPage(Integer projectId) {
        LambdaUpdateWrapper<Member> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Member::getProjectId,projectId);
        List<Member> memberList = memberMapper.selectList(lambdaUpdateWrapper);
        return Result.success(memberList);
    }
}

