package com.lodging.restaurant.service;

import com.lodging.restaurant.model.InventoryItem;
import com.lodging.restaurant.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    public InventoryItem getInventoryItemById(Long id) {
        return inventoryItemRepository.findById(id).orElse(null);
    }

    public InventoryItem saveInventoryItem(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    public void deleteInventoryItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
