package com.irvan.seblakpredator.model;

import com.irvan.seblakpredator.SecondTransaction;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
    private String customerName;
    private String price;
    private String description;
    private boolean isSelected;

    // Tambah field topping
    private List<SecondTransaction.SelectedTopping> toppings;

    public CartItem(String customerName, String price, String description, boolean isSelected, List<SecondTransaction.SelectedTopping> toppings) {
        this.customerName = customerName;
        this.price = price;
        this.description = description;
        this.isSelected = isSelected;
        this.toppings = toppings;
    }

    public String getCustomerName() { return customerName; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
    public List<SecondTransaction.SelectedTopping> getToppings() { return toppings; }
}
