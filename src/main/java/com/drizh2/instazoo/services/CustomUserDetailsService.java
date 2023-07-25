package com.drizh2.instazoo.services;

import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final ProfileRepository profileRepository;

    @Autowired
    public CustomUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findProfileByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found with username: " + username));

        return build(profile);
    }

    public Profile loadUserById(Long id) {
        return profileRepository.findProfileById(id).orElse(null);
    }

    public static Profile build(Profile profile) {
        List<GrantedAuthority> authorities = profile.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new Profile(profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getPassword(),
                authorities);
    }
}
