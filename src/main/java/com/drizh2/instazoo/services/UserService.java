package com.drizh2.instazoo.services;

import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.entities.enums.ERoles;
import com.drizh2.instazoo.exceptions.UserExistException;
import com.drizh2.instazoo.payload.request.SignupRequest;
import com.drizh2.instazoo.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private ProfileRepository profileRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(ProfileRepository profileRepository, CustomPasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder.getPasswordEncoder();
    }

    public Profile createProfile(SignupRequest profileIn) {
        Profile profile = new Profile();
        profile.setEmail(profileIn.getEmail());
        profile.setName(profileIn.getFirstname());
        profile.setLastname(profileIn.getLastname());
        profile.setUsername(profileIn.getUsername());
        profile.setPassword(passwordEncoder.encode(profileIn.getPassword()));
        profile.getRoles().add(ERoles.ROLE_USER);

        try {
            LOG.info("User has been created!");
            return profileRepository.save(profile);
        } catch (Exception e) {
            LOG.error("Error during registration: {}", e.getMessage());
            throw new UserExistException("User with username: " + profileIn.getUsername() + " already exists! Please change username!");
        }
    }
}
