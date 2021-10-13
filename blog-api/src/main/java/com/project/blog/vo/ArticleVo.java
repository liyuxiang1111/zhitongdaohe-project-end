package com.project.blog.vo;


import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

//    因为前端的经度损失问题 所以和前端交互的id使用string
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;


}
