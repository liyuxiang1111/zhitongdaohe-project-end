package com.project.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.blog.dao.mapper.CommentMapper;
import com.project.blog.dao.pojo.Comment;
import com.project.blog.dao.pojo.SysUser;
import com.project.blog.service.CommentsService;
import com.project.blog.service.SysUserService;
import com.project.blog.utils.UserThreadLocal;
import com.project.blog.vo.CommentVo;
import com.project.blog.vo.Result;
import com.project.blog.vo.UserVo;
import com.project.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class CommentsServiceImpl implements CommentsService {



    @Resource
    private CommentMapper commentMapper;

    @Resource
    private SysUserService sysUserService;

    @Override
    public Result commentsByarticleId(Long id) {
        /**
         * 1.根据文章Id查看评论列表  从comments表里面查找
         * 2.根据作者id 查询作者信息
         * 3. 判断如果level = 1 要取查询他有没有评论
         * 4. 如果有 根据评论id 进行查询 (parent_id)
         * @param id
         * @return
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        //回复作者的评论
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    /**
     * 发表评论
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    /**
     * 类型转化
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> voList = new ArrayList<>();
        for(Comment comment : comments){
            voList.add(copy(comment));
        }
        return voList;
    }


    /**
     * 具体转化
     * @param comment
     * @return
     */
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        commentVo.setId(String.valueOf(comment.getId()));
        BeanUtils.copyProperties(comment,commentVo);
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo uservo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(uservo);
        // 子评论
        Integer commentLevel = comment.getLevel();
        if(1 == commentLevel){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        // to user 给谁评论
        if( commentLevel > 1){
            Long toUid = comment.getToUid();
            UserVo toUservo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUservo);
        }
        return commentVo;
    }

    /**
     * 查询对用id 的子评论列表
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
