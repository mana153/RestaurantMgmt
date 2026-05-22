package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.ApiUsage;
import com.lodging.restaurant.model.PartnerService;
import com.lodging.restaurant.repository.ApiUsageRepository;
import com.lodging.restaurant.repository.PartnerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class PartnerApiController {

    @Autowired
    private PartnerServiceRepository repository;

    @Autowired
    private ApiUsageRepository apiUsageRepository;


    
    // LISTING
    @GetMapping
    public List<PartnerService> getAllServices() {
        trackApi("GET /api/services");
        return repository.findAll();
    }
    
    // GET BY ID
    @GetMapping("/{id}")
    public PartnerService getServiceById(@PathVariable Long id) {
        trackApi("GET /api/services/{id}");
        return repository.findById(id).orElse(null);
    }

    // CREATE
    @PostMapping
    public PartnerService createService(@RequestBody PartnerService service) {
        trackApi("POST /api/services");
        return repository.save(service);
    }

    // UPDATE
    @PutMapping("/{id}")
    public PartnerService updateService(@PathVariable Long id,
                                        @RequestBody PartnerService updatedService) {
        trackApi("PUT /api/services/{id}");
        Optional<PartnerService> optional = repository.findById(id);

        if(optional.isPresent()) {

            PartnerService service = optional.get();

            service.setCustomerName(updatedService.getCustomerName());
            service.setServiceType(updatedService.getServiceType());
            service.setStatus(updatedService.getStatus());

            return repository.save(service);
        }

        return null;
    }

    // CANCEL / DELETE
    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable Long id) {
        trackApi("DELETE /api/services/{id}");
        repository.deleteById(id);

        return "Service Cancelled Successfully";
    }

    private void trackApi(String endpoint) {

        ApiUsage usage = apiUsageRepository.findByEndpointName(endpoint);

        if (usage == null) {

            usage = new ApiUsage(endpoint, 1);

        } else {

            usage.setHitCount(usage.getHitCount() + 1);
        }

        apiUsageRepository.save(usage);
    }
}
