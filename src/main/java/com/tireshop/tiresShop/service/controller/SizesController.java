package com.tireshop.tiresShop.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.model.Sizes;
import com.tireshop.tiresShop.service.service.SizesService;

@RestController
@CrossOrigin
public class SizesController {

    @Autowired
    private SizesService service;

    @GetMapping("/sizes/model-sizes/{modelId}")
    public List<Sizes> getSizesByModelId(@PathVariable String modelId) {
        return service.getSizesByModelId(modelId);
    }

    @GetMapping("/sizes")
    public List<Models> getModelsWithSizes(
            @RequestParam("width") String width,
            @RequestParam("height") String height,
            @RequestParam("diameter") String diameter,
            @RequestParam("season") String season,
            @RequestParam("manufacturer") String manufacturer) {

        return service.getModelsWithSizes(width, height, diameter, season, manufacturer);
    }
}