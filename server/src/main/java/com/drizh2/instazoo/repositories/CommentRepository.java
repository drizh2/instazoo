package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.Comment;
import com.drizh2.instazoo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentById(Long commentId);

    List<Comment> findAllByPost(Post post);

    Comment findCommentByIdAndProfileId(Long commentId, Long profileId);
}
