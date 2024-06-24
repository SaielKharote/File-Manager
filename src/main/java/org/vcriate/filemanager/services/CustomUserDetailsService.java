package org.vcriate.filemanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vcriate.filemanager.config.CustomUserDetails;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.repositories.UserRepository;

import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found!");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
return new CustomUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), Collections.singletonList(authority));
    }
}


