package com.irvan.seblakpredator.model;

public class SpiceLevel {

    private String id;
    private String name;
    private double price;
    private String image;
    private String category_id;
    private boolean is_active;
    private int sort_order;
    private String category_name; // dari join categories
    private String category_type; // dari join categories
    private String created_at;    // opsional, bisa ditambahkan jika ingin
    private String updated_at;    // opsional, bisa ditambahkan jika ingin

    // ======== Constructor ========

    public SpiceLevel(String id, String name, double price, String image, String category_id,
                      boolean is_active, int sort_order, String category_name, String category_type,
                      String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category_id = category_id;
        this.is_active = is_active;
        this.sort_order = sort_order;
        this.category_name = category_name;
        this.category_type = category_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // ======== Getter & Setter ========
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
