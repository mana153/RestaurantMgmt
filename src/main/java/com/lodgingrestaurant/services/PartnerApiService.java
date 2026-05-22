package com.lodgingrestaurant.services;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PartnerApiService {

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger listingRequests = new AtomicInteger(0);
    private final AtomicInteger createSuccess = new AtomicInteger(0);
    private final AtomicInteger createFailure = new AtomicInteger(0);
    private final AtomicInteger updateSuccess = new AtomicInteger(0);
    private final AtomicInteger updateFailure = new AtomicInteger(0);
    private final AtomicInteger cancelSuccess = new AtomicInteger(0);
    private final AtomicInteger cancelFailure = new AtomicInteger(0);

    // Live Activity Log (Thread-safe rolling log of last 10 requests)
    private final List<String> recentActivity = new ArrayList<>();

    public void incrementListingRequests() {
        totalRequests.incrementAndGet();
        listingRequests.incrementAndGet();
        logActivity("Partner requested available listings (Rooms & Menu Items)");
    }

    public void incrementCreateSuccess(String partner, String type, String details) {
        totalRequests.incrementAndGet();
        createSuccess.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Successfully created " + type + ": " + details);
    }

    public void incrementCreateFailure(String partner, String type, String reason) {
        totalRequests.incrementAndGet();
        createFailure.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Failed to create " + type + ". Reason: " + reason);
    }

    public void incrementUpdateSuccess(String partner, String type, Long id) {
        totalRequests.incrementAndGet();
        updateSuccess.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Successfully updated " + type + " #" + id);
    }

    public void incrementUpdateFailure(String partner, String type, Long id, String reason) {
        totalRequests.incrementAndGet();
        updateFailure.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Failed to update " + type + " #" + id + ". Reason: " + reason);
    }

    public void incrementCancelSuccess(String partner, String type, Long id) {
        totalRequests.incrementAndGet();
        cancelSuccess.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Successfully cancelled " + type + " #" + id);
    }

    public void incrementCancelFailure(String partner, String type, Long id, String reason) {
        totalRequests.incrementAndGet();
        cancelFailure.incrementAndGet();
        logActivity("[" + partner.toUpperCase() + "] Failed to cancel " + type + " #" + id + ". Reason: " + reason);
    }

    private synchronized void logActivity(String log) {
        recentActivity.add(0, java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + " - " + log);
        if (recentActivity.size() > 10) {
            recentActivity.remove(recentActivity.size() - 1);
        }
    }

    // Getters for stats
    public int getTotalRequests() {
        return totalRequests.get();
    }

    public int getListingRequests() {
        return listingRequests.get();
    }

    public int getCreateSuccess() {
        return createSuccess.get();
    }

    public int getCreateFailure() {
        return createFailure.get();
    }

    public int getUpdateSuccess() {
        return updateSuccess.get();
    }

    public int getUpdateFailure() {
        return updateFailure.get();
    }

    public int getCancelSuccess() {
        return cancelSuccess.get();
    }

    public int getCancelFailure() {
        return cancelFailure.get();
    }

    public synchronized List<String> getRecentActivity() {
        return new ArrayList<>(recentActivity);
    }

    // Direct setters for initializing demo data
    public void initStats(int total, int listing, int cSuccess, int cFailure, int uSuccess, int uFailure, int canSuccess, int canFailure) {
        this.totalRequests.set(total);
        this.listingRequests.set(listing);
        this.createSuccess.set(cSuccess);
        this.createFailure.set(cFailure);
        this.updateSuccess.set(uSuccess);
        this.updateFailure.set(uFailure);
        this.cancelSuccess.set(canSuccess);
        this.cancelFailure.set(canFailure);
        
        logActivity("System Initialized with mock historical API transaction stats.");
    }
}
