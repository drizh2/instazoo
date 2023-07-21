package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByProfileId(Long profileId);

    Optional<ImageModel> findByPostId(Long postId);

}
