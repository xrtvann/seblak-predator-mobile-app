package com.irvan.seblakpredator.model;

public class CartItem {
    private String customerName;
    private String price;
    private String description;
    private boolean isSelected;

    public CartItem(String customerName, String price, String description, boolean isSelected) {
        this.customerName = customerName;
        this.price = price;
        this.description = description;
        this.isSelected = isSelected;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
