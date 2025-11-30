package com.irvan.seblakpredator.model;

public class CreateOrderResponse {
    public boolean success;
    public String message;
    public Data data;

    public static class Data {
        public String id;
        public String order_number;
    }
}

