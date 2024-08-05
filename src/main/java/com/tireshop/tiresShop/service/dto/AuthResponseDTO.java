package com.tireshop.tiresShop.service.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private Long userId;
    private String token;
    private String tokenType = "Bearer ";
    private String username;
    private String firstName;
    private String lastName;

    public AuthResponseDTO(String token, Long userId, String username, String firstName,
            String lastName) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastName = lastName;
    }
}