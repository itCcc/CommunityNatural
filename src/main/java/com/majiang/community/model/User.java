package com.majiang.community.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String account_id;
    private String account_name;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
}
