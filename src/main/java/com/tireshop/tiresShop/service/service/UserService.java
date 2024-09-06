package com.tireshop.tiresShop.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tireshop.tiresShop.service.dto.UpdateDto;
import com.tireshop.tiresShop.service.dto.UpdateResponse;
import com.tireshop.tiresShop.service.model.UsersCartItems;
import com.tireshop.tiresShop.service.repo.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public List<UsersCartItems> getUserCartItems(int userId) {
        return repo.getUserCartItems(userId);
    }

    public ResponseEntity<String> addItemInShoppingCart(int userId, int quantity, int tireId) {
        return repo.addItemInShoppingCart(userId,
                quantity, tireId);
    }

    public ResponseEntity<String> updateUser(UpdateDto updateDto) {
        return repo.updateUser(updateDto);
    }

    public ResponseEntity<String> deleteItemInShoppingCart(Long userId, int itemId) {
        return repo.deleteItemInShoppingCart(userId, itemId);
    }

    public ResponseEntity<UpdateResponse> updateItemInShoppingCart(Long userId, int itemId, int quantity) {
        return repo.updateItemInShoppingCart(userId, itemId, quantity);
    }
}
