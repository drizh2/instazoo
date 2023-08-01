package com.drizh2.instazoo.controllers;

import com.drizh2.instazoo.payload.request.LoginRequest;
import com.drizh2.instazoo.payload.request.SignupRequest;
import com.drizh2.instazoo.payload.response.JWTSuccessResponse;
import com.drizh2.instazoo.payload.response.MessageResponse;
import com.drizh2.instazoo.security.JWTProvider;
import com.drizh2.instazoo.security.SecurityConstants;
import com.drizh2.instazoo.services.ProfileService;
import com.drizh2.instazoo.validations.ResponseErrorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest request,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errorMap = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errorMap)) return errorMap;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest request,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errorMap = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errorMap)) return errorMap;

        profileService.createProfile(request);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }
}
