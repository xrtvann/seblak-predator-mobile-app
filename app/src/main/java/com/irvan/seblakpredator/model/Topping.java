package com.irvan.seblakpredator.model;

import com.google.gson.annotations.SerializedName;

public class Topping {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private int price;

    @SerializedName("image")
    private String image;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("is_available")
    private boolean isAvailable;

    @SerializedName("sort_order")
    private int sortOrder;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("is_active")
    private boolean isActive;

    @SerializedName("image_url")
    private String imageUrl;

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImage() { return "http://192.168.0.168/seblak-predator-web-app/uploads/menu-images/" + image; }
    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public boolean isAvailable() { return isAvailable; }
    public int getSortOrder() { return sortOrder; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return isActive; }
    public String getImageUrl() { return imageUrl; }
}