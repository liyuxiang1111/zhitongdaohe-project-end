package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.group.service.LoginService;
import com.project.group.dao.mapper.UserMapper;
import com.project.group.dao.pojo.User;
import com.project.group.service.SysUserService;
import com.project.group.utils.UserThreadLocal;
import com.project.group.vo.ErrorCode;
import com.project.group.vo.LoginUserVo;
import com.project.group.vo.Result;
import com.project.group.vo.UserVo;
import com.project.group.vo.params.RegisterParam;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginService loginService;


    /**
     * 修改用户信息
     * @param request
     * @return
     */
    @Override
    public Result changeUserInformation(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Long userId = user.getUserId();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        StringBuffer fileStr = new StringBuffer();  //上传的文件名，最后输出用
        try {
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();  //工厂实例
                ServletFileUpload upload = new ServletFileUpload(factory);  //ServletFileUpload实例依赖于FileItemFactory工厂
                List<FileItem> itemList = upload.parseRequest((RequestContext) request);  //解析表单字段，封装成一个FileItem实例的集合
                Iterator<FileItem> iterator = itemList.iterator();  //迭代器
                while (iterator.hasNext()) {
                    FileItem fileItem = iterator.next();  //依次解析每一个FileItem实例，即表单字段
                    String fileName = "";
                    if (!fileItem.isFormField()) {
                        String FileName = String.valueOf(userId);
                        //文件表单字段
                        String fileUpName = FileName + ".png";  //用户上传的文件名
                        File fileImg = new File("C:\\java项目\\r_note\\src\\main\\webapp\\userImg\\"+fileUpName);  //要保存到图片路径
                        //新建项目资源目录
                        if (!fileImg.exists()) {
                            fileImg.createNewFile();  //一开始肯定是没有的，所以先创建出来
                        }
                        fileItem.write(fileImg);  //写入，保存到目标文件
                        fileStr.append(fileUpName + "、");
                    }
                    else {
                        //普通表单字段
                        if (fileItem.getFieldName().equals("userName")) {
                            user.setUserName(fileItem.getString("UTF-8"));
                        } else if (fileItem.getFieldName().equals("userTelephone")) {
                            user.setUserTelephone(fileItem.getString("UTF-8"));
                        } else if (fileItem.getFieldName().equals("userLabel")) {
                            user.setUserLabel(fileItem.getString("UTF-8"));
                        } else if (fileItem.getFieldName().equals("userContent")) {
                            user.setUserContent(fileItem.getString("UTF-8"));
                        } else if (fileItem.getFieldName().equals("userEmail")) {
                            user.setUserEmail(fileItem.getString("UTF-8"));
                        } else if(fileItem.getFieldName().equals("sex")){
                            user.setUserSex(Integer.valueOf(fileItem.getString("UTF-8")));
                        } else if(fileItem.getFieldName().equals("userBirthday")){
                            user.setUserBirthday(fileItem.getString("UTF-8"));
                        }
                    }
                }
                User finded =  userMapper.findUserName(user.getUserName(),user.getUserId());
                if(finded != null){
                    return Result.fail(ErrorCode.PARAMS_REPEAT.getCode(),ErrorCode.PARAMS_REPEAT.getMsg());
                }
                UserThreadLocal.put(user);
                UserThreadLocal.remove();
                LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(User::getUserId,user.getUserId());
                userMapper.update(user,lambdaQueryWrapper);
                return Result.success(null);
            }
        }catch(Exception e){

        }
        return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
    }




    @Override
    public UserVo findUserVoById(Long id) {
        User user = userMapper.selectById(id);
        if(user == null){
            user = new User();
            user.setUserId(1L);
            user.setUserImg("/static/img/logo.b3a48c0.png");
            user.setUserName("heyongqiang");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }

    @Override
    public User findUserBid(Long id) {
        User user = userMapper.selectById(id);
        if(user == null){
            user = new User();
            user.setUserName("何勇强");
        }
        return user;
    }

    @Override
    public User findUser(String userName, String userPwd) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        queryWrapper.eq(User::getUserPwd,userPwd);
        queryWrapper.select(User::getUserName, User::getUserId, User::getUserPwd);
        queryWrapper.last(" limit 1");
        return userMapper.selectOne(queryWrapper);
    }



    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *      是否为空 是否成功解析 redis是否存在
         * 2.如果校验失败 返回错误
         * 3. 如果成功 返回对应结果
         */

        User user =  loginService.checkToken(token);
        if(user == null){
            //token不合法
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(user.getUserId()));
        loginUserVo.setUserName(user.getUserName());
        loginUserVo.setUserRealName(user.getUserRealName());
        loginUserVo.setUserImg(user.getUserImg());
        return Result.success(loginUserVo);
    }

    @Override
    public User findUserName(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        queryWrapper.last(" limit 1");
        this.userMapper.selectOne(queryWrapper);
        return this.userMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(User user) {
        //保存用户 这个id 会自动生成
        // 这个地方默认生成的id 分布式Id 生成使用的是雪花算法
        //mybatis
        this.userMapper.insert(user);
    }
}
