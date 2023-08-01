package com.drizh2.instazoo.facade;

import com.drizh2.instazoo.dto.ProfileDTO;
import com.drizh2.instazoo.entities.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileFacade {

    public ProfileDTO profileToProfileDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setId(profile.getId());
        profileDTO.setFirstname(profile.getName());
        profileDTO.setLastname(profile.getLastname());
        profileDTO.setUsername(profile.getUsername());
        profileDTO.setBio(profile.getBio());
        return profileDTO;
    }

}
