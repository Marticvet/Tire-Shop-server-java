package com.tireshop.tiresShop.service.model;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Manufacturers {
    private int id;
    private String name;
    private String image_url;
}
