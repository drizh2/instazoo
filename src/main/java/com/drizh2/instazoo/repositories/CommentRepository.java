package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.Comment;
import com.drizh2.instazoo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUser(Long commentId, Long userId);
}
