package com.project.blog.vo;


import lombok.Data;

/**
 * vo对象一般是何前端页面进行交互的
 *  数据库的数据一般不会直接何前端相连
 */


@Data
public class TagVo {

    private String id;

    private String tagName;

    private String avatar;

}
