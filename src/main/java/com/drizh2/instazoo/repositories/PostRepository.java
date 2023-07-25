package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.Post;
import com.drizh2.instazoo.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByProfileOrderByCreatedDateDesc(Profile profile);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndProfile(Long id, Profile profile);
}
