package com.majiang.community.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Select;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long mgt_create;
    private Integer status;
    private Long outer_id;
    private String notifier_name;
    private Long notifier_id;
    private String outer_title;
    private String typeName;
    private Integer type;
}
