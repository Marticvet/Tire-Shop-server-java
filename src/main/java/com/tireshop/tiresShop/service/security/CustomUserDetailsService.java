package com.tireshop.tiresShop.service.security;

import com.tireshop.tiresShop.service.model.UserEntity;
import com.tireshop.tiresShop.service.repo.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // Optional<UserEntity> userOptional = userRepository.findByEmail(username);
    // UserEntity user = userOptional.orElseThrow(() -> new
    // UsernameNotFoundException("Username not found"));

    // return new
    // org.springframework.security.core.userdetails.User(user.getUsername(),
    // user.getPassword(),
    // new ArrayList<>());
    // }
}
