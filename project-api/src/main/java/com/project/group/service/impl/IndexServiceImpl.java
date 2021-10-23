package com.project.group.service.impl;


import com.alibaba.fastjson.JSON;
import com.project.group.service.LoginService;
import com.project.group.dao.pojo.User;
import com.project.group.service.SysUserService;
import com.project.group.utils.JWTUtils;
import com.project.group.vo.ErrorCode;
import com.project.group.vo.Result;
import com.project.group.vo.params.LoginParam;
import com.project.group.vo.params.RegisterParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class IndexServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String slat = "123456heyongqiang!@#$$";
    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户名何密码去user表中查询 是否存在
         * 3.如果不存在 登录失败
         * 4.如果存在 使用jwt 生成token 返回给前端
         * 5.token 放入redis 当中  token: user 设置过期实际
         * （登录认证的时候 先演示token是否合法 然后验证redis是否存在）
         */
        String account = loginParam.getUserName();
        String password = loginParam.getUserPwd();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
//        加密语言
        password = DigestUtils.md5Hex(password+slat);
        User user = sysUserService.findUser(account,password);
        if(user == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(user.getUserId());
        redisTemplate.opsForValue().set("TOKEN_" + token , JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public User checkToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if(map == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        //token校验通过那么解析为token字符串返回
        User user = JSON.parseObject(userJson, User.class);
        return user;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+ token);
        return Result.success(null);
    }

    @Override
    public Result register(RegisterParam registerParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在  存在返回账户已经存在
         * 3. 不存在 那么就注册用户
         * 4.生产token
         * 5.存入reidis 并返回
         * 注意加上事物 一旦中间的任何过程出现了问题注册的用户就需要回滚
         *
         */
        String userPwd = registerParam.getUserPwd();
        String userName = registerParam.getUserName();
        String userRealName = registerParam.getUserRealName();
        Integer userSex = registerParam.getUserSex();
        if(StringUtils.isBlank(userRealName) || StringUtils.isBlank(userPwd) || StringUtils.isBlank(userName) ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        User user =  sysUserService.findUserName(userName);
        if(user != null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),"账户已经被注册了!!");
        }
        user = new User();
        user.setUserName(userName);
        user.setUserPwd(DigestUtils.md5Hex(userPwd+slat));
        user.setUserRealName(userRealName);
        user.setUserImg("/static/img/logo.base.png");
        user.setUserRole(1);
        user.setUserSex(userSex);
        this.sysUserService.save(user);
        String token = JWTUtils.createToken(user.getUserId());
        redisTemplate.opsForValue().set("TOKEN_" + token , JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }

}
