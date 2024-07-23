package com.tireshop.tiresShop.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.model.User;
import com.tireshop.tiresShop.service.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    // @Autowired
    // private JwtService JwtService;

    // @Autowired
    // AuthenticationManager authenticationManager;

    @PostMapping("users/register")
    public User registerUser(@RequestBody User user) {
        return service.registerUser(user);
    }
}

// getAllUsers -> "users" -> get method;
// getUserCartItems -> "users/shoppingCart/" + userId -> get method;
// addItemInShoppingCart -> "users/shoppingCart" -> post method
// editItemQuantity -> "users" -> put method
// deleteItemInShoppingCart -> "users/shoppingCart/" + id -> delete method
// emptyUsersShoppingCart -> "users/shoppingCart/" + userId -> delete method
// loginUser -> "users/login" -> post method
// registerUser -> "users/register" -> post method
// updateUser -> "users/update" -> put method
// logoutUser -> "users/logout" -> delete method