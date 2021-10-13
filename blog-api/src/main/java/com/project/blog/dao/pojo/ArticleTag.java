package com.project.blog.dao.pojo;

import lombok.Data;

@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
