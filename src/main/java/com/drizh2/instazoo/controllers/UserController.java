package com.drizh2.instazoo.controllers;

import com.drizh2.instazoo.dto.ProfileDTO;
import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.facade.ProfileFacade;
import com.drizh2.instazoo.services.ProfileService;
import com.drizh2.instazoo.validations.ResponseErrorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/profile")
@CrossOrigin
public class UserController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileFacade profileFacade;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @GetMapping("/")
    public ResponseEntity<ProfileDTO> getCurrentProfile(Principal principal) {
        Profile profile = profileService.getCurrentProfile(principal);
        ProfileDTO profileDTO = profileFacade.profileToProfileDTO(profile);
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("profileId") String profileId) {
        Profile profile = profileService.getProfileByID(Long.parseLong(profileId));
        ProfileDTO profileDTO = profileFacade.profileToProfileDTO(profile);

        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody ProfileDTO profileDTO,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        ResponseEntity<Object> errorMap = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errorMap)) return errorMap;

        Profile profile = profileService.updateProfile(profileDTO, principal);
        ProfileDTO updatedProfile = profileFacade.profileToProfileDTO(profile);

        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

}
