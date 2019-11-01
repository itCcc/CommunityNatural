package com.majiang.community.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateDTO {
    private Long parent_id;
    private Integer type;
    private String content;
}
