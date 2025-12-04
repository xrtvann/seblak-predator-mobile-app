package com.irvan.seblakpredator.model;

import java.util.List;

public class SnapRequest {
    private String order_number;
    private String customer_name;
    private int total_amount;
    private List<ItemModel> items;

    public SnapRequest(String order_number, String customer_name, int total_amount, List<ItemModel> items) {
        this.order_number = order_number;
        this.customer_name = customer_name;
        this.total_amount = total_amount;
        this.items = items;
    }

    public class ItemModel {
        public String id;
        public String name;
        public int price;
        public int quantity;
    }

}
