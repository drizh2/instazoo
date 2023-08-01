package com.drizh2.instazoo.controllers;

import com.drizh2.instazoo.dto.CommentDTO;
import com.drizh2.instazoo.entities.Comment;
import com.drizh2.instazoo.facade.CommentFacade;
import com.drizh2.instazoo.payload.response.MessageResponse;
import com.drizh2.instazoo.services.CommentService;
import com.drizh2.instazoo.validations.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @PostMapping("/{postId}/create")
    public ResponseEntity<CommentDTO> createComment(@PathVariable String postId,
                                                    @RequestBody CommentDTO commentDTO,
                                                    Principal principal) {
        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment has been deleted!"), HttpStatus.OK);
    }

}
