package com.project.group.vo;

/**
 * 根据枚举进行定义
 */
public enum  ErrorCode {

    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不存在"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),
    TOKEN_ERROR(10002,"token为空!!"),
    PARAMS_REPEAT(10003,"参数名字重复!!"),
    Authority_LARGEST(10004,"用户权限最大了!!"),
    Authority_SMALLEST(10004,"用户权限最小了!!");


    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
