package com.tireshop.tiresShop.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.service.SearchService;

@RestController
@CrossOrigin
public class SearchController {
    @Autowired
    private SearchService service;

    @GetMapping("/searchBy")
    public List<Models> searchedBy(@RequestParam("manufacturer") String manufacturer,
            @RequestParam("modelName") String modelName) {
        return service.searchedBy(manufacturer, modelName);
    }
}