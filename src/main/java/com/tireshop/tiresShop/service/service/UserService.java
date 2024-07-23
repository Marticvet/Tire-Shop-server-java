package com.tireshop.tiresShop.service.service;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tireshop.tiresShop.service.model.User;
import com.tireshop.tiresShop.service.user.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository repo;

    // private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {

        // user.setPassword(encoder.encode(user.getPassword()));

        System.out.println(user.getPassword() + " THIS IS PASSSWORRRRDDD!!!!!!!!!!!!!!");
        return null;
    }

}
