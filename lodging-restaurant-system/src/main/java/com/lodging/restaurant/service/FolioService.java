package com.lodging.restaurant.service;

import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.repository.FolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolioService {

    @Autowired
    private FolioRepository folioRepository;

    public List<Folio> getAllFolios() {
        return folioRepository.findAll();
    }

    public Folio getFolioById(Long id) {
        return folioRepository.findById(id).orElse(null);
    }

    public Folio saveFolio(Folio folio) {
        return folioRepository.save(folio);
    }

    public void deleteFolio(Long id) {
        folioRepository.deleteById(id);
    }
}
