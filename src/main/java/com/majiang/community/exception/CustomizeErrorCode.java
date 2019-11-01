package com.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不换一个试试?"),
    TARGET_PARAM_NOT_FOUND(2002,"未选择任何问题或评论进行回复"),
    NO_LOGIN(2003,"未登录，请登录后重试"),
    SYS_ERROR(2204,"服务器冒烟了，要不稍后再试试？"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论已不存在"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFICATION_EMPTY(2008,"无权限访问！"),
    NOTIFICATION_EMPTY(2009, "消息已不存在"),
    NOT_USER(2010, "你查看的用户不存在");


    private Integer code;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    CustomizeErrorCode(Integer code,String message) {
        this.code=code;
        this.message = message;
    }

}
