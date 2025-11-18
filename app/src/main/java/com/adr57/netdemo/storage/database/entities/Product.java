package com.adr57.netdemo.storage.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    public Product(int id, String name, double price, String description, String product_image){
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.product_image = product_image;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "product_image")
    public String product_image;

}
