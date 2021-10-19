package com.project.group.service;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.group.dao.mapper.ProjectMapper;
import com.project.group.dao.pojo.Project;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {


    /**
     *      使用线程池更新文章的阅读数量 这样就不会影响主线程
     * @param articalMapper
     * @param project
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ProjectMapper articalMapper, Project project) {
        int viewCounts = project.getVisitNumber();
        Project projectUpdate = new Project();
        projectUpdate.setVisitNumber(viewCounts + 1);
        LambdaUpdateWrapper<Project> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Project::getProjectId,project.getProjectId());
        //设置一个 为了在多线程的环境下线程安全
        updateWrapper.eq(Project::getVisitNumber,viewCounts);
        //  update article set view_count = 100 where view_count == 99 and id = ??
        articalMapper.update(projectUpdate,updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println("更新完成了!!");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
