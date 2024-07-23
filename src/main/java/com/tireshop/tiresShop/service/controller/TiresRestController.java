package com.tireshop.tiresShop.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.model.Manufacturers;
import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.service.TiresService;

@RestController
@CrossOrigin
public class TiresRestController {

    @Autowired
    private TiresService service;

    @GetMapping("/tires/manufacturers")
    public List<Manufacturers> getAllTiresManufacturers() {
        return service.getAllTiresManufacturers();
    }

    @GetMapping("/tires/manufacturers/{manufacturerName}")
    public Manufacturers getTireManufacturerByName(@PathVariable String manufacturerName) {
        return service.getTireManufacturerByName(manufacturerName);
    }

    @GetMapping("/tires/manufacturers/{manufacturerName}/tire-models")
    public List<Models> getAllModelsByManufacturerName(@PathVariable String manufacturerName) {
        return service.getAllModelsByManufacturerName(manufacturerName);
    }

    @GetMapping("/tires/manufacturers/{manufacturerName}/tire-model/{tireId}")
    public List<Models> getManufacturerModelByTireId(@PathVariable String manufacturerName,
            @PathVariable String tireId) {
        return service.getManufacturerModelByTireId(manufacturerName, tireId);
    }
}