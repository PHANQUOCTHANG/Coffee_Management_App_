package com.example.javafxapp.Service;

import com.example.javafxapp.Model.WareHouse;
import com.example.javafxapp.Repository.WareHouseRepository;

import java.util.List;

public class WareHouseService {

    private final WareHouseRepository repository;

    public WareHouseService() {
        this.repository = new WareHouseRepository();
    }

    // Add
    public void addWareHouse(WareHouse wareHouse) {
        repository.add(wareHouse);
    }

    // Update
    public void updateWareHouse(WareHouse wareHouse) {
        repository.update(wareHouse);
    }

    // Soft delete
    public void deleteWareHouse(int id) {
        repository.delete(id);
    }


    // Get all
    public List<WareHouse> getAllWareHouses() {
        return repository.getAll();
    }

    // Find by ID
    public WareHouse findWareHouseByID(int id) {
        return repository.findByID(id);
    }

    // Find by name
    public WareHouse findWareHouseByName(String name) {
        return repository.findByName(name);
    }

    // Search by keyword
    public List<WareHouse> searchByKeyword(String keyword) {
        return repository.findAllByKeyword(keyword);
    }

    // Toggle status
    public void toggleStatus(int id, boolean currentStatus) {
        repository.changeStatus(id, currentStatus);
    }

    // Update quantity
    public void updateQuantity(int id, int quantity) {
        repository.updateQuantity(id, quantity);
    }
}
