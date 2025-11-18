package com.adr57.netdemo.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("product_image")
    private String product_image;

    public Product() {}

    public Product(int id, String name, double price, String description, String product_image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.product_image = product_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public com.adr57.netdemo.storage.database.entities.Product toEntity() {
        return new com.adr57.netdemo.storage.database.entities.Product(id, name, price, description, product_image);
    }
}
