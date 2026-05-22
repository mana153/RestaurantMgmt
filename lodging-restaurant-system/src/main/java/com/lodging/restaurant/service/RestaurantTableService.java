package com.lodging.restaurant.service;

import com.lodging.restaurant.model.RestaurantTable;
import com.lodging.restaurant.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public RestaurantTable getTableById(Long id) {
        return restaurantTableRepository.findById(id).orElse(null);
    }

    public RestaurantTable saveTable(RestaurantTable table) {
        return restaurantTableRepository.save(table);
    }

    public void deleteTable(Long id) {
        restaurantTableRepository.deleteById(id);
    }
}
