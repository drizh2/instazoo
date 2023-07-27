package com.drizh2.instazoo.services;

import com.drizh2.instazoo.dto.PostDTO;
import com.drizh2.instazoo.entities.ImageModel;
import com.drizh2.instazoo.entities.Post;
import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.exceptions.PostNotFoundException;
import com.drizh2.instazoo.repositories.ImageRepository;
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
public class PostService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, ProfileRepository profileRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        Profile profile = getProfileByPrincipal(principal);
        Post post = new Post();
        post.setProfile(profile);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("User is creating with username {}", profile.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        Profile profile = getProfileByPrincipal(principal);
        return postRepository.findPostByIdAndProfile(postId, profile)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));
    }

    public List<Post> getAllPostsForProfile(Principal principal) {
        Profile profile = getProfileByPrincipal(principal);
        return postRepository.findAllByProfileOrderByCreatedDateDesc(profile);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found!"));

        Optional<String> profileLiked = post.getLikedProfiles()
                .stream()
                .filter(u -> u.equals(username))
                .findAny();

        if (profileLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedProfiles().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedProfiles().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findImageModelByPostId(postId);
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }

    private Profile getProfileByPrincipal(Principal principal) {
        String username = principal.getName();
        return profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }
}
