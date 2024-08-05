package com.tireshop.tiresShop.service.model;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Models {
    private int id;
    private String tireLoudnessLevel; // Changed from tire_loudness_level
    private double tirePrice; // Changed from tire_price
    private int tireQuantity; // Changed from tire_quantity
    private int tireLoadIndex; // Changed from tire_load_Index
    private String tireSpeedRating; // Changed from tire_speed_rating
    private String tireSeason; // Changed from tire_season
    private String modelName; // Changed from model_name
    private String modelImageUrl; // Changed from model_imageUrl
    private String modelDescription; // Changed from model_description
    private String manufacturerName; // Changed from manufacturer_name
    private int dimensionWidth; // Changed from dimention_width
    private int dimensionHeight; // Changed from dimention_height
    private int dimensionDiameter; // Changed from dimention_diameter
    private String fuelEfficiency; // Changed from fuel_efficiency
    private String gripRating; // Changed from grip_rating
    private String carType; // Changed from car_type
    private int tireModelId; // Changed from tire_model_id
    private String manufacturerImageUrl; // Changed from manufacturer_imageUrl
}
