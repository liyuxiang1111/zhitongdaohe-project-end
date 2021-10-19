package com.project.group.common.cache;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

//切点
public @interface Cache {
    long expire() default 1 * 60 * 1000;
    // 缓存的表示 key
    String name() default "";
}
