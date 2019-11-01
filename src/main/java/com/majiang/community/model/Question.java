package com.majiang.community.model;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String title;//标题
    private String description;//补充内容
    private Long gmt_create;//时间
    private Long gmt_modified;
    private Long creator;
    private Long comment_count;
    private Long view_count;//浏览数
    private Long like_count;//点赞数
    private String tag;//标签
    private Long likeCount;
}
