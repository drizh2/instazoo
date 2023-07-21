package com.drizh2.instazoo.services;

import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found with username: " + username));

        return build(profile);
    }

    public Profile loadUserById(Long id) {
        return userRepository.findById(id).orElse(null);
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
