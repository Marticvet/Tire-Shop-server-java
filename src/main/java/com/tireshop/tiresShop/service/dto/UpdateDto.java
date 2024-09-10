package com.tireshop.tiresShop.service.dto;

import lombok.Data;

@Data
public class UpdateDto {
    private String username;
    private String password;
    private String newPassword;
    private String firstName;
    private String lastName;
}
