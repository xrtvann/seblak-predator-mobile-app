package com.irvan.seblakpredator.model;

import java.util.List;

public class OrderRequest {
    private String user_id;
    private String delivery_address;
    private List<SelectedMenuRequest> items;
    private String payment_method;

    public OrderRequest(String user_id, String delivery_address, List<SelectedMenuRequest> items, String payment_method) {
        this.user_id = user_id;
        this.delivery_address = delivery_address;
        this.items = items;
        this.payment_method = payment_method;
    }

    public String getUser_id() { return user_id; }
    public String getDelivery_address() { return delivery_address; }
    public List<SelectedMenuRequest> getItems() { return items; }
    public String getPayment_method() { return payment_method; }

    // Inner class, tidak public
    public static class SelectedMenuRequest {
        private String name, level, kuah, telur, kencur;
        private int harga_level, harga_kuah, harga_telur, harga_kencur;
        private List<SelectedToppingRequest> toppings;

        public SelectedMenuRequest(String name, String level, String kuah, String telur, String kencur,
                                   int harga_level, int harga_kuah, int harga_telur, int harga_kencur,
                                   List<SelectedToppingRequest> toppings) {
            this.name = name;
            this.level = level;
            this.kuah = kuah;
            this.telur = telur;
            this.kencur = kencur;
            this.harga_level = harga_level;
            this.harga_kuah = harga_kuah;
            this.harga_telur = harga_telur;
            this.harga_kencur = harga_kencur;
            this.toppings = toppings;
        }

        public String getName() { return name; }
        public String getLevel() { return level; }
        public String getKuah() { return kuah; }
        public String getTelur() { return telur; }
        public String getKencur() { return kencur; }
        public int getHarga_level() { return harga_level; }
        public int getHarga_kuah() { return harga_kuah; }
        public int getHarga_telur() { return harga_telur; }
        public int getHarga_kencur() { return harga_kencur; }
        public List<SelectedToppingRequest> getToppings() { return toppings; }
    }

    public static class SelectedToppingRequest {
        private String id, name;
        private int quantity, price;

        public SelectedToppingRequest(String id, String name, int quantity, int price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }
    }
}
