package com.drizh2.instazoo.controllers;

import com.drizh2.instazoo.dto.PostDTO;
import com.drizh2.instazoo.entities.Post;
import com.drizh2.instazoo.facade.PostFacade;
import com.drizh2.instazoo.payload.response.MessageResponse;
import com.drizh2.instazoo.services.PostService;
import com.drizh2.instazoo.validations.ResponseErrorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                              BindingResult bindingResult,
                                              Principal principal) {
        ResponseEntity<Object> errorMap = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errorMap)) return errorMap;

        Post post = postService.createPost(postDTO, principal);
        PostDTO createdPost = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostsForProfile(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable String postId,
                                            @PathVariable String username) {
        Post post = postService.likePost(Long.parseLong(postId), username);
        PostDTO likedPost = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(likedPost, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable String postId,
                                                      Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);

        return new ResponseEntity<>(new MessageResponse("Post has been deleted"), HttpStatus.OK);
    }
}
