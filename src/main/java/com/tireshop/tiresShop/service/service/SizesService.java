package com.tireshop.tiresShop.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.model.Sizes;
import com.tireshop.tiresShop.service.repo.SizesRepo;

@Service
public class SizesService {

    @Autowired
    private SizesRepo repo;

    public List<Sizes> getSizesByModelId(String modelId) {
        return repo.getSizesByModelId(modelId);
    }

    public List<Models> getModelsWithSizes(String width, String height, String diameter, String season,
            String manufacturer) {
        return repo.getModelsWithSizes(width, height, diameter, season, manufacturer);
    }
}
