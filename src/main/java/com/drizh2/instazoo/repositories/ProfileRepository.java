package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByUsername(String username);

    Optional<Profile> findProfileByEmail(String email);

    Optional<Profile> findProfileById(Long id);
}
