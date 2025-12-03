package com.irvan.seblakpredator.model;

import java.util.List;

public class CreateOrderRequest {
    public String customer_name;
    public String table_number;
    public String order_type;
    public String payment_method;
    public String payment_status;
    public int tax;
    public int discount;
    public List<OrderItem> items;
}

