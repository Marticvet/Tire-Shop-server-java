package com.tireshop.tiresShop.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tireshop.tiresShop.service.model.Manufacturers;
import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.repo.TiresRepo;

@Service
public class TiresService {

    @Autowired
    public TiresRepo repo;

    public List<Manufacturers> getAllTiresManufacturers() {
        return repo.getAllTiresManufacturers();
    }

    public Manufacturers getTireManufacturerByName(String manufacturerName) {
        return repo.getTireManufacturerByName(manufacturerName);
    }

    public List<Models> getAllModelsByManufacturerName(String manufacturerName) {
        return repo.getAllModelsByManufacturerName(manufacturerName);
    }

    public List<Models> getManufacturerModelByTireId(String manufacturerName, String tireId) {
        return repo.getManufacturerModelByTireId(manufacturerName, tireId);
    }

}
