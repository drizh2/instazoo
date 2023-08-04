package com.drizh2.instazoo.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {

    private Long id;
    private String caption;
    private String location;
    private String username;
    private String title;
    private Integer likes;
    private Set<String> userLiked;
}
