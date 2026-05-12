package com.laundry.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicePackage {
    private int id;
    private String packageName;
    private String description;
    private BigDecimal pricePerKg;
    private int durationDays;
    private LocalDateTime createdAt;
    
    // Constructors
    public ServicePackage() {}
    
    public ServicePackage(String packageName, String description, BigDecimal pricePerKg, int durationDays) {
        this.packageName = packageName;
        this.description = description;
        this.pricePerKg = pricePerKg;
        this.durationDays = durationDays;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(BigDecimal pricePerKg) { this.pricePerKg = pricePerKg; }
    
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return packageName + " - Rp " + pricePerKg + "/kg";
    }
}
