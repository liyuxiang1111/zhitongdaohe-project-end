package com.project.blog.service;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.blog.dao.mapper.ArticleMapper;
import com.project.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {


    /**
     *      使用线程池更新文章的阅读数量 这样就不会影响主线程
     * @param articalMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articalMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        //  update article set view_count = 100 where view_count == 99 and id = ??
        articalMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println("更新完成了!!");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
