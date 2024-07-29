package com.tireshop.tiresShop.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tireshop.tiresShop.service.model.UsersCartItems;
import com.tireshop.tiresShop.service.repo.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public List<UsersCartItems> getUserCartItems(int userId) {
        return repo.getUserCartItems(userId);
    }

}
