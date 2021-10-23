package com.project.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.group.dao.mapper.ProjectBodyMapper;
import com.project.group.dao.mapper.ProjectMapper;
import com.project.group.dao.mapper.ProjectResourceMapper;
import com.project.group.dao.mapper.TypeMapper;
import com.project.group.dao.pojo.*;
import com.project.group.service.*;
import com.project.group.vo.*;
import com.project.group.vo.params.PageParams;
import com.project.group.dao.pojo.ProjectResource;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {


    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private ProjectBodyMapper projectBodyMapper;

    @Resource
    private ThreadService threadService;

    @Resource
    private ProjectResourceMapper projectResourceMapper;

    @Resource
    private TypeMapper typeMapper;


    /**
     * 按id删除指定的project
     * @param projectId
     * @return
     */
    @Override
    public Result deleteProjectById(Integer projectId) {
        Project project = projectMapper.selectById(projectId);
        if(project == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        projectMapper.deleteById(projectId);
        return Result.success(null);
    }

    /**
     * 删除指定项目下的指定资源
     * @param projectResource
     * @return
     * @throws IOException
     */

    @Override
    public Result deleteResource(ProjectResource projectResource) throws IOException {
        ProjectResource re = projectResourceMapper.selectById(projectResource.getDocId());
        if(re != null){
            /**
             * 没有这个资源
             */
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        int i = projectResourceMapper.deleteById(projectResource.getDocId());
        if(i != 1){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String realPath = "C:\\java项目\\r_note\\src\\main\\webapp\\projectFile";
        File delFile = new File(realPath+"\\" + projectResource.getProjectId()+"\\"+projectResource.getResourceName());
        FileUtils.forceDelete(delFile);
        return Result.success(null);
    }

    /**
     * 创建项目
     * @param request
     * @return
     * @throws IOException
     */

    @Override
    public Result createProject(HttpServletRequest request ) throws IOException {
        boolean isMultipart= ServletFileUpload.isMultipartContent(request);  //enctype属性是否是multipart/form-data
        ProjectBody projectBody = new ProjectBody();
        Project project = new Project();
        User user = (User) request.getSession().getAttribute("user");
        Long userId = user.getUserId();
        project.setUserId(userId);
        project.setVisitNumber(0);
        StringBuffer fileStr=new StringBuffer();  //上传的文件名，最后输出用
        //写入上传的图片
        try {
            if (isMultipart){
                FileItemFactory factory=new DiskFileItemFactory();  //工厂实例
                ServletFileUpload upload=new ServletFileUpload(factory);  //ServletFileUpload实例依赖于FileItemFactory工厂
                List<FileItem> itemList=upload.parseRequest((RequestContext) request);  //解析表单字段，封装成一个FileItem实例的集合
                Iterator<FileItem> iterator=itemList.iterator();  //迭代器
                while (iterator.hasNext()){
                    FileItem fileItem=iterator.next();  //依次解析每一个FileItem实例，即表单字段
                    if (fileItem.isFormField()){
                        //普通表单字段
                        if (fileItem.getFieldName().equals("projectName")){
                            project.setProjectName(fileItem.getString("UTF-8"));
                        } else if(fileItem.getFieldName().equals("projectHead")){
                            projectBody.setProjectContent(fileItem.getString("UTF-8"));
                        } else if(fileItem.getFieldName().equals("projectContent_real")){
                            projectBody.setContentHtml(fileItem.getString("UTF-8"));
                        } else if(fileItem.getFieldName().equals("projectType_real")){
                            project.setProjectTypeId(Integer.parseInt(fileItem.getString("UTF-8")));
                        } else if(fileItem.getFieldName().equals("province")){
                            project.setProvince(Integer.parseInt(fileItem.getString("UTF-8")));
                        }
                    }else {
                        LambdaUpdateWrapper<Project> searchName = new LambdaUpdateWrapper<>();
                        searchName.eq(Project::getProjectName, project.getProjectName());
                        Project finded = projectMapper.selectOne(searchName);
                        if(finded != null){
                            return Result.fail(ErrorCode.PARAMS_REPEAT.getCode(),ErrorCode.PARAMS_REPEAT.getMsg());
                        }
                        this.projectMapper.insert(project);
                        String FileName = String.valueOf(project.getProjectId());
                        //存入图片
                        project.setProjectImg(FileName);
                        //文件表单字段
                        String fileUpName=FileName+".png" ;  //用户上传的文件名
                        LambdaUpdateWrapper<Project> update = new LambdaUpdateWrapper<>();
                        update.eq(Project::getProjectId,project.getProjectId()).set(Project::getProjectImg,fileUpName);
                        projectMapper.update(null,update);
                        File fileImg=new File("C:\\java项目\\r_note\\src\\main\\webapp\\projectImg\\"+fileUpName);  //要保存到图片路径
                        FileUtils.forceMkdir(new File("C:\\java项目\\r_note\\src\\main\\projectFile\\"+fileUpName));     //新建项目资源目录
                        if (!fileImg.exists()){
                            fileImg.createNewFile();  //一开始肯定是没有的，所以先创建出来
                        }
                        fileItem.write(fileImg);  //写入，保存到目标文件
                        fileStr.append(fileUpName+"、");
                    }
                }
            }
            return Result.success(null);
        }catch (Exception e){

        }
        return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
    }


    /**
     * 条件查询
     * @param pageParams
     * @return
     */

    @Override
    public Result listProject(PageParams pageParams) {

        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Integer count = projectMapper.selectCount(lambdaQueryWrapper);
        List<Project> projectList =  projectMapper.listProject(
                pageParams.getPageNum(),
                pageParams.getPageSize(),
                pageParams.getProjectName(),
                pageParams.getSearchDay(),
                pageParams.getProjectTypeId(),
                pageParams.getMemberNum(),
                pageParams.getUserSex(),
                pageParams.getUserStudy());
        Page<ProjectVo> page = new Page<>(pageParams.getPageNum(),pageParams.getPageSize(),count);
        page.setDataList(copyList(projectList,true,true));
        return Result.success(page);
    }



    /**
     * 进入文章详情页面
     * @param articleId
     * @return
     */
    @Override
    public Result findProjectById(Long articleId) {
        /**
         * 1. 根据id 查看文章信息
         * 2. 根据bodyid 何 cotegoryid 取做关联查询
         */
        Project article = this.projectMapper.selectById(articleId);
        ProjectVo articlevo = copy(article, true, true,true,true);
        //查看完文章之后新增阅读数
        //查看完文章之后 本来应该直接返回数据 这个时候更新数据 更新时加写锁 阻塞其他的读操作 性能就会比较低
        //更新增加了此次接口的 耗时  如果一旦更新出了问题 查看文章的操作
        //线程池 可以把更新操作扔到线程池中执行 和主线程就不相关了
        threadService.updateArticleViewCount(projectMapper,article);
        return Result.success(articlevo);
    }


    private ProjectBodyVo findArticleBodyById(Long bodyId) {
        ProjectBody projectBody = projectBodyMapper.selectById(bodyId);
        ProjectBodyVo projectBodyVo = new ProjectBodyVo();
        projectBodyVo.setContent(projectBody.getProjectContent());
        return projectBodyVo;
    }

    @Override
    public Result hotProject(int limit) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Project::getVisitNumber);
        queryWrapper.select(Project::getProjectId, Project::getProjectName,Project::getProjectImg);
        queryWrapper.last("limit "+ limit);
        // select id,title from article order by new_counts desc limit 5
        List<Project> articles = projectMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 上传资源
     * @param request
     * @param response
     * @return
     */

    @Override
    public Result postThisResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Integer projectId = Integer.valueOf(request.getParameter("projectId"));
        String postTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        request.setCharacterEncoding("utf-8");//从前端获取的文件类型编码设置
        response.setCharacterEncoding("utf-8");//响应的文件类型的编码
        response.setContentType("text/html;charset=UTF-8");//响应文件的类型MIME类型设置
        //上传文件到servlet
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);//判断前端发送的数据是否是multipart类型
        if(isMultipart) {//确定前端form表单中是否有enctype元素
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory); //这样就可以完美的处理数据了
            //设置缓冲区的大小
            factory.setSizeThreshold(10240);
            //通过parseRequest解析form中的所有字段 并保存到items集合中
            try {
                List<FileItem> items = upload.parseRequest((RequestContext) request);//处理前端的请求数据  即前端传过来的sname sno spicture 就存储在items中
                Iterator<FileItem> iter = items.iterator();//设置遍历的类型
                while (iter.hasNext()) {
                    FileItem item = iter.next();//依次取出数据 数据可以看做是字典对象
                    String itemName = item.getFieldName();//获取name属性值
                    //判断上传类型是否符合要求  不符合要求返回
                    int sno = -1;
                    String sname = null;
                    if (item.isFormField()) {
                        if (itemName.equals("sno")) {//判断是否是sno属性
                            sno = Integer.parseInt(item.getString());
                        } else {//判断是否是sname属性
                            sname = item.getString();
                        }
                    } else {//是文件字段
                        String fileName = item.getName();//获取文件名
//                        检查是存在文件了
                        LambdaQueryWrapper<ProjectResource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(ProjectResource::getResourceName,fileName);
                        lambdaQueryWrapper.eq(ProjectResource::getProjectId,projectId);
                        ProjectResource re = projectResourceMapper.selectOne(lambdaQueryWrapper);
                        if(re != null){
                            return Result.fail(ErrorCode.PARAMS_REPEAT.getCode(),ErrorCode.PARAMS_REPEAT.getMsg());
                        }
                        String path = "C:\\java项目\\r_note\\src\\main\\webapp\\projectFile\\"+projectId+"\\";//设置路径为定值
                        String type = fileName.substring(fileName.indexOf(".") + 1);//获取文件的后缀
                        File file = new File(path, fileName);
                        item.write(file);//这样就将文件存储到指定的文件了
                        return Result.success(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
    }

    //    public Result listArticle(PageParams pageParams) {
//
//        /**
//         * 分页查询数据库的表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if(pageParams.getCategoryId() != null ){
//            // and category_id = #{categortyId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> list = new ArrayList<>();
//        if(pageParams.getTagId() != null){
//            // and tag_id = #{tagId}
//            // 但是 article表中没有tag字段 应为一篇文章内部有多个标签
//            // article_tag article_id 1 : n tag_id
//            LambdaQueryWrapper<ArticleTag> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleLambdaQueryWrapper);
//            //填入对应的list
//            for(ArticleTag articleTag : articleTags ){
//                list.add(articleTag.getArticleId());
//            }
//            //非空判断
//            if(list.size() > 0){
//                queryWrapper.in(Article::getId,list);
//            }
//        }
//        //是否置顶进行排序 (权重)
//        queryWrapper.orderByDesc(Article::getWeight);
//        // order by create_data desc
//        queryWrapper.orderByDesc(Article::getCreateDate);
//        Page<Article> articlePage =  articleMapper.selectPage(page,queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        //不可以直接返回
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    /**
     * 数据转移
     * @param records
     * @return
     */
    private List<ProjectVo> copyList(List<Project> records, boolean isType, boolean isAuthor) {
        List<ProjectVo> projectVoList = new ArrayList<>();
        for(Project record : records){
            projectVoList.add(copy(record,isType,isAuthor,false,false));
        }
        return projectVoList;
    }


    /**
     * 重载
     * @param records
     * @param isTag
     * @param isAuthor
     * @return
     */
    private List<ProjectVo> copyList(List<Project> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ProjectVo> projectVoList = new ArrayList<>();
        for(Project record : records){
            projectVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return projectVoList;
    }


    private ProjectVo copy(Project project, boolean isType, boolean isAuthor, boolean isBody, boolean isCategory){
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        projectVo.setCreateTime(new DateTime(project.getCreateTime()).toString("yyyy-MM-dd HH:mm"));
        projectVo.setProjectImg(project.getProjectImg());
        //并不是所有的接口都需要标签 作者信息

        if(isType){
            LambdaQueryWrapper<Type> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Type::getId,project.getProjectTypeId());
            Type type = typeMapper.selectOne(lambdaQueryWrapper1);
            projectVo.setProjectName(type.getTypeName());
        }
        if(isAuthor){
            Long authorId = project.getUserId();
            projectVo.setAuthor(sysUserService.findUserBid(authorId));
        }
        if(isBody){
            Integer bodyId = project.getProjectBodyId();
            projectVo.setProjectBody(projectBodyMapper.selectById(bodyId));
        }
        return projectVo;
    }

}
