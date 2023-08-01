package com.drizh2.instazoo.facade;

import com.drizh2.instazoo.dto.PostDTO;
import com.drizh2.instazoo.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setLocation(post.getLocation());
        postDTO.setUsername(post.getProfile().getUsername());
        postDTO.setTitle(post.getTitle());
        postDTO.setLikedProfiles(post.getLikedProfiles());

        return postDTO;
    }

}
