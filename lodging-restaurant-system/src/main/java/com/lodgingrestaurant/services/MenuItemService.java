package com.lodgingrestaurant.services;

import com.lodgingrestaurant.models.MenuItem;
import com.lodgingrestaurant.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        if (category == null || category.isEmpty() || "ALL".equalsIgnoreCase(category)) {
            return menuItemRepository.findAll();
        }
        return menuItemRepository.findByCategory(category.toUpperCase());
    }

    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public MenuItem saveMenuItem(MenuItem menuItem) {
        // Enforce upper case category
        if (menuItem.getCategory() != null) {
            menuItem.setCategory(menuItem.getCategory().toUpperCase());
        }
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
