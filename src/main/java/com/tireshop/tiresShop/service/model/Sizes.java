package com.tireshop.tiresShop.service.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Sizes {
    private int id;
    private String loadIndex;
    private String speedRating;
    private int tireModelId;
    private int tireQuantity;

    private int tireDimensionId;
    private int dimensionWidth;
    private int dimensionHeight;
    private int dimensionDiameter;
}