package com.irvan.seblakpredator.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    private String status;
    private String message;
    private Data data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {
        @SerializedName("order_number")
        private String orderNumber;

        @SerializedName("total_amount")
        private int totalAmount;

        public String getOrderNumber() { return orderNumber; }
        public int getTotalAmount() { return totalAmount; }
    }
}
