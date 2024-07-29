package com.tireshop.tiresShop.service.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UsersCartItems {
    private int id;
    private int userId;
    private int quantity;
    private int tireId;
    private String email;
    private String firstName;
    private String lastName;
    private int tireLoudnessLevel;
    private Double tirePrice;
    private int tireQuantity;
    private int tireLoadIndex;
    private String tireSpeedRating;
    private int tireModelId;
    private String tireSeason;
    private String modelImageUrl;
    private String modelDescription;
    private String manufacturerName;
    private int dimensionWidth;
    private int dimensionHeight;
    private int dimensionDiameter;
    private String fuelEfficiency;
    private String gripRating;
    private String carType;
    private String modelName; // Changed from model_name
}
