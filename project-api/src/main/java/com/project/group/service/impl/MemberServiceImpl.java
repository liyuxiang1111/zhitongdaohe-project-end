package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.group.dao.mapper.MemberMapper;
import com.project.group.dao.mapper.ReceiveRequestMapper;
import com.project.group.dao.mapper.UserMapper;
import com.project.group.dao.pojo.ProjectMember;
import com.project.group.dao.pojo.ProjectReceiveRequest;
import com.project.group.dao.pojo.User;
import com.project.group.service.MemberService;
import com.project.group.utils.UserThreadLocal;
import com.project.group.vo.ErrorCode;
import com.project.group.vo.Result;
import com.project.group.vo.params.AskParam;
import com.project.group.vo.params.Authority;
import com.project.group.vo.params.JoinProjectParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ReceiveRequestMapper receiveRequestMapper;

    @Resource
    private MemberService memberService;

    @Resource
    private UserMapper userMapper;


    @Override
    public Result agreeThisPost(AskParam askParam) {
        if(askParam.getSituation() == true){
            LambdaQueryWrapper<ProjectReceiveRequest> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProjectReceiveRequest::getRequestId,askParam.getRequestId());
            this.memberService.save(askParam.getRequestId());
            receiveRequestMapper.delete(lambdaQueryWrapper);
            return Result.success(null);
        } else {
            return Result.fail(ErrorCode.SESSION_TIME_OUT.getCode(),ErrorCode.SESSION_TIME_OUT.getMsg());
        }
    }

    @Override
    public Result memberPage(Integer projectId) {
        LambdaUpdateWrapper<ProjectMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ProjectMember::getProjectId,projectId);
        List<ProjectMember> projectMemberList = memberMapper.selectList(lambdaUpdateWrapper);
        return Result.success(projectMemberList);
    }


    /**
     * 提高用户权限
     * @param authority
     * @return
     */
    @Override
    public Result memberUpAuthority(Authority authority) {
        /**
         * 判断权限是否 0 ~ 2
         */
        if(authority.getAuthority() == 2){
            return Result.fail(ErrorCode.Authority_LARGEST.getCode(),ErrorCode.Authority_LARGEST.getMsg());
        }
        LambdaUpdateWrapper<ProjectMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ProjectMember::getMemberId,authority.getMemberId()).set(ProjectMember::getMemberAssionment,authority.getAuthority()+1);
        memberMapper.update(null,lambdaUpdateWrapper);
        return Result.success(null);
    }

    /**
     * 降低用户权限
     * @param authority
     * @return
     */
    @Override
    public Result memberLowAuthority(Authority authority) {
        /**
         * 判断权限是否 0 ~ 2
         */
        if(authority.getAuthority() == 0){
            return Result.fail(ErrorCode.Authority_SMALLEST.getCode(),ErrorCode.Authority_SMALLEST.getMsg());
        }
        LambdaUpdateWrapper<ProjectMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ProjectMember::getMemberId,authority.getMemberId()).set(ProjectMember::getMemberAssionment,authority.getAuthority()-1);
        memberMapper.update(null,lambdaUpdateWrapper);
        return Result.success(null);
    }


    /**
     * 移除组员
     * @param memberId
     * @return
     */
    @Override
    public Result removeMemberById(Integer memberId) {
        LambdaQueryWrapper<ProjectMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProjectMember::getMemberId,memberId);
        int delete = memberMapper.delete(lambdaQueryWrapper);
        if(delete != 1){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //移除成功
        return Result.success(null);
    }

    /**
     * 加入这个项目
     * @param joinProjectParams
     * @return
     */
    @Override
    public Result joinThisProject(JoinProjectParams joinProjectParams) {
        /**
         * 插入之前先查询一下有没有重复的名字
         */
        LambdaQueryWrapper<ProjectReceiveRequest> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProjectReceiveRequest::getProjectId,joinProjectParams.getProjectId());
        lambdaQueryWrapper.eq(ProjectReceiveRequest::getUserName,UserThreadLocal.get().getUserName());
        ProjectReceiveRequest re = receiveRequestMapper.selectOne(lambdaQueryWrapper);
        if(re != null){
            // 参数重复了
            return Result.fail(ErrorCode.PARAMS_REPEAT.getCode(),ErrorCode.PARAMS_REPEAT.getMsg());
        }
        ProjectReceiveRequest projectReceiveRequest = new ProjectReceiveRequest();
        projectReceiveRequest.setProjectId(joinProjectParams.getProjectId());
        projectReceiveRequest.setUserId(UserThreadLocal.get().getUserId());
        projectReceiveRequest.setUserIntroduce(joinProjectParams.getUserIntroduction());
        projectReceiveRequest.setUserLargestStudy(UserThreadLocal.get().getUserLargestStudy());
        projectReceiveRequest.setUserLabel(UserThreadLocal.get().getUserLabel());
        projectReceiveRequest.setUserName(UserThreadLocal.get().getUserName());
        receiveRequestMapper.insert(projectReceiveRequest);
        return Result.success(null);
    }

    @Override
    public void save(Integer requestId) {
        LambdaQueryWrapper<ProjectReceiveRequest> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProjectReceiveRequest::getRequestId,requestId);
        ProjectReceiveRequest request = receiveRequestMapper.selectOne(lambdaQueryWrapper);
        User user =  userMapper.selectById(request.getUserId());
        ProjectMember projectMember = new ProjectMember();
        projectMember.setJoinDay(System.currentTimeMillis());
        projectMember.setCreateUserId(UserThreadLocal.get().getUserId());
        projectMember.setMemberAssionment(0);
        projectMember.setMemberImg(user.getUserImg());
        projectMember.setMemberLabel(user.getUserLabel());
        projectMember.setUserLargestStudy(user.getUserLargestStudy());
        projectMember.setUserName(user.getUserName());
        projectMember.setProjectId(request.getProjectId());
        memberMapper.insert(projectMember);
    }
}

