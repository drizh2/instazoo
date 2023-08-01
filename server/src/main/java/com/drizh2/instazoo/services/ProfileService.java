package com.drizh2.instazoo.services;

import com.drizh2.instazoo.dto.ProfileDTO;
import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.entities.enums.ERoles;
import com.drizh2.instazoo.exceptions.UserExistException;
import com.drizh2.instazoo.payload.request.SignupRequest;
import com.drizh2.instazoo.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProfileService {
    public static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    private ProfileRepository profileRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, CustomPasswordEncoder passwordEncoder) {
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

    public Profile updateProfile(ProfileDTO profileDTO, Principal principal) {
        Profile profile = getProfileByPrincipal(principal);
        profile.setName(profileDTO.getFirstname());
        profile.setLastname(profileDTO.getLastname());
        profile.setBio(profileDTO.getBio());
        profile.setUsername(profileDTO.getUsername());

        return profileRepository.save(profile);
    }

    public Profile getCurrentProfile(Principal principal) {
        return getProfileByPrincipal(principal);
    }

    private Profile getProfileByPrincipal(Principal principal) {
        String username = principal.getName();
        return profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    public Profile getProfileByID(Long profileId) {
        return profileRepository.findProfileById(profileId).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
