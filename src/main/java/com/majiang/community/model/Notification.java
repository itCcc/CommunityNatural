package com.majiang.community.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notification {

    private Long id;
    //发起通知人ID
    private Long notifier;
    //接受通知人ID
    private Long receiver;
    //通知类型ID
    private Long outer_id;
    //通知类型
    private Integer type;
    //通知时间
    private Long mgt_create;
    //是否已读
    private Integer status;
    //发起通知人name
    private String notifier_name;
    //通知来源标题
    private String outer_title;
    //通知来源问题ID
    private Long outerQuestion_id;
}
