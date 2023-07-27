package com.drizh2.instazoo.dto;

import com.drizh2.instazoo.entities.Profile;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {

    private Long id;
    private Profile profile;
    private String caption;
    private String location;
    private String username;
    private String title;
    private Integer likes;
    private Set<String> likedProfiles;
}
