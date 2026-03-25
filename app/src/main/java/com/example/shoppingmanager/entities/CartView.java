package com.example.shoppingmanager.entities;

import androidx.room.DatabaseView;

// Đây là class chứa dữ liệu hiển thị trong hóa đơn
public class CartView {
    public String productName;
    public int quantity;
    public double unitPrice;
    public double total;

    // Constructor, Getters/Setters nếu cần
}