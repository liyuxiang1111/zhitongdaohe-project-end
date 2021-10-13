package com.project.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.blog.service.LoginService;
import com.project.blog.dao.mapper.SysUserMapper;
import com.project.blog.dao.pojo.SysUser;
import com.project.blog.service.SysUserService;
import com.project.blog.vo.ErrorCode;
import com.project.blog.vo.LoginUserVo;
import com.project.blog.vo.Result;
import com.project.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private LoginService loginService;


    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("heyongqiang");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    @Override
    public SysUser findUserBid(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("何勇强");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last(" limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }



    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *      是否为空 是否成功解析 redis是否存在
         * 2.如果校验失败 返回错误
         * 3. 如果成功 返回对应结果
         */

        SysUser sysUser =  loginService.checkToken(token);
        if(sysUser == null){
            //token不合法
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last(" limit 1");
        this.sysUserMapper.selectOne(queryWrapper);
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户 这个id 会自动生成
        // 这个地方默认生成的id 分布式Id 生成使用的是雪花算法
        //mybatis
        this.sysUserMapper.insert(sysUser);
    }
}
