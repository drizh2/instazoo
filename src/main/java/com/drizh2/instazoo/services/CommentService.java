package com.drizh2.instazoo.services;

import com.drizh2.instazoo.dto.CommentDTO;
import com.drizh2.instazoo.entities.Comment;
import com.drizh2.instazoo.entities.Post;
import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.exceptions.PostNotFoundException;
import com.drizh2.instazoo.repositories.CommentRepository;
import com.drizh2.instazoo.repositories.PostRepository;
import com.drizh2.instazoo.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, ProfileRepository profileRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        Profile profile = getProfileByPrincipal(principal);

        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found for username: " + profile.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUsername(profile.getUsername());
        comment.setProfileId(profile.getId());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}", postId);

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findCommentById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private Profile getProfileByPrincipal(Principal principal) {
        String username = principal.getName();
        return profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }
}
