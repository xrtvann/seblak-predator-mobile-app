package com.irvan.seblakpredator.model;
import java.io.Serializable;

public class SelectedTopping implements Serializable {

    private String id;
    private String name;
    private int quantity;
    private int price;
    private String image; // URL gambar topping

    // Constructor
    public SelectedTopping(String id, String name, int quantity, int price, String image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    // Default constructor
    public SelectedTopping() {}

    // Getter & Setter untuk id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getter & Setter untuk name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter & Setter untuk quantity
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter & Setter untuk price
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    // Getter & Setter untuk image
    public String getImage() {
        return "http://192.168.0.168/seblak-predator-web-app/uploads/menu-images/" + image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    // Optional: toString() untuk debug
    @Override
    public String toString() {
        return "SelectedTopping{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}

