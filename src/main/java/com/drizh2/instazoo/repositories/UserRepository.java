package com.drizh2.instazoo.repositories;

import com.drizh2.instazoo.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByEmail(String email);

    Optional<Profile> findById(Long Id);
}
