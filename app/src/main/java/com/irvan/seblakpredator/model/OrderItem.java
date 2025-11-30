package com.irvan.seblakpredator.model;

import java.util.List;

public class OrderItem {
    public String product_id;
    public String product_name;
    public int quantity;
    public int unit_price;
    public List<OrderItemTopping> toppings;
}

