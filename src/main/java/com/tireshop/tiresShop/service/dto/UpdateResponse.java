package com.tireshop.tiresShop.service.dto;

import java.util.List;

import com.tireshop.tiresShop.service.model.UsersCartItems;

public class UpdateResponse {
    private String message;
    private List<UsersCartItems> updatedItems;

    public UpdateResponse(String message, List<UsersCartItems> updatedItems) {
        this.message = message;
        this.updatedItems = updatedItems;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UsersCartItems> getUpdatedItems() {
        return this.updatedItems;
    }

    public void setUpdatedItems(List<UsersCartItems> updatedItems) {
        this.updatedItems = updatedItems;
    }

}