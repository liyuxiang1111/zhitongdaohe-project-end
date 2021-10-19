package com.project.group.handler;


import com.project.group.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//对加了controller注解的方法做捕获
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json数据
    public Result doException(Exception ex){
        //打印堆栈
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
