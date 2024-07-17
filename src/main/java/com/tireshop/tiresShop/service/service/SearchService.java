package com.tireshop.tiresShop.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.repo.SearchRepo;

@Service
public class SearchService {

    @Autowired
    private SearchRepo repo;

    public List<Models> searchedBy(String manufacturer, String modelName) {
        return repo.searchedBy(manufacturer, modelName);
    }

}