package com.project.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.blog.dao.dos.Archives;
import com.project.blog.dao.mapper.ArticleBodyMapper;
import com.project.blog.dao.mapper.ArticleMapper;
import com.project.blog.dao.mapper.ArticleTagMapper;
import com.project.blog.dao.pojo.Article;
import com.project.blog.dao.pojo.ArticleBody;
import com.project.blog.dao.pojo.ArticleTag;
import com.project.blog.dao.pojo.SysUser;
import com.project.blog.service.*;
import com.project.blog.vo.ArticleBodyVo;
import com.project.blog.vo.ArticleVo;
import com.project.blog.vo.Result;
import com.project.blog.vo.TagVo;
import com.project.blog.vo.params.ArticleParam;
import com.project.blog.vo.params.PageParams;
import com.project.blog.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {


    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private ThreadService threadService;

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的构建 article 对象
         * 2.  作者id  当前的登录用户
         * 3. 标签 要将标签加入关联列表中
         * 4. body 内容存储
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        //tags
        List<TagVo> tags = articleParam.getTags();

        //插入之后就会得到一个文章id
        this.articleMapper.insert(article);
        if(tags != null){
            for(TagVo tag : tags){
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        // body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        // 也是先插入时候才会产生对应的id
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

        articleMapper.listArchive(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = page.getRecords();
        return Result.success(copyList(records,true,true));
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
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : records){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }


    /**
     * 重载
     * @param records
     * @param isTag
     * @param isAuthor
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : records){
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }


    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);


        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口都需要标签 作者信息

        if(isTag){
            Long articleId =  article.getId();
            articleVo.setTags(tagService.findTagByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserBid(authorId).getNickname());
        }
        if(isCategory){
            Long categoryId =  article.getCategoryId();
            articleVo.setCategory(categoryService.findArticleCategoryById(categoryId));
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        return articleVo;
    }

    @Autowired
    private CategoryService categoryService;


    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Resource
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+ limit);
        // select id,title from article order by new_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }





    /**
     * 进入文章详情页面
     * @param articleId
     * @return
     */
    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id 查看文章信息
         * 2. 根据bodyid 何 cotegoryid 取做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articlevo = copy(article, true, true,true,true);
        //查看完文章之后新增阅读数
        //查看完文章之后 本来应该直接返回数据 这个时候更新数据 更新时加写锁 阻塞其他的读操作 性能就会比较低
        //更新增加了此次接口的 耗时  如果一旦更新出了问题 查看文章的操作
        //线程池 可以把更新操作扔到线程池中执行 和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articlevo);
    }
}
