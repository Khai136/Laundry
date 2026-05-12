package com.laundry.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LaundryOrder {
    private int id;
    private int customerId;
    private int packageId;
    private BigDecimal weightKg;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime pickupDate;
    private String notes;
    
    // Additional fields for display
    private String customerName;
    private String packageName;
    
    // Constructors
    public LaundryOrder() {}
    
    public LaundryOrder(int customerId, int packageId, BigDecimal weightKg, BigDecimal totalPrice, String notes) {
        this.customerId = customerId;
        this.packageId = packageId;
        this.weightKg = weightKg;
        this.totalPrice = totalPrice;
        this.notes = notes;
        this.status = "pending";
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
}
